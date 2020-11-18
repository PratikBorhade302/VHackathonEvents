package com.example.vhackathonevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore fireStore;
    CollectionReference collectionReference;

    RecyclerView recyclerView;
    FloatingActionButton addEventBtn;
    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireBaseRef();
        recyclerView = findViewById(R.id.eventsRecyclerView);
        addEventBtn = findViewById(R.id.AddEventBtn);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        displayEvents();

        EventAdapter.setOnCLickEvent(new EventAdapter.OnClickEvent() {
            @Override
            public void openDetailActivity(String posterURL, String eventName, String organizer, String dateAndTime, String link, String description) {
                // TODO: 26-10-2020 start detail activity and pass parameters
                Intent toDetailsActivity = new Intent(MainActivity.this , EventDetailsActivity.class);

                toDetailsActivity.putExtra("posterURL", posterURL);
                toDetailsActivity.putExtra("eventName", eventName);
                toDetailsActivity.putExtra("organizer", organizer);
                toDetailsActivity.putExtra("dateNTime", dateAndTime);
                toDetailsActivity.putExtra("link", link);
                toDetailsActivity.putExtra("description", description);

                startActivity(toDetailsActivity);
            }

            public void deleteEvent(String documentName){
                collectionReference.document(documentName).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "";
                        if(task.isSuccessful()) msg = "Deleted Successfully";
                        else msg = "Try Again";

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    private void fireBaseRef() {
        fireStore = FirebaseFirestore.getInstance();
        collectionReference = fireStore.collection("CollegeEvents");
    }

    private void displayEvents(){
        Query query = collectionReference.orderBy("sortingParameter" , Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<EventModel> options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query , EventModel.class)
                .build();
        adapter = new EventAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //on click floating button
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , AddEventActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}