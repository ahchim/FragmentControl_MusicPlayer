package com.ahchim.android.fragmentcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private Listener mListener;
    private ArrayList<Music> datas;


    public ListFragment() {
        // Required empty public constructor
        // 비어 있어야만 함.
    }

    ////////////////////
    // 프래그먼트가 Activity에서 호출되는 순간, 호출한 Activity의 Context가 넘어온다.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 넘어온 context(Activity)가 OnFragment리스너의 구현체인지를 확인
        // instanceof : 타입 체크
        // 넘어온 activity를 Listener로 캐스팅해서 mListener 변수에 담아둔다.
        // 3. 사용하는 사용자측에서 인터페이스 필터링...
        // (메인액티비티가 아니라 리스너로만 담아서 일부 기능만 사용해서 필터링이라고 한다.)
        mListener = (Listener) context;   // 여기서 캐스팅 오류나는데 무엇 때문일까
        datas = mListener.getData();

    }
    // 1. 인터페이스 선언
    // 나를 호출하는 activity에서 구현해야되는 인터페이스
    public interface Listener {
        ArrayList<Music> getData();
        void goDetail(int position);
    }
    //////////////////


    // 거의 이자식만 쓴다고 보면 된다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            return inflater.inflate(R.layout.fragment_list, container, false);
        } else {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_list, container, false);
            init(view);
            return view;
        }
    }


    public void init(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // 2. 아답터 생성
        // 3. 뷰에 아답터 세팅
        recyclerView.setAdapter(new MusicAdapter(mListener.getData(), view.getContext()));

        // 4. 레이아웃 매니저
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // 리사이클러뷰 아답터를 생성합니다. (Music Adapter를 복사해서 사용해도 됩니다.)
    public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.Holder>{
        private ArrayList<Music> datas;
        private Context context;
        private Intent intent = null;

        public MusicAdapter(ArrayList<Music> datas, Context context){
            this.datas = datas;
            this.context = context;
            // 인텐트를 매번 부르지 않도록 해본다.
            this.intent = new Intent(context, PlayerActivity.class);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final Music music = datas.get(position);

            holder.getTxtTitle().setText(music.getTitle());
            holder.getTxtArtist().setText(music.getArtist());

            holder.setPosition(position);

            // 1. URI 직접 넣기 (가장 간단한 방법)
            //holder.getImgView().setImageURI(music.getAlbum_image());

            // 2. 앨범아트 BitMap으로 생성해서 넣기
            //if(music.getBitmap_image() != null) holder.getImgView().setImageBitmap(music.getBitmap_image());

            // 3. Glide 사용하기
            Glide.with(context).load(music.getAlbum_image()).into(holder.getImgView());
            //                       1. 로드할 대상 URI            2. 입력될 이미지뷰


            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            holder.cardView.setAnimation(animation);
        }
        @Override
        public int getItemCount() {
            return datas.size();
        }

        public class Holder extends RecyclerView.ViewHolder{
            private TextView txtTitle, txtArtist;
            private ImageView imgView;
            private CardView cardView;

            private int position;

            public Holder(View view) {
                super(view);

                this.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                this.txtArtist = (TextView) view.findViewById(R.id.txtArtist);
                this.imgView = (ImageView) view.findViewById(R.id.imgView);
                this.cardView = (CardView) view.findViewById(R.id.cardView);

                // Holder에 클릭리스너 코드를 넣는 것
                cardView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        mListener.goDetail(0);
                    }
                });
            }

            public TextView getTxtTitle() {
                return txtTitle;
            }

            public TextView getTxtArtist() {
                return txtArtist;
            }

            public ImageView getImgView() {
                return imgView;
            }

            public void setPosition(int position) { this.position = position; }
        }
    }

}

