package com.example.wogus.katalk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.wogus.katalk.Activity.ChattingRoomActivity;
import com.example.wogus.katalk.Activity.MainActivity;
import com.example.wogus.katalk.Class.Chatting;
import com.example.wogus.katalk.Class.ChattingRoom;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.Message;
import com.example.wogus.katalk.R;


import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by wogus on 2018-02-06.
 */

public class AdapterChattingList extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<String> chattingroomUUIDList;      	// 채팅방리스트
	private HashMap<String,ChattingRoom> chattingroomMap;		//
	private InfoManagement infoManagement;
	View.OnTouchListener onTouchListener;
	MainActivity mainActivity;
	public AdapterChattingList(Context context, InfoManagement infoManagement,View.OnTouchListener onTouchListener,MainActivity mainActivity) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.infoManagement = infoManagement;
		this.chattingroomUUIDList = infoManagement.loginUser.chattingroomUUIDList;
		this.chattingroomMap = infoManagement.loginUser.chattingroomMap;
		this.onTouchListener = onTouchListener;
		this.mainActivity = mainActivity;
	}
	@Override
	public int getCount() {
		return chattingroomUUIDList.size();
	}

	@Override
	public ChattingRoom getItem(int position) {
		return chattingroomMap.get(chattingroomUUIDList.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_chattingroom, parent, false);                    // listview_friend.xml 생성

			HolderChattingRoomList holder = new HolderChattingRoomList();

			holder.ivEmoticon = convertView.findViewById(R.id.ivEmoticon);
			holder.ivChattingRoomPicture =  convertView.findViewById(R.id.ivChattingRoomPicture);
			holder.tvChattingCal = convertView.findViewById(R.id.tvChattingCal);
			holder.tvChattingRoomName = convertView.findViewById(R.id.tvChattingRoomName);
			holder.tvLastMessage = convertView.findViewById(R.id.tvLastMessage);
			holder.tvUnreadMessageNum = convertView.findViewById(R.id.tvUnreadMessageNum);
			holder.tvUserNumber = convertView.findViewById(R.id.tvUserNumber);

			convertView.setTag(holder);
		}
		final ChattingRoom chattingRoom =getItem(position);
		final Chatting chatting = chattingRoom.chatting;
		HolderChattingRoomList holder = (HolderChattingRoomList)convertView.getTag();

		String picture=infoManagement.findUserByUUID(infoManagement.loginUser.uuid).profilePicture;

		for(int i=0; i< chatting.uuidList.size();i++){
			if(!chatting.uuidList.get(i).equals(infoManagement.loginUser.uuid)){
				picture = infoManagement.findUserByUUID(chatting.uuidList.get(i)).profilePicture;
				break;
			}
		}

		Message lastMessage = chattingRoom.messageList.get(chattingRoom.messageList.size()-1);
		holder.ivChattingRoomPicture.setImageURI(Uri.parse(picture));
		holder.tvChattingCal.setText(lastMessage.time_format());

		if(chattingRoom.unreadMessageNum==0)
			holder.tvUnreadMessageNum.setVisibility(View.GONE);
		else {
			holder.tvUnreadMessageNum.setVisibility(View.VISIBLE);
			if(chattingRoom.unreadMessageNum<200)
				holder.tvUnreadMessageNum.setText(chattingRoom.unreadMessageNum + "");
			else {
				holder.tvUnreadMessageNum.setWidth((int)(45*inflater.getContext().getResources().getDisplayMetrics().density));
				holder.tvUnreadMessageNum.setText("200+");
			}
		}

		holder.tvChattingRoomName.setText(chattingRoom.ChattingRoomName(infoManagement));
		holder.tvLastMessage.setText(lastMessage.message);

		if(chatting.type==1){
			holder.tvUserNumber.setVisibility(View.VISIBLE);
			holder.tvUserNumber.setText(chatting.uuidList.size()+"");
		}else{
			holder.tvUserNumber.setVisibility(View.GONE);
		}
		if(lastMessage.isImage){
			holder.ivEmoticon.setVisibility(View.VISIBLE);
			holder.ivEmoticon.setBackgroundResource(lastMessage.emoticon);
			AnimationDrawable animationDrawable = (AnimationDrawable)holder.ivEmoticon.getBackground();
			animationDrawable.stop();
			animationDrawable.start();
		}else{
			holder.ivEmoticon.setVisibility(View.GONE);
		}

		convertView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view){
				mainActivity.displayMenuBar(position);
				return true;
			}
		});
		convertView.setOnClickListener(new View.OnClickListener() {            // 사용자 사진 클릭시 프로필사진 변경 activity로 이동
			@Override
			public void onClick(View view) {											// 사용자 이미지 클릭시
				Intent intent = new Intent(inflater.getContext(), ChattingRoomActivity.class);
				intent.putExtra("type",1);
				intent.putExtra("chattingRoomPosition",position);
				inflater.getContext().startActivity(intent);
			}
		});
		convertView.setOnTouchListener(onTouchListener);
		return convertView;
	}
}
