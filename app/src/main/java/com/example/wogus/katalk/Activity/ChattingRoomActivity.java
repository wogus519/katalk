package com.example.wogus.katalk.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wogus.katalk.Adapter.AdapterDrawerLayout;
import com.example.wogus.katalk.Adapter.AdapterMessageList;
import com.example.wogus.katalk.Class.Chatting;
import com.example.wogus.katalk.Class.ChattingRoom;
import com.example.wogus.katalk.Class.Friend;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.Message;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by wogus on 2018-02-07.
 */

public class ChattingRoomActivity extends AppCompatActivity {        // 채팅방 화면 activity

	ChattingRoom chattingRoom;                                        // 현재 채팅방
	Chatting chatting;
	InfoManagement infoManagement;                                    // 전체 모든 정보 저장
	User loginUser;
	AdapterMessageList adapterMessageList;
	AdapterDrawerLayout adapterDrawerLayout;

	Button btSend;
	EditText etChatting;
	TextView tvChattingRoomName;
	ListView lvMessageList;
	ListView lvUserList;

	LinearLayout llEmoticonCollection;
	ImageView ivEmoticonShow;
	ImageView[] ivEmoticonType,ivEmoticon;
	boolean isDisplayEmoticon;

	RelativeLayout rlSelectEmoticon;
	ImageView ivSelectEmoticon,ivEmoticonCancle;
	boolean isSelectedEmoticon;

	int[] emoticonTypeImage,emoticonTypeSelectImage,emotionImage;
	int selectedEmoticonType;
	int emotionDrawable;

	private DrawerLayout drawerLayout;
	private View drawerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting_room);

		infoManagement = (InfoManagement) getApplication();                        // 모든 정보 불러옴
		loginUser = infoManagement.loginUser;

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerView = (View) findViewById(R.id.drawer);

		initWidget();
		setChattingRoom();
		setListener();
		setAdapter();
	}

	@Override
	protected void onPause() {                                                                                    // pause상태일 때
		super.onPause();
		infoManagement.saveData();
	}
	@Override
	public void onBackPressed() {
		if(drawerLayout.isDrawerOpen(drawerView))
			drawerLayout.closeDrawer(drawerView);
		else if(isDisplayEmoticon){
			llEmoticonCollection.setVisibility(View.GONE);
			if(isSelectedEmoticon)
				rlSelectEmoticon.setVisibility(View.GONE);
			ivEmoticonShow.setImageResource(R.drawable.iv_emoticon);
			isSelectedEmoticon = false;
			isDisplayEmoticon = false;
		}else
			super.onBackPressed();
	}
	public void initWidget() {
		tvChattingRoomName = findViewById(R.id.tvChattingRoomName);                                                                                    // 채팅방 이름
		lvMessageList = findViewById(R.id.lvMesaageList);                                                                                                // 채팅메세지 listview
		lvUserList = findViewById(R.id.lvUserList);
		btSend = findViewById(R.id.btSend);                                                                                                        // 전송버튼
		etChatting = findViewById(R.id.etChatting);                                                                                                // 메세지입력창


		llEmoticonCollection = findViewById(R.id.llEmoticonCollection);
		ivEmoticonShow = findViewById(R.id.ivEmoticonShow);

		ivEmoticon = new ImageView[4];
		ivEmoticon[0] = findViewById(R.id.ivEmoticon1);
		ivEmoticon[1] = findViewById(R.id.ivEmoticon2);
		ivEmoticon[2] = findViewById(R.id.ivEmoticon3);
		ivEmoticon[3] = findViewById(R.id.ivEmoticon4);

		ivEmoticonType = new ImageView[3];
		ivEmoticonType[0] = findViewById(R.id.ivEmoticonType1);
		ivEmoticonType[1] = findViewById(R.id.ivEmoticonType2);
		ivEmoticonType[2] = findViewById(R.id.ivEmoticonType3);

		emoticonTypeImage = new int[3];
		emoticonTypeImage[0] = R.drawable.iv_emoticon_type1;
		emoticonTypeImage[1] = R.drawable.iv_emoticon_type2;
		emoticonTypeImage[2] = R.drawable.iv_emoticon_type3;

		emoticonTypeSelectImage = new int[3];
		emoticonTypeSelectImage[0] = R.drawable.iv_emoticon_type1_select;
		emoticonTypeSelectImage[1] = R.drawable.iv_emoticon_type2_select;
		emoticonTypeSelectImage[2] = R.drawable.iv_emoticon_type3_select;

		emotionImage = new int[12];
		emotionImage[0] = R.drawable.anim_emoticon1;
		emotionImage[1] = R.drawable.anim_emoticon2;
		emotionImage[2] = R.drawable.anim_emoticon3;
		emotionImage[3] = R.drawable.anim_emoticon4;
		emotionImage[4] = R.drawable.anim_emoticon5;
		emotionImage[5] = R.drawable.anim_emoticon6;
		emotionImage[6] = R.drawable.anim_emoticon7;
		emotionImage[7] = R.drawable.anim_emoticon8;
		emotionImage[8] = R.drawable.anim_emoticon9;
		emotionImage[9] = R.drawable.anim_emoticon10;
		emotionImage[10] = R.drawable.anim_emoticon11;
		emotionImage[11] = R.drawable.anim_emoticon12;

		rlSelectEmoticon = findViewById(R.id.rlSelectEmoticon);
		ivSelectEmoticon = findViewById(R.id.ivSelectEmoticon);
		ivEmoticonCancle = findViewById(R.id.ivEmoticonCancle);
	}

	public void setChattingRoom() {
		int type = getIntent().getIntExtra("type", 0);
		if (type == 0) {                                                                                            // 친구목록을 통해 채팅방 들어왔을 경우,
			Friend friend = loginUser.friendList.get(getIntent().getStringExtra("frienduuid"));
			chattingRoom = friend.chattingRoom;                                                                                                            // 해당 친구와의 채팅방 가져옴
		} else if (type == 1)                                                                                                                                        // 채팅목록을 통해 채팅방으로 들어왔을 경우
			chattingRoom = loginUser.chattingroomMap.get(loginUser.chattingroomUUIDList.get(getIntent().getIntExtra("chattingRoomPosition", 0)));                            // 채팅방 번호로 채팅방 가져옴
		else {            // 채팅방생성으로 만든 방일시
			chattingRoom = new ChattingRoom(infoManagement.makeChatting((ArrayList<String>) getIntent().getSerializableExtra("uuidList"), 1));
		}
		chattingRoom.unreadMessageNum = 0;
		chatting = chattingRoom.chatting;

		if (chattingRoom.name != null) {
			tvChattingRoomName.setText(chattingRoom.name);                                                                                                    // 채팅방이름 출력
		} else {
			tvChattingRoomName.setText(chattingRoom.ChattingRoomName(infoManagement));
		}
	}

	public void setListener() {
		for(int i=0;i<ivEmoticonType.length;i++)
			ivEmoticonType[i].setOnClickListener(emoticonTypeListener);

		for(int i=0;i<ivEmoticon.length;i++)
			ivEmoticon[i].setOnClickListener(emoticonClickListener);

		ivSelectEmoticon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AnimationDrawable animationDrawable = (AnimationDrawable)ivSelectEmoticon.getBackground();
				animationDrawable.stop();
				animationDrawable.start();
			}
		});
		ivEmoticonCancle.setOnClickListener(new View.OnClickListener() {      // 전송버튼 눌렀을 때
			@Override
			public void onClick(View view) {
				isSelectedEmoticon = false;
				rlSelectEmoticon.setVisibility(View.GONE);
				if (etChatting.getText().toString().replaceAll(" ", "").length() == 0)
					btSend.setVisibility(View.GONE);
			}
		});
		ivEmoticonShow.setOnClickListener(new View.OnClickListener() {      // 전송버튼 눌렀을 때
			@Override
			public void onClick(View view) {                                                                                                                        // 채팅전송시
				if(!isDisplayEmoticon){
					ivEmoticonShow.setImageResource(R.drawable.iv_emoticon_select);
					llEmoticonCollection.setVisibility(View.VISIBLE);
					setEmoticonType(0);
				}else{
					ivEmoticonShow.setImageResource(R.drawable.iv_emoticon);
					llEmoticonCollection.setVisibility(View.GONE);
					rlSelectEmoticon.setVisibility(View.GONE);
					isSelectedEmoticon = false;
				}
				isDisplayEmoticon = !isDisplayEmoticon;
			}
		});
		btSend.setOnClickListener(new View.OnClickListener() {      // 전송버튼 눌렀을 때
			@Override
			public void onClick(View view) {
				boolean flag = etChatting.getText().toString().replaceAll(" ", "").length() != 0;
				chattingRoom.sendMessage(new Message(loginUser.uuid, etChatting.getText().toString(), Calendar.getInstance(),emotionDrawable,flag,isSelectedEmoticon), infoManagement);                        // 해당 메세지 전송
				etChatting.setText("");                                                                                                                            // 메세지입력창 초기화
				adapterMessageList.notifyDataSetChanged();                                                                                                        // 해당 listview 갱신
				if(isDisplayEmoticon) {
					ivEmoticonShow.setImageResource(R.drawable.iv_emoticon);
					llEmoticonCollection.setVisibility(View.GONE);
					rlSelectEmoticon.setVisibility(View.GONE);
					isDisplayEmoticon=false;
					isSelectedEmoticon = false;
				}
				lvMessageList.setSelection(chattingRoom.messageList.size());
			}
		});
		etChatting.addTextChangedListener(new TextWatcher() {                                                    // 채팅메세지에 변화 생겼을시
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                        // 채팅메세지 변화 생겼을 시
				if (s.toString().replaceAll(" ", "").length() != 0 || isSelectedEmoticon) {                                // 빈칸을 제외하고 아무문자가 존재할 경우(전송할 메세지가 있을 경우)
					btSend.setVisibility(View.VISIBLE);                                                            // 전송버튼 보여줌
				} else {                                                                                            // 아무문자없거나 빈칸만 있을 경우(전송할 메세지 없을 경우)
					btSend.setVisibility(View.GONE);                                                        // 전송버튼 안보여줌
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
		});
		findViewById(R.id.btBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {                                                // 뒤로버튼 클릭시
				Intent intent = new Intent(ChattingRoomActivity.this, MainActivity.class);        // 현재 activity에서 메인activity로 목적지 설정
				intent.putExtra("selectCategory", 1);                                                // 메인문으로 갔을시 1번(채팅)카테고리 보이게 설정
				finish();                                                                                        // 현재 activity종료 (채팅방에서 나갔는데 안드로이드 뒤로가기 버튼누르면 해당창으로 와지는거 방지)
				startActivity(intent);                                                                            // 액티비티 이동
			}
		});
		findViewById(R.id.ivSetChattingRoom).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {                                                // 뒤로버튼 클릭시
				drawerLayout.openDrawer(drawerView);                                                                        // 액티비티 이동
			}
		});
		findViewById(R.id.ivOut).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {                                                // 뒤로버튼 클릭시
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

				// AlertDialog 셋팅
				alertDialogBuilder
						.setMessage("채팅방에서 나가시겠습니까?")
						.setCancelable(false)
						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										infoManagement.loginUser.chattingRoomOut(chatting, infoManagement);
										Intent intent = new Intent(ChattingRoomActivity.this, MainActivity.class);        // 현재 activity에서 메인activity로 목적지 설정
										intent.putExtra("selectCategory", 1);                                                // 메인문으로 갔을시 1번(채팅)카테고리 보이게 설정
										finish();                                                                                        // 현재 activity종료 (채팅방에서 나갔는데 안드로이드 뒤로가기 버튼누르면 해당창으로 와지는거 방지)
										startActivity(intent);
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
	}
	View.OnClickListener emoticonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			ImageView clickImage = (ImageView)view;

			if(!isSelectedEmoticon){
				rlSelectEmoticon.setVisibility(View.VISIBLE);
				isSelectedEmoticon = true;
				if(btSend.getVisibility()==View.GONE)
					btSend.setVisibility(View.VISIBLE);
			}
			for(int i=0; i<ivEmoticon.length;i++){
				if(clickImage == ivEmoticon[i]){
					emotionDrawable=emotionImage[selectedEmoticonType*4+i];
				}
			}
			ivSelectEmoticon.setBackgroundResource(emotionDrawable);
			AnimationDrawable animationDrawable = (AnimationDrawable)ivSelectEmoticon.getBackground();
			animationDrawable.stop();
			animationDrawable.start();
		}
	};
	View.OnClickListener emoticonTypeListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			ImageView clickImage = (ImageView)view;
			if(clickImage == ivEmoticonType[0])
				setEmoticonType(0);
			else if(clickImage == ivEmoticonType[1])
				setEmoticonType(1);
			else
				setEmoticonType(2);
		}
	};
	public void setAdapter() {
		adapterMessageList = new AdapterMessageList(this, chattingRoom.messageList, infoManagement);                        // 메세지listview의 어뎁터
		lvMessageList.setAdapter(adapterMessageList);                                                                                // 어뎁터 연결

		adapterDrawerLayout = new AdapterDrawerLayout(this, infoManagement, chatting.uuidList);
		lvUserList.setAdapter(adapterDrawerLayout);

		lvMessageList.setSelection(chattingRoom.messageList.size());
	}
	public void setEmoticonType(int selectEmoticonType){
		for(int i=0;i<4;i++){
			ivEmoticon[i].setBackgroundResource(emotionImage[4*selectEmoticonType+i]);
		}

		ivEmoticonType[selectedEmoticonType].setImageResource(emoticonTypeImage[selectedEmoticonType]);
		ivEmoticonType[selectedEmoticonType].setBackgroundColor(Color.parseColor("#edeced"));
		ivEmoticonType[selectEmoticonType].setImageResource(emoticonTypeSelectImage[selectEmoticonType]);
		ivEmoticonType[selectEmoticonType].setBackgroundColor(Color.parseColor("#d8d7d8"));

		selectedEmoticonType = selectEmoticonType;
	}
}
