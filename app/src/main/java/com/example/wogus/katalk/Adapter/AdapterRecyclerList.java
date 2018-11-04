package com.example.wogus.katalk.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wogus.katalk.Activity.MakeChattingActivity;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wogus on 2018-02-20.
 */


public class AdapterRecyclerList  extends RecyclerView.Adapter<HolderRecyclerList>{

	private Context context;
	private int resourceId;
	private InfoManagement infoManagement;

	public MakeChattingActivity makeChattingActivity;
	public ArrayList<String> dataList;
	public HashMap<String,Boolean> isChecked;
	public AdapterMakeChattinglist adapterMakeChattinglist;

	public AdapterRecyclerList(Context context,int resourceId,InfoManagement infoManagement,HashMap<String,Boolean> isChecked,MakeChattingActivity makeChattingActivity){
		this.context=context;
		this.resourceId=resourceId;
		this.infoManagement = infoManagement;
		this.isChecked = isChecked;
		this.dataList = new ArrayList<>();
		this.makeChattingActivity = makeChattingActivity;
	}
	@Override
	public HolderRecyclerList onCreateViewHolder(ViewGroup parent, int viewType) {
		return new HolderRecyclerList(LayoutInflater.from(context).inflate(resourceId,parent,false));
	}

	@Override
	public void onBindViewHolder(HolderRecyclerList holder, int position) {
		final User user = infoManagement.findUserByUUID(dataList.get(position));

		holder.ivFriendPicture.setImageURI(Uri.parse(user.profilePicture));
		holder.tvFriendName.setText(user.name);
		holder.ivDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isChecked.put(user.uuid,false);
				dataList.remove(user.uuid);

				adapterMakeChattinglist.notifyDataSetChanged();
				notifyDataSetChanged();
				makeChattingActivity.setAbove();
			}
		});
	}
	@Override
	public int getItemCount() {
		return dataList.size();
	}

}
