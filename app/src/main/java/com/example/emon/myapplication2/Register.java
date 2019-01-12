package com.example.emon.myapplication2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    private EditText email,password,username,conPassword,phoneNo;
    private Button submit;
    private Button button;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CircleImageView imageButton;
     private Uri resultUri;
    private String photoStringLink;
    private StorageReference storageReference;
    private static final int code=10;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private String gender;
    private int selected;
    private String imgUri;
   // Button imageButton;
    //CircleImageView circleImageView;
    private TextView faltu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog=new ProgressDialog(this);
        username=findViewById(R.id.registerUsernameId);
        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.registeremailId);
        password=findViewById(R.id.registepasswordId);
        conPassword=findViewById(R.id.registeconfirmpasswordId);
        imageButton=findViewById(R.id.imageButtonId);
        submit=findViewById(R.id.registerSubmitId);
        phoneNo=findViewById(R.id.registerPhoneId);
        radioGroup=findViewById(R.id.registerRadiogroup);
        //circleImageView=findViewById(R.id.regImageId);
        storageReference=FirebaseStorage.getInstance().getReference().child("Profile Images");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("All Users");
        databaseReference2=FirebaseDatabase.getInstance().getReference().child("User List");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRegister();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select picture"),code);
            }
        });
        faltu=findViewById(R.id.faltuview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        email.setText("");
        password.setText("");
        username.setText("");
        conPassword.setText("");
        phoneNo.setText("");
    }

    public void UserRegister()
    {

        final String em,ps,uname,conPass,phone;
        uname=username.getText().toString();
        em=email.getText().toString();
        ps=password.getText().toString();
        phone=phoneNo.getText().toString();
        conPass=conPassword.getText().toString();
        if(TextUtils.isEmpty(resultUri.toString()))
        {
            Toast.makeText(Register.this,"Please,Select an image",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(em)||TextUtils.isEmpty(ps)||TextUtils.isEmpty(conPass))
        {
            Toast.makeText(Register.this,"Fields cant be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!ps.equals(conPass))
        {
            Toast.makeText(Register.this,"Confirm Password Correctly",Toast.LENGTH_LONG);
            password.setText("");
            conPassword.setText("");
            return;
        }
        selected=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(selected);
        gender=radioButton.getText().toString();

         progressDialog.setMessage("Registering........");

         //progressDialog.show();
        final StorageReference filepath=storageReference.child(resultUri.getLastPathSegment());
        filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
               // Toast.makeText(Register.this,"Successful 1",Toast.LENGTH_SHORT).show();
                firebaseAuth.createUserWithEmailAndPassword(em,ps).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // Toast.makeText(Register.this,"Successful 2",Toast.LENGTH_SHORT).show();
                        if(task.isSuccessful())
                        {


                           // Toast.makeText(Register.this,"Successful 3",Toast.LENGTH_SHORT).show();
                            photoStringLink=taskSnapshot.toString();
                           // String downloadUri=taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(Register.this,"Registerd successful",Toast.LENGTH_SHORT).show();
                            // startActivity(new Intent(Register.this,MainActivity.class));
                            String userId=firebaseAuth.getCurrentUser().getUid();
                            final DatabaseReference current_user=databaseReference.child(userId);
                            current_user.child("username").setValue(uname);
                            current_user.child("email").setValue(em);
                            current_user.child("password").setValue(ps);
                            current_user.child("phone").setValue(phone);
                            current_user.child(getString(R.string.numberOfFriends)).setValue(0);
                            //current_user.child("image").setValue(photoStringLink);
                            final String string=photoStringLink;
                           // faltu.setText(sss[0]);
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgUri=uri.toString();
                                    current_user.child("profilepic").setValue(imgUri);
                                }
                            });

                            if(gender.equals("Female"))
                            {
                                current_user.child("gender").setValue("Female");
                            }
                            else
                            {
                                current_user.child("gender").setValue("Male");
                            }
                            //current_user.child("uri").setValue(resultUri);

                           // progressDialog.dismiss();

                           //

                        }
                        else
                        {
                            Toast.makeText(Register.this,"Not Successful",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
               // startActivity(new Intent(Register.this,AfterRegister.class));
              //  finish();
            }

        });
        filepath.getDownloadUrl();



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==code && resultCode==RESULT_OK )
        {
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1).start(this);

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {

                resultUri=result.getUri();
                imageButton.setImageURI(resultUri);
                //circleImageView.setImageURI(resultUri);
                //Toast.makeText(Register.this,"in result "+resultUri,Toast.LENGTH_SHORT).show();
            }
            else if(code==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception exception=result.getError();
            }
        }
    }



}
