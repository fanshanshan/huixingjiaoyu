package com.qulink.hxedu.ui.fragment;


import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.qulink.hxedu.R;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.util.ScreenUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneFragment extends Fragment {


    @BindView(R.id.recycle_official_action)
    RecyclerView recycleOfficialAction;
    @BindView(R.id.recycle_subject)
    RecyclerView recycleSubject;
    @BindView(R.id.recycle_share_content)
    RecyclerView recycleShareContent;
    private View rootView;

    public ZoneFragment() {
        // Required empty public constructor
    }
    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_zone, container, false);

            ButterKnife.bind(this, rootView);
            initOffcialaction();
            initSubject();
            initShareContent();
        }
        return rootView;
    }


    List<String> actionList;
    CommonRcvAdapter<String> actionAdapter;

    void initOffcialaction() {
        actionList = new ArrayList<>();
        actionList.add("社区怎么玩，我来教你啊");
        actionList.add("社区怎么玩，我来教你啊");
        actionList.add("社区怎么玩，我来教你啊");

        actionAdapter = new CommonRcvAdapter<String>(actionList) {
            TextView tvTtitle;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.official_action_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvTtitle = root.findViewById(R.id.tv_title);
                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {

                        tvTtitle.setText(o.toString());
                    }
                };
            }
        };
        recycleOfficialAction.setAdapter(actionAdapter);
        recycleOfficialAction.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    List<SubjectBean> subjectBeanList;
    CommonRcvAdapter<SubjectBean> subjectAdapter;

    void initSubject() {
        subjectBeanList = new ArrayList<>();
        subjectBeanList.add(new SubjectBean("社区到底怎么玩啊", 1));
        subjectBeanList.add(new SubjectBean("社区到底怎么玩啊", 2));
        subjectBeanList.add(new SubjectBean("社区到底怎么玩啊", 0));
        subjectBeanList.add(new SubjectBean("社区到底怎么玩啊", 0));
        subjectBeanList.add(new SubjectBean("社区到底怎么玩啊", 2));
        subjectBeanList.add(new SubjectBean("社区到底怎么玩啊", -1));


        subjectAdapter = new CommonRcvAdapter<SubjectBean>(subjectBeanList) {
            SubjectBean subjectBean = null;

            TextView tvTitle;
            ImageView ivimg;

            @Override
            public Object getItemType(SubjectBean subjectBean) {

                return subjectBean.type;
            }

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {

                if ((int) type == -1) {
                    return new MoreItem();
                }
                return new TitleItem();
            }
        };

        recycleSubject.setAdapter(subjectAdapter);
        recycleSubject.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    private List<ShareContentBean> shareContentBeanList;
    CommonRcvAdapter<ShareContentBean> shareContentBeanCommonRcvAdapter;

    void initShareContent() {
        shareContentBeanList = new ArrayList<>();
        ArrayList<String> imglist;
        imglist = new ArrayList<>();
        imglist.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1285056959,3836499669&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        shareContentBeanList.add(new ShareContentBean("http://img1.imgtn.bdimg.com/it/u=3234180860,1920348045&fm=26&gp=0.jpg",
                "我是幂总啊", "幂 编辑 讨论99+ 上传视频 \n" +
                "杨幂，1986年9月12日出生于北京市，中国内地影视女演员、流行乐歌手、影视制片人。2005年，杨幂进入北京电影学院表演系本科班就读。2006年，因出演金庸武侠剧《神雕侠侣》而崭露头角。2008年，凭借古装剧《王昭君》获得了第24届中国电视金鹰奖观众喜爱的电视剧女演员奖提名。2009年，杨幂在“80后新生代娱乐大明星”评选活动中与其她三位女演员共同被评为“四小花旦”。2011年，凭借穿越剧《宫锁心",
                imglist, "这个妹子怎么样", 123, 456
        ));
        imglist = new ArrayList<>();
        imglist.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1285056959,3836499669&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        shareContentBeanList.add(new ShareContentBean("http://img1.imgtn.bdimg.com/it/u=3234180860,1920348045&fm=26&gp=0.jpg",
                "我是幂总啊", "幂 编辑 讨论99+ 上传视频 \n" +
                "杨幂，1986年9月12日出生于北京市，中国内地影视女演员、流行乐歌手、影视制片人。2005年，杨幂进入北京电影学院表演系本科班就读。2006年，因出演金庸武侠剧《神雕侠侣》而崭露头角。2008年，凭借古装剧《王昭君》获得了第24届中国电视金鹰奖观众喜爱的电视剧女演员奖提名。2009年，杨幂在“80后新生代娱乐大明星”评选活动中与其她三位女演员共同被评为“四小花旦”。2011年，凭借穿越剧《宫锁心",
                imglist, "这个妹子怎么样", 123, 456
        ));
        imglist = new ArrayList<>();
        imglist.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1285056959,3836499669&fm=111&gp=0.jpg");
        imglist.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1285056959,3836499669&fm=111&gp=0.jpg");
        imglist.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1285056959,3836499669&fm=111&gp=0.jpg");
        imglist.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2788273389,252614550&fm=111&gp=0.jpg");
        shareContentBeanList.add(new ShareContentBean("http://img1.imgtn.bdimg.com/it/u=3234180860,1920348045&fm=26&gp=0.jpg",
                "我是幂总啊", "幂 编辑 讨论99+ 上传视频 \n" +
                "杨幂，1986年9月12日出生于北京市，中国内地影视女演员、流行乐歌手、影视制片人。2005年，杨幂进入北京电影学院表演系本科班就读。2006年，因出演金庸武侠剧《神雕侠侣》而崭露头角。2008年，凭借古装剧《王昭君》获得了第24届中国电视金鹰奖观众喜爱的电视剧女演员奖提名。2009年，杨幂在“80后新生代娱乐大明星”评选活动中与其她三位女演员共同被评为“四小花旦”。2011年，凭借穿越剧《宫锁心",
                imglist, "这个妹子怎么样", 123, 456
        ));
        shareContentBeanCommonRcvAdapter = new CommonRcvAdapter<ShareContentBean>(shareContentBeanList) {

            TextView tvName;
            TextView tvCommentNum;
            TextView tvLikeNum;
            TextView tvItem;
            ExpandableTextView expandableTextView;
            RecyclerView imgRecycleview;
            ImageView ivHeadimg;
            LinearLayout levelContanier;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {


                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.subject_share_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {

                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                        tvName = root.findViewById(R.id.tv_name);
                        tvCommentNum = root.findViewById(R.id.tv_comment_num);
                        tvLikeNum = root.findViewById(R.id.tv_like_num);
                        expandableTextView = root.findViewById(R.id.content);
                        tvItem = root.findViewById(R.id.tv_item);
                        ivHeadimg = root.findViewById(R.id.iv_headimg);
                        imgRecycleview = root.findViewById(R.id.recycle_img);
                        levelContanier = root.findViewById(R.id.ll_level_contanier);

                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {

                        if (o instanceof ShareContentBean) {
                            ShareContentBean shareContentBean = (ShareContentBean) o;
                            tvLikeNum.setText(shareContentBean.commentNum + "");
                            tvCommentNum.setText(shareContentBean.commentNum + "");
                            tvName.setText(shareContentBean.name + "");

                            expandableTextView.setContent(shareContentBean.content);
                            Glide.with(getActivity()).load(shareContentBean.headimg).into(ivHeadimg);

                            ImageView imageView = new ImageView(getActivity());
                            imageView.setImageResource(R.drawable.hg);

                            levelContanier.addView(imageView);

                            imageView = new ImageView(getActivity());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.leftMargin = 6;
                            imageView.setLayoutParams(layoutParams);
                            imageView.setImageResource(R.drawable.xx);

                            levelContanier.addView(imageView);

                            if(shareContentBean.item!=""){
                                tvItem.setText(shareContentBean.item);
                                tvItem.setVisibility(View.VISIBLE);
                            }else{
                                tvItem.setVisibility(View.GONE);

                            }

                            imgRecycleview.setAdapter(new CommonRcvAdapter<String>(shareContentBean.imgList) {

                                ImageView iv;

                                @NonNull
                                @Override
                                public AdapterItem createItem(Object type) {
                                    return new AdapterItem() {
                                        @Override
                                        public int getLayoutResId() {
                                            return R.layout.imageview_item;
                                        }

                                        @Override
                                        public void bindViews(@NonNull View root) {
                                            iv = root.findViewById(R.id.iv);
                                        }

                                        @Override
                                        public void setViews() {


                                        }

                                        @Override
                                        public void handleData(Object o, int position) {
                                            iv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getActivity(), ImagesPreviewActivity.class);
                                                    intent.putExtra("position",position);
                                                    intent.putStringArrayListExtra("data",shareContentBean.imgList);
                                                    startActivity(intent);
                                                }
                                            });
                                            Glide.with(getActivity()).load(o.toString()).into(iv);
                                        }
                                    };
                                }
                            });
                            imgRecycleview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                        }

                    }
                };
            }
        };
        recycleShareContent.addItemDecoration(new SpacesItemDecoration(0, 16,0,0));

        recycleShareContent.setAdapter(shareContentBeanCommonRcvAdapter);
        recycleShareContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    class TitleItem implements AdapterItem {
        TextView tvTitle;
        ImageView ivImag;

        @Override
        public int getLayoutResId() {
            return R.layout.subject_item;
        }

        @Override
        public void bindViews(@NonNull View root) {

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvTitle = root.findViewById(R.id.tv_title);
            ivImag = root.findViewById(R.id.iv_img);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(Object o, int position) {

            SubjectBean subjectBean = (SubjectBean) o;

            tvTitle.setText(subjectBean.title);
            if (subjectBean.type == 1) {
                ivImag.setImageResource(R.drawable.hot);
            } else if (subjectBean.type == 2) {
                ivImag.setImageResource(R.drawable.tj);
            }
        }
    }

    class MoreItem implements AdapterItem {
        TextView tvMore;

        @Override
        public int getLayoutResId() {
            return R.layout.more_subject_text;
        }

        @Override
        public void bindViews(@NonNull View root) {

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvMore = root.findViewById(R.id.tv_more);
        }

        @Override
        public void setViews() {

            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(getActivity(), "更多");
                }
            });
        }

        @Override
        public void handleData(Object o, int position) {

        }
    }

    class SubjectBean {
        String title;
        int type;

        public SubjectBean() {
        }

        public SubjectBean(String title, int type) {
            this.title = title;
            this.type = type;
        }
    }

    class ShareContentBean {
        String headimg;
        String name;
        String content;
        ArrayList<String> imgList;
        String item;
        int commentNum;
        int likeNum;

        public ShareContentBean(String headimg, String name, String content, ArrayList<String> imgList, String item, int commentNum, int likeNum) {
            this.headimg = headimg;
            this.name = name;
            this.content = content;
            this.imgList = imgList;
            this.item = item;
            this.commentNum = commentNum;
            this.likeNum = likeNum;
        }
    }



}




