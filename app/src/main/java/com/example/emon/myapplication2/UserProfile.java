package com.example.emon.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class UserProfile extends AppCompatActivity {
    private ImageButton back;
    private CircleImageView pic;
    private TextView name,gender,email,noOfFriend,phone;
    private String propicUri;
    private Uri resultUri,imageUri;
    private TextView uriView,changePic;
    private int code=1;
    private StorageReference storageReference;
    private DatabaseReference mdata,databaseList;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private TextView nameProfile;
    String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        gender=findViewById(R.id.profile_gender);
        email=findViewById(R.id.profile_email);
        recyclerView=findViewById(R.id.userProfileRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nameProfile=findViewById(R.id.profileNameId);
        noOfFriend=findViewById(R.id.profile_noOfFriend);
        phone=findViewById(R.id.profile_phone);
        pic=findViewById(R.id.profile_pic);
        name=findViewById(R.id.profile_username);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("All Users");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            uname=snapshot.getValue().toString();
                            name.setText(snapshot.getValue().toString());
                            nameProfile.setText(snapshot.getValue().toString()+"'s profile");
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
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            gender.setText(snapshot.getValue().toString());
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
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            phone.setText(snapshot.getValue().toString());
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
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            email.setText(snapshot.getValue().toString());
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
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.numberOfFriends)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            noOfFriend.setText(snapshot.getValue().toString());
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

                            propicUri= snapshot.getValue().toString();
                            Picasso.get().load(propicUri).into(pic);
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
        changePic=findViewById(R.id.changePicId);
     pic.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
                selectImage();
         }
     });
     changePic.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Change();
         }
     });
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        mdata=FirebaseDatabase.getInstance().getReference().child("All Users").child(firebaseUser.getUid());
    }
    @Override
    protected void onStart() {
        super.onStart();

        // databaseList=FirebaseDatabase.getInstance().getReference().child("Upload Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseList= FirebaseDatabase.getInstance().getReference().child("Upload Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseRecyclerAdapter<UserImageList,UserProfile.UploadImageHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserImageList, UserProfile.UploadImageHolder>(
                UserImageList.class,
                R.layout.userimagelist,
                UserProfile.UploadImageHolder.class,
                databaseList

        ) {
            @Override
            protected void populateViewHolder( UserProfile.UploadImageHolder viewHolder, final UserImageList model, int position) {
                //final String user_key=getRef(position).getKey();

                viewHolder.setTitle(uname,model.getId());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setId(model.getId());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                    }
                });




            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
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
    public void Change()
    {
        final StorageReference filepath=storageReference.child("Profile Images").child(resultUri.getLastPathSegment());
        filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                mdata.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downUri =uri;
                                Toast.makeText(UserProfile.this,"Profile Picture Changed",Toast.LENGTH_SHORT).show();
                                String downloadUri=downUri.toString();
                              //  final DatabaseReference current_user=databaseReference.push();
                               // String userId=firebaseAuth.getCurrentUser().getUid();
                                // DatabaseReference current_user=databaseReference.child(userId);
                               // DatabaseReference new_post=databaseReference.child(userId);
                                //DatabaseReference new_post=current_user.push();
                                // DatabaseReference new_post=current_user.child(userId);
                                //DatabaseReference new_post=databaseReference.push();
                                mdata.child("profilepic").setValue(downloadUri);
                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                // ref=FirebaseDatabase.getInstance().getReference();
                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("All Users");
                                //current_user.child("title").setValue(tit);
                                //pic.setImageURI(Uri.parse(downloadUri));
                                Picasso.get().load(downloadUri).into(pic);
                               // current_user.child("desc").setValue(des);
                                //current_user.child("image").setValue(downloadUri);
                               // Intent intent=new Intent(ImagePostActivity.this,Welcome.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               // startActivity(intent);
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //   Toast.makeText(ImagePostActivity.this,"in startpost",Toast.LENGTH_SHORT).show();


                // String downloadUri=taskSnapshot.getDownloadUrl().toString();
                // Toast.makeText(Register.this,"Registerd successful",Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(Register.this,MainActivity.class));



            }
        });
    }
    public void selectImage()
    {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select picture"),code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==code && resultCode==RESULT_OK )
        {
            imageUri=data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1).start(this);

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
//Toast.makeText(Register.this,"in result",Toast.LENGTH_SHORT).show();

                resultUri=result.getUri();
                pic.setImageURI(resultUri);

            }
            else if(code==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception exception=result.getError();
            }
        }
    }

}
