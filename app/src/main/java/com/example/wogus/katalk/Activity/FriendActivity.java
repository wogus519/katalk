package com.example.wogus.katalk.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wogus.katalk.Class.Friend;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

/**
 * Created by wogus on 2018-02-06.
 */

public class FriendActivity extends AppCompatActivity{			// 친구의 정보를 보여주는 activity

	InfoManagement infoManagement;									// 전체적인 데이터 관리하는 클래스
	String uuid;											// 현재 정보를 표시할 친구의 번호
	User friend;													// 현재 정보를 표시한 친구

	ImageView btCall;
	ImageView btChatting;
	ImageView btAddFriend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_activity);

		infoManagement = (InfoManagement) getApplication();                        				// 전체적인 데이터 저장개체(db역할)
		uuid = getIntent().getStringExtra("uuid");											// 친구의 번호를 불러옴
		friend = infoManagement.findUserByUUID(uuid);												// 해당 번호의 친구 객체

		ImageView ivProfilePicture = findViewById(R.id.ivProfilePicture);							// 친구의 사진을 보여준는 imageview
		TextView tvName = findViewById(R.id.tvName);												// 친구의 이름
		TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);									// 친구의 핸드폰 번호

		btCall = findViewById(R.id.btCall);
		btChatting = findViewById(R.id.btChatting);
		btAddFriend = findViewById(R.id.btAddFriend);

		if(infoManagement.loginUser.friendList.get(uuid) != null){
			btCall.setVisibility(View.VISIBLE);
			btChatting.setVisibility(View.VISIBLE);
			btAddFriend.setVisibility(View.GONE);
		}else{
			btCall.setVisibility(View.GONE);
			btChatting.setVisibility(View.GONE);
			btAddFriend.setVisibility(View.VISIBLE);
		}
		ivProfilePicture.setImageURI(Uri.parse(friend.profilePicture));							// 친구 사진 출력
		tvName.setText(friend.name);																// 친구 이름 출력
		tvPhoneNumber.setText(friend.phoneNumber);												// 친구 핸드폰 번호 출력

		btCall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {														// 전화버튼 클릭시
				call();
			}
		});

		btChatting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {											// 채팅버튼 클릭시
				Intent intent = new Intent(FriendActivity.this, ChattingRoomActivity.class);	// 채팅방 activity로 목적지 설정
				intent.putExtra("type",0);															// 친구 카테고리통해서 들어간다는 표시
				intent.putExtra("frienduuid",uuid);														// 친구의 번호
				finish();																						// 해당 액티비티 끝냄 (채팅방에서 뒤로가면 이화면안오고 바로 메인activity 로이동)
				startActivity(intent);
			}
		});
		btAddFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {											// 채팅버튼 클릭시
				infoManagement.loginUser.addFriend(friend,infoManagement);
				btCall.setVisibility(View.VISIBLE);
				btChatting.setVisibility(View.VISIBLE);
				btAddFriend.setVisibility(View.GONE);
			}
		});
		findViewById(R.id.btBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {												// 뒤로버튼 클릭시
				Intent intent = new Intent(FriendActivity.this, MainActivity.class);			// 메인activity로 목적지 설정
				finish();																						// 해당 액티비티 끝냄, 해당버튼 눌러서 mainactivity갔는데 거기서 안드로이드 뒤로버튼누르면 여기 화면으로 와지는거 방지
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onPause() {                                                                                    // pause상태일 때
		super.onPause();
		infoManagement.saveData();
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					call();
				} else {// 권한 거부	}
					return;
				}
		}
	}
	public void call(){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + friend.phoneNumber));                // 전화번호 Uri정보 intent설정
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {                // 전화 권한있는지 확인
			ActivityCompat.requestPermissions(FriendActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);                    // 권한 부여할 것인지 요청창 뜨게함
		} else {
			startActivity(intent);
		}
	}
}
