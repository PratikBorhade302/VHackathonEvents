package com.example.vhackathonevents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class EventAdapter extends FirestoreRecyclerAdapter<EventModel , EventAdapter.EventHolder> {

        private static OnClickEvent onClick;

    public EventAdapter(@NonNull FirestoreRecyclerOptions<EventModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventHolder holder, int position, @NonNull final EventModel model) {
        holder.date.setText(model.getDate());
        Picasso.get()
                .load(model.getPoster())
                .into(holder.eventPoster);

        holder.eventPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 26-10-2020 open detail activity
                String dateAndTime = model.getDate()  +"\n" +  model.getTime();
                onClick.openDetailActivity(model.getPoster() ,model.getEventName() , model.getCommittee() , dateAndTime , model.getLink() , model.getDescription());
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.deleteEvent(model.eventName);
            }
        });
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card , parent, false);
        return new EventHolder(view);
    }

    public class EventHolder extends RecyclerView.ViewHolder {

        TextView  date ;
        ImageView eventPoster;
        Button deleteBtn;
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.EventCardDate);
            eventPoster = itemView.findViewById(R.id.EventCardImage);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    public static void setOnCLickEvent(OnClickEvent onCLickEvent){
         onClick = onCLickEvent;
    }

    public interface OnClickEvent{
        void openDetailActivity(String posterURL,String eventName, String organizer, String dateAndTime , String link ,String description);
        void deleteEvent(String documentName);
    }

}
