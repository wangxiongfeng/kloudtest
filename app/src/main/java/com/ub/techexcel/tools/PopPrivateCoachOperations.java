package com.ub.techexcel.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kloudsync.techexcel5.R;
import com.kloudsync.techexcel5.bean.EventSelectSpeakerMode;
import com.kloudsync.techexcel5.bean.MeetingConfig;
import com.kloudsync.techexcel5.config.AppConfig;
import com.ub.techexcel.bean.AgoraMember;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PopPrivateCoachOperations implements View.OnClickListener {
    private Context mContext;
    private PopupWindow mPopupWindow;
    private View view;
    private RelativeLayout oneLayout, twoLayout, threeLayout;
    private MeetingConfig meetingConfig;
    private AgoraMember agoraMember;
    public interface OnCoachOperationsListener{
        void onInviteAudienceClicked(AgoraMember agoraMember);
    }

    private OnCoachOperationsListener onCoachOperationsListener;


    public void setOnCoachOperationsListener(OnCoachOperationsListener onCoachOperationsListener) {
        this.onCoachOperationsListener = onCoachOperationsListener;
    }

    public PopPrivateCoachOperations(Context context) {
        this.mContext = context;
        getPopupWindowInstance();
    }

    public void getPopupWindowInstance() {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    public void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.pop_private_coach_options, null);
        oneLayout = (RelativeLayout) view.findViewById(R.id.layout_one);
        twoLayout = (RelativeLayout) view.findViewById(R.id.layout_two);
        threeLayout = (RelativeLayout) view.findViewById(R.id.layout_three);

        oneLayout.setOnClickListener(this);
        twoLayout.setOnClickListener(this);
        threeLayout.setOnClickListener(this);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
            }

        });
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
    }

    int displayMode;

    @SuppressLint("NewApi")
    public void show(View v, MeetingConfig meetingConfig, AgoraMember agoraMember) {
        this.meetingConfig = meetingConfig;
        this.agoraMember = agoraMember;
        if(agoraMember.isCoachingStudent()){

        }else if(agoraMember.isCoachingAudience()){

        }else if(agoraMember.isCoachingTobeAudience()){

        }else{
            threeLayout.setVisibility(View.GONE);
        }
        mPopupWindow.showAsDropDown(v, 0, dp2px(mContext, 5));
    }

    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public boolean isShowing() {
        if (mPopupWindow == null) {
            return false;
        }
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mPopupWindow = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_one:
                if(onCoachOperationsListener != null){
                    onCoachOperationsListener.onInviteAudienceClicked(this.agoraMember);
                }
                dismiss();
                break;
            case R.id.layout_two:

                dismiss();
                break;

            case R.id.layout_hide:
                dismiss();
                break;
            case R.id.layout_three:

                dismiss();
                break;

            default:
                break;
        }
    }


}
