package com.example.wogus.katalk.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wogus.katalk.R;

/**
 * Created by wogus on 2018-02-28.
 */

public class HolderRecommendList extends RecyclerView.ViewHolder{
	ImageView ivFriendPicture;
	public TextView tvFriendName;
	Button btAddFriend;

	public HolderRecommendList(View itemView){
		super(itemView);
		ivFriendPicture=itemView.findViewById(R.id.ivFriendPicture);
		tvFriendName=itemView.findViewById(R.id.tvFriendName);
		btAddFriend = itemView.findViewById(R.id.btAddFriend);
	}
}
