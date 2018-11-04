package com.example.wogus.katalk.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.wogus.katalk.Activity.MakeChattingActivity;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wogus on 2018-02-19.
 */

public class AdapterMakeChattinglist extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<String> friendUUIDList;
	private InfoManagement infoManagement;

	public MakeChattingActivity makeChattingActivity;
	public AdapterRecyclerList adapterRecyclerList;
	public HashMap<String,Boolean> isChecked;

	public AdapterMakeChattinglist(Context context, InfoManagement infoManagement,ArrayList<String> friendUUIDList,HashMap<String,Boolean> isChecked, MakeChattingActivity makeChattingActivity) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.friendUUIDList = friendUUIDList;
		this.infoManagement = infoManagement;
		this.isChecked = isChecked;
		this.makeChattingActivity = makeChattingActivity;
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
			convertView = inflater.inflate(R.layout.listview_makechatting, parent, false);

			HolderMakeChattingList holder = new HolderMakeChattingList();

			holder.ivFriendPicture = convertView.findViewById(R.id.ivFriendPciture);
			holder.tvFriendName = convertView.findViewById(R.id.tvFriendName);
			holder.cbChecked = convertView.findViewById(R.id.CheckBox);

			convertView.setTag(holder);
		}
		final HolderMakeChattingList holder = (HolderMakeChattingList)convertView.getTag();
		final User user = infoManagement.findUserByUUID(friendUUIDList.get(position));

		holder.ivFriendPicture.setImageURI(Uri.parse(user.profilePicture));
		holder.tvFriendName.setText(user.name);
		holder.cbChecked.setChecked(isChecked.get(user.uuid));

		holder.cbChecked.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {                                   							// 체크박스 클릭시
				CheckBox checkBox = (CheckBox) v;
				isChecked.put(user.uuid,checkBox.isChecked());

				if(checkBox.isChecked()) {
					adapterRecyclerList.dataList.add(0,user.uuid);
				}else {
					adapterRecyclerList.dataList.remove(user.uuid);
				}

				adapterRecyclerList.notifyDataSetChanged();
				makeChattingActivity.setAbove();
			}
		} );
		return convertView;
	}

	public void changeList(ArrayList<String> list){
		friendUUIDList = list;
	}
}

