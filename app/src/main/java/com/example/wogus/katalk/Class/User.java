package com.example.wogus.katalk.Class;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wogus on 2018-01-29.
 */

public class User {

	public String uuid;
	public String id;                                        // id
	public String password;
	public String name;                                        // 이름
	public String phoneNumber;                                // 폰번호
	public String profilePicture;                                // 사진 uri 주소  Uri는 serializable안되서 string
	public String stateMessage;                                // 상태 메세지

	public ArrayList<String> recommendFreindUUIDList;
	public HashMap<String, Friend> recommendFriendList;

	public ArrayList<String> friendUUIDList;
	public HashMap<String, Friend> friendList;                    // 친구목록

	public ArrayList<String> chattingroomUUIDList;        		// 채팅방리스트
	public HashMap<String, ChattingRoom> chattingroomMap;        //

	public User(String uuid, String id, String password, String name, String phoneNumber, String profilePicture, String stateMessage) {   // 생성자
		this.uuid = uuid;
		this.password = password;
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.profilePicture = profilePicture;
		this.stateMessage = stateMessage;

		recommendFriendList = new HashMap<String, Friend>();
		recommendFreindUUIDList = new ArrayList<String>();
		friendUUIDList = new ArrayList<String>();
		friendList = new HashMap<String, Friend>();
		chattingroomUUIDList = new ArrayList<String>();
		chattingroomMap = new HashMap<String, ChattingRoom>();
	}

	public void addFriend(User friend,InfoManagement infoManagement) {

		if(!recommendFreindUUIDList.contains(friend.uuid)) {
			ArrayList<String> uuidList = new ArrayList<>();
			uuidList.add(friend.uuid);
			uuidList.add(this.uuid);

			Chatting chatting = infoManagement.makeChatting(uuidList, 0);
			infoManagement.allChatting.put(chatting.chattingUUID, chatting);

			friendUUIDList.add(friend.uuid);
			friendList.put(friend.uuid, new Friend(friend.uuid,chatting));

			friend.recommendFriendList.put(this.uuid,new Friend(this.uuid,chatting));
			friend.recommendFreindUUIDList.add(this.uuid);
		}else{
			friendUUIDList.add(friend.uuid);
			friendList.put(friend.uuid, recommendFriendList.get(friend.uuid));
			recommendFreindUUIDList.remove(friend.uuid);
			recommendFriendList.remove(friend.uuid);
		}
	}
	public void deleteFriend(Friend friend,InfoManagement infoManagement){
		friend.name = null;
		chattingRoomOut(friend.chattingRoom.chatting,infoManagement);

		friendUUIDList.remove(friend.uuid);
		recommendFreindUUIDList.add(friend.uuid);

		recommendFriendList.put(friend.uuid,friendList.remove(friend.uuid));
	}
	public void receiveMessage(ChattingRoom sendChattingroom, Message message,InfoManagement infoManagement) {
		Chatting chatting = sendChattingroom.chatting;
		ChattingRoom chattingroom = this.chattingroomMap.get(chatting.chattingUUID);
		if (chattingroom == null) {
			if(chatting.type==0 ) {
				if(!this.uuid.equals(message.sender)) {
					if(this.friendList.get(message.sender)!=null)
						chattingroom = this.friendList.get(message.sender).chattingRoom;
					else
						chattingroom = this.recommendFriendList.get(message.sender).chattingRoom;
				}else{
					String frienduuid = this.uuid.equals(chatting.uuidList.get(0))?chatting.uuidList.get(chatting.uuidList.size()-1):chatting.uuidList.get(0);
					chattingroom=this.friendList.get(frienduuid).chattingRoom;
				}
			}else {
				if(this.uuid.equals(message.sender))
					chattingroom = sendChattingroom;
				else
					chattingroom = new ChattingRoom(chatting);
			}
			settingChattingRoom(chattingroom);
			if(!infoManagement.allChatting.containsKey(chatting.chattingUUID))
				infoManagement.allChatting.put(chatting.chattingUUID,chatting);
		}

		this.chattingroomUUIDList.remove(chattingroom.chatting.chattingUUID);
		this.chattingroomUUIDList.add(0, chattingroom.chatting.chattingUUID);

		if(!this.uuid.equals(message.sender))
			chattingroom.unreadMessageNum++;
		chattingroom.messageList.add(message);
	}

	public void settingChattingRoom(ChattingRoom chattingRoom){
		this.chattingroomUUIDList.add(0, chattingRoom.chatting.chattingUUID);
		this.chattingroomMap.put(chattingRoom.chatting.chattingUUID, chattingRoom);
	}
	public void chattingRoomOut(Chatting chatting,InfoManagement infoManagement){
		if(chatting.type==0){
			String frienduuid = this.uuid.equals(chatting.uuidList.get(0))?chatting.uuidList.get(chatting.uuidList.size()-1):chatting.uuidList.get(0);
			this.friendList.get(frienduuid).chattingRoom.name=null;
			this.friendList.get(frienduuid).chattingRoom.messageList.clear();
			this.friendList.get(frienduuid).chattingRoom.unreadMessageNum=0;
		}
		else {
			chatting.uuidList.remove(this.uuid);
			if(chatting.uuidList.size()==0)
				infoManagement.allChatting.remove(chatting.chattingUUID);
		}
		this.chattingroomMap.remove(chatting.chattingUUID);
		this.chattingroomUUIDList.remove(chatting.chattingUUID);
	}
}
