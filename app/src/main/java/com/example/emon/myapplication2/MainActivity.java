package com.example.emon.myapplication2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    private EditText email,password;
    private Button login,register;
    public FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "MainActivity";
    private SignInButton signInButton;
    private ProgressBar progressBar;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton=findViewById(R.id.googleSignButton);
        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.signemailId);
        password=findViewById(R.id.signpasswordId);
        login=findViewById(R.id.signLoginId);
        register=findViewById(R.id.signCreateAccountId);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    startActivity(new Intent(MainActivity.this,Welcome.class));

                }
            }
        };
        databaseReference=FirebaseDatabase.getInstance().getReference().child("All Users");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sign_in_google();
            }
        });

    }
    public void signInGSO()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();




    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        password.setText("");
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void SignIn()
   {
       String em=email.getText().toString();
       String pas=password.getText().toString();
       if(TextUtils.isEmpty(em)||TextUtils.isEmpty(pas))
       {
           Toast.makeText(MainActivity.this,"Fiels are empty!",Toast.LENGTH_SHORT).show();
           return;
       }
       firebaseAuth.signInWithEmailAndPassword(em,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
          if(task.isSuccessful())
          {

            //  Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
              //gotoWelcome();
              chekUser();

          }
          else
          {
              Toast.makeText(MainActivity.this,"Login Unsuccessful",Toast.LENGTH_SHORT).show();
              return;
          }
           }
       });

   }
   public void gotoWelcome()
   {
       startActivity(new Intent(MainActivity.this,Welcome.class));
   }
   private void chekUser()
   {
       final String user_id=firebaseAuth.getCurrentUser().getUid();
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChild(user_id))
               {
                   startActivity(new Intent(MainActivity.this,Welcome.class));
               }
               else
               {
                    Toast.makeText(MainActivity.this,"You need to create an account",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,Register.class));
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
   }


}