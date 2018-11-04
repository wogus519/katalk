package com.example.wogus.katalk.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.example.wogus.katalk.Class.InfoManagement;

import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.util.ArrayList;

/**
 * Created by wogus on 2018-02-21.
 */
public class AdapterDrawerLayout extends BaseAdapter{
	private LayoutInflater inflater;
	private InfoManagement infoManagement;
	private ArrayList<String> userUUIDList;

	public AdapterDrawerLayout(Context context, InfoManagement infoManagement,ArrayList<String> userUUIDList){
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.infoManagement = infoManagement;
		this.userUUIDList = userUUIDList;
	}

	@Override
	public int getCount() {
		return userUUIDList.size();
	}

	@Override
	public String getItem(int position) {
		return userUUIDList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.listview_drawer_friend, parent, false);						// listview_friend.xml 생성

			HolderDrawerLayout holder = new HolderDrawerLayout();

			holder.ivUserPicture = convertView.findViewById(R.id.ivUserPicture);										// listview_friend.xml에 있는 ivFriendPicture가져옴
			holder.tvUserName = convertView.findViewById(R.id.tvUserName);											// listview_friend.xml에 있는 tvFriendName가져옴
			holder.btInsertFriend = convertView.findViewById(R.id.btInsertFriend);

			convertView.setTag(holder);
		}
		HolderDrawerLayout holder = (HolderDrawerLayout)convertView.getTag();
		final User user = infoManagement.findUserByUUID(getItem(position));

		holder.ivUserPicture.setImageURI(Uri.parse(user.profilePicture));
		if(infoManagement.loginUser.friendList.get(user.uuid)==null)
			holder.tvUserName.setText(user.name);
		else
			holder.tvUserName.setText(infoManagement.loginUser.friendList.get(user.uuid).getName(infoManagement));

		if(infoManagement.loginUser.friendList.get(user.uuid)==null){
			holder.btInsertFriend.setVisibility(View.VISIBLE);
		}else{
			holder.btInsertFriend.setVisibility(View.GONE);
		}
		holder.btInsertFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				infoManagement.loginUser.addFriend(user,infoManagement);
				view.setVisibility(View.GONE);
			}
		});
		return convertView;
	}

}
