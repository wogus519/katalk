package com.example.wogus.katalk.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;


import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.Message;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wogus on 2018-02-07.
 */

public class AdapterMessageList extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Message> messageList;
	private User loginUser;
	private InfoManagement infoManagement;

	public AdapterMessageList(Context context, ArrayList<Message> messageList, InfoManagement infoManagement) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.messageList = messageList;
		this.loginUser = infoManagement.loginUser;
		this.infoManagement = infoManagement;
	}

	@Override
	public int getCount() {
		return messageList.size();
	}

	@Override
	public Message getItem(int position) {
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (messageList.get(position).sender.equals(loginUser.uuid))
			return 0;
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			if (getItemViewType(position) == 0) {                                                    // 내가보낸 메세지일경우
				convertView = inflater.inflate(R.layout.listview_mymessage, parent, false);
			} else
				convertView = inflater.inflate(R.layout.listview_othermessage, parent, false);

			HolderMessageList holder = new HolderMessageList();                                                    // holder 생성

			holder.llMesaageDate = convertView.findViewById(R.id.llMesaageDate);
			holder.ivSenderPicture = convertView.findViewById(R.id.ivSenderPicture);
			holder.tvSenderName = convertView.findViewById(R.id.tvSenderName);
			holder.tvMessage = convertView.findViewById(R.id.tvMessage);
			holder.tvMessageTime = convertView.findViewById(R.id.tvMessageTime);
			holder.ivEmoticon = convertView.findViewById(R.id.ivEmoticon);

			convertView.setTag(holder);                                                            // holder 저장
		}
		final HolderMessageList holder = (HolderMessageList) convertView.getTag();
		Message message = messageList.get(position);

		if (position == 0 || (compareDate(message, messageList.get(position - 1)) == 0))
			holder.llMesaageDate.setVisibility(View.VISIBLE);
		else
			holder.llMesaageDate.setVisibility(View.GONE);

		if (position < messageList.size() - 1 && messageList.get(position).sender.equals(messageList.get(position + 1).sender) && messageList.get(position).time_format().equals(messageList.get(position + 1).time_format())) {
			holder.tvMessageTime.setVisibility(View.GONE);
		} else {
			holder.tvMessageTime.setVisibility(View.VISIBLE);
			holder.tvMessageTime.setText(message.time_format());
		}

		if (getItemViewType(position) == 1) { // 남이보낼 메세지일 경우
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT ) ;
			if (position > 0 && messageList.get(position).sender.equals(messageList.get(position - 1).sender) && messageList.get(position).time_format().equals(messageList.get(position - 1).time_format())) {
				holder.tvSenderName.setVisibility(View.GONE);
				holder.ivSenderPicture.setVisibility(View.GONE);
				params.setMargins( holder.ivSenderPicture.getLayoutParams().width, 0, 0, 0 );
			} else {
				holder.tvSenderName.setVisibility(View.VISIBLE);
				holder.ivSenderPicture.setVisibility(View.VISIBLE);

				holder.ivSenderPicture.setImageURI(Uri.parse(infoManagement.findUserByUUID(message.sender).profilePicture));
				holder.tvSenderName.setText(infoManagement.findUserByUUID(message.sender).name);

				params.setMargins( 0, 0, 0, 0 );
			}
			holder.tvMessage.setLayoutParams(params);
		}

		if(message.isImage) {
			holder.ivEmoticon.setBackgroundResource(message.emoticon);
			holder.ivEmoticon.setVisibility(View.VISIBLE);
			AnimationDrawable animationDrawable = (AnimationDrawable)holder.ivEmoticon.getBackground();
			animationDrawable.start();
			holder.ivEmoticon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					AnimationDrawable animationDrawable = (AnimationDrawable)holder.ivEmoticon.getBackground();
					animationDrawable.stop();
					animationDrawable.start();
				}
			});
		}else{
			holder.ivEmoticon.setVisibility(View.GONE);
		}
		if(message.isMessage) {
			holder.tvMessage.setText(message.message);
			holder.tvMessage.setVisibility(View.VISIBLE);
		}else{
			holder.tvMessage.setVisibility(View.GONE);
		}
		return convertView;
	}

	public int compareDate(Message messageBefor, Message messageNow) {                                            // 0: 년 월 일 틀릴 때
		if (messageBefor.cal.get(Calendar.YEAR) != messageNow.cal.get(Calendar.YEAR))                                // 1: 년 월 일 맞고 시분초 틀릴때
			return 0;                                                                                            // 2: 전부 맞을 때 (초는 상관없음)
		else if (messageBefor.cal.get(Calendar.MONTH) != messageNow.cal.get(Calendar.MONTH))
			return 0;
		else if (messageBefor.cal.get(Calendar.DATE) != messageNow.cal.get(Calendar.DATE))
			return 0;
		else if (messageBefor.cal.get(Calendar.HOUR_OF_DAY) != messageNow.cal.get(Calendar.HOUR_OF_DAY))
			return 1;
		else if (messageBefor.cal.get(Calendar.MINUTE) != messageNow.cal.get(Calendar.MINUTE))
			return 1;
		return 2;
	}
}
