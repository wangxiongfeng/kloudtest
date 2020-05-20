package com.kloudsync.techexcel5.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kloudsync.techexcel5.R;
import com.kloudsync.techexcel5.bean.EventShowFullAgora;
import com.kloudsync.techexcel5.bean.MeetingConfig;
import com.kloudsync.techexcel5.config.AppConfig;
import com.kloudsync.techexcel5.dialog.loading.KloudLoadingView;
import com.kloudsync.techexcel5.help.MeetingKit;
import com.kloudsync.techexcel5.httpgetimage.ImageLoader;
import com.ub.techexcel.adapter.SingleAgoraRightAdapter;
import com.ub.techexcel.bean.AgoraMember;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseWebActivity extends FragmentActivity {

    @Bind(R.id.layout_enter_loading)
    protected LinearLayout enterLoading;

    @Bind(R.id.enter_loading)
    protected KloudLoadingView loadingView;

    @Bind(R.id.web)
    protected WebView web;

    @Bind(R.id.web_note)
    protected WebView noteWeb;

    @Bind(R.id.menu)
    protected ImageView menuIcon;

    @Bind(R.id.layout_note_users)
    LinearLayout noteUsersLayout;

    @Bind(R.id.single_layout_item_full_screen)
    LinearLayout itemFullScreenLayout;

    @Bind(R.id.single_right_camera_list)
    RecyclerView single_right_camera_list;

    @Bind(R.id.layout_fullsceen_vedio)
    FrameLayout fullScreenVedio;

    @Bind(R.id.single_txt_name)
    TextView singleTextName;

    @Bind(R.id.icon_back_single_full_screen)
    ImageView singleFullScreenImage;

    @Bind(R.id.single_image_audio_status)
    ImageView singleAudioStatusImage;

    @Bind(R.id.single_image_vedio_status)
    ImageView singleVedioStatusImage;

    @Bind(R.id.handupnumberrl)
    RelativeLayout handupnumberrl;

    @Bind(R.id.handupsumber)
    TextView handupsumber;

    @Bind(R.id.single_member_icon)
    ImageView singleMemberIcon;

    @Bind(R.id.icon_videofull_single_full_screen)
    ImageView icon_videofull_single_full_screen;

    @Bind(R.id.rly_meeting_pause_layout)
    RelativeLayout mRlyMeetingPauseLayout;
    protected AgoraMember currentAgoraMember;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("BaseActivity", "on_create");
        setContentView(R.layout.activity_doc_and_meeting_v2);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        showEnterLoading();
        initView();
        initData();
//        Toast.makeText(this,"on_create",Toast.LENGTH_SHORT).show();

    }

    private void initView() {
        Configuration configuration = getResources().getConfiguration();
        RelativeLayout.LayoutParams layoutParams = null;
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(DocAndMeetingActivity.this,"现在是竖屏", Toast.LENGTH_SHORT).show();
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.dp_70);
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(DocAndMeetingActivity.this,"现在是横屏", Toast.LENGTH_SHORT).show();
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.dp_9);

        }
        mRlyMeetingPauseLayout.setLayoutParams(layoutParams);
    }

    public void showEnterLoading() {
        enterLoading.setVisibility(View.VISIBLE);
        loadingView.smoothToShow();
    }

    public void hideEnterLoading() {
        loadingView.hide();
        enterLoading.setVisibility(View.GONE);
    }

    public abstract void showErrorPage();

    public abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void hideAgoraFull() {
        fullScreenVedio.removeAllViews();
        itemFullScreenLayout.setVisibility(View.GONE);
        rightAgoraAdapter = new SingleAgoraRightAdapter(this,new MeetingConfig());
        rightAgoraAdapter.setMembers(new ArrayList<AgoraMember>());
        single_right_camera_list.setVisibility(View.GONE);
    }

    protected void hideAgoraFull2() {
        fullScreenVedio.removeAllViews();
        itemFullScreenLayout.setVisibility(View.GONE);
        rightAgoraAdapter = new SingleAgoraRightAdapter(this,new MeetingConfig());
        rightAgoraAdapter.setMembers(new ArrayList<AgoraMember>());
        single_right_camera_list.setVisibility(View.GONE);
    }


    protected void showAgoraFull(AgoraMember agoraMember, MeetingConfig meetingConfig) {
        currentAgoraMember = agoraMember;
        itemFullScreenLayout.setVisibility(View.VISIBLE);
        SurfaceView target = agoraMember.getSurfaceView();
        if (!agoraMember.isMuteVideo() && target != null) {
            Log.e("showAgoraFull", "show");
            target.setVisibility(View.VISIBLE);
            stripSurfaceView(target);
            fullScreenVedio.addView(target, 0, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            Log.e("showAgoraFull", "surface_view_gone");
            stripSurfaceView(target);
            target.setVisibility(View.INVISIBLE);
        }
        if (TextUtils.isEmpty(agoraMember.getUserName())) {
            singleTextName.setVisibility(View.GONE);
            singleTextName.setText("");
        } else {
            singleTextName.setVisibility(View.VISIBLE);
            singleTextName.setText(agoraMember.getUserName());
        }

        if (agoraMember.isMuteAudio()) {
            singleAudioStatusImage.setImageResource(R.drawable.microphone);
        } else {
            singleAudioStatusImage.setImageResource(R.drawable.microphone_enable);
        }
//
        if (TextUtils.isEmpty(agoraMember.getIconUrl())) {
            singleMemberIcon.setImageResource(R.drawable.hello);
        } else {
            new ImageLoader(this).DisplayImage(agoraMember.getIconUrl(), singleMemberIcon);
        }

        if (agoraMember.isMuteVideo()) {
            singleMemberIcon.setVisibility(View.VISIBLE);
            singleVedioStatusImage.setImageResource(R.drawable.icon_command_webcam_disable);

        } else {
            singleMemberIcon.setVisibility(View.GONE);
            singleVedioStatusImage.setImageResource(R.drawable.icon_command_webcam_enable);
        }

        switchVideo(agoraMember, meetingConfig);
    }


    private SingleAgoraRightAdapter rightAgoraAdapter;

    private void switchVideo(final AgoraMember user, final MeetingConfig meetingConfig) {
        if (user == null) {
            return;
        }
        single_right_camera_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        single_right_camera_list.setDrawingCacheEnabled(true);
        single_right_camera_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        List<AgoraMember> agoraMembers = meetingConfig.getAgoraMembers();
        List<AgoraMember> agoraMembers1 = new ArrayList<>();

        agoraMembers1.clear();
        agoraMembers1.addAll(agoraMembers);

        for (int i = 0; i < agoraMembers1.size(); i++) {
            AgoraMember agoraMember = agoraMembers1.get(i);
            if (agoraMember.getUserId() == user.getUserId()) {
                agoraMembers1.remove(i);
                break;
            }
        }

        //------开关闭摄像头---
        if(meetingConfig.getPresenterId().equals(AppConfig.UserID)||meetingConfig.getMeetingHostId().equals(AppConfig.UserID)){
            singleVedioStatusImage.setVisibility(View.VISIBLE);
        }else{
            if((user.getUserId()+"").equals(AppConfig.UserID)){
                singleVedioStatusImage.setVisibility(View.VISIBLE);
            }else{
                singleVedioStatusImage.setVisibility(View.GONE);
            }
        }
        singleVedioStatusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((user.getUserId()+"").equals(AppConfig.UserID)){
                    MeetingKit.getInstance().menuCameraClicked(user.isMuteVideo());
                }else{
                    MeetingKit.getInstance().muteRemoteVideoStream(user.getUserId(),user.isMuteVideo());
                }
                showAgoraFull(user,meetingConfig);
            }
        });
        //-----开关闭摄像头---

        if (agoraMembers1.size() > 0) {
            single_right_camera_list.setVisibility(View.VISIBLE);
            rightAgoraAdapter = new SingleAgoraRightAdapter(this,meetingConfig);
            rightAgoraAdapter.setOnCameraOptionsListener(new SingleAgoraRightAdapter.OnCameraOptionsListener() {
                @Override
                public void onVideoClick() {
                    showAgoraFull(user,meetingConfig);
                }
            });
            rightAgoraAdapter.setMembers(agoraMembers1);
            single_right_camera_list.setAdapter(rightAgoraAdapter);
        } else {
            single_right_camera_list.setVisibility(View.GONE);
        }
    }


    protected final void stripSurfaceView(SurfaceView view) {
        if (view == null) {
            return;
        }
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((FrameLayout) parent).removeView(view);
        }
    }
}
