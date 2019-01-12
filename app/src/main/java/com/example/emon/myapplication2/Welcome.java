package com.example.emon.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Welcome extends AppCompatActivity {
    RecyclerView rvImageLIst;
    DatabaseReference databaseList;
    private ImageView imageView;
    private LinearLayout layout;
    private CircleImageView profilePic;
    private ImageButton optionBar,findFriend,messanger;

    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;
    FirebaseAuth mAuth;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        layout=findViewById(R.id.welcome_layoutId);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this,UserProfile.class));
            }
        });
        profilePic=findViewById(R.id.welcome_profile_pic);
        name=findViewById(R.id.welcome_nameId);
        //imageView1=findViewById(R.id.showimage);
    rvImageLIst=findViewById(R.id.rvimageListId);
    findFriend=findViewById(R.id.find_friend_menu);
    findFriend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(Welcome.this,RequestPage.class));
        }
    });
    rvImageLIst.setHasFixedSize(true);
    rvImageLIst.setLayoutManager(new LinearLayoutManager(this));
    authStateListener=new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(user==null)
            {
                startActivity(new Intent(Welcome.this,MainActivity.class));
            }
        }
    };
    optionBar=findViewById(R.id.option_menubarId);
    mAuth=FirebaseAuth.getInstance();
    databaseList= FirebaseDatabase.getInstance().getReference().child("Upload Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    //databaseList.keepSynced(true);
        optionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this,OptionPage.class));
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("All Users");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            name.setText(snapshot.getValue().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println( " it's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilepic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {

                            String propicUri= snapshot.getValue().toString();
                            Picasso.get().load(propicUri).into(profilePic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println( " it's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        messanger=findViewById(R.id.messenger_bar);
        messanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this,Messanger.class));
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menubar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.postimageId)
        {
            startActivity(new Intent(Welcome.this,ImagePostActivity.class));
        }
        if(item.getItemId()==R.id.menu_logout)
        {
            logOut();
        }
        return super.onOptionsItemSelected(item);
    }
    private void logOut()
    {

            mAuth.signOut();
            finish();
        //startActivity(new Intent(Welcome.this,MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<UserImageList,UploadImageHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserImageList, UploadImageHolder>(
                UserImageList.class,
                R.layout.userimagelist,
                UploadImageHolder.class,
                databaseList

        ) {
            @Override
            protected void populateViewHolder(UploadImageHolder viewHolder, final UserImageList model, int position) {
                //final String user_key=getRef(position).getKey();
                viewHolder.setTitle(model.getTitle(),model.getId());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setId(model.getId());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!model.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                            {
                            Intent friend_intent=new Intent(Welcome.this,Friend_Profile_Activity.class);
                            friend_intent.putExtra(getString(R.string.friend_add_key),model.getId());
                            startActivity(friend_intent);
                            }
                            else
                            {
                                startActivity(new Intent(Welcome.this,UserProfile.class));
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
