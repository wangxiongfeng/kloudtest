package com.ub.techexcel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kloudsync.techexcel5.bean.EventShowFullAgora;
import com.kloudsync.techexcel5.bean.MeetingConfig;
import com.kloudsync.techexcel5.config.AppConfig;
import com.kloudsync.techexcel5.help.MeetingKit;
import com.kloudsync.techexcel5.httpgetimage.ImageLoader;
import com.kloudsync.techexcel5.view.CircleImageView;
import com.kloudsync.techexcel5.R;
import com.ub.techexcel.bean.AgoraMember;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SingleAgoraRightAdapter extends RecyclerView.Adapter<SingleAgoraRightAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<AgoraMember> users;
    private Context mContext;
    private ImageLoader imageLoader;
    private MeetingConfig meetingConfig;


    public List<AgoraMember> getUsers() {
        return users;
    }

    public SingleAgoraRightAdapter(Context context, MeetingConfig meetingConfig) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        users = new ArrayList<>();
        this.meetingConfig=meetingConfig;
        imageLoader = new ImageLoader(context);
    }


    private OnCameraOptionsListener onCameraOptionsListener;

    public void setOnCameraOptionsListener(OnCameraOptionsListener onCameraOptionsListener) {
        this.onCameraOptionsListener = onCameraOptionsListener;
    }

    public interface OnCameraOptionsListener {
            void onVideoClick();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
//        view = inflater.inflate(R.layout.medium_camera_item, parent, false);
        view = inflater.inflate(R.layout.small_camera_item, parent, false);
        return new SingleAgoraRightAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SingleAgoraRightAdapter.ViewHolder myHolder = holder;
        final AgoraMember user =users.get(position);
        if (user == null) {
            return;
        }
        holder.vedioFrame.removeAllViews();
        if (holder.vedioFrame.getChildCount() == 0) {
            View d = inflater.inflate(R.layout.framelayout_head, null);
            TextView videoname = (TextView) d.findViewById(R.id.videoname);
            ViewParent parent = videoname.getParent();
            if (parent != null) {
                ((RelativeLayout) parent).removeView(videoname);
            }
            holder.vedioFrame.addView(videoname);
            SurfaceView target = user.getSurfaceView();
            Log.e("agora_camera_adapter", "is_mute_video:" + user.isMuteVideo() + ",surfaceview:" + target);
            if (!user.isMuteVideo()) {
                if (target != null) {
                    user.setSurfaceShowing(true);
                    target.setVisibility(View.VISIBLE);
                    stripSurfaceView(target);
                    holder.vedioFrame.addView(target, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
            } else {
                user.setSurfaceShowing(false);
                Log.e("onBindViewHolder", "target_gone");
                if (target != null) {
                    stripSurfaceView(target);
                    target.setVisibility(View.INVISIBLE);
                }
            }

            final AgoraMember _user = user;
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventShowFullAgora showFullAgora = new EventShowFullAgora();
                    showFullAgora.setAgoraMember(_user);
                    EventBus.getDefault().post(showFullAgora);
                }
            });
            //---
            if (!TextUtils.isEmpty(user.getUserName())) {
                holder.nameText.setText(user.getUserName());
            } else {
                holder.nameText.setText("");
            }



            if (user.isMuteAudio()) {
                holder.audioStatusImage.setImageResource(R.drawable.microphone);
            } else {
                holder.audioStatusImage.setImageResource(R.drawable.microphone_enable);
                user.setHaveShowUnMuteAudioImage(true);
            }
            holder.audioStatusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (TextUtils.isEmpty(user.getIconUrl())) {
                holder.iconImage.setImageResource(R.drawable.hello);
            } else {
                imageLoader.DisplayImage(user.getIconUrl(), holder.iconImage);
            }


            if(meetingConfig.getPresenterId().equals(AppConfig.UserID)||meetingConfig.getMeetingHostId().equals(AppConfig.UserID)){
                holder.image_vedio_status.setVisibility(View.VISIBLE);
            }else{
                if((user.getUserId()+"").equals(AppConfig.UserID)){
                    holder.image_vedio_status.setVisibility(View.VISIBLE);
                }else{
                    holder.image_vedio_status.setVisibility(View.GONE);
                }
            }
            holder.image_vedio_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((user.getUserId()+"").equals(AppConfig.UserID)){
                        MeetingKit.getInstance().menuCameraClicked(user.isMuteVideo());
                    }else{
                        MeetingKit.getInstance().muteRemoteVideoStream(user.getUserId(),user.isMuteVideo());
                    }
                    onCameraOptionsListener.onVideoClick();
                }
            });

            if(user.isMuteVideo()){
                holder.image_vedio_status.setImageResource(R.drawable.icon_command_webcam_disable);
                holder.iconImage.setVisibility(View.VISIBLE);

            }else {
                holder.image_vedio_status.setImageResource(R.drawable.icon_command_webcam_enable);
                holder.iconImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    protected final void stripSurfaceView(SurfaceView view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((FrameLayout) parent).removeView(view);
        }
    }



    public void setMembers(List<AgoraMember> users) {
        if (users != null) {
            this.users.clear();
            this.users.addAll(users);
            Collections.sort(users);
        }
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            vedioFrame = view.findViewById(R.id.video_view_container);
            nameText = view.findViewById(R.id.txt_name);
            audioStatusImage = view.findViewById(R.id.image_audio_status);
            iconImage = view.findViewById(R.id.member_icon);
            selectMemberContainer = view.findViewById(R.id.select_peple_container);
            selectPeopleImage = view.findViewById(R.id.icon_select_people);
            image_vedio_status = view.findViewById(R.id.image_vedio_status);
        }

        public FrameLayout vedioFrame;
        public TextView nameText;
        public ImageView audioStatusImage;
        public CircleImageView iconImage;
        public RelativeLayout selectMemberContainer;
        public ImageView selectPeopleImage;
        public ImageView image_vedio_status;
    }


    public void muteVideo(AgoraMember member, boolean isMute) {
        int index = this.users.indexOf(member);
        if (index >= 0) {
            this.users.get(index).setMuteVideo(isMute);
            notifyItemChanged(index);
        }
    }

    public void muteAudio(AgoraMember member, boolean isMute) {
        int index = this.users.indexOf(member);
        Log.e("AgoraCameraAdapter", "muteAudio:" + isMute + ",index:" + index);
        if (index >= 0) {
            this.users.get(index).setMuteAudio(isMute);
            notifyItemChanged(index);
        }
    }

    public void refreshAgoraMember(AgoraMember agoraMember) {
        int index = this.users.indexOf(agoraMember);
        if (index >= 0) {
            Log.e("AgoraCameraAdapter", "refresh_agora_member");
            notifyItemChanged(index);
        }
    }

    public void refreshVideoStatus(AgoraMember member) {

        int index = this.users.indexOf(member);

        if (index >= 0) {
            AgoraMember agoraMember = this.users.get(index);

            if (member.isMuteVideo() == agoraMember.isMuteVideo()) {
                if (!agoraMember.isMuteVideo()) {
                    if (!agoraMember.isSurfaceShowing()) {
                        notifyItemChanged(index);
                    }
                } else {
                    if (agoraMember.isSurfaceShowing()) {
                        notifyItemChanged(index);
                    }
                }
            }

        }
    }

    public void refreshAudioStatus(AgoraMember member) {
        int index = this.users.indexOf(member);

        if (index >= 0) {
            AgoraMember agoraMember = this.users.get(index);

            if (!(agoraMember.isMuteAudio() == member.isMuteAudio())) {
                Log.e("refreshAudioStatus", "member:" + member.isMuteAudio() + ",agoraMember:" + agoraMember.isMuteAudio());
                agoraMember.setMuteAudio(member.isMuteAudio());
                notifyItemChanged(index);

            } else {
                Log.e("refreshAudioStatus", "member:" + member.isMuteAudio() + ",agoraMember:" + agoraMember.isMuteAudio());
                Log.e("refreshAudioStatus", "have_show_un_mute:" + agoraMember.isHaveShowUnMuteAudioImage());
                if (!agoraMember.isMuteAudio()) {
                    if (!agoraMember.isHaveShowUnMuteAudioImage()) {
                        notifyItemChanged(index);
                    }
                }
            }

        }
    }

    public void showFull(int position) {
        EventShowFullAgora showFullAgora = new EventShowFullAgora();
        showFullAgora.setAgoraMember(users.get(position));
        EventBus.getDefault().post(showFullAgora);
    }

    public void showFull(AgoraMember member) {
        EventShowFullAgora showFullAgora = new EventShowFullAgora();
        showFullAgora.setAgoraMember(member);
        EventBus.getDefault().post(showFullAgora);
    }

    public void setMySelfVedioSurface(SurfaceView surface, int userId) {
        int index = this.users.indexOf(new AgoraMember(userId));
        if (index >= 0) {
            if (this.users.get(index).getSurfaceView() == null) {
                this.users.get(index).setSurfaceView(surface);
                notifyItemChanged(index);
            }
        }
    }

    public void refreshMyAgoraStatus(AgoraMember agoraMember) {
        int index = this.users.indexOf(agoraMember);
//        Log.e("refreshMyAgoraStatus","notifyItemChanged,user:" + this.users.get(index));
        if (index >= 0) {
            AgoraMember _member = this.users.get(index);
            _member.setMuteAudio(agoraMember.isMuteAudio());
            _member.setMuteVideo(agoraMember.isMuteVideo());
            Log.e("refreshMyAgoraStatus", "notifyItemChanged,user:" + this.users.get(index));
            notifyItemChanged(index);
        }
    }




}
