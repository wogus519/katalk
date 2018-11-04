package com.example.wogus.katalk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wogus.katalk.Class.ChattingRoom;
import com.example.wogus.katalk.Class.Friend;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.R;

/**
 * Created by wogus on 2018-03-08.
 */

public class ChangeFriendName extends AppCompatActivity{

	Friend friend;
	InfoManagement infoManagement;

	Button btSubmit;
	EditText etFriendName;
	TextView tvTextNum,tvFriendName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_friend_name);

		infoManagement = (InfoManagement) getApplication();
		setChattingRoom();
	}

	public void setChattingRoom() {
		btSubmit = findViewById(R.id.btSubmit);
		etFriendName = findViewById(R.id.etFriendName);
		tvTextNum = findViewById(R.id.tvTextNum);
		tvFriendName = findViewById(R.id.tvFriendName);

		friend = infoManagement.loginUser.friendList.get(infoManagement.loginUser.friendUUIDList.get(getIntent().getIntExtra("FriendPosition", 0)));                            // 채팅방 번호로 채팅방 가져옴

		tvFriendName.setText(friend.getName(infoManagement));
		etFriendName.setHint(infoManagement.findUserByUUID(friend.uuid).name);

		btSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				friend.name = etFriendName.getText().toString();
				Intent intent = new Intent(ChangeFriendName.this, MainActivity.class);        // 현재 activity에서 메인activity로 목적지 설정
				intent.putExtra("selectCategory", 0);                                                // 메인문으로 갔을시 1번(채팅)카테고리 보이게 설정
				finish();                                                                                        // 현재 activity종료 (채팅방에서 나갔는데 안드로이드 뒤로가기 버튼누르면 해당창으로 와지는거 방지)
				startActivity(intent);
			}
		});
		etFriendName.addTextChangedListener(new TextWatcher() {                                                    // 채팅메세지에 변화 생겼을시
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                        // 채팅메세지 변화 생겼을 시
				tvTextNum.setText(s.length()+"/15");
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				// 입력이 끝났을 때 호출된다.
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// 입력하기 전에 호출된다.
			}
		});
	}

}
