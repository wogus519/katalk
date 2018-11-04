package com.example.wogus.katalk.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wogus.katalk.Adapter.AdapterChattingListEdit;
import com.example.wogus.katalk.Class.ChattingRoom;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wogus on 2018-02-07.
 */

public class ChattingRoomEditActivity extends AppCompatActivity {		// 채팅방목록 관리(현재는 삭제)하는 activity

	InfoManagement infoManagement;									// 전체적인 데이터 관리하는 클래스
	AdapterChattingListEdit adapterChattingListEdit;				// 각 채팅방list에 대한 어뎁터
	public ArrayList<String> chattingroomUUIDList;				// 현재 로그인한 유저의 채팅방목록
	public HashMap<String,ChattingRoom> chattingroomMap;		//

	TextView tvSelectNumber;
	Button btDelete;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_chattingroom);

		infoManagement = (InfoManagement) getApplication();            							// 전체적인 데이터 저장개체(db역할)
		chattingroomUUIDList = infoManagement.loginUser.chattingroomUUIDList;							// 현재 로그인한 유저의 채팅방 목록
		chattingroomMap=infoManagement.loginUser.chattingroomMap;
		btDelete = findViewById(R.id.btDelete);												// 나가기(삭제)버튼 (채팅방에서 나간다의 의미)
		tvSelectNumber = findViewById(R.id.tvSelectNumber);
		ListView lvEditChattingList = findViewById(R.id.lvEditChattingList);						// 각 채팅창 설정에 대한 리스트뷰

		adapterChattingListEdit = new AdapterChattingListEdit(this,infoManagement,btDelete,tvSelectNumber);			//어뎁터 생성, 각 채팅방 설정중 체크된게 있어야 나가기버튼이 활성화되므로 생성자에 넣어줌
		lvEditChattingList.setAdapter(adapterChattingListEdit);													//  어뎁터 연결

		btDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {																	// 나가기버튼 클릭시
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

				// AlertDialog 셋팅
				alertDialogBuilder
						.setMessage("채팅방에서 나가시겠습니까?")
						.setCancelable(false)
						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										for(int i=chattingroomUUIDList.size()-1; i>=0;i--){												// 채팅방목록의 size만큼 반복문, 삭제를 하면 채팅방목록의size가 변화(줄어)되므로 반복문을 뒤에서 앞으로 돌림
											if(adapterChattingListEdit.ischecked[i]) {													// 채팅방 나가기 체크박스가 체크되어있을 경우
												infoManagement.loginUser.chattingRoomOut(chattingroomMap.get(chattingroomUUIDList.get(i)).chatting,infoManagement);
											}
										}
										Intent intent = new Intent(ChattingRoomEditActivity.this, MainActivity.class);		// MainActivity로 목적지 설정
										intent.putExtra("selectCategory",1);													// MainActivity이동시 1번(채팅)카테고리 보이도록 설정
										finish();																							// 현재 액티비티 종료!
										startActivity(intent);																				// MainAcitivyt로 이동
									}
								})
						.setNegativeButton("취소",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										// 다이얼로그를 취소한다
										dialog.cancel();
									}
								});

				// 다이얼로그 생성
				final AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

				alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
					@Override
					public void onShow(DialogInterface arg0) {
						alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));
						alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
					}
				});
			}
		});
		findViewById(R.id.cbAllSelect).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {												// 전부체크 체크박스 클릭시
				CheckBox cb = (CheckBox)view;																		// 전부체크 체크박스 객체 받아옴
				boolean ischecked = cb.isChecked();

				for(int i=0;i<adapterChattingListEdit.ischecked.length;i++)									// 각 채팅방list에 존재하는 체크박스 개수 만큼 반복문
					adapterChattingListEdit.ischecked[i]=ischecked;										// 각 채팅방의 체크박스에 전체체크 체크박스의 상태(true,false)대입

				if(ischecked){
					tvSelectNumber.setText(chattingroomUUIDList.size()+"");
					btDelete.setEnabled(true);
					btDelete.setTextColor(Color.parseColor("#e6de11"));
					adapterChattingListEdit.CheckedCheckBoxNum=chattingroomUUIDList.size();
				}else{
					tvSelectNumber.setText("0");
					btDelete.setEnabled(false);
					btDelete.setTextColor(Color.parseColor("#b3a0a0"));
					adapterChattingListEdit.CheckedCheckBoxNum=0;
				}

				adapterChattingListEdit.notifyDataSetChanged();													// 리스트 갱신
			}
		});
	}
	@Override
	protected void onPause() {                                                                                    // pause상태일 때
		super.onPause();
		infoManagement.saveData();
	}
}
