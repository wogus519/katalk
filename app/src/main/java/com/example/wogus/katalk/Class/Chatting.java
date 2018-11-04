package com.example.wogus.katalk.Class;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wogus on 2018-02-02.
 */

public class Chatting {

	public String chattingUUID;
	public int type;														// 채팅창종류 0:친구랑1대1, 1: 단톡방
	public ArrayList<String> uuidList;                              		// 채팅창 상대목록

	public Chatting(ArrayList<String> uuidList,String chattingUUID,int type) {      // 생성자
		this.chattingUUID = chattingUUID;
		this.uuidList = uuidList;
		this.type = type;
	}

}
