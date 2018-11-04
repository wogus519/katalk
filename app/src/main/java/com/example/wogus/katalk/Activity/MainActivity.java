package com.example.wogus.katalk.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wogus.katalk.Adapter.AdapterChattingList;
import com.example.wogus.katalk.Adapter.AdapterFriendList;
import com.example.wogus.katalk.Class.ChattingRoom;
import com.example.wogus.katalk.Class.Friend;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.R;

public class MainActivity extends AppCompatActivity {

	InfoManagement infoManagement;                                                    // 전체적인 데이터 저장개체(db역할)

	ImageView[] ivCategory;                                                        // 각 위의 카테고리 이미지뷰들
	ImageView ivInsertFriend, ivInsertChatting;                                    // 친구추가, 채팅추가 버튼
	TextView tvCategoryName, tvFriendNumber, tvSelectLine;
	String[] CategoryName;                                                            // 각 카테고리의 이름
	int[] noSelectedCategoryImage;                                                // 각 카테고리가 선택 안됬을 때의 이미지,Drawble
	int[] selectedCategoryImage;                                                    // 각 카테고리가 선택 됬을 때의 이미지
	int selectedCategory, screenWidth;                                            // 현재 보여지고있는 카테고리
	float toX, prevX, currentX;
	double startTime, endTime;

	LinearLayout news1, news2, news3, news4, news5, news6, news7, news8;       // 뉴스 카테고리에서 각 뉴스들 들어가있는 레이아웃
	LinearLayout[] llCategoryView;                                                    // 각 카테고리를 감싸고있는 layout, (frameLayout의 자식)
	ListView lvFriendList, lvChattingList;

	AdapterFriendList adapterFriendList;                                            // 친구목록에 대한 어댑터
	AdapterChattingList adapterChattingList;                                        // 채팅목록에 대한 어댑터

	public RelativeLayout rlChattingDrawer;
	public TextView tvMenuBarName, tvMenuBarNameSet, tvMenuBarEnter, tvMenuBarExit;

	public RelativeLayout rlFriendDrawer;
	public TextView tvMenuBarName2, tvMenuBarChange, tvMenuBarDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		infoManagement = (InfoManagement) getApplication();                                    // 전체적인 데이터 저장개체(db역할)
		screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;

		rlChattingDrawer = findViewById(R.id.rlChattingDrawer);
		tvMenuBarName = findViewById(R.id.tvMenuBarName);
		tvMenuBarNameSet = findViewById(R.id.tvMenuBarNameSet);
		tvMenuBarEnter = findViewById(R.id.tvMenuBarEnter);
		tvMenuBarExit = findViewById(R.id.tvMenuBarExit);
		rlFriendDrawer = findViewById(R.id.rlFriendDrawer);
		tvMenuBarName2 = findViewById(R.id.tvMenuBarName2);
		tvMenuBarChange = findViewById(R.id.tvMenuBarChange);
		tvMenuBarDelete = findViewById(R.id.tvMenuBarDelete);

		settingCategoryInfo();                                                                    // 각 카테고리 버튼의 이미지등 필요한 정보 셋팅
		settingSettingCategory();                                                                // 설정카테고리 셋팅
		settingFriendCategory();                                                                // 친구카테고리 셋팅
		settingChattingCategory();                                                                // 채팅카테고리 셋팅
		settingNewsCategory();                                                                    // 뉴스카테고리 셋팅

		display(getIntent().getIntExtra("selectCategory", 0));                // 현재 보여줄 카테고리를 불러와서 해당 카테고리 보여줌(해당화면이 첫시작이라 없을경우 default 0번(친구)카테고리 버여줌)

		settingListener();

	}

	@Override
	protected void onResume() {                                                                                        // 화면출력 바로전
		super.onResume();

		settingSettingCategory();                                                                // 설정카테고리 셋팅
		settingFriendCategory();                                                                // 친구카테고리 셋팅
		settingChattingCategory();                                                                // 채팅카테고리 셋팅
		settingNewsCategory();                                                                    // 뉴스카테고리 셋팅

		tvFriendNumber.setText((infoManagement.loginUser.friendList.size() - 1) + "");                                // 친구 수를 출력.
		adapterFriendList.notifyDataSetChanged();                                                                        // 친구 listview 갱신
		adapterChattingList.notifyDataSetChanged();                                                                    // 채팅 listview 갱신
	}
	@Override
	public void onBackPressed() {
		if(rlChattingDrawer.getVisibility() == View.VISIBLE)
			rlChattingDrawer.setVisibility(View.GONE);
		else if(rlFriendDrawer.getVisibility() == View.VISIBLE)
			rlFriendDrawer.setVisibility(View.GONE);
		else
			super.onBackPressed();
	}
	@Override
	protected void onPause() {                                                                                            // 화면출력 바로전
		super.onPause();
		infoManagement.saveData();
	}

	public void settingCategoryInfo() {                                                        // 각 카테고리 버튼(이미지뷰)에 필요한 정보(이미지,이름 등등)저장
		ivCategory = new ImageView[4];                                                            // 카테고리 버튼(이미지뷰) 배열
		ivCategory[0] = findViewById(R.id.ivFriendCategory);                                    // 0번 카테고리에 친구카테고리 설정
		ivCategory[1] = findViewById(R.id.ivChattingCategory);                                // 1번 카테고리에 채팅카테고리 설정
		ivCategory[2] = findViewById(R.id.ivNewsCategory);                                    // 2번 카테고리에 뉴스카테고리 설정
		ivCategory[3] = findViewById(R.id.ivSettingCategory);                                // 3번 카테고리에 설정카테고리 설정

		for (int i = 0; i < 4; i++)                                                                    // 각 카테고리에 리스너연결
			ivCategory[i].setOnClickListener(categoryClickListener);

		tvFriendNumber = findViewById(R.id.tvFriendNumber);                                    // 친구카테고리에서 친구수를 보여주는 textview
		tvCategoryName = findViewById(R.id.tvCategoryName);                                    // 각 카테고리의 이름을 보여주는 textview

		CategoryName = new String[4];                                                            // 카테고리의 이름 배열 설정
		CategoryName[0] = "친구";
		CategoryName[1] = "채팅";
		CategoryName[2] = "뉴스";
		CategoryName[3] = "설정";

		noSelectedCategoryImage = new int[4];                                                // 포커싱이 안되있을 때의 카테고리의 이미지 배열 설정
		noSelectedCategoryImage[0] = R.drawable.bt_friend;
		noSelectedCategoryImage[1] = R.drawable.bt_chat;
		noSelectedCategoryImage[2] = R.drawable.bt_news;
		noSelectedCategoryImage[3] = R.drawable.bt_set;

		selectedCategoryImage = new int[4];                                                    // 포커싱된 카테고리의 이미지 배열 설정
		selectedCategoryImage[0] = R.drawable.bt_friend_select;
		selectedCategoryImage[1] = R.drawable.bt_chat_select;
		selectedCategoryImage[2] = R.drawable.bt_news_select;
		selectedCategoryImage[3] = R.drawable.bt_set_select;

		llCategoryView = new LinearLayout[4];                                                    // 각 카테고리의 layout, (framelayout의 자식뷰)
		llCategoryView[0] = (LinearLayout) findViewById(R.id.llFriend);
		llCategoryView[1] = (LinearLayout) findViewById(R.id.llChatting);
		llCategoryView[2] = (LinearLayout) findViewById(R.id.llNews);
		llCategoryView[3] = (LinearLayout) findViewById(R.id.llSetting);

		tvSelectLine = findViewById(R.id.tvSelectLine);
		tvSelectLine.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth / 4, ViewGroup.LayoutParams.MATCH_PARENT));
		System.out.println("길이:" + screenWidth / 4);
	}

	public void display(int selectCategory) {
		tvSelectLine.setX(screenWidth / 4 * selectCategory);

		ivCategory[selectedCategory].setImageResource(noSelectedCategoryImage[selectedCategory]);            // 원래 포커싱을 받고있던 카테고리에 포커싱안된 상태의 이미지로 설정해줌
		llCategoryView[selectedCategory].setVisibility(View.INVISIBLE);                                            // 원래 포커싱을 받고있던 카테고리의 내용을 안보여줌

		ivCategory[selectCategory].setImageResource(selectedCategoryImage[selectCategory]);                        // 현재 포커싱을 받은 카테고리에 포커싱된 상태의 이미지로 설정해줌
		llCategoryView[selectCategory].setVisibility(View.VISIBLE);                                                // 현재 포커싱을 받은 카테고리의 내용을 보여줌
		llCategoryView[selectCategory].setX(0);

		tvCategoryName.setText(CategoryName[selectCategory]);                                                    // 카테고리의 이름 출력

		if (selectCategory == 0)                                                                                        // 친구카테고리일 경우
			tvFriendNumber.setVisibility(View.VISIBLE);                                                            // 친구의 인원수 출력
		else                                                                                                        // 친구카테고리가 아닐 경우
			tvFriendNumber.setVisibility(View.INVISIBLE);                                                            // 친구의 인원수 가려줌
		selectedCategory = selectCategory;                                                                        // 포커싱을 받고있던 카테고리 정보에 현재 포커싱을 받게된 카테고리의 정보 넣어줌
	}

	public void settingSettingCategory() {
		ImageView ivUserPicture;                                                                                // 설정카테고리에서 사용자 사진
		ivUserPicture = findViewById(R.id.ivUserPicture);                                                        // 설정카테고리에서 사용자 사진
		ivUserPicture.setImageURI(Uri.parse(infoManagement.loginUser.profilePicture));                        // 이미지 설정

		TextView tvUserName = findViewById(R.id.tvUserName);
		tvUserName.setText(infoManagement.loginUser.name);

		TextView tvUserId = findViewById(R.id.tvUserId);
		tvUserId.setText(infoManagement.loginUser.id);

		ivUserPicture.setOnClickListener(new View.OnClickListener() {            // 사용자 사진 클릭시 프로필사진 변경 activity로 이동
			@Override
			public void onClick(View view) {                                                            // 사용자 이미지 클릭시
				Intent intent = new Intent(MainActivity.this, ProfilePictureActivity.class);    // 프로필수정 acitivy로 목적지 설정
				startActivity(intent);                                                                            // 프로필수정 activity로 이동
			}
		});
	}

	public void settingFriendCategory() {
		ivInsertFriend = findViewById(R.id.ivInsertFriend);
		lvFriendList = (ListView) findViewById(R.id.lvFriendList);                                    // 친구목록 listview
		adapterFriendList = new AdapterFriendList(this, infoManagement, onTouchListener, this);                            // 친구목록listview에 대한 어댑터 생성
		lvFriendList.setAdapter(adapterFriendList);                                                            // 어뎁터 연결

		lvFriendList.setOnTouchListener(onTouchListener);
	}

	public void settingChattingCategory() {
		ivInsertChatting = findViewById(R.id.ivInsertChatting);
		lvChattingList = (ListView) findViewById(R.id.lvChattingList);                                            // 채팅목록 listview
		adapterChattingList = new AdapterChattingList(this, infoManagement, onTouchListener, this);                                    // 채팅목록listview에 대한 어뎁터 생성
		lvChattingList.setAdapter(adapterChattingList);                                                                    // 어뎁터 연결
		lvChattingList.setOnTouchListener(onTouchListener);
	}

	public void settingNewsCategory() {                        // 뉴스카테고리의 뉴스들 정의

		news1 = findViewById(R.id.news1);
		news2 = findViewById(R.id.news2);
		news3 = findViewById(R.id.news3);
		news4 = findViewById(R.id.news4);
		news5 = findViewById(R.id.news5);
		news6 = findViewById(R.id.news6);


		news1.setOnClickListener(newsClickListener);
		news2.setOnClickListener(newsClickListener);
		news3.setOnClickListener(newsClickListener);
		news4.setOnClickListener(newsClickListener);
		news5.setOnClickListener(newsClickListener);
		news6.setOnClickListener(newsClickListener);


		news1.setOnTouchListener(onTouchListener);
		news2.setOnTouchListener(onTouchListener);
		news3.setOnTouchListener(onTouchListener);
		news4.setOnTouchListener(onTouchListener);
		news5.setOnTouchListener(onTouchListener);
		news6.setOnTouchListener(onTouchListener);

	}

	public void settingListener() {
		rlChattingDrawer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rlChattingDrawer.setVisibility(View.GONE);
			}
		});
		rlFriendDrawer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rlFriendDrawer.setVisibility(View.GONE);
			}
		});
		findViewById(R.id.ivBelow).setOnTouchListener(onTouchListener);
		findViewById(R.id.ivRight).setOnTouchListener(onTouchListener);

		ivInsertChatting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, MakeChattingActivity.class);
				startActivity(intent);
			}
		});
		ivInsertFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, InsertFriendActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {                                                    // 화면 오른쪽 맨위의 설정 이미지(세로로 점3개) 클릭시
				if (selectedCategory == 1) {                                                                                // 1번(채팅) 카테고리에서 해당 이미지 클릭하였을 시
					Intent intent = new Intent(MainActivity.this, ChattingRoomEditActivity.class);        // 채팅창 변경(삭제) activity로 목적지 설정
					startActivity(intent);                                                                                // 액티비티 실행
				}
			}
		});
	}

	public void displayFriendMenuBar(final int position) {
		final Friend friend = infoManagement.loginUser.friendList.get(infoManagement.loginUser.friendUUIDList.get(position));

		rlFriendDrawer.setVisibility(View.VISIBLE);

		tvMenuBarName2.setText(friend.getName(infoManagement));

		tvMenuBarChange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rlFriendDrawer.setVisibility(View.GONE);
				Intent intent = new Intent(MainActivity.this, ChangeFriendName.class);
				intent.putExtra("FriendPosition", position);
				startActivity(intent);
			}
		});
		tvMenuBarDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

				// AlertDialog 셋팅
				alertDialogBuilder
						.setMessage("친구를 삭제하시겠습니까?")
						.setCancelable(false)
						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										rlFriendDrawer.setVisibility(View.GONE);
										Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_exit_chat);
										animation.setAnimationListener(new Animation.AnimationListener() {
											@Override
											public void onAnimationStart(Animation animation) {

											}

											@Override
											public void onAnimationEnd(Animation animation) {
												infoManagement.loginUser.deleteFriend(friend, infoManagement);
												adapterFriendList.notifyDataSetChanged();
												tvFriendNumber.setText((infoManagement.loginUser.friendList.size() - 1) + "");
											}

											@Override
											public void onAnimationRepeat(Animation animation) {
											}
										});
										lvFriendList.getChildAt(position).startAnimation(animation);
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

	public void displayMenuBar(final int position) {
		final ChattingRoom chattingroom = infoManagement.loginUser.chattingroomMap.get(infoManagement.loginUser.chattingroomUUIDList.get(position));

		rlChattingDrawer.setVisibility(View.VISIBLE);
		tvMenuBarName.setText(chattingroom.ChattingRoomName(infoManagement));
		tvMenuBarNameSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rlChattingDrawer.setVisibility(View.GONE);
				Intent intent = new Intent(MainActivity.this, ChangeChatName.class);
				intent.putExtra("chattingRoomPosition", position);
				startActivity(intent);
			}
		});
		tvMenuBarEnter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rlChattingDrawer.setVisibility(View.GONE);
				Intent intent = new Intent(MainActivity.this, ChattingRoomActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("chattingRoomPosition", position);
				startActivity(intent);
			}
		});
		tvMenuBarExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

				// AlertDialog 셋팅
				alertDialogBuilder
						.setMessage("채팅방에서 나가시겠습니까?")
						.setCancelable(false)
						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										rlChattingDrawer.setVisibility(View.GONE);
										Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_exit_chat);
										animation.setAnimationListener(new Animation.AnimationListener() {
											@Override
											public void onAnimationStart(Animation animation) {

											}

											@Override
											public void onAnimationEnd(Animation animation) {
												infoManagement.loginUser.chattingRoomOut(chattingroom.chatting, infoManagement);
												adapterChattingList.notifyDataSetChanged();
											}

											@Override
											public void onAnimationRepeat(Animation animation) {
											}
										});
										lvChattingList.getChildAt(position).startAnimation(animation);
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

	View.OnTouchListener onTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {

			switch (motionEvent.getAction()) {

				case MotionEvent.ACTION_DOWN: {
					prevX = motionEvent.getX();
					startTime = System.currentTimeMillis();
					toX = 0;
				}
				case MotionEvent.ACTION_MOVE:
					currentX = motionEvent.getX();
					if (!(selectedCategory == 0 && toX + (currentX - prevX) >= 0) && !(selectedCategory == 3 && toX + (currentX - prevX) <= 0)) {
						toX += (currentX - prevX);
						llCategoryView[selectedCategory].setX(toX);
						tvSelectLine.setX(screenWidth / 4 * selectedCategory - toX / 4);

						if (llCategoryView[selectedCategory].getX() > 0) {
							llCategoryView[selectedCategory - 1].setVisibility(View.VISIBLE);
							llCategoryView[selectedCategory - 1].setX(toX - screenWidth);
							if (selectedCategory != 3 && llCategoryView[selectedCategory + 1].getX() != screenWidth) {
								llCategoryView[selectedCategory + 1].setX(screenWidth);
							}
						} else {
							llCategoryView[selectedCategory + 1].setVisibility(View.VISIBLE);
							llCategoryView[selectedCategory + 1].setX(toX + screenWidth);

							if (selectedCategory != 0 && llCategoryView[selectedCategory - 1].getX() != -screenWidth) {
								llCategoryView[selectedCategory - 1].setX(-screenWidth);
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					endTime = System.currentTimeMillis();

					if (llCategoryView[selectedCategory].getX() == 0 || (toX<10&& toX>-10) )
						return false;
					if ((toX / (endTime - startTime) > 1.2 || toX / (endTime - startTime) < -1.2 || (toX > screenWidth / 2 || toX < -screenWidth / 2))) {
						if (llCategoryView[selectedCategory].getX() < 0) {                // 왼쪽으로 움직였을 때
							ObjectAnimator.ofFloat(tvSelectLine, "translationX", screenWidth / 4 * (selectedCategory + 1)).setDuration(500).start();
							ObjectAnimator.ofFloat(llCategoryView[selectedCategory], "translationX", -screenWidth).setDuration(500).start();
							ObjectAnimator animator = ObjectAnimator.ofFloat(llCategoryView[selectedCategory + 1], "translationX", 0);
							animator.setDuration(500).start();
							animator.addListener(new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									super.onAnimationEnd(animation);
									display(selectedCategory + 1);
								}
							});

						} else {
							ObjectAnimator.ofFloat(tvSelectLine, "translationX", screenWidth / 4 * (selectedCategory - 1)).setDuration(500).start();
							ObjectAnimator.ofFloat(llCategoryView[selectedCategory], "translationX", screenWidth).setDuration(500).start();
							ObjectAnimator animator = ObjectAnimator.ofFloat(llCategoryView[selectedCategory - 1], "translationX", 0);
							animator.setDuration(500).start();
							animator.addListener(new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									super.onAnimationEnd(animation);
									display(selectedCategory - 1);
								}
							});

						}
					} else {
						ObjectAnimator.ofFloat(tvSelectLine, "translationX", screenWidth / 4 * selectedCategory).setDuration(500).start();
						if (llCategoryView[selectedCategory].getX() < 0) {                // 왼쪽으로 움직였을 때
							ObjectAnimator.ofFloat(llCategoryView[selectedCategory], "translationX", 0).setDuration(500).start();
							ObjectAnimator.ofFloat(llCategoryView[selectedCategory + 1], "translationX", screenWidth).setDuration(500).start();
						} else {
							ObjectAnimator.ofFloat(llCategoryView[selectedCategory], "translationX", 0).setDuration(500).start();
							ObjectAnimator.ofFloat(llCategoryView[selectedCategory - 1], "translationX", -screenWidth).setDuration(500).start();
						}
					}
					return true;
			}
			if (selectedCategory == 3)                // 설정 카테고리일때만 return true로 안하면 Action_Down다음에 move,up이벤트가 안됨;
				return true;
			return false;
		}
	};
	ImageView.OnClickListener categoryClickListener = new View.OnClickListener() {        //위의 친구,채팅목록 등등 클릭했을 때 이벤트
		@Override
		public void onClick(View view) {                // 카테고리 버튼(이미지뷰) 클릭시
			ImageView ivselectCategory = (ImageView) view;                            // 클릭된 카테고리 이미지뷰 저장
			for (int i = 0; i < 4; i++) {
				if (ivselectCategory == ivCategory[i]) {                                // 클릭된 카테고리가 몇번 카테고리인지 알아낸 다음
					display(i);                                                        // 해당 카테고리로 화면 변경
					break;                                                            // 변경완료시 반복문 종료
				}
			}
		}
	};
	ImageView.OnClickListener newsClickListener = new View.OnClickListener() {        //위의 친구,채팅목록 등등 클릭했을 때 이벤트
		@Override
		public void onClick(View view) {                                                // 뉴스 기사들 클릭했을 때, 클릭한 뉴스에 따라 해당 uri로 이동
			LinearLayout layout = (LinearLayout) view;
			Intent intent;
			if (view == news1)
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cafe.daum.net/ssaumjil/LnOm/1915192?svc=kakaotalkTab&bucket=toros_cafe_channel_base"));
			else if (view == news2)
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cafe.daum.net/ok1221/9Zdf/1031914?q=%BC%B1%B9%CC%B6%FB%20%B0%B0%C0%CC%20%BC%BF%C4%AB%20%C2%EF%B0%ED%20%BD%CD%C0%BA%20%B3%B2%C6%D2"));
			else if (view == news3)
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cafe.daum.net/incheonhealing/KXlD/465?q=%BD%CF%20%B4%D9%20%BE%F3%BE%EE%B9%F6%B8%B0%20%C7%D1%B1%B9"));
			else if (view == news4)
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cafe.daum.net/subdued20club/ReHf/1871122?q=%BE%C9%BE%C6%BC%AD%202%BE%EF%20%B9%FA%C0%BD.jpg"));
			else if (view == news5)
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cafe.daum.net/subdued20club/ReHf/1872410?q=40%BB%EC%20%BE%C6%C0%CC%B5%B9%20%C5%B0"));
			else if (view == news6)
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cafe.daum.net/ok1221/9Zdf/1040361?q=%BC%D2%BC%D3%BB%E7%20%BB%E7%C0%E5%C0%B8%B7%CE%20%BC%BA%B0%F8%C7%D1%20%BA%B8%BE%C6%20%B8%C5%B4%CF%C0%FA%B5%E9"));
			else if (view == news7)
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cafe.daum.net/subdued20club/ReHf/1844045?q=%C3%DF%BD%C5%BC%F6%B3%D7%20%B5%FE%20%C3%BC%B7%C2%20%BC%F6%C1%D8"));
			else
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cafe.daum.net/ok1221/9Zdf/1036888?q=%B8%AE%BE%F3%B8%AE%C6%BC%BF%A1%BC%AD%20%BA%B8%BE%C6%B0%A1%20%BE%F0%B1%DE%C7%D1%20sm%C7%D2%B7%CE%C0%A9%20%C6%C4%C6%BC%20%BE%C6%C0%CC%BE%F0%B8%C7"));

			startActivity(intent);    // 해당 uri 인터넷 창을 실행시킴
		}
	};
}
