package com.example.wogus.katalk.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wogus.katalk.R;

/**
 * Created by wogus on 2018-02-21.
 */

public class HolderRecyclerList extends RecyclerView.ViewHolder{
	ImageView ivFriendPicture;
	ImageView ivDelete;
	TextView tvFriendName;

	public HolderRecyclerList(View itemView){
		super(itemView);
		ivFriendPicture=itemView.findViewById(R.id.ivFriendPicture);
		ivDelete = itemView.findViewById(R.id.ivX);
		tvFriendName=itemView.findViewById(R.id.tvFriendName);
	}
}