package com.test.friendship;

import android.content.Context;
import android.content.Intent;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.test.friendship.R;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.friendship.Model.Events;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
public class EventsAdapter extends FirebaseRecyclerAdapter<Events,EventsAdapter.userAdapterHolder> {

    public EventsAdapter(@NonNull FirebaseRecyclerOptions<Events> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull Events model) {
        try {
            holder.date.setText(model.getDate());
            holder.month.setText(model.getMonth());
            holder.description.setText(model.getDescription());
            holder.eventname.setText(model.getEventname());
            holder.eventtitle.setText(model.getEventtitle());
            Glide.with(holder.eventpurl.getContext()).load(model.getEventpurl()).into(holder.eventpurl);
            String link1,link2,link3;
            link1 = model.getLink1();
            link2 = model.getLink2();
            link3 = model.getLink3();
            if(!(link1 ==null)){
                holder.link1.setVisibility(View.VISIBLE);
                holder.link1.setText(link1);
            }if(!(link2 ==null)){
                holder.link2.setVisibility(View.VISIBLE);
                holder.link2.setText(link2);
            }if(!(link3 ==null)){
                holder.link3.setVisibility(View.VISIBLE);
                holder.link3.setText(link3);
            }


        }catch (Exception e){
            Toast.makeText(holder.eventname.getContext(), "Something wrong in Events",Toast.LENGTH_SHORT).show();
        }

        holder.eventpurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Intent i = new Intent(cont, FullProfileLoader.class);
                i.putExtra("purl",model.getEventpurl());
                cont.startActivity(i);

            }
        });


    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        ImageView eventpurl;
        TextView eventname,eventtitle,link1,link2,link3,description,date,month;
        RelativeLayout expandableView;
        Button arrowBtn;
        CardView cardView;


        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            eventpurl = itemView.findViewById(R.id.eventpurl);
            eventname = itemView.findViewById(R.id.tvTitle);
            eventtitle = itemView.findViewById(R.id.tvSubTitle);
            link1 = itemView.findViewById(R.id.link1);
            link2 = itemView.findViewById(R.id.link2);
            link3 = itemView.findViewById(R.id.link3);
            month = itemView.findViewById(R.id.tvLession);
            date = itemView.findViewById(R.id.tvDate);
            description = itemView.findViewById(R.id.description);
            expandableView = itemView.findViewById(R.id.expandableView);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            cardView = itemView.findViewById(R.id.cardView);

            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        arrowBtn.setBackgroundResource(R.drawable.arrowdown);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        arrowBtn.setBackgroundResource(R.drawable.terms);
                    }
                }
            });





        }
    }
}
