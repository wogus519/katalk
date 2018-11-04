package com.example.wogus.katalk.Class;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by wogus on 2018-01-29.
 */
import com.example.wogus.katalk.R;
public class Message {
	public String sender;           	// 메세지 발신자의 uuid
	public String message;          	// 메세지 내용
	public Calendar cal;            	// 메세지 발신일
	public int emoticon;
	public boolean isMessage;
	public boolean isImage;

	public Message(String sender, String message, Calendar cal,int emoticon, boolean isMessage,boolean isImage) {   // 생성자
		this.sender = sender;
		this.message = message;
		this.cal = cal;
		this.emoticon = emoticon;
		this.isMessage = isMessage;
		this.isImage = isImage;
	}

	public String time_format() {
		String state, hour, min;

		if (cal.get(Calendar.HOUR_OF_DAY) >= 12) {
			hour = (cal.get(Calendar.HOUR_OF_DAY) - 12) + "";
			if (hour.equals("0"))
				hour = "12";
			state = "오후 ";
		} else {
			hour = cal.get(Calendar.HOUR_OF_DAY) + "";
			state = "오전 ";
		}
		min = cal.get(Calendar.MINUTE) + "";
		if (cal.get(Calendar.MINUTE) < 10)
			min = "0" + min;
		return state + hour + ":" + min;
	}
}
