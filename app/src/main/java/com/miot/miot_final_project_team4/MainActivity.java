package com.miot.miot_final_project_team4;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference("/");
    Typewriter message;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler animationCompleteCallBack = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.i("Log", "Animation Completed");
                return false;
            }
        });
        message = new Typewriter(this);
        message.setCharacterDelay(100);
        message.setTextSize(30);
        message.setTypeface(null, Typeface.BOLD);
        message.setPadding(20, 20, 20, 20);
        message.setAnimationCompleteListener(animationCompleteCallBack);


        setContentView(message);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });



        myRef.child("user-data/user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap newSnap = (HashMap<String, Object>)dataSnapshot.getValue();
                if (newSnap.containsKey("description")) {
                    HashMap<String, Object> description = (HashMap<String, Object>) newSnap.get("description");
                    if (description.containsKey("captions")) {
                        HashMap<String, Object> captions = (HashMap<String, Object>)(((ArrayList<HashMap>)description.get("captions")).get(0));
                        if (captions.containsKey("text")) {
                            String text = captions.get("text").toString();
                            message.animateText(text);
                            //noinspection deprecation
                            t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, String> map = (HashMap<String, String>)dataSnapshot.getValue();
                for (String key: map.keySet()) {

                }
                String x = dataSnapshot.getValue().toString();
                message.setText(x);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


    }
}
