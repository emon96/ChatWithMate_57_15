package com.example.emon.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friend_Profile_Activity extends AppCompatActivity {
    private String friend_id,name,image,my_id;
    private CircleImageView propic;
    private TextView username;
    private Button send_req,cancel_req;
    int currentState=0;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference,databaseList;
    private FirebaseAuth mAuth;
    private RecyclerView rvImageLIst;
    private String friend1,friend2;
    private int fr=0,frr=0,i,ii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend__profile_);

        friend_id=getIntent().getStringExtra(getString(R.string.friend_add_key));
        propic=findViewById(R.id.friend_profile_img);
        username=findViewById(R.id.friend_profile_name);
        send_req=findViewById(R.id.friend_profile_send_request);
        cancel_req=findViewById(R.id.friend_profile_cancel_request);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference();

        rvImageLIst=findViewById(R.id.friendProfileRecycleview);
        rvImageLIst.setHasFixedSize(true);
        rvImageLIst.setLayoutManager(new LinearLayoutManager(this));
        my_id=mAuth.getCurrentUser().getUid();

        DatabaseReference d1=FirebaseDatabase.getInstance().getReference().child("All Users").child(my_id);
        DatabaseReference d2=FirebaseDatabase.getInstance().getReference().child("All Users").child(friend_id);

        d1.child(getString(R.string.numberOfFriends)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i=Integer.parseInt(dataSnapshot.getValue().toString() ) ;
                i=i+1;
                fr=1;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        d2.child(getString(R.string.numberOfFriends)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ii=Integer.parseInt(dataSnapshot.getValue().toString()) ;
                ii=ii+1;


                frr=1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        mReference.child(getString(R.string.request)).child(friend_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(my_id))
                {
                    String req=dataSnapshot.child(my_id).child(getString(R.string.request_type)).getValue().toString();
                    if(req.equals(getString(R.string.received)))
                    {
                        send_req.setText("cancel request");
                       // currentState=1;
                        send_req.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                send_req.setText("Send request");
                                cancelRequest();
                            }
                        });

                        //Log.d("TAG","--------------------------------------------------");
                    }
                    else if(req.equals("sent"))
                    {
                        send_req.setText("Accept request");
                        currentState=2;
                        send_req.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeFriend();
                                addId();
                            }
                        });

                        cancel_req.setVisibility(View.VISIBLE);
                        //add_friend_request();
                        cancel_req.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                decline_reqest();
                            }
                        });

                        //Log.d("TAG","--------------------------------------------------");
                    }
                    else if(req.equals("friend"))
                    {
                        cancel_req.setVisibility(View.INVISIBLE);
                        send_req.setText("Unfriend");
                        send_req.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Unfriend();
                            }
                        });
                    }
                    else if(req.equals(getString(R.string.req_canceled)))
                    {
                        send_req.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                add_friend_request();
                            }
                        });
                    }
                    else if(req.equals(getString(R.string.unfriended)))
                    {
                        send_req.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                add_friend_request();

                            }
                        });
                    }
                    else if(req.equals(getString(R.string.req_declined)))
                    {
                        cancel_req.setVisibility(View.INVISIBLE);
                        send_req.setText("Send Request");
                        send_req.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                add_friend_request();

                            }
                        });
                    }
                }
                else
                {
                    send_req.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            add_friend_request();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mReference.child("All Users").child(friend_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("username").getValue().toString();
                image=dataSnapshot.child("profilepic").getValue().toString();
                username.setText(name);
                friend2=name;
                Picasso.get().load(image).into(propic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mReference.child("All Users").child(my_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friend1=dataSnapshot.child("username").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    public void makeFriend()
    {
                        //   mReference.child(getString(R.string.request)).child(friend_id).child(my_id).
                        // removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        //  @Override
                        //  public void onComplete(@NonNull Task<Void> task) {
                        //   mReference.child(getString(R.string.request)).child(my_id).child(friend_id).child(getString(R.string.request_type)).
                        //       removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        //  @Override
                        //   public void onComplete(@NonNull Task<Void> task) {
                        send_req.setText("Unfriend");
                        cancel_req.setVisibility(View.INVISIBLE);

                        currentState=5;
                        mReference.child(getString(R.string.request)).child(friend_id).child(my_id)
                                .child(getString(R.string.request_type)).setValue("friend");
                        mReference.child(getString(R.string.request)).child(my_id).child(friend_id)
                                .child(getString(R.string.request_type)).setValue("friend");
                         mReference.child(getString(R.string.request)).child(friend_id).child(my_id)
                                  .child(getString(R.string.friend_1)).setValue(friend1);
                         mReference.child(getString(R.string.request)).child(friend_id).child(my_id)
                                 .child(getString(R.string.friend_2)).setValue(friend2);
                         mReference.child(getString(R.string.request)).child(my_id).child(friend_id)
                                  .child(getString(R.string.friend_1)).setValue(friend1);
                         mReference.child(getString(R.string.request)).child(my_id).child(friend_id)
                                  .child(getString(R.string.friend_2)).setValue(friend2);




    }
    public void addId()
    {


        FirebaseDatabase.getInstance().getReference().child("All Users").child(my_id).child("Friend").child(getString(R.string.numberOfFriends)).setValue(i);
        FirebaseDatabase.getInstance().getReference().child("All Users").child(my_id).child("Friend").child("friend_"+i).setValue(friend_id);
        FirebaseDatabase.getInstance().getReference().child("All Users").child(my_id).child(getString(R.string.numberOfFriends)).setValue(i);
        System.out.println("again printd--------------------------");
        FirebaseDatabase.getInstance().getReference().child("All Users").child(friend_id).child("Friend").child(getString(R.string.numberOfFriends)).setValue(ii);
        FirebaseDatabase.getInstance().getReference().child("All Users").child(friend_id).child("Friend").child("friend_"+ii).setValue(my_id);
        FirebaseDatabase.getInstance().getReference().child("All Users").child(friend_id).child(getString(R.string.numberOfFriends)).setValue(ii);
    }
    public void cancelRequest()
    {
        mReference.child("Request").child(friend_id).child(my_id).child(getString(R.string.request_type)).setValue(getString(R.string.req_canceled));
        mReference.child("Request").child(my_id).child(friend_id).child(getString(R.string.request_type)).setValue(getString(R.string.req_canceled));
    }
    public void Unfriend()
    {
        send_req.setText("Send Request");
        mReference.child("Request").child(friend_id).child(my_id).child(getString(R.string.request_type)).setValue(getString(R.string.unfriended));
        mReference.child("Request").child(my_id).child(friend_id).child(getString(R.string.request_type)).setValue(getString(R.string.unfriended));

    }
    public void decline_reqest()
    {
        mReference.child("Request").child(friend_id).child(my_id).child(getString(R.string.request_type)).setValue(getString(R.string.req_declined));
        mReference.child("Request").child(my_id).child(friend_id).child(getString(R.string.request_type)).setValue(getString(R.string.req_declined));
    }
    public void add_friend_request()
    {

            mReference.child(getString(R.string.request)).child(friend_id).child(my_id).child(getString(R.string.request_type)).
                    setValue(getString(R.string.received)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mReference.child(getString(R.string.request)).child(my_id).child(friend_id).child(getString(R.string.request_type)).
                            setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            send_req.setText("cancel request");
                        }
                    });
                }
            });

                   // });
               // }
           // });
       // }



    }

    @Override
    protected void onStart() {
        super.onStart();
       // databaseList=FirebaseDatabase.getInstance().getReference().child("Upload Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseList= FirebaseDatabase.getInstance().getReference().child("Upload Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseRecyclerAdapter<UserImageList,Friend_Profile_Activity.UploadImageHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserImageList, Friend_Profile_Activity.UploadImageHolder>(
                UserImageList.class,
                R.layout.userimagelist,
                Friend_Profile_Activity.UploadImageHolder.class,
                databaseList

        ) {
            @Override
            protected void populateViewHolder( Friend_Profile_Activity.UploadImageHolder viewHolder, final UserImageList model, int position) {
                //final String user_key=getRef(position).getKey();

                viewHolder.setTitle(model.getTitle(),model.getId());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setId(model.getId());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(model.getId().equals(friend_id))
                        {

                           // Intent friend_intent=new Intent(Friend_Profile_Activity.this,Friend_Profile_Activity.class);
                            //friend_intent.putExtra(getString(R.string.friend_add_key),model.getId());
                            //startActivity(friend_intent);
                        }


                    }
                });




            }
        };
        rvImageLIst.setAdapter(firebaseRecyclerAdapter);
    }
    public static   class UploadImageHolder extends RecyclerView.ViewHolder{
        View view;
        private DatabaseReference ref;

        public UploadImageHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setTitle(String title, final String id)
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
        public void setId(String id)
        {
            //TextView name=view.findViewById(R.id.personNameId);

            //String username = user.getDisplayName();
            //Log.d("id-------------------",id);
        }

    }
}
