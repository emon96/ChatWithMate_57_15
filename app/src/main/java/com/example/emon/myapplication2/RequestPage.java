package com.example.emon.myapplication2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RequestPage extends AppCompatActivity {
    private RecyclerView request_list;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference,mFriend_Ref,mReceiverRef,existRef;
    ImageView iv_search;
    private TextView request_list_page,friend_list_page;
    private String my_id,name,image,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);
        request_list_page=findViewById(R.id.request_list_id);
        friend_list_page=findViewById(R.id.friend_list_id);
        friend_list_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestPage.this,Friend_Tab.class));
            }
        });
        my_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase=FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference().child(getString(R.string.request)).child(my_id);
        mFriend_Ref=mDatabase.getReference().child(getString(R.string.users));
        request_list=findViewById(R.id.rv_request_list_id);
        request_list.setHasFixedSize(true);
        request_list.setLayoutManager(new LinearLayoutManager(this));
        iv_search=findViewById(R.id.search_user);
        existRef=mDatabase.getReference();
        Request_Friend_list();
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestPage.this,FriendsPage.class));
            }
        });

    mReceiverRef=mDatabase.getReference();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(RequestPage.this,Welcome.class));
    }

    private void Request_Friend_list()
    {

        FirebaseRecyclerAdapter<Search_user,search_Holder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Search_user, search_Holder>(

                Search_user.class,
                R.layout.show_search_friend,
                search_Holder.class,
                mReference
        ) {
            @Override
            protected void populateViewHolder(final search_Holder viewHolder, Search_user model, int position) {
                final String friend_key=getRef(position).getKey();
                existRef.child("Request").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(my_id))
                        {
                            mReceiverRef.child(getString(R.string.request)).child(my_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {

                                    String request=dataSnapshot.child(friend_key).child(getString(R.string.request_type)).getValue().toString();
                                    if(request.equals(getString(R.string.received)))
                                    {
                                        mFriend_Ref.child(friend_key).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                name=dataSnapshot.child("username").getValue().toString();
                                                image=dataSnapshot.child("profilepic").getValue().toString();
                                                status=dataSnapshot.child("gender").getValue().toString();
                                                viewHolder.setName(name);
                                                viewHolder.setStatus(status);
                                                viewHolder.setProfilepic(getApplicationContext(),image);
                                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent friend_intent=new Intent(RequestPage.this,Friend_Profile_Activity.class);
                                                        friend_intent.putExtra(getString(R.string.friend_add_key),friend_key);
                                                        startActivity(friend_intent);

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else
                                    {
                                        request_list.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        };
        request_list.setAdapter(firebaseRecyclerAdapter);
    }
}
