package com.example.wogus.katalk.Activity;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wogus.katalk.Adapter.AdapterRecommendList;
import com.example.wogus.katalk.Adapter.AdapterRecyclerList;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wogus on 2018-02-18.
 */

public class InsertFriendActivity extends AppCompatActivity {
	InfoManagement infoManagement;                                    // 전체적인 데이터 관리하는 클래스

	Button btFind, btInsertFriend;
	EditText etFindUserId;
	ImageView ivNoUser, ivFriendPciture;
	RelativeLayout rlSearchUser;
	LinearLayout llExplain;
	TextView tvFriendName, tvLoginUserId,tvRecommnedFreind;
	RecyclerView recyclerView;
	User findUser;
	boolean isExplainDisplayed;
	boolean isDragging;
	AdapterRecommendList adapterRecommendList;
	ArrayList<String> recommendFriendUUIDList;
	Handler handler;

	float prevX;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_friend);

		infoManagement = (InfoManagement) getApplication();                                        // 전체적인 데이터 저장개체(db역할)

		btFind = findViewById(R.id.btFind);
		btInsertFriend = findViewById(R.id.btInsertFriend);
		etFindUserId = findViewById(R.id.etFindUserId);
		ivNoUser = findViewById(R.id.ivNoUser);
		ivFriendPciture = findViewById(R.id.ivFriendPciture);
		rlSearchUser = findViewById(R.id.rlSearchUser);
		llExplain = findViewById(R.id.llExplain);
		tvFriendName = findViewById(R.id.tvFriendName);
		tvLoginUserId = findViewById(R.id.tvLoginUserId);
		tvLoginUserId.setText(infoManagement.loginUser.id);
		tvRecommnedFreind = findViewById(R.id.tvRecommnedFreind);
		recyclerView = findViewById(R.id.rvRecommendFriend);

		isExplainDisplayed = true;
		handler = new Handler();
		settingListener();
		setRecyclerView();
	}

	public void setRecyclerView() {
		recommendFriendUUIDList = infoManagement.loginUser.recommendFreindUUIDList;
		if(recommendFriendUUIDList.size()==0)
			tvRecommnedFreind.setVisibility(View.GONE);
		else
			tvRecommnedFreind.setVisibility(View.VISIBLE);

		adapterRecommendList = new AdapterRecommendList(getApplicationContext(), R.layout.recyclerview_recommend, infoManagement, recommendFriendUUIDList, recyclerView);

		LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
		horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

		recyclerView.setLayoutManager(horizontalLayoutManager);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapterRecommendList);
		Thread thread= new Thread(new Runnable() {
			@Override
			public void run() {
				while (recommendFriendUUIDList.size()>5) {
					try {
						Thread.sleep(700);
					} catch (Exception e) {
						System.out.println("에러:" + e);
					}
					if(!isDragging) {
						handler.post(new Runnable() {
							@Override
							public void run() {
							recyclerView.smoothScrollBy(100, 0);
							//ObjectAnimator.ofInt(recyclerView, "scrollX", 40).setDuration(10).start();
							}
						});
					}
				}
			}
		});
		thread.start();

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);

				if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
					isDragging = true;
				}
				else if(newState == RecyclerView.SCROLL_STATE_IDLE)
					isDragging = false;
				else{}
			}
//			@Override
//			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//				super.onScrolled(recyclerView, dx, dy);
//				int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//				int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
//
//				if (lastVisibleItemPosition == itemTotalCount&& adapterRecommendList.getItemCount()>=6) {
//					adapterRecommendList.addItem();
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							adapterRecommendList.notifyDataSetChanged();
//						}
//					});
//					//recyclerView.scrollToPosition(itemTotalCount-5);
//				}
//			}
		});
	}
	public void settingListener() {
		etFindUserId.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                        // 채팅메세지 변화 생겼을 시
				if (!isExplainDisplayed) {
					isExplainDisplayed = true;
					llExplain.setVisibility(View.VISIBLE);
					rlSearchUser.setVisibility(View.GONE);
					ivNoUser.setVisibility(View.GONE);
				}
				if (s.toString().length() != 0) {
					btFind.setEnabled(true);
					btFind.setTextColor(Color.parseColor("#ffffff"));
				} else {
					btFind.setEnabled(false);
					btFind.setTextColor(Color.parseColor("#b3a0a0"));
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
		btFind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isExplainDisplayed = false;
				llExplain.setVisibility(View.INVISIBLE);
				findUser = infoManagement.allUser.get(etFindUserId.getText().toString());

				if (findUser == null) {
					rlSearchUser.setVisibility(View.INVISIBLE);
					ivNoUser.setVisibility(View.VISIBLE);
				} else {
					ivNoUser.setVisibility(View.INVISIBLE);
					rlSearchUser.setVisibility(View.VISIBLE);
					ivFriendPciture.setImageURI(Uri.parse(findUser.profilePicture));
					tvFriendName.setText(findUser.name);

					if (infoManagement.loginUser.friendList.get(findUser.uuid) != null)
						btInsertFriend.setVisibility(View.GONE);
					else
						btInsertFriend.setVisibility(View.VISIBLE);
				}
			}
		});
		btInsertFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				infoManagement.loginUser.addFriend(findUser, infoManagement);
				btInsertFriend.setVisibility(View.GONE);
				adapterRecommendList.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		infoManagement.saveData();
	}
}
