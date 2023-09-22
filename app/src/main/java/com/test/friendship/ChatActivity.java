package com.test.friendship;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;
import com.test.friendship.MessageNotif.APIService;
import com.test.friendship.MessageNotif.Client;
import com.test.friendship.MessageNotif.Data;
import com.test.friendship.MessageNotif.MyResponse;
import com.test.friendship.MessageNotif.NotificationSender;
import com.test.friendship.MessageNotif.Token;
import com.test.friendship.Model.Messages;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import com.test.friendship.R;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private String messageRecieverId,getMessageRecievername,messagereceiverimage,messageSenderId;
    private TextView username,userlastseen;
    private CircleImageView userprofile;
    private Toolbar chattoolbar;
    private ImageButton sendMessageButton,sendFileButton;
    private EditText messagesentinput;
    private FirebaseAuth mauth;
    String userid;
    boolean notify = false;
    DatabaseReference reference;
    private DatabaseReference RootRef;
    private final List<Messages> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView usermessagerecyclerview;
    ValueEventListener seenListener;
    private String savecurrentTime,savecurrentDate;
    private String checker="",myUrl="";
    private StorageTask uploadTask;
    private Uri fileuri;
    boolean b= false;
    private ProgressDialog loadingBar;
    private APIService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        loadingBar=new ProgressDialog(this);
        mauth=FirebaseAuth.getInstance();
        messageSenderId=mauth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();

        messageRecieverId=getIntent().getExtras().get("visit_user_id").toString();
        getMessageRecievername=getIntent().getExtras().get("visit_user_name").toString();
        messagereceiverimage=getIntent().getExtras().get("visit_image").toString();

        username=findViewById(R.id.username);
        userlastseen=findViewById(R.id.userlastseen);
        userprofile=findViewById(R.id.profile_image);
        sendMessageButton=findViewById(R.id.btn_send);
        sendFileButton=findViewById(R.id.send_files_btn);

        messagesentinput=findViewById(R.id.input_messages);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        messageAdapter=new MessageAdapter(messagesList);
        usermessagerecyclerview=findViewById(R.id.private_message_list_of_users);
        linearLayoutManager=new LinearLayoutManager(this);
        usermessagerecyclerview.setLayoutManager(linearLayoutManager);
        usermessagerecyclerview.setAdapter(messageAdapter);

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/yyyy");
        savecurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        savecurrentTime=currentTime.format(calendar.getTime());


        username.setText(getMessageRecievername);
        Picasso.get().load(messagereceiverimage).into(userprofile);
        Displaylastseen();


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                SendMessage();
            }
        });

        sendFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[]=new CharSequence[]{
                        "Images","PDF Files","Ms Word Files"
                };

                AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select File");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            checker="image";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent,"Select Image"),555);

                        }else if(which==1)
                        {
                            checker="pdf";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent,"Select PDF File"),555);


                        }else if(which==2)
                        {
                            checker="docx";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(intent.createChooser(intent,"Select Ms Word File"),555);
                        }
                    }
                });

                builder.show();
            }
        });

        RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages=dataSnapshot.getValue(Messages.class);
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                usermessagerecyclerview.smoothScrollToPosition(usermessagerecyclerview.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==555 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            loadingBar.setTitle("Sending File");
            loadingBar.setMessage("please wait, we are sending that file...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileuri=data.getData();
            if(!checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Document Files");

                final String messageSenderRef="Messages/"+messageSenderId+"/"+messageRecieverId;
                final String messageReceiverRef="Messages/"+messageRecieverId+"/"+messageSenderId;

                DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();
                final String messagePushID=Usermessagekeyref.getKey();

                final StorageReference filepath=storageReference.child(messagePushID+"."+checker);

                filepath.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                Map messageDocsBody = new HashMap();
                                messageDocsBody.put("message",downloadUrl);
                                messageDocsBody.put("name",fileuri.getLastPathSegment());
                                messageDocsBody.put("type",checker);
                                messageDocsBody.put("from",messageSenderId);
                                messageDocsBody.put("to", messageRecieverId);
                                messageDocsBody.put("messageID", messagePushID);
                                messageDocsBody.put("time", savecurrentTime);
                                messageDocsBody.put("date", savecurrentDate);
                                messageDocsBody.put("isseen","false");


                                Map messageBodyDDetail = new HashMap();
                                messageBodyDDetail.put(messageSenderRef + "/" + messagePushID, messageDocsBody);
                                messageBodyDDetail.put(messageReceiverRef + "/" + messagePushID, messageDocsBody);

                                RootRef.updateChildren(messageBodyDDetail);
                                loadingBar.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingBar.dismiss();
                                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double p=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        loadingBar.setMessage((int) p+" % Uploading...");
                    }
                });
            }
            else if(checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image Files");

                final String messageSenderRef="Messages/"+messageSenderId+"/"+messageRecieverId;
                final String messageReceiverRef="Messages/"+messageRecieverId+"/"+messageSenderId;

                DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();
                final String messagePushID=Usermessagekeyref.getKey();

                final StorageReference filepath=storageReference.child(messagePushID+"."+"jpg");

                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileuri);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                // Compress the image with reduced quality (adjust quality as needed)
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos); // Adjust the quality here (50 in this example)

                // Convert the compressed Bitmap to bytes
                byte[] data1 = baos.toByteArray();

                // Upload the compressed image to Firebase Storage
                UploadTask uploadTask = filepath.putBytes(data1);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Uri downloadUrl=task.getResult();
                            myUrl=downloadUrl.toString();

                            Map messageTextBody=new HashMap();
                            messageTextBody.put("message",myUrl);
                            messageTextBody.put("name",fileuri.getLastPathSegment());
                            messageTextBody.put("type",checker);
                            messageTextBody.put("from",messageSenderId);
                            messageTextBody.put("to",messageRecieverId);
                            messageTextBody.put("messageID",messagePushID);
                            messageTextBody.put("time",savecurrentTime);
                            messageTextBody.put("date",savecurrentDate);
                            messageTextBody.put("isseen","false");

                            Map messageBodyDetails =new HashMap();
                            messageBodyDetails.put(messageSenderRef+"/"+messagePushID,messageTextBody);
                            messageBodyDetails.put(messageReceiverRef+"/"+messagePushID,messageTextBody);

                            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful())
                                    {
                                        loadingBar.dismiss();
                                        //Toast.makeText(ChatActivity.this,"Message sent Successfully...",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(ChatActivity.this,"Error:",Toast.LENGTH_SHORT).show();
                                    }
                                    messagesentinput.setText("");
                                }
                            });
                        }
                    }
                });

            }
            else
            {
                loadingBar.dismiss();
                Toast.makeText(this,"please select file",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Displaylastseen()
    {
        RootRef.child("Users").child(messageRecieverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("status"))
                {

                    String state=dataSnapshot.child("status").getValue().toString();

                    if(state.equals("online"))
                    {
                        userlastseen.setText("online");
                    }
                    else if(state.equals("offline"))
                    {
                        userlastseen.setText("offline");
                    }
                }
                else
                {
                    userlastseen.setText("offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void SendMessage() {
        String messagetext=messagesentinput.getText().toString();
        if(TextUtils.isEmpty(messagetext))
        {
            Toast.makeText(this,"Please enter message first..",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef="Messages/"+messageSenderId+"/"+messageRecieverId;
            String messageReceiverRef="Messages/"+messageRecieverId+"/"+messageSenderId;

            DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();
            String messagePushID=Usermessagekeyref.getKey();
            Map messageTextBody=new HashMap();
            messageTextBody.put("message",messagetext);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderId);
            messageTextBody.put("to",messageRecieverId);
            messageTextBody.put("messageID",messagePushID);
            messageTextBody.put("time",savecurrentTime);
            messageTextBody.put("date",savecurrentDate);
            messageTextBody.put("isseen","false");

            Map messageBodyDetails =new HashMap();
            messageBodyDetails.put(messageSenderRef+"/"+messagePushID,messageTextBody);
            messageBodyDetails.put(messageReceiverRef+"/"+messagePushID,messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        final String msg = messagetext;
                        reference = FirebaseDatabase.getInstance().getReference("students").child(messageSenderId);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserModel user = dataSnapshot.getValue(UserModel.class);
                                if (notify) {
                                    sendNotifiaction(messageRecieverId, user.getName(), msg);
                                }
                                notify = false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        // Toast.makeText(ChatActivity.this,"Message sent Successfully...",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this,"Error:",Toast.LENGTH_SHORT).show();
                    }
                    messagesentinput.setText("");
                }
            });




        }
        seenMessage(messageRecieverId);
    }
    private void sendNotifiaction(String receiver, final String username, final String message) {


        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    Data data = new Data(messageSenderId, R.mipmap.ic_launcher, username+":"+message, "New Message",
                            userid);

                    NotificationSender sender = new NotificationSender(data, token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200){
                                if (response.body().success != 1){
//                                    Toast.makeText(ChatActivity.this, String.valueOf(response.body().success), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ChatActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(ChatActivity.this, "Add", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(ChatActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void seenMessage(final String userid){


        try {
            reference = FirebaseDatabase.getInstance().getReference("Messages").child(messageRecieverId).child(messageSenderId);
            seenListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Messages chat = snapshot.getValue(Messages.class);
                        if (chat.getTo().equals(FirebaseAuth.getInstance().getUid()) && chat.getFrom().equals(userid)){
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isseen", "true");
                            b = true;
                            snapshot.getRef().updateChildren(hashMap);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }catch (Exception e){}
        finally {
            if(b){

            }
            b=false;



        }
    }



    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");

        currentUser(messageRecieverId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
//        reference = FirebaseDatabase.getInstance().getReference("students").child(FirebaseAuth.getInstance().getUid());
//        reference.removeEventListener(seenListener);
        currentUser("none");
    }
}
