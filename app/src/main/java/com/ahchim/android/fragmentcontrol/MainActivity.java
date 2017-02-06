package com.ahchim.android.fragmentcontrol;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Music> datas;
    ListFragment list;
    DetailFragment detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 프래그먼트 생성
        list = new ListFragment();
        detail = new DetailFragment();

        datas = DataLoader.get(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        } else {
            init();
        }

        // 2. 프래그먼트 매니저 가져오기
        //if(sdk 버전체크){
        //getFragmentManager();                                   // 앱호환성이 없는 매니저함수
        //}
        //else {
        FragmentManager manager = getSupportFragmentManager();    // 앱호환성이 있는 매니저함수
        // 요 아이가 동작 안하는 곳도 있기 때문에 둘다 알아둬야 한다.
        //}

        // 태스크 그룹에서 문제가 생겼을 때 원복시키는 과정 (초기화의 단위)- transaction
        // 3. 프래그먼트를 실행하기위한 트랜잭션 시작
        FragmentTransaction transaction = manager.beginTransaction();
        // 4. 프래그먼트를 레이아웃에 add한다.
        transaction.add(R.id.fragment, list);

        transaction.addToBackStack(null);
        // 5. git의 commit과 같은 기능 - commit 하면 되돌릴 수 없고, commit이 되기 전에 오류가 나면 자동으로 돌려준다.
        transaction.commit();
    }

    private final int REQ_CODE = 100;

    // 1. 권한체크
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(){
        // 1.1 런타임 권한체크
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // 1.2 요청할 권한 목록 작성
            String permArr[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
            // 1.3 시스템에 권한 요청
            requestPermissions(permArr, REQ_CODE);
        } else{
            init();
        }
    }

    // 2. 권한체크 후 콜백 < 사용자가 확인후 시스템이 호출하는 함수
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
                super.onRequestPermissionsResult(requestCode, permissions, grantResult);
                // 2.1 배열에 넘긴 런타임권한을 체크해서 승인이 되면
                if(requestCode == REQ_CODE){
                    if(grantResult[0] == PackageManager.PERMISSION_GRANTED){
                        // 2.2 프로그램 실행
                        init();
                    } else{
                Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다", Toast.LENGTH_SHORT).show();
                //checkPermission();
                finish();
            }
        }
    }

    // 데이터를 로드할 함수
    private void init(){
        Toast.makeText(this, "프로그램을 실행합니다.", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_main);
    }

    // 2. 인터페이스 구현
    //@Override
    public ArrayList<Music> getData() {
        return datas;
    }

    public void goDetail(int position){

        // 2. 프래그먼트 매니저 가져오기
        //if(sdk 버전체크){
            //getFragmentManager();                                   // 앱호환성이 없는 매니저함수
        //}
        //else {
            FragmentManager manager = getSupportFragmentManager();    // 앱호환성이 있는 매니저함수
            // 요 아이가 동작 안하는 곳도 있기 때문에 둘다 알아둬야 한다.
        //}

        // 태스크 그룹에서 문제가 생겼을 때 원복시키는 과정 (초기화의 단위)- transaction
        // 3. 프래그먼트를 실행하기위한 트랜잭션 시작
        FragmentTransaction transaction = manager.beginTransaction();
        // 4. 프래그먼트를 레이아웃에 add한다.
        transaction.add(R.id.fragment, detail);
        // 5. git의 commit과 같은 기능 - commit 하면 되돌릴 수 없고, commit이 되기 전에 오류가 나면 자동으로 돌려준다.
        transaction.commit();
    }


    public void goList(){
        // 2. 프래그먼트 매니저 가져오기
        //if(sdk 버전체크){
        //getFragmentManager();                                   // 앱호환성이 없는 매니저함수
        //}
        //else {
        FragmentManager manager = getSupportFragmentManager();    // 앱호환성이 있는 매니저함수
        //}

        // 태스크 그룹에서 문제가 생겼을 때 원복시키는 과정 (초기화의 단위)- transaction
        // 3. 프래그먼트를 실행하기위한 트랜잭션 시작
        FragmentTransaction transaction = manager.beginTransaction();
        // 4. Detail 프래그먼트 하나를 떼어낸다.
        transaction.detach(detail);
        // 5. git의 commit과 같은 기능 - commit 하면 되돌릴 수 없고, commit이 되기 전에 오류가 나면 자동으로 돌려준다.
        transaction.commit();
    }
}
