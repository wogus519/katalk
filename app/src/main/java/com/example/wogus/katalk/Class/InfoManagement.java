package com.example.wogus.katalk.Class;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.wogus.katalk.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by wogus on 2018-01-29.
 */

public class InfoManagement extends Application {

	public User loginUser;                      		// 현재 로그인한 유저

	public HashMap<String, User> allUser;        		// 회원가입된 전체 유저  <id,user>
	public HashMap<String, String> allUserId;        	// <uuid,id>

	public HashMap<String,Chatting> allChatting;		// 전체 채팅방들의 채팅저장

	public InfoManagement() {
		allUser = new HashMap<String, User>();
		allUserId = new HashMap<String, String>();
		allChatting = new HashMap<String,Chatting>();
	}

	public void makeSample() {

		makeNewMember("iu","friend","아이유","01052724337");
		allUser.get("iu").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile_iu;

		makeNewMember("hong","friend","홍길동","01012344567");
		allUser.get("hong").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile_hong;

		makeNewMember("sana","friend","사나","01031234557");
		allUser.get("sana").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile_sana;

		makeNewMember("dahyun","friend","다현","01017983817");
		allUser.get("dahyun").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile_dahyun;

		makeNewMember("nayeon","friend","나연","010345632887");
		allUser.get("nayeon").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile_nayun;

		makeNewMember("cheyoung","friend","채영","01049122887");
		allUser.get("cheyoung").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile_chang;

		makeNewMember("jjwhi","friend","쯔위","01099122887");
		allUser.get("jjwhi").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile_jjwhi;

		makeNewMember("momo","friend","모모","01097972887");
		allUser.get("momo").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile_momo;

		makeNewMember("wogus","wogus519","박재현","010-3293-2887");
		allUser.get("wogus").profilePicture = "android.resource://com.example.wogus.katalk/" +R.drawable.profile;

//		connectFriend(allUser.get("wogus519"),allUser.get("friend0"));
//		connectFriend(allUser.get("wogus519"),allUser.get("friend1"));
//		connectFriend(allUser.get("wogus519"),allUser.get("friend2"));
//		connectFriend(allUser.get("wogus519"),allUser.get("friend3"));
//		connectFriend(allUser.get("wogus519"),allUser.get("friend4"));
//		connectFriend(allUser.get("wogus519"),allUser.get("friend5"));
//		connectFriend(allUser.get("wogus519"),allUser.get("friend6"));
//		connectFriend(allUser.get("wogus519"),allUser.get("friend7"));

	}

	public void makeNewMember(String id,String password,String name,String phoneNumber){
		String uuid;

		do {
			uuid=UUID.randomUUID().toString();
		}while (allUserId.get(uuid)!=null);

		User user = new User(uuid,id,password,name,phoneNumber, "android.resource://com.example.wogus.katalk/" +R.drawable.profile_default,null);
		allUser.put(id,user);
		allUserId.put(uuid,id);

		ArrayList<String> uuidList = new ArrayList<>();
		uuidList.add(user.uuid);

		Chatting chatting = makeChatting(uuidList,0);
		allChatting.put(chatting.chattingUUID,chatting);

		user.addFriend(user,this);
		user.friendList.get(uuid).chattingRoom.chatting.uuidList.remove(1);
		user.recommendFreindUUIDList.remove(0);
	}

	public Chatting makeChatting(ArrayList<String> uuidList,int type){
		String uuid;
		do {
			uuid=UUID.randomUUID().toString();
		}while (allChatting.get(uuid)!=null);

		return new Chatting(uuidList,uuid,type);
	}

	public User findUserByUUID(String uuid){
		return allUser.get(allUserId.get(uuid));
	}

	public void readData(){
		SharedPreferences sp = getSharedPreferences("katalk", MODE_PRIVATE);

		this.allUserId = new Gson().fromJson(sp.getString("allUserId","null"), new TypeToken<HashMap<String, String>>() {}.getType());
		this.allUser = new Gson().fromJson(sp.getString("allUser","null"), new TypeToken<HashMap<String, User>>() {}.getType());
		this.allChatting = new Gson().fromJson(sp.getString("allChatting","null"), new TypeToken<HashMap<String,Chatting>>() {}.getType());
		if(allUser==null)
			allUser = new HashMap<>();
		if(allUserId==null)
			allUserId = new HashMap<>();
		if(allChatting==null)
			allChatting = new HashMap<>();
	}
	public void saveData(){
		SharedPreferences sp = getSharedPreferences("katalk", MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		editor.putString("allUserId",new Gson().toJson(allUserId,new TypeToken<HashMap<String, String>>() {}.getType()));
		editor.putString("allUser",new Gson().toJson(allUser,new TypeToken<HashMap<String, User>>() {}.getType()));
		editor.putString("allChatting",new Gson().toJson(allChatting,new TypeToken<HashMap<String,Chatting>>() {}.getType()));
		editor.commit();
	}
}
