package com.example.wogus.katalk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wogus.katalk.Activity.FriendActivity;
import com.example.wogus.katalk.Activity.InsertFriendActivity;
import com.example.wogus.katalk.Activity.MakeChattingActivity;
import com.example.wogus.katalk.Class.Friend;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Handler;
import java.util.logging.LogRecord;

/**
 * Created by wogus on 2018-02-28.
 */

public class AdapterRecommendList extends RecyclerView.Adapter<HolderRecommendList>{

	private Context context;
	private int resourceId;
	private InfoManagement infoManagement;
	ArrayList<String> recommendFreindUUIDList;
	Handler handler;
	RecyclerView recyclerView;
	public int addPosition;

	public AdapterRecommendList(Context context, int resourceId, InfoManagement infoManagement, ArrayList<String> recommendFreindUUIDList, RecyclerView recyclerView){
		this.context=context;
		this.resourceId=resourceId;
		this.infoManagement = infoManagement;
		this.recyclerView = recyclerView;
		this.recommendFreindUUIDList =  new ArrayList<>();
		this.recommendFreindUUIDList.addAll(recommendFreindUUIDList);
		handler = new Handler();
	}
	@Override
	public HolderRecommendList onCreateViewHolder(ViewGroup parent, int viewType) {
		return new HolderRecommendList(LayoutInflater.from(context).inflate(resourceId,parent,false));
	}

	@Override
	public void onBindViewHolder(HolderRecommendList holder, final int position) {
		final User user = infoManagement.findUserByUUID(recommendFreindUUIDList.get(position));

		holder.ivFriendPicture.setImageURI(Uri.parse(user.profilePicture));

//		if(infoManagement.loginUser.friendList.get(user.uuid)==null)
//			holder.tvFriendName.setText(user.name);
//		else
//			holder.tvFriendName.setText(infoManagement.loginUser.friendList.get(user.uuid).getName(infoManagement));
		holder.tvFriendName.setText(user.name);


		holder.btAddFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				infoManagement.loginUser.addFriend(infoManagement.findUserByUUID(recommendFreindUUIDList.get(position)),infoManagement);

				int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
				int deleteUUIDNumber =  deleteItem(recommendFreindUUIDList.get(position));
				recyclerView.scrollToPosition(lastVisibleItemPosition-deleteUUIDNumber-2);

				if(infoManagement.loginUser.recommendFreindUUIDList.size()==5){
					recommendFreindUUIDList.clear();
					recommendFreindUUIDList.addAll(infoManagement.loginUser.recommendFreindUUIDList);
				}
				notifyDataSetChanged();
			}
		});
		if(position == getItemCount()-1 && getItemCount()>5) {
			recommendFreindUUIDList.add(recommendFreindUUIDList.get(addPosition++));
			handler.post(new Runnable() {
				@Override
				public void run() {
					notifyDataSetChanged();
				}
			});
		}
		holder.ivFriendPicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, FriendActivity.class);
				intent.putExtra("uuid", user.uuid);
				context.startActivity(intent);
			}
		});
	}

	public int deleteItem(String uuid){
		int num=0;
		for(int i=getItemCount()-1; i>=0;i-- ){
			if(recommendFreindUUIDList.get(i).equals(uuid)){
				recommendFreindUUIDList.remove(i);
				num++;
			}
		}
		addPosition -= (num-1);
		return num;
	}

	@Override
	public int getItemCount() {
		return recommendFreindUUIDList.size();
	}


}
