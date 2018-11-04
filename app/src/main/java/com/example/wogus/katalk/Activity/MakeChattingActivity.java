package com.example.wogus.katalk.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wogus.katalk.Adapter.AdapterMakeChattinglist;
import com.example.wogus.katalk.Adapter.AdapterRecyclerList;
import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wogus on 2018-02-19.
 */

public class MakeChattingActivity extends AppCompatActivity{

	InfoManagement infoManagement;									// 전체 모든 정보 저장

	EditText etFindUserId;
	TextView tvInviteUserNum;
	Button btInvite;

	AdapterMakeChattinglist adapterMakeChattinglist;
	AdapterRecyclerList adapterRecyclerList;
	HashMap<String,Boolean> isChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_chattingroom);

		infoManagement = (InfoManagement) getApplication();
		etFindUserId = findViewById(R.id.etFindUserId);
		tvInviteUserNum = findViewById(R.id.tvInviteUserNum);
		btInvite = findViewById(R.id.btInvite);

		setIsChecked();
		setListView();
		setRecyclerView();
		setListener();

		adapterRecyclerList.adapterMakeChattinglist = adapterMakeChattinglist;
		adapterMakeChattinglist.adapterRecyclerList = adapterRecyclerList;
	}
	public void setRecyclerView(){
		RecyclerView recyclerView = findViewById(R.id.rvSelectFriend);

		adapterRecyclerList=new AdapterRecyclerList(getApplicationContext(),R.layout.recyclerview_item,infoManagement,isChecked,this);

		LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
		horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

		recyclerView.setLayoutManager(horizontalLayoutManager);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapterRecyclerList);
	}
	public void setListView(){
		ListView lvFriendList = (ListView) findViewById(R.id.lvFriendList);									// 친구목록 listview
		adapterMakeChattinglist= new AdapterMakeChattinglist(this, infoManagement,checkName(""),isChecked,this);							// 친구목록listview에 대한 어댑터 생성
		lvFriendList.setAdapter(adapterMakeChattinglist);
	}
	public void setListener(){
		btInvite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(adapterRecyclerList.dataList.size()!=1) {
					adapterRecyclerList.dataList.add(infoManagement.loginUser.uuid);
					Intent intent = new Intent(MakeChattingActivity.this, ChattingRoomActivity.class);
					intent.putExtra("type", 2);
					intent.putExtra("uuidList", (Serializable) adapterRecyclerList.dataList);
					startActivity(intent);
					finish();
				}else{
					Intent intent = new Intent(MakeChattingActivity.this, ChattingRoomActivity.class);
					intent.putExtra("type", 0);
					intent.putExtra("frienduuid", adapterRecyclerList.dataList.get(0));
					startActivity(intent);
					finish();
				}
			}
		});

		etFindUserId.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                        // 채팅메세지 변화 생겼을 시
				adapterMakeChattinglist.changeList(checkName(s));
				adapterMakeChattinglist.notifyDataSetChanged();
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

	public ArrayList<String> checkName(CharSequence typedName){
		ArrayList<String> friendUUIDListBuf = new ArrayList<>();
		User loginUser = infoManagement.loginUser;

		for(int i=1; i< loginUser.friendUUIDList.size();i++){
			User user = infoManagement.findUserByUUID(loginUser.friendUUIDList.get(i));
			if(user.name.contains(typedName))
				friendUUIDListBuf.add(user.uuid);
		}
		return friendUUIDListBuf;
	}

	public void setIsChecked(){
		User loginUser = infoManagement.loginUser;
		isChecked = new HashMap<>();

		for(int i=1;i<loginUser.friendUUIDList.size();i++){
			isChecked.put(loginUser.friendUUIDList.get(i),false);
		}
	}

	public void setAbove(){
		int size = adapterRecyclerList.dataList.size();

		tvInviteUserNum.setText(size+"");
		if(size == 0 ){
			btInvite.setTextColor(Color.parseColor("#8b7878"));
			btInvite.setEnabled(false);
		}else{
			btInvite.setTextColor(Color.parseColor("#ffffff"));
			btInvite.setEnabled(true);
		}
	}
	@Override
	protected  void onPause(){
		super.onPause();
		infoManagement.saveData();
	}
}
