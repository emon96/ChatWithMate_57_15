package com.example.emon.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FriendsPage extends AppCompatActivity {
    private RecyclerView search_list;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ImageView iv_search;
    EditText et_search;
    String mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);
        mDatabase=FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference();
        search_list=findViewById(R.id.rv_search_friend);
        search_list.setHasFixedSize(true);
        search_list.setLayoutManager(new LinearLayoutManager(this));
        iv_search=findViewById(R.id.search_user);
        et_search=findViewById(R.id.et_user_search);
        search_friend();
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearch=et_search.getText().toString();
                search_friend();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FriendsPage.this,Welcome.class));
    }

    private void search_friend()
    {
        Query search_query=mReference.child("All Users").
                orderByChild("username").
                startAt(mSearch).
                endAt(mSearch+"\uf8ff");

        FirebaseRecyclerAdapter<Search_user,search_Holder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Search_user, search_Holder>(

                Search_user.class,
                R.layout.show_search_friend,
                search_Holder.class,
                search_query
        ) {
            @Override
            protected void populateViewHolder(search_Holder viewHolder, Search_user model, int position) {
                final String user_key=getRef(position).getKey();
                //Log.d("TAG","PopulateView key: "+user_key);
                if(!user_key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    viewHolder.setName(model.getUsername());
                    viewHolder.setStatus(model.getGender());
                    viewHolder.setId(model.getId());
                    viewHolder.setProfilepic(getApplicationContext(), model.getProfilepic());
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent friend_intent=new Intent(FriendsPage.this,Friend_Profile_Activity.class);
                            friend_intent.putExtra(getString(R.string.friend_add_key),user_key);
                            startActivity(friend_intent);

                        }
                    });
                }
                else
                {
                    viewHolder.setName(model.getUsername());
                    viewHolder.setStatus(model.getGender());
                    viewHolder.setId(model.getId());
                    viewHolder.setProfilepic(getApplicationContext(), model.getProfilepic());
                }

            }
        };
        search_list.setAdapter(firebaseRecyclerAdapter);
    }
}
