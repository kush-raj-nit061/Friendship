
package com.example.friendship;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendship.Model.Messages;
import com.example.friendship.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> UserMessageList;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    public MessageAdapter (List<Messages> UserMessageList)
    {
        this.UserMessageList=UserMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_messages_layout,parent,false);
        MessageViewHolder messageViewHolder=new MessageViewHolder(view);
        mAuth=FirebaseAuth.getInstance();

        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String messagesenderid=mAuth.getCurrentUser().getUid();
        final Messages messages=UserMessageList.get(position);

        String fromuserid=messages.getFrom();
        String frommessagetype=messages.getType();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(fromuserid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("image"))
                {
                    String receiverprofileimage=dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(receiverprofileimage).into(holder.receiverprofileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.receivermessagetext.setVisibility(View.GONE);
        holder.receiverprofileimage.setVisibility(View.GONE);
        holder.sendermessagetext.setVisibility(View.GONE);
        holder.messageSenderPicture.setVisibility(View.GONE);
        holder.messageReceiverPicture.setVisibility(View.GONE);
        if(frommessagetype.equals("text"))
        {
            if(fromuserid.equals(messagesenderid))
            {
                holder.sendermessagetext.setVisibility(View.VISIBLE);
                holder.tvTimeSender.setText(messages.getTime());
                holder.sendermessagetext.setText(messages.getMessage());
            }
            else
            {
                holder.receivermessagetext.setVisibility(View.VISIBLE);
                holder.receiverprofileimage.setVisibility(View.GONE);
                holder.tvTimeReceiver.setText(messages.getTime());
                holder.receivermessagetext.setText(messages.getMessage());
            }
        }
        else  if(frommessagetype.equals("image"))
        {
            if(fromuserid.equals(messagesenderid))
            {
                holder.messageSenderPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messageSenderPicture);
            }
            else
            {

                holder.messageReceiverPicture.setVisibility(View.VISIBLE);
                holder.receiverprofileimage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messageReceiverPicture);
            }
        }
        else if(frommessagetype.equals("pdf") || frommessagetype.equals("docx"))
        {
            if(fromuserid.equals(messagesenderid))
            {
                holder.messageSenderPicture.setVisibility(View.GONE);
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tesla-members-record.appspot.com/o/pngwing.com%20(4).png?alt=media&token=fd13080c-6968-421c-94e3-038c45689f8a")
                        .into(holder.messageSenderPicture);
                holder.messageSenderPicture.setVisibility(View.VISIBLE);


            }
            else
            {

                holder.messageReceiverPicture.setVisibility(View.VISIBLE);
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tesla-members-record.appspot.com/o/pngwing.com%20(4).png?alt=media&token=fd13080c-6968-421c-94e3-038c45689f8a")
                        .into(holder.messageReceiverPicture);

                holder.receiverprofileimage.setVisibility(View.VISIBLE);

            }
        }

        if(fromuserid.equals(messagesenderid))
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(UserMessageList.get(position).getType().equals("pdf") || UserMessageList.get(position).getType().equals("docx"))
                        {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "➪Download and view content","➪Delete for me","➪Delete for everyone","➪Cancel"
                                    };

                            AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                            builder.setTitle("Delete Message?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==1)
                                    {
                                        deleteSentMessage(position,holder);

                                    }else if(which==0)
                                    {
                                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(UserMessageList.get(position).getMessage()));
                                        holder.itemView.getContext().startActivity(intent);
                                    }else if(which==3)
                                    {
                                        //for cancel do not do anything
                                    }
                                    else if(which==2)
                                    {
                                        deleteMessageForEveryone(position,holder);

                                    }
                                }
                            });

                            builder.show();
                        }
                        else if(UserMessageList.get(position).getType().equals("text") )
                        {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "➪Delete for me","➪Delete for everyone","➪Cancel"
                                    };

                            AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                            builder.setTitle("Delete Message?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==0)
                                    {
                                        deleteSentMessage(position,holder);

                                    }else if(which==2)
                                    {
                                        //for cancel do not do anything
                                    }else if(which==1)
                                    {
                                        deleteMessageForEveryone(position,holder);

                                    }

                                }
                            });

                            builder.show();
                        }
                        else  if(UserMessageList.get(position).getType().equals("image") )
                        {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "➪View This Image","➪Delete for me","➪Delete for everyone","➪Cancel"
                                    };

                            AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                            builder.setTitle("Delete Message?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==1)
                                    {
                                        deleteSentMessage(position,holder);

                                    }else if(which==0)
                                    {
                                        Intent intent=new Intent(holder.itemView.getContext(), FullProfileLoader.class);
                                        intent.putExtra("purl",UserMessageList.get(position).getMessage());
                                        holder.itemView.getContext().startActivity(intent);

                                    }else if(which==3)
                                    {
                                        //for cancel do not do anything
                                    }
                                    else if(which==2)
                                    {
                                        deleteMessageForEveryone(position,holder);

                                    }
                                }
                            });

                            builder.show();
                        }
                    }catch (Exception e){}

                }
            });
        }
        else
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(UserMessageList.get(position).getType().equals("pdf") || UserMessageList.get(position).getType().equals("docx"))
                        {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "➪Download and view content","➪Delete for me","➪Cancel"
                                    };

                            AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                            builder.setTitle("Delete Message?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==1)
                                    {
                                        deleteReceiveMessage(position,holder);

                                    }else if(which==0)
                                    {
                                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(UserMessageList.get(position).getMessage()));
                                        holder.itemView.getContext().startActivity(intent);
                                    }else if(which==2)
                                    {
                                        //for cancel do not do anything
                                    }

                                }
                            });

                            builder.show();
                        }
                        else if(UserMessageList.get(position).getType().equals("text") )
                        {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "Delete for me","Cancel"
                                    };

                            AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                            builder.setTitle("Delete Message?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==0)
                                    {
                                        deleteReceiveMessage(position,holder);

                                    }else if(which==1)
                                    {
                                        //for cancel do not do anything
                                    }

                                }
                            });

                            builder.show();
                        }
                        else  if(UserMessageList.get(position).getType().equals("image") )
                        {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "View This Image","Delete for me","Cancel"
                                    };

                            AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                            builder.setTitle("Delete Message?");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==1)
                                    {
                                        deleteReceiveMessage(position,holder);

                                    }else if(which==0)
                                    {
                                        Intent intent=new Intent(holder.itemView.getContext(),FullProfileLoader.class);
                                        intent.putExtra("purl",UserMessageList.get(position).getMessage());
                                        holder.itemView.getContext().startActivity(intent);

                                    }else if(which==2)
                                    {
                                        //for cancel do not do anything
                                    }

                                }
                            });

                            builder.show();
                        }
                    }catch (Exception e){}



                }
            });
        }
    }
    private void deleteSentMessage(final int position,final MessageViewHolder holder)
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();

        try {
            rootRef.child("Messages").child(UserMessageList.get(position).getFrom())
                    .child(UserMessageList.get(position).getTo()).child(UserMessageList.get(position).getMessageID())
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                notifyItemRemoved(position);
                                UserMessageList.remove(position);
                                notifyItemRangeChanged(position, UserMessageList.size());
                                Toast.makeText(holder.itemView.getContext(),"Message deleted...",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(holder.itemView.getContext(),"Error...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }catch (Exception e){}




    }

    private void deleteReceiveMessage(final int position,final MessageViewHolder holder)
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();

        try {
            rootRef.child("Messages").child(UserMessageList.get(position).getTo())
                    .child(UserMessageList.get(position).getFrom()).child(UserMessageList.get(position).getMessageID())
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                notifyItemRemoved(position);
                                UserMessageList.remove(position);
                                notifyItemRangeChanged(position, UserMessageList.size());
                                Toast.makeText(holder.itemView.getContext(),"Message deleted...",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(holder.itemView.getContext(),"Error...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }catch (Exception e){}


    }

    private void deleteMessageForEveryone(final int position,final MessageViewHolder holder)
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();


        try {
            rootRef.child("Messages").child(UserMessageList.get(position).getFrom())
                    .child(UserMessageList.get(position).getTo()).child(UserMessageList.get(position).getMessageID())
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
                                rootRef.child("Messages").child(UserMessageList.get(position).getTo())
                                        .child(UserMessageList.get(position).getFrom()).child(UserMessageList.get(position).getMessageID())
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    notifyItemRemoved(position);
                                                    UserMessageList.remove(position);
                                                    notifyItemRangeChanged(position, UserMessageList.size());
                                                    Toast.makeText(holder.itemView.getContext(),"Message deleted...",Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(holder.itemView.getContext(),"Error...",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(holder.itemView.getContext(),"Error...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }catch (Exception e){}




    }
    @Override
    public int getItemCount() {
        return UserMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView sendermessagetext,receivermessagetext,tvTimeSender,tvTimeReceiver;
        public CircleImageView receiverprofileimage;
        public ImageView messageSenderPicture,messageReceiverPicture;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sendermessagetext=itemView.findViewById(R.id.sender_message_text);
            receivermessagetext=itemView.findViewById(R.id.receiver_message_text);
            receiverprofileimage=itemView.findViewById(R.id.message_profile_image);
            messageSenderPicture=itemView.findViewById(R.id.message_sender_image_view);
            messageReceiverPicture=itemView.findViewById(R.id.message_receiver_image_view);
            tvTimeSender = itemView.findViewById(R.id.time_tv_sender);
            tvTimeReceiver = itemView.findViewById(R.id.time_tv_receiver);
        }
    }


}
