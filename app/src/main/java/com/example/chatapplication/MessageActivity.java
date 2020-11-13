package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextView textView;
    private DatabaseReference reference;
    private Intent intent;
    private EditText eSend;
    private ImageButton send;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private AdapterSend adapterSend;
    private List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        circleImageView = findViewById(R.id.image_toolbar_message);
        textView = findViewById(R.id.text_toolbar_message);
        intent = getIntent();
        send = findViewById(R.id.image_button_send);
        eSend = findViewById(R.id.edit_text_send);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.recycler_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        final String uId = intent.getStringExtra("getId");
        reference = FirebaseDatabase.getInstance().getReference("user").child(uId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getImage().equals("Default")) {
                    circleImageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImage()).into(circleImageView);
                }
                textView.setText(user.getName());

                readMessage(firebaseUser.getUid(),uId,user.getImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Message = eSend.getText().toString().trim();
                if (!Message.equals("")) {
                    sendMessage(firebaseUser.getUid(), uId, Message);
                } else {
                    Toast.makeText(MessageActivity.this, "empty text", Toast.LENGTH_SHORT).show();
                }
                eSend.setText("");
            }
        });

    }

    private void sendMessage(String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> users = new HashMap<>();
        users.put("sender", sender);
        users.put("receiver", receiver);
        users.put("message", message);

        reference.child("chats").push().setValue(users);


    }

    private void readMessage(final String myId , final String userId , final String imageUrl){
        chatList=new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                    chat.getReceiver().equals(userId) && chat.getSender().equals(myId) ){
                        chatList.add(chat);
                    }

                    adapterSend = new AdapterSend(MessageActivity.this,chatList,imageUrl);
                    recyclerView.setAdapter(adapterSend);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}