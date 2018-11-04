package com.example.wogus.katalk.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.wogus.katalk.Class.Chatting;
import com.example.wogus.katalk.Class.ChattingRoom;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wogus on 2018-02-07.
 */

public class AdapterChattingListEdit extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<String> chattingroomUUIDList;      	// 채팅방리스트
	private HashMap<String,ChattingRoom> chattingroomMap;		//
	private InfoManagement infoManagement;
	private Button btDelete;
	TextView tvSelectNumber;
	public  boolean[] ischecked;
	public int CheckedCheckBoxNum;

	public AdapterChattingListEdit(Context context, InfoManagement infoManagement, Button btDelete,TextView tvSelectNumber) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.chattingroomUUIDList = infoManagement.loginUser.chattingroomUUIDList;
		this.chattingroomMap = infoManagement.loginUser.chattingroomMap;
		this.infoManagement = infoManagement;
		this.btDelete = btDelete;
		this.tvSelectNumber = tvSelectNumber;
		ischecked = new boolean[chattingroomUUIDList.size()];
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
			convertView = inflater.inflate(R.layout.listview_chattingroom_edit, parent, false);

			HolderChattingRoomEdit holder = new HolderChattingRoomEdit();
			holder.ivChattingRoomPicture =  convertView.findViewById(R.id.ivChattingRoomPicture);
			holder.tvChattingRoomName = convertView.findViewById(R.id.tvChattingRoomName);
			holder.tvLastMessage = convertView.findViewById(R.id.tvLastMessage);
			holder.cbIsDelete = convertView.findViewById(R.id.cbIsDelete);
			holder.tvUserNumber = convertView.findViewById(R.id.tvUserNumber);

			convertView.setTag(holder);
		}

		final ChattingRoom chattingRoom= getItem(position);
		final Chatting chatting = chattingRoom.chatting;
		HolderChattingRoomEdit holder = (HolderChattingRoomEdit)convertView.getTag();
		String picture=infoManagement.findUserByUUID(infoManagement.loginUser.uuid).profilePicture;

		for(int i=0; i< chatting.uuidList.size();i++){
			if(chatting.uuidList.get(i)!=infoManagement.loginUser.uuid){
				picture = infoManagement.findUserByUUID(chatting.uuidList.get(i)).profilePicture;
				break;
			}
		}

		if(chatting.type==1){
			holder.tvUserNumber.setVisibility(View.VISIBLE);
			holder.tvUserNumber.setText(chatting.uuidList.size()+"");
		}else{
			holder.tvUserNumber.setVisibility(View.GONE);
		}

		holder.ivChattingRoomPicture.setImageURI(Uri.parse(picture));
		if(chattingRoom.name==null)
			holder.tvChattingRoomName.setText(chattingRoom.ChattingRoomName(infoManagement));
		else
			holder.tvChattingRoomName.setText(chattingRoom.name);
		holder.tvLastMessage.setText(chattingRoom.messageList.get(chattingRoom.messageList.size()-1).message);
		holder.cbIsDelete.setChecked(ischecked[position]);

		holder.cbIsDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox checkBox = (CheckBox) v;
				ischecked[position] = checkBox.isChecked();
				if(checkBox.isChecked()) {
					if(CheckedCheckBoxNum==0) {
						btDelete.setEnabled(true);
						btDelete.setTextColor(Color.parseColor("#e6de11"));
					}
					CheckedCheckBoxNum++;
				}else {
					CheckedCheckBoxNum--;
					if(CheckedCheckBoxNum==0) {
						btDelete.setEnabled(false);
						btDelete.setTextColor(Color.parseColor("#b3a0a0"));
					}
				}
				tvSelectNumber.setText(CheckedCheckBoxNum+"");
			}
		} );
		return convertView;
	}
}
