package com.example.vhackathonevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener , DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {

    FirebaseFirestore fireStore;
    StorageReference storageReferenceParent;
    Uri imageUri;
    ImageView eventPoster;
    EditText eventNameTF,eventOrganizerTF , eventFormLinkTF , eventDescriptionTF;
    Button uploadEventBtn ,setDateBtn , setTimeBtn;
    String eventTime , eventDate ;
    String eventSortingParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        reference();
        fireBaseRef();

        onClickRef();

    }

    private void reference() {
        eventPoster        = findViewById(R.id.EventPoster);
        eventNameTF        = findViewById(R.id.EventName);
        eventOrganizerTF   = findViewById(R.id.EventOrganizer);
        eventFormLinkTF    = findViewById(R.id.EventsRegisterationLink);
        eventDescriptionTF = findViewById(R.id.EventDescription);
        uploadEventBtn     = findViewById(R.id.UploadEventBtn);
        setDateBtn     = findViewById(R.id.date);
        setTimeBtn     = findViewById(R.id.time);
    }

    private void fireBaseRef() {
        fireStore = FirebaseFirestore.getInstance();
        storageReferenceParent = FirebaseStorage.getInstance().getReference("Events");
    }


    private void onClickRef() {
        eventPoster.setOnClickListener(this);
        uploadEventBtn.setOnClickListener(this);
        setDateBtn.setOnClickListener(this);
        setTimeBtn.setOnClickListener(this);
    }

    //open gallery
    private void uploadImage() {
        Intent galleryIntent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1000);
    }

    //choose and store URI of gallery image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000 && data != null) {
            imageUri = data.getData();
            eventPoster.setImageURI(imageUri);
        }
    }


    //to set time //with use of TimeFragment
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
            eventTime = hour +" : " + minutes;
            setTimeBtn.setText(eventTime);
    }

    private void callTimeFragment(){
        TimeFragment timeFragment = new TimeFragment();
        timeFragment.show(getSupportFragmentManager(), "time");
    }


    //to set event date //with use of DateFragment
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR , year);
        c.set(Calendar.MONTH , month);
        c.set(Calendar.DAY_OF_MONTH , day);

        eventDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        eventSortingParameter =String.valueOf(c.get(Calendar.DAY_OF_YEAR)); //list sorting parameter to sort events
        setDateBtn.setText(eventDate);
    }

    private void callDateFragment(){
        CalenderFragment calenderFragment = new CalenderFragment();
        calenderFragment.show(getSupportFragmentManager() , "Pick Up Date");
    }



    private void uploadToFireStore(){
        final String eventNameS,eventOrganizerS ,eventFormLinkS , eventDescriptionS;
        eventNameS = eventNameTF.getText().toString();
        eventOrganizerS = eventOrganizerTF.getText().toString();
        eventFormLinkS = eventFormLinkTF.getText().toString();
        eventDescriptionS = eventDescriptionTF.getText().toString();

        if(imageUri == null){
            Toast.makeText(this, "Add ImageTo Upload", Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(eventNameS)){
            eventNameTF.setError("Eneter Organization/Committee name");
            return;
        }

        if(TextUtils.isEmpty(eventOrganizerS)){
            eventOrganizerTF.setError("Eneter Organization/Committee name");
            return;
        }

        if(TextUtils.isEmpty(eventTime)){
            Toast.makeText(this, "Choose Event Time", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(eventDate)){
            Toast.makeText(this, "Choose Event Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(eventFormLinkS)){
            eventFormLinkTF.setError("Eneter Organization/Committee name");
            return;
        }

        if(TextUtils.isEmpty(eventDescriptionS)){
            eventDescriptionTF.setError("Eneter Organization/Committee name");
            return;
        }
         final StorageReference storageRefChild = storageReferenceParent.child(eventOrganizerS +".png");
        UploadTask uploadTask = storageRefChild.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageRefChild.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    Uri downloadedUri = task.getResult();
                    String uploadUri = downloadedUri.toString();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("poster", uploadUri);
                    map.put("time" , eventTime);
                    map.put("date" , eventDate);
                    map.put("eventName" , eventNameS);
                    map.put("committee", eventOrganizerS);
                    map.put("link", eventFormLinkS);
                    map.put("description", eventDescriptionS);
                    map.put("sortingParameter", eventSortingParameter);

                    DocumentReference documentReference = fireStore.collection("CollegeEvents").document(eventNameS);
                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddEventActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddEventActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(AddEventActivity.this, "In progress", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.EventPoster : uploadImage(); break;
            case R.id.UploadEventBtn : uploadToFireStore(); break;
            case R.id.date : callDateFragment(); break;
            case R.id.time : callTimeFragment(); break;
            default: break;
        }
    }
}