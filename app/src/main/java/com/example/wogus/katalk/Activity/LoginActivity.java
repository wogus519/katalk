package com.example.wogus.katalk.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

/**
 * Created by wogus on 2018-02-10.
 */

public class LoginActivity extends AppCompatActivity{

	InfoManagement infoManagement;            										// 전체적인 데이터 저장개체(db역할)
	Button btLogin;
	EditText etLoginId,etLoginPassword;
	TextView tvLoginCheck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		infoManagement = (InfoManagement) getApplication();                        			// 전체적인 데이터 저장개체(db역할)

		tvLoginCheck = findViewById(R.id.tvLoginCheck);
		btLogin = findViewById(R.id.btLogin);
		etLoginId = findViewById(R.id.etLoginId);
		etLoginPassword = findViewById(R.id.etLoginPassword);

		etLoginId.addTextChangedListener(textWatcher);
		etLoginPassword.addTextChangedListener(textWatcher);

		infoManagement.readData();
		//infoManagement.makeSample();

		btLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				User loginUser=infoManagement.allUser.get(etLoginId.getText().toString());
				if(loginUser!=null && loginUser.password.equals(etLoginPassword.getText().toString())){
					infoManagement.loginUser = loginUser;
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);					// 채팅창 변경(삭제) activity로 목적지 설정
					startActivity(intent);																				// 액티비티 실행
				}else{
					tvLoginCheck.setVisibility(View.VISIBLE);
				}
			}
		});
		findViewById(R.id.btMemberJoin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, JoinMemberActivity.class);					// 채팅창 변경(삭제) activity로 목적지 설정
				startActivity(intent);																				// 액티비티 실행
			}
		});
	}
	TextWatcher textWatcher=new TextWatcher() {													// 채팅메세지에 변화 생겼을시
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {						// 채팅메세지 변화 생겼을 시
			if(etLoginId.getText().toString().replaceAll(" ","").length() !=0 && etLoginPassword.getText().toString().length()!=0) {                // 전송버튼 안보여줌
				btLogin.setTextColor(Color.parseColor("#ffffff"));
				btLogin.setEnabled(true);
			}else {
				btLogin.setTextColor(Color.parseColor("#8e7974"));
				btLogin.setEnabled(false);
			}
		}
		@Override
		public void afterTextChanged(Editable arg0) {
			// 입력이 끝났을 때 호출된다.
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// 입력하기 전에 호출된다.
		}
	};
	@Override
	protected void onResume() {																						// 화면출력 바로전
		super.onResume();
		tvLoginCheck.setVisibility(View.GONE);
		etLoginId.setText("");
		etLoginPassword.setText("");
	}
	@Override
	protected  void onPause(){
		super.onPause();
		infoManagement.saveData();
	}
}
