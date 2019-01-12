package com.example.emon.myapplication2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emon.myapplication2.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class search_Holder extends RecyclerView.ViewHolder{
    View mView;
    public search_Holder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
    }
    public void setName(String name)
    {
            TextView tv_name=mView.findViewById(R.id.et_friend_name);
            tv_name.setText(name);



    }
    public void setStatus(String status)
    {

            RecyclerView recyclerView=mView.findViewById(R.id.rv_search_friend);
           // recyclerView.setVisibility(View.INVISIBLE);
            TextView tv_status=mView.findViewById(R.id.tv_friend_status);
            tv_status.setText(status);


    }
    public void setProfilepic(Context context,String profilepic)
    {

            //ImageView tv_image=mView.findViewById(R.id.tv_profile_image);
            CircleImageView tv_image=mView.findViewById(R.id.tv_profile_image);
            Picasso.get().load(profilepic).into(tv_image);

    }

    public void setId(String id)
    {
        System.out.println("------------------------"+id);
    }

}
