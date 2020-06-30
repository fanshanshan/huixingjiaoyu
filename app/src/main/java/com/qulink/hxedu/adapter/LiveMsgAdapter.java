package com.qulink.hxedu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AudienceMsg;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AudienceNotice;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AuthorMsg;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AuthorNotice;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.SysMsg;
import com.qulink.hxedu.util.ViewUtils;

import java.util.List;

public class LiveMsgAdapter extends RecyclerView.Adapter {
    private List data;
    private Context context;

    public LiveMsgAdapter(List data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sys_msg_item, parent, false);
            SysMsgHolder sysMsgHolder = new SysMsgHolder(view);
            return sysMsgHolder;
        }else if(viewType==2||viewType==4){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audience_msg_item, parent, false);
            AudienceMsgHolder sysMsgHolder = new AudienceMsgHolder(view);
            return sysMsgHolder;
        }else if(viewType==3||viewType==5){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audience_notice_item, parent, false);
            AudienceNoticeHolder sysMsgHolder = new AudienceNoticeHolder(view);
            return sysMsgHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SysMsgHolder){
            SysMsgHolder sysMsgHolder = (SysMsgHolder)holder;
            if(data.get(position) instanceof SysMsg){
                SysMsg sysMsg = (SysMsg)data.get(position);
                sysMsgHolder.tMsg.setText(sysMsg.content);
            }
        }else if(holder instanceof AudienceMsgHolder){
            AudienceMsgHolder audienceMsgHolder = (AudienceMsgHolder)holder;
            if(data.get(position) instanceof  AudienceMsg){
                AudienceMsg audienceMsg = (AudienceMsg)data.get(position);
                audienceMsgHolder.tMsg.setText(audienceMsg.msg);
                audienceMsgHolder.tvName.setText(audienceMsg.audienceInfo.userName+": ");
                audienceMsgHolder. tvLevel.setBackgroundResource(ViewUtils.getLevelBgByLevel(audienceMsg.audienceInfo.level));
                audienceMsgHolder. tvLevel.setText("Lv" + audienceMsg.audienceInfo.level);
            }else if(data.get(position) instanceof  AuthorMsg){
                AuthorMsg audienceMsg = (AuthorMsg)data.get(position);
                audienceMsgHolder.tMsg.setText(audienceMsg.msg);
                audienceMsgHolder. tvLevel.setBackgroundResource(ViewUtils.getLevelBgByLevel(audienceMsg.anchorInfo.level));
                audienceMsgHolder. tvLevel.setText("Lv" + audienceMsg.anchorInfo.level);
                audienceMsgHolder.tvName.setText(audienceMsg.anchorInfo.userName+": ");
            }
        }else if(holder instanceof AudienceNoticeHolder){
            AudienceNoticeHolder audienceMsgHolder = (AudienceNoticeHolder)holder;
            if(data.get(position) instanceof  AudienceNotice){
                AudienceNotice audienceMsg = (AudienceNotice)data.get(position);
                audienceMsgHolder. tvLevel.setBackgroundResource(ViewUtils.getLevelBgByLevel(audienceMsg.audienceInfo.level));
                audienceMsgHolder. tvLevel.setText("Lv" + audienceMsg.audienceInfo.level);
                audienceMsgHolder.tMsg.setText(audienceMsg.msg);
                audienceMsgHolder.tvName.setText(audienceMsg.audienceInfo.userName+": ");
            }else if(data.get(position) instanceof  AuthorNotice){
                AuthorNotice audienceMsg = (AuthorNotice)data.get(position);
                audienceMsgHolder.tMsg.setText(audienceMsg.msg);
                audienceMsgHolder. tvLevel.setBackgroundResource(ViewUtils.getLevelBgByLevel(audienceMsg.anchorInfo.level));
                audienceMsgHolder. tvLevel.setText("Lv" + audienceMsg.anchorInfo.level);
                audienceMsgHolder.tvName.setText(audienceMsg.anchorInfo.userName+": ");
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position) instanceof SysMsg){
            return 1;
        }else  if(data.get(position) instanceof AudienceMsg){
            return 2;
        }else  if(data.get(position) instanceof AudienceNotice){
            return 3;
        }else  if(data.get(position) instanceof AuthorMsg){
            return 4;
        }else  if(data.get(position) instanceof AuthorNotice){
            return 5;
        }
        return super.getItemViewType(position);
    }


    class SysMsgHolder extends RecyclerView.ViewHolder{
        TextView tMsg;
        public SysMsgHolder(@NonNull View root) {
            super(root);
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tMsg = root.findViewById(R.id.tv_msg);
        }
    }  class AudienceMsgHolder extends RecyclerView.ViewHolder{
        TextView tMsg;
        TextView tvLevel;
        TextView tvName;
        public AudienceMsgHolder(@NonNull View root) {
            super(root);
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tMsg = root.findViewById(R.id.tv_msg);
            tvName = root.findViewById(R.id.tv_name);
            tvLevel = root.findViewById(R.id.tv_level);
        }
    }class AudienceNoticeHolder extends RecyclerView.ViewHolder{
        TextView tMsg;
        TextView tvLevel;

        TextView tvName;
        public AudienceNoticeHolder(@NonNull View root) {
            super(root);
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tMsg = root.findViewById(R.id.tv_msg);
            tvName = root.findViewById(R.id.tv_name);
            tvLevel = root.findViewById(R.id.tv_level);

        }
    }

}
