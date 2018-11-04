package com.example.wogus.katalk.Class;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wogus on 2018-02-05.
 */

public class Friend {
	public String uuid;
	public String name;
	public ChattingRoom chattingRoom;

	public Friend(String uuid,Chatting chatting){
		this.uuid = uuid;
		this.chattingRoom = new ChattingRoom(chatting);
	}
	public String getName(InfoManagement infoManagement){
		if(name != null)
			return name;

		return infoManagement.findUserByUUID(uuid).name;
	}
}
