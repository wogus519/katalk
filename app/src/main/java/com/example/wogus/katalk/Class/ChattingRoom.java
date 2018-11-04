package com.example.wogus.katalk.Class;

import java.util.ArrayList;

/**
 * Created by wogus on 2018-02-12.
 */

public class ChattingRoom {

	public int unreadMessageNum;
	public Chatting chatting;
	public String name;
	public ArrayList<Message> messageList;                          // 메세지 목록

	public ChattingRoom(Chatting chatting){
		this.chatting = chatting;
		this.messageList = new ArrayList<Message>();
	}

	public String ChattingRoomName(InfoManagement infoManagement){
		String chattingRoomName="";
		User user;

		if(name != null)
			return name;

		if(chatting.uuidList.size()==1)
			chattingRoomName = infoManagement.loginUser.name;
		else {
			for (int i = 0; i < chatting.uuidList.size(); i++) {
				if (!chatting.uuidList.get(i).equals(infoManagement.loginUser.uuid)) {
					user = infoManagement.findUserByUUID(chatting.uuidList.get(i));
					if(infoManagement.loginUser.friendList.get(user.uuid)==null)
						chattingRoomName = chattingRoomName + user.name + ",";
					else
						chattingRoomName = chattingRoomName +infoManagement.loginUser.friendList.get(user.uuid).getName(infoManagement)+ ",";
				}
			}
			chattingRoomName = chattingRoomName.substring(0, chattingRoomName.length() - 1);
		}
		return chattingRoomName;
	}

	public void sendMessage(Message message, InfoManagement infoManagement){
		for(int i=0;i<chatting.uuidList.size();i++){
			infoManagement.findUserByUUID(chatting.uuidList.get(i)).receiveMessage(this,message,infoManagement);
		}
	}
}
