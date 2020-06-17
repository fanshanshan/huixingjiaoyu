package com.qulink.hxedu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.ui.zone.ArticalDetailActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.FastClick;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;

import org.xutils.image.ImageOptions;

import java.util.ArrayList;
import java.util.List;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;


/**
 * Created by Administrator on 2018/11/24 0024.
 */

public class TopicAdapter extends RecyclerView.Adapter {
    private List<PicBean> data;
    private Context context;

    public TopicAdapter(List<PicBean> list, Context context) {
        this.data = list;
        this.context = context;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_share_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position, @NonNull List payloads) {
       // super.onBindViewHolder(viewHolder, position, payloads);
        if(payloads.isEmpty()){
            onBindViewHolder(viewHolder,position);
        }else{
            PicBean shareContentBean = data.get(position);
            ViewHolder holder = (ViewHolder)viewHolder;
            holder.tvLikeNum.setText(shareContentBean.getThumbs() + "");
            holder. tvCommentNum.setText(shareContentBean.getComments() + "");

            if (CourseUtil.isOk(shareContentBean.getThumbStatus())) {
                holder. ivLike.setImageResource(R.drawable.liked);
            } else {
                holder.  ivLike.setImageResource(R.drawable.like);
            }
            if (!shareContentBean.isInitMaster()) {
                if (shareContentBean.getPicMaster() == null) {
                    shareContentBean.setInitMaster(true);
                    if(picAdapterController!=null){
                        picAdapterController.getPicMaster(shareContentBean.getUserId());
                    }
                } else {
                    holder.tvName.setText(shareContentBean.getPicMaster().getNickname() + "");
                    App.getInstance().getDefaultSetting(context, new DefaultSettingCallback() {
                        @Override
                        public void getDefaultSetting(DefaultSetting defaultSetting) {
                            Glide.with(context).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), shareContentBean.getPicMaster().getHeadImg())).into(holder.ivHeadimg);
                        }
                    });
                    if (CourseUtil.isOk(shareContentBean.getPicMaster().getStatus())) {
                        ImageView imageView = new ImageView(context);
                        imageView.setImageResource(R.drawable.hg);
                        holder.levelContanier.addView(imageView);
                    }else{
                        holder.levelContanier.removeAllViews();
                    }
                    if (shareContentBean.getPicMaster().getBadge()!=null&&!shareContentBean.getPicMaster().getBadge().isEmpty()) {

                        ImageView imageView = new ImageView(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.leftMargin = 6;
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageResource(R.drawable.xx);
                        holder.levelContanier.addView(imageView);
                    }
                }
            }

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        PicBean shareContentBean = data.get(position);
        ViewHolder holder = (ViewHolder)viewHolder;
        if (!shareContentBean.isInitMaster()) {
            if (shareContentBean.getPicMaster() == null) {
                shareContentBean.setInitMaster(true);
                if(picAdapterController!=null){
                    picAdapterController.getPicMaster(shareContentBean.getUserId());
                }
            } else {
                holder.tvName.setText(shareContentBean.getPicMaster().getNickname() + "");
                App.getInstance().getDefaultSetting(context, new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        Glide.with(context).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), shareContentBean.getPicMaster().getHeadImg())).into(holder.ivHeadimg);
                    }
                });
                if (CourseUtil.isOk(shareContentBean.getPicMaster().getStatus())) {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.drawable.hg);
                    holder.levelContanier.addView(imageView);
                }else{
                    holder.levelContanier.removeAllViews();
                }
                if (!shareContentBean.getPicMaster().getBadge().isEmpty()) {

                    ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = 6;
                    imageView.setLayoutParams(layoutParams);
                    imageView.setImageResource(R.drawable.xunzhang);
                    holder.levelContanier.addView(imageView);
                }
            }
        }
        if (CourseUtil.isOk(shareContentBean.getThumbStatus())) {
            holder. ivLike.setImageResource(R.drawable.liked);
        } else {
            holder.  ivLike.setImageResource(R.drawable.like);
        }
        //点赞 和取消点赞
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.getInstance().isLogin(context,true)){
                    if(FastClick.isFastClick()){
                        if(CourseUtil.isOk(shareContentBean.getThumbStatus())){
                            if(picAdapterController!=null){
                                picAdapterController.cancelLikeArtical(shareContentBean.getId());
                            }
                        }else{
                            if(picAdapterController!=null){
                                picAdapterController.likeArtical(shareContentBean.getId());
                            }
                        }
                    }
                }

            }
        });

        holder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picAdapterController!=null){
                    picAdapterController.goArticalDetail(shareContentBean);
                }
            }
        });


        holder.tvLikeNum.setText(shareContentBean.getThumbs() + "");
        holder. tvCommentNum.setText(shareContentBean.getComments() + "");
        holder. expandableTextView.setContent(shareContentBean.getContent());
        holder. levelContanier.removeAllViews();

        if (!TextUtils.isEmpty(shareContentBean.getTopicName())) {
            holder. tvItem.setText(shareContentBean.getTopicName());
            holder. tvItem.setVisibility(View.VISIBLE);
            holder. tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RouteUtil.goSubjectPage((Activity) context,shareContentBean.getTopicName(),shareContentBean.getTopicId(),0);
                }
            });
        } else {
            holder.tvItem.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(shareContentBean.getImgPath())) {
            App.getInstance().getDefaultSetting(context, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    final ArrayList<String> imgLig = new ArrayList<>();
                    String[] strings = shareContentBean.getImgPath().split(",");
                    for (String s : strings) {
                        s = ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), s);
                        imgLig.add(s);
                    }

                    holder. imgRecycleview.setAdapter(new CommonRcvAdapter<String>(imgLig) {

                        ImageView iv;

                        @NonNull
                        @Override
                        public AdapterItem createItem(Object type) {
                            return new AdapterItem<String>() {
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
                                public void handleData(String o, int position) {
                                    iv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(context, ImagesPreviewActivity.class);
                                            intent.putExtra("position", position);
                                            intent.putStringArrayListExtra("data", imgLig);
                                           Activity activity = (Activity)context;
                                            activity. startActivity(intent);
                                        }
                                    });
                                    try {
                                        Glide.with(context).load(o).into(iv);

                                    } catch (Exception e) {
                                    }
                                }
                            };
                        }
                    });
                    holder. imgRecycleview.setLayoutManager(new GridLayoutManager(context, 3));

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvCommentNum;
        TextView tvLikeNum;
        TextView tvItem;
        ExpandableTextView expandableTextView;
        RecyclerView imgRecycleview;
        ImageView ivHeadimg;
        ImageView ivLike;
        LinearLayout llRoot;
        LinearLayout levelContanier;
        LinearLayout llLike;

        ViewHolder(View root) {
            super(root);

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            tvName = root.findViewById(R.id.tv_name);
            tvCommentNum = root.findViewById(R.id.tv_comment_num);
            tvLikeNum = root.findViewById(R.id.tv_like_num);
            expandableTextView = root.findViewById(R.id.content);
            tvItem = root.findViewById(R.id.tv_item);
            ivHeadimg = root.findViewById(R.id.iv_headimg);
            imgRecycleview = root.findViewById(R.id.recycle_img);
            llRoot = root.findViewById(R.id.ll_root);
            levelContanier = root.findViewById(R.id.ll_level_contanier);
            llLike = root.findViewById(R.id.ll_like);
            ivLike = root.findViewById(R.id.iv_like);

        }
    }



//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//        if (manager instanceof GridLayoutManager) {
//            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    int type = getItemViewType(position);
//                    switch (type) {
//                        case 0: //这两种方式都是一列的，所以返回6
//                            return 1;
//                        case 1: //两列，返回3
//                            return 3;
//                        default:
//                            return 1;
//                    }
//                }
//            });
//        }
//    }



    PicAdapterController picAdapterController;

    public PicAdapterController getClickListener() {
        return picAdapterController;
    }

    public void setClickListener(PicAdapterController clickListener) {
        this.picAdapterController = clickListener;
    }

    public  interface PicAdapterController {
        void getPicMaster(int userId);
        void cancelLikeArtical(int id);
        void likeArtical(int id);
        void goArticalDetail(PicBean picBean);
    }

}
