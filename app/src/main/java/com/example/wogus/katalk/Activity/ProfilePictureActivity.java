package com.example.wogus.katalk.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.wogus.katalk.Class.InfoManagement;
import com.example.wogus.katalk.R;

/**
 * Created by wogus on 2018-01-31.
 */

public class ProfilePictureActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int CROP_FROM_CAMERA = 2;

    InfoManagement infoManagement;
    ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_picture);

        infoManagement= (InfoManagement) getApplication();
        ivPicture = findViewById(R.id.ivProfilePicture);
        ivPicture.setImageURI(Uri.parse(infoManagement.loginUser.profilePicture));

        findViewById(R.id.btBack).setOnClickListener(new View.OnClickListener(){      // 전송버튼 눌렀을 때
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ProfilePictureActivity.this, MainActivity.class);
                intent.putExtra("selectCategory",3);
                finish();
                startActivity(intent);
            }
        });
        findViewById(R.id.btPhoto).setOnClickListener(new View.OnClickListener(){      // 카메라버튼 눌렀을 때
            @Override
            public void onClick(View view){
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1 , PICK_FROM_CAMERA);
            }
        });
        findViewById(R.id.btAlbum).setOnClickListener(new View.OnClickListener(){      // 앨범버튼 눌렀을 때
            @Override
            public void onClick(View view){         
                System.out.println("앨범 누름");

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_GALLERY);
            }
        });
    }
    @Override
    protected void onPause() {																							// 화면출력 바로전
        super.onPause();
        infoManagement.saveData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을경우
            System.out.println("권한없음");
            // 최초 권한 요청인지, 혹은 사용자에 의한 재요청인지 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 사용자가 임의로 권한을 취소시킨 경우
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                // 최초로 권한을 요청하는 경우(첫실행)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }else {
            switch (requestCode) {
                case PICK_FROM_GALLERY:
                {
                    System.out.println("사진시작");
                    Uri mImageCaptureUri = data.getData();
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mImageCaptureUri, "image/*");
                    // crop한 이미지를 저장할때 200x200 크기로 저장
                    intent.putExtra("outputX", 200); // crop한 이미지의 x축 크기
                    intent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
                    //intent.putExtra("aspectX", 2); // crop 박스의 x축 비율
                    // intent.putExtra("aspectY", 1); // crop 박스의 y축 비율
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CROP_FROM_CAMERA);
                    break;
                }
                case PICK_FROM_CAMERA:{
                    System.out.println("픽프롬카메라");
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivPicture.setImageBitmap(bitmap);
                    break;
                }
                case CROP_FROM_CAMERA:
                {
                    System.out.println("사진 가져옴");
                    ivPicture.setImageURI(data.getData());
                    infoManagement.loginUser.profilePicture=data.getData().toString();
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // 갤러리 사용권한에 대한 콜백을 받음
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 동의 버튼 선택
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_GALLERY);
                } else {
                    System.out.println("여기서이동");
                    Intent intent = new Intent(ProfilePictureActivity.this, MainActivity.class);
                    intent.putExtra("selectCategory",3);
                    startActivity(intent);
                }
                return;
            }
            // 예외케이스
        }
    }
}