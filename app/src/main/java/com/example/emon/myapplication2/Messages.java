package com.example.emon.myapplication2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Messages extends AppCompatActivity {
    private EditText msg;
    private Button sndButton;
    private String messg,my_id,friend_id,myName,friendName;
    private DatabaseReference databaseReference,messageReference;
    private RecyclerView mesageList;
    private String mImg,frndImag;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        name=findViewById(R.id.messageNameId);
        mesageList=findViewById(R.id.messageRecyclerviewId);
        mesageList.setHasFixedSize(true);
        mesageList.setLayoutManager(new LinearLayoutManager(this));
        msg=findViewById(R.id.messageEdittextId);
        my_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        friend_id=getIntent().getStringExtra("message_key");
        sndButton=findViewById(R.id.messageSendButtonId);
        databaseReference= FirebaseDatabase.getInstance().getReference().child(getString(R.string.request));
        databaseReference.child(my_id).child(friend_id).child(getString(R.string.friend_1)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myName=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(my_id).child(friend_id).child(getString(R.string.friend_2)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendName=dataSnapshot.getValue().toString();
                name.setText(friendName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messg=msg.getText().toString();
                send_message();
                showMessage();
                msg.setText("");
            }
        });
        FirebaseDatabase.getInstance().getReference().child("All Users").child(my_id).child("profilepic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mImg =dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("All Users").child(friend_id).child("profilepic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                frndImag =dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        messageReference=FirebaseDatabase.getInstance().getReference().child(getString(R.string.request)).child(my_id).child(friend_id).child(getString(R.string.messages));
    }

    @Override
    protected void onStart() {
        super.onStart();
        showMessage();
    }

    public void showMessage()
    {
        FirebaseRecyclerAdapter<UserImageList,Messages.UploadImageHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserImageList, Messages.UploadImageHolder>(
                UserImageList.class,
                R.layout.usermessagelist,
                Messages.UploadImageHolder.class,
                messageReference

        ) {
            @Override
            protected void populateViewHolder(Messages.UploadImageHolder viewHolder, UserImageList model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());


            }
        };
        mesageList.setAdapter(firebaseRecyclerAdapter);
    }
    public static   class UploadImageHolder extends RecyclerView.ViewHolder{
        View view;
        private DatabaseReference ref;
        private LinearLayout linearLayout;

        public UploadImageHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setTitle(String title)
        {
            TextView image_title=view.findViewById(R.id.tvUserImageTitleId);
            image_title.setText(title);



        }
        public void setDesc(String des)
        {
            TextView image_desc=view.findViewById(R.id.tvUserImageDesId);
            image_desc.setText(des);
        }
        public void setImage(Context context, String image)
        {
            ImageView imageView=view.findViewById(R.id.UserImageId);
            Picasso.get().load(image).into(imageView);
            //imageView1.setImageURI(imageView);

        }


    }
    public void send_message()
    {
            DatabaseReference d1=databaseReference.child(my_id).child(friend_id).child(getString(R.string.messages)).push();


              d1.child("title").setValue(friendName);
              d1.child("image").setValue(mImg);
              d1.child("desc").setValue(messg);
              d1.child("color").setValue("#ff0900");
            DatabaseReference d2=databaseReference.child(friend_id).child(my_id).child(getString(R.string.messages)).push();
        d2.child("title").setValue(friendName);
        d2.child("image").setValue(mImg);
        d2.child("desc").setValue(messg);
        d2.child("color").setValue("#0b45a3");

    }
}
