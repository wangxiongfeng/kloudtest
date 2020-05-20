package com.kloudsync.techexcel5.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kloudsync.techexcel5.R;
import com.kloudsync.techexcel5.bean.EventSelfJoinCoachAudience;
import com.ub.techexcel.bean.AgoraMember;
import com.ub.techexcel.tools.ServiceInterfaceTools;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BeInvitedCoachAudienceDialog implements OnClickListener {

    public Context mContext;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                mPopupWindow.dismiss();
                break;
            case R.id.ok:
                processAcceptInvite();
                mPopupWindow.dismiss();
                break;

            default:
                break;
        }
    }

    private void processAcceptInvite() {
        Observable.just("accept_invite").observeOn(Schedulers.io()).map(new Function<String, JSONObject>() {
            @Override
            public JSONObject apply(final String str) throws Exception {
                JSONObject params = new JSONObject();
                return ServiceInterfaceTools.getinstance().syncRequstAudienceAcceptInvitation(params);
            }
        }).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<JSONObject>() {
            @Override
            public void accept(JSONObject jsonObject) throws Exception {
                if (jsonObject != null && jsonObject.has("code")) {
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        EventBus.getDefault().post(new EventSelfJoinCoachAudience());
                    } else {

                    }
                }
            }
        }).subscribe();
    }





    public BeInvitedCoachAudienceDialog(Context context) {
        this.mContext = context;
        getPopupWindowInstance();
        mPopupWindow.getWindow().setWindowAnimations(R.style.PopupAnimation3);
    }

    public Dialog mPopupWindow;

    public void getPopupWindowInstance() {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    private TextView cancel, ok;
    private View popupWindow;

    @SuppressWarnings("deprecation")
    public void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        popupWindow = layoutInflater.inflate(R.layout.dialog_be_invited_coach_audience, null);
        cancel = (TextView) popupWindow.findViewById(R.id.cancel);
        ok = (TextView) popupWindow.findViewById(R.id.ok);
        mPopupWindow = new Dialog(mContext, R.style.my_dialog);
        mPopupWindow.setContentView(popupWindow);
        WindowManager.LayoutParams lp = mPopupWindow.getWindow().getAttributes();
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.8f);
        mPopupWindow.getWindow().setAttributes(lp);
        mPopupWindow.setCancelable(false);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

    }

    public void show() {
        if (mPopupWindow != null) {
            mPopupWindow.show();
        }
    }

    public void cancel(){
        if(mPopupWindow != null){
            mPopupWindow.cancel();
            mPopupWindow = null;
        }
    }


}
