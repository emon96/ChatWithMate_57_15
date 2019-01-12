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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImagePostActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private RecyclerView search_list;
    private Uri imageUri;
    private EditText title,desctiption;
    private StorageReference storageReference;
    private Uri resultUri;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,mdata,dataRef1,dataRef2;
    private static final int code=10;
    private Button postButton;
    FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private DatabaseReference data1,data2;
    private Integer n,i,j=0,k,x=0,p=0;
    private String[] friend=new String[100];
    private TextView textView;
    private String name,imageuri,Desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Upload Images");

        data1=FirebaseDatabase.getInstance().getReference().child("Upload Images");
        imageButton=findViewById(R.id.postimageButtonId);
        desctiption=findViewById(R.id.posDesId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addPost();
                selectImage();
            }
        });
        storageReference= FirebaseStorage.getInstance().getReference();

        firebaseAuth=FirebaseAuth.getInstance();
        postButton=findViewById(R.id.postButtonId);
        firebaseUser=firebaseAuth.getCurrentUser();
        mdata=FirebaseDatabase.getInstance().getReference().child("All Users").child(firebaseUser.getUid());
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post1();
            }
        });
        x=0;
        progressBar=findViewById(R.id.imagePostProgressbarId);
    }
    public void post2()
    {
        dataRef1=databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        startPost(dataRef1);
        //dataRef1=mdata.child("Upload Image").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //startPost(dataRef1);

        //startPost(FirebaseDatabase.getInstance().getReference().child("All Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Upload Images"));

        finish();
        startActivity(new Intent(ImagePostActivity.this,Welcome.class));
    }
    public void post1()
    {



        FirebaseDatabase.getInstance().getReference().child("All Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.numberOfFriends)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int p;
                n=Integer.parseInt(dataSnapshot.getValue().toString());
                for ( p=1;p<=n;p++)
                {
                    // final int finalI = i;


                    final int finalP = p;
                    FirebaseDatabase.getInstance().getReference().child("All Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Friend").child("friend_"+p).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //DatabaseReference d1=FirebaseDatabase.getInstance().getReference().child("Upload Images");
                            // final DatabaseReference d2 = d1.child(dataSnapshot.getValue().toString()).push();
                            //d2.child("title").setValue(name);
                            System.out.println("id--------"+ finalP +"----------------------------------------"+dataSnapshot.getValue().toString());
                            dataRef1=databaseReference.child(dataSnapshot.getValue().toString());
                            startPost(dataRef1);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    })  ;
                }
                j=2;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        post2();
        //addPost();

    }
    public void startPost(final DatabaseReference dataRef)
    {

        final String des=desctiption.getText().toString();
        Desc=des;

        final StorageReference filepath=storageReference.child("Upload Images").child(resultUri.getLastPathSegment());
        filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(0);
                    }
                },1000);
                mdata.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downUri =uri;

                                Toast.makeText(ImagePostActivity.this,"Upload Successful",Toast.LENGTH_SHORT).show();
                                String downloadUri=downUri.toString();

                                    final DatabaseReference current_user=dataRef.push();
                                    //String userId=firebaseAuth.getCurrentUser().getUid();
                                    // DatabaseReference current_user=databaseReference.child(userId);
                                    //DatabaseReference new_post=databaseReference.child(userId);
                                    //DatabaseReference new_post=current_user.push();
                                    // DatabaseReference new_post=current_user.child(userId);
                                    //DatabaseReference new_post=databaseReference.push();
                                    System.out.println("in -------------------not loop------------"+dataRef);
                                    // FirebaseUser  user=FirebaseAuth.getInstance().getCurrentUser();
                                    // ref=FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference().child("All Users");
                                    databaseReference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            try {
                                                if (snapshot.getValue() != null) {
                                                    try {
                                                        // Log.e("TAG", "" + snapshot.getValue()); // your name values you will get here
                                                        //String username = user.getDisplayName();
                                                        // TextView name=view.findViewById(R.id.personNameId);
                                                        //name.setText(snapshot.getValue().toString());
                                                        name=snapshot.getValue().toString();
                                                        current_user.child("title").setValue(snapshot.getValue().toString());
                                                        //image_title.setText(snapshot.getValue().toString());
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

                                    //current_user.child("title").setValue(tit);
                                    current_user.child("desc").setValue(des);
                                    current_user.child("image").setValue(downloadUri);
                                    imageuri=downloadUri;
                                    current_user.child("id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());


                                if(x==0)
                                {
                                    //addPost(name,des,downloadUri);
                                   // x=x+1;
                                }

                              /*  for(int i=0;i<n;i++)
                                {
                                    String s=friend[i];
                                    data2= data1.child(friend[i]);
                                   final DatabaseReference data=data2.push();
                                    data.child("desc").setValue(des);
                                    data.child("title").setValue(name);
                                    data.child("image").setValue(downloadUri);
                                    data.child("id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                }*/
                                //Intent intent=new Intent(ImagePostActivity.this,Welcome.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //startActivity(intent);
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImagePostActivity.this,"Upload Failed,Try Again",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double prgress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressBar.setProgress((int)prgress);

            }
        });


    }
    public void addPost(String n,String d,String i)
    {
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("All Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Upload Images");
            DatabaseReference d1=databaseReference.push();

            d1.child("desc").setValue(d);
            d1.child("image").setValue(i);
            d1.child("title").setValue(n);


    }
    public static class post_Holder extends RecyclerView.ViewHolder{
        View mView;
        public post_Holder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setName(String name)
        {
            // TextView tv_name=mView.findViewById(R.id.et_friend_name);
            // tv_name.setText(name);
        }
        public void setStatus(String status)
        {
            // TextView tv_status=mView.findViewById(R.id.tv_friend_status);
            // tv_status.setText(status);
        }
        public void setProfilepic(Context context, String profilepic)
        {
            //ImageView tv_image=mView.findViewById(R.id.tv_profile_image);
            //CircleImageView tv_image=mView.findViewById(R.id.tv_profile_image);
            //Picasso.get().load(profilepic).into(tv_image);
        }
        public void setId(String id)
        {
            Log.d("id--------------------",id);
        }

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
                    .setAspectRatio(9,13).start(this);

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
//Toast.makeText(Register.this,"in result",Toast.LENGTH_SHORT).show();
                resultUri=result.getUri();
                imageButton.setImageURI(resultUri);

            }
            else if(code==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception exception=result.getError();
            }
        }
    }



}