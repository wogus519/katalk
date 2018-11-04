package com.example.wogus.katalk.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.Class.User;
import com.example.wogus.katalk.R;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wogus on 2018-02-13.
 */

public class JoinMemberActivity extends AppCompatActivity {

	InfoManagement infoManagement;                                                    // 전체적인 데이터 저장개체(db역할)
	Handler handler;
	Thread thDecreaseValidTime;
	boolean[] isPassed;
	int randNumber, validTime;

	EditText etId, etPassword, etPasswordCheck, etName, etPhoneNumber, etCertification;
	TextView tvId, tvPassword, tvPasswordCheck, tvPhoneNumber, tvName, tvCertification, tvJoinMember, tvEffectiveTime;
	Button btPhoneNumber, btCertification, btJoinMember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_join_activity);

		init();
		setListener();
	}

	@Override
	protected void onPause() {                                                                                    // pause상태일 때
		super.onPause();
		infoManagement.saveData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if (thDecreaseValidTime != null && !thDecreaseValidTime.isInterrupted())
//			thDecreaseValidTime.interrupt();
	}

	public void init() {
		infoManagement = (InfoManagement) getApplication();                                    // 전체적인 데이터 저장개체(db역할)
		isPassed = new boolean[6];

		etId = findViewById(R.id.etId);
		etPassword = findViewById(R.id.etPassword);
		etPasswordCheck = findViewById(R.id.etPasswordCheck);
		etName = findViewById(R.id.etName);
		etPhoneNumber = findViewById(R.id.etPhoneNumber);
		etCertification = findViewById(R.id.etCertification);

		tvEffectiveTime = findViewById(R.id.tvEffectiveTime);
		tvId = findViewById(R.id.tvId);
		tvPassword = findViewById(R.id.tvPassword);
		tvPasswordCheck = findViewById(R.id.tvPasswordCheck);
		tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
		tvName = findViewById(R.id.tvName);
		tvCertification = findViewById(R.id.tvCertification);
		tvJoinMember = findViewById(R.id.tvJoinMember);

		btCertification = findViewById(R.id.btCertification);
		btPhoneNumber = findViewById(R.id.btPhoneNumber);
		btJoinMember = findViewById(R.id.btJoinMember);

		handler = new Handler();
	}

	public void setListener() {
		btJoinMember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isPassed[0] == true && isPassed[1] == true && isPassed[2] == true && isPassed[3] == true && isPassed[4] == true && isPassed[5] == true) {
					infoManagement.makeNewMember(etId.getText().toString(), etPassword.getText().toString(), etName.getText().toString(), etPhoneNumber.getText().toString());
					Intent intent = new Intent(JoinMemberActivity.this, LoginActivity.class);                    // 채팅창 변경(삭제) activity로 목적지 설정
					finish();
					startActivity(intent);                                                                                        // 액티비티 실행
				} else
					tvJoinMember.setVisibility(View.VISIBLE);
			}
		});
		btCertification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tvCertification.setVisibility(View.VISIBLE);

				if (validTime == -1) {
					tvCertification.setText("인증번호 유효시간이 만료되었습니다.");
					tvCertification.setTextColor(Color.RED);
					isPassed[5] = false;
				} else if (etCertification.getText().toString().equals(randNumber + "")) {
					tvCertification.setText("인증번호가 일치합니다.");
					tvCertification.setTextColor(Color.GREEN);
					isPassed[5] = true;
				} else {
					tvCertification.setText("인증번호가 일치하지 않습니다.");
					tvCertification.setTextColor(Color.RED);
					isPassed[5] = false;
				}
			}
		});
		btPhoneNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tvPhoneNumber.setVisibility(View.VISIBLE);
				isPassed[5] = false;

				if (validTime > 150) {
					tvPhoneNumber.setText((validTime-150)+"초 뒤 다시 시도해주세요.");
					tvPhoneNumber.setTextColor(Color.RED);
					isPassed[4] = false;
				} else {
					try {
						Integer.parseInt(etPhoneNumber.getText().toString());
						randNumber = new Random().nextInt(900000) + 100000;

						sendSMS(etPhoneNumber.getText().toString(), "["+randNumber+"]  해당번호를 인증번호칸에 입력하세요.");

						tvPhoneNumber.setText("인증번호가 발송되었습니다.");
						tvPhoneNumber.setTextColor(Color.GREEN);
						isPassed[4] = true;

						if (thDecreaseValidTime != null && !thDecreaseValidTime.isInterrupted())
							thDecreaseValidTime.interrupt();

						thDecreaseValidTime = new Thread(new Runnable() {
							@Override
							public void run() {
								validTime = 180;
								while (--validTime >= 0) {
									handler.post(new Runnable() {
										@Override
										public void run() {
											System.out.println("시간:" + validTime);
											tvEffectiveTime.setText(validTimeToString());
										}
									});
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										validTime = 0;
										Thread.currentThread().interrupt();
									}
								}
							}
						});
						thDecreaseValidTime.start();
					} catch (NumberFormatException e) {
						tvPhoneNumber.setText("휴대전화번호를 확인해주세요.");
						tvPhoneNumber.setTextColor(Color.RED);
						isPassed[4] = false;
					} catch (Exception e) {	}
				}
			}
		});
		etId.addTextChangedListener(new TextWatcher() {                                                            // 채팅메세지에 변화 생겼을시
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                        // 채팅메세지 변화 생겼을 시
				String regExp_symbol = "^[a-z0-9]*$";
				Pattern pattern_symbol = Pattern.compile(regExp_symbol);

				tvId.setVisibility(View.VISIBLE);
				if (!pattern_symbol.matcher(s).find() || s.length() < 5 || s.length() > 15) {
					tvId.setText("5~15자의 영문 소문자와 숫자만 사용 가능합니다.");
					tvId.setTextColor(Color.RED);
					isPassed[0] = false;
				} else if (infoManagement.allUser.get(s.toString()) != null) {
					tvId.setText("이미 사용중인 아이디입니다.");
					tvId.setTextColor(Color.RED);
					isPassed[0] = false;
				} else {
					tvId.setText("사용할 수 있는 아이디입니다.");
					tvId.setTextColor(Color.GREEN);
					isPassed[0] = true;
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
		etPassword.addTextChangedListener(new TextWatcher() {                                                            // 채팅메세지에 변화 생겼을시
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                        // 채팅메세지 변화 생겼을 시
				String regExp_symbol = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";
				Pattern pattern_symbol = Pattern.compile(regExp_symbol);

				tvPassword.setVisibility(View.VISIBLE);
				if (pattern_symbol.matcher(s).find()) {
					tvPassword.setText("사용할 수 있는 비밀번호입니다.");
					tvPassword.setTextColor(Color.GREEN);
					isPassed[1] = true;
				} else {
					tvPassword.setText("8~16자의 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
					tvPassword.setTextColor(Color.RED);
					isPassed[1] = false;
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
		etPasswordCheck.addTextChangedListener(new TextWatcher() {                                                            // 채팅메세지에 변화 생겼을시
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                        // 채팅메세지 변화 생겼을 시

				if (etPassword.getText().toString().length() > 0) {
					tvPasswordCheck.setVisibility(View.VISIBLE);
					if (!etPassword.getText().toString().equals(s.toString())) {
						tvPasswordCheck.setText("비밀번호가 일치하지 않습니다.");
						tvPasswordCheck.setTextColor(Color.RED);
						isPassed[2] = false;
					} else {
						tvPasswordCheck.setText("사용할 수 있는 비밀번호입니다.");
						tvPasswordCheck.setTextColor(Color.GREEN);
						isPassed[2] = true;
					}
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});

		etName.addTextChangedListener(new TextWatcher() {                                                            // 채팅메세지에 변화 생겼을시
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                        // 채팅메세지 변화 생겼을 시

				if (etName.getText().toString().length() > 0) {
					tvName.setVisibility(View.GONE);
					isPassed[3] = true;
				} else {
					tvName.setText("이름을 입력하세요.");
					tvName.setTextColor(Color.RED);
					isPassed[3] = false;
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
	}

	public String validTimeToString() {
		if (validTime % 60 < 10)
			return validTime / 60 + ":0" + validTime % 60;
		else
			return validTime / 60 + ":" + validTime % 60;
	}
	public void sendSMS(String smsNumber, String smsText) {

		if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {                // 전화 권한있는지 확인
			ActivityCompat.requestPermissions(JoinMemberActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);                    // 권한 부여할 것인지 요청창 뜨게함
		} else {
			SmsManager mSmsManager = SmsManager.getDefault();
			mSmsManager.sendTextMessage(smsNumber, null, smsText, null, null);
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					sendSMS(etPhoneNumber.getText().toString(), "[" + randNumber + "]  해당번호를 인증번호칸에 입력하세요.");
				}
				return;
		}
	}

}
