package com.example.wogus.katalk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.example.wogus.katalk.Activity.FriendActivity;
import com.example.wogus.katalk.Activity.MainActivity;
import com.example.wogus.katalk.Class.Friend;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wogus on 2018-02-04.
 */

public class AdapterFriendList extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<String> friendUUIDList;
	private InfoManagement infoManagement;
	View.OnTouchListener onTouchListener;
	MainActivity mainActivity;

	public AdapterFriendList(Context context,InfoManagement infoManagement,View.OnTouchListener onTouchListener,MainActivity mainActivity) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.friendUUIDList = infoManagement.loginUser.friendUUIDList;
		this.infoManagement = infoManagement;
		this.onTouchListener = onTouchListener;
		this.mainActivity = mainActivity;
	}
	@Override
	public int getCount() {
		return friendUUIDList.size();
	}

	@Override
	public String getItem(int position) {
		return friendUUIDList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_friend, parent, false);						// listview_friend.xml 생성

			HolderFriendList holder = new HolderFriendList();

			holder.ivFriendPicture = convertView.findViewById(R.id.ivFriendPciture);										// listview_friend.xml에 있는 ivFriendPicture가져옴
			holder.tvFriendName = convertView.findViewById(R.id.tvFriendName);											// listview_friend.xml에 있는 tvFriendName가져옴
			holder.tvFriendStateMessage = convertView.findViewById(R.id.tvFriendStateMessage);							// listview_friend.xml에 있는 tvFriendStateMessage가져옴
			holder.tvMyProfile = convertView.findViewById(R.id.tvMyProfile);
			holder.tvFriend = convertView.findViewById(R.id.tvFriend);
			holder.tvLine1 = convertView.findViewById(R.id.tvLine1);
			holder.tvLine2 = convertView.findViewById(R.id.tvLine2);
			holder.tvLine3 = convertView.findViewById(R.id.tvLine3);
			holder.tvLine4 = convertView.findViewById(R.id.tvLine4);
			holder.rlUserInfo = convertView.findViewById(R.id.rlUserInfo);
			convertView.setTag(holder);
		}

		final User friend = infoManagement.findUserByUUID(friendUUIDList.get(position));
		final Friend Friend = infoManagement.loginUser.friendList.get(friendUUIDList.get(position));
		HolderFriendList holder = (HolderFriendList)convertView.getTag();

		if(position==0){
			holder.tvMyProfile.setVisibility(View.VISIBLE);
			holder.tvFriend.setVisibility(View.VISIBLE);
			holder.tvLine1.setVisibility(View.VISIBLE);
			holder.tvLine2.setVisibility(View.VISIBLE);
			holder.tvLine3.setVisibility(View.VISIBLE);
			holder.tvLine4.setVisibility(View.GONE);
		}else{
			holder.tvMyProfile.setVisibility(View.GONE);
			holder.tvFriend.setVisibility(View.GONE);
			holder.tvLine1.setVisibility(View.GONE);
			holder.tvLine2.setVisibility(View.GONE);
			holder.tvLine3.setVisibility(View.GONE);
			holder.tvLine4.setVisibility(View.VISIBLE);
		}

		holder.ivFriendPicture.setImageURI(Uri.parse(friend.profilePicture));
		holder.tvFriendName.setText(Friend.getName(infoManagement));

		if(friend.stateMessage==null){
			holder.tvFriendStateMessage.setVisibility(View.GONE);
		}else {
			holder.tvFriendStateMessage.setVisibility(View.VISIBLE);
			holder.tvFriendStateMessage.setText(friend.stateMessage);
		}
		holder.rlUserInfo.setOnClickListener(new View.OnClickListener() {            // 사용자 사진 클릭시 프로필사진 변경 activity로 이동
			@Override
			public void onClick(View view) {											// 사용자 이미지 클릭시
				Intent intent = new Intent(inflater.getContext(), FriendActivity.class);
				intent.putExtra("uuid", friend.uuid);
				inflater.getContext().startActivity(intent);
			}
		});
		holder.rlUserInfo.setOnTouchListener(onTouchListener);
		if(position!=0){
			holder.rlUserInfo.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View view){
					mainActivity.displayFriendMenuBar(position);
					return true;
				}
			});
		}
		return convertView;
	}
}
