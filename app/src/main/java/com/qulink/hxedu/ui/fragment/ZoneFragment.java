package com.qulink.hxedu.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_zone, container, false);

            ButterKnife.bind(this, rootView);
            initOffcialaction();
            initSubject();
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
