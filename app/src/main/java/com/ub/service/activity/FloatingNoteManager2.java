package com.ub.service.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kloudsync.techexcel5.R;
import com.kloudsync.techexcel5.bean.MeetingConfig;
import com.kloudsync.techexcel5.config.AppConfig;
import com.kloudsync.techexcel5.dialog.RecordNoteActionManager;
import com.ub.techexcel.bean.Note;
import com.ub.techexcel.tools.FileUtils;
import com.ub.techexcel.tools.MeetingServiceTools;
import com.ub.techexcel.tools.ServiceInterfaceListener;
import com.ub.techexcel.tools.ServiceInterfaceTools;
import com.ub.techexcel.tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static io.rong.imkit.utilities.RongUtils.screenWidth;

/**
 * Created by wang on 2017/6/19.
 */

public class FloatingNoteManager2 implements View.OnClickListener ,OnTouchListener{
    /**
     * 定义浮动窗口布局
     */
    View mView;
    /**
     * 悬浮窗控件
     */
    private MeetingConfig meetingConfig;
    private ImageView backImage;
    private WebView floatwebview;
    private TextView title;
    private ImageView changefloatingnote;
    LayoutInflater inflater;

    public static FloatingNoteManager2 instance;
    private Context mContext;


    public interface  FloatingChangeListener{
        void changeHomePage(int noteId);
    }

    private FloatingChangeListener floatingChangeListener;

    public void  setFloatingChangeListener(FloatingChangeListener floatingChangeListener){
        this.floatingChangeListener=floatingChangeListener;
    }

    public static FloatingNoteManager2 getManager(Context context,View view) {
        if (instance == null) {
            synchronized (FloatingNoteManager2.class) {
                if (instance == null) {
                    instance = new FloatingNoteManager2(context,view);
                }
            }
        }
        return instance;
    }


    public FloatingNoteManager2(Context context,View view) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        mView = view;
        initFloating();
    }



    private void initFloating() {
        backImage = mView.findViewById(R.id.back);
        backImage.setOnClickListener(this);
        floatwebview = mView.findViewById(R.id.xwalkview);
        title = mView.findViewById(R.id.title);
        title.setOnTouchListener(this);
        changefloatingnote = mView.findViewById(R.id.changefloatingnote);
        changefloatingnote.setOnClickListener(this);
        initWeb();
    }

    @SuppressLint("JavascriptInterface")
    private void initWeb() {
//        floatwebview.setZOrderOnTop(false);
        floatwebview.getSettings().setJavaScriptEnabled(true);
        floatwebview.getSettings().setDomStorageEnabled(true);
        floatwebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        floatwebview.addJavascriptInterface(new FloatNoteJavascriptInterface(), "AnalyticsWebInterface");
//        XWalkPreferences.setValue("enable-javascript", true);
//        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
//        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
//        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
        Log.e("floatingnote", "加载浮窗");
        String indexUrl = "file:///android_asset/index.html";
        floatwebview.loadUrl(indexUrl, null);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                closeFloating();
                break;
            case R.id.changefloatingnote:  //跳到主界面
                if(currentNote!=null){
                    RecordNoteActionManager.getManager(mContext).sendDisplayPopupHomepageActions(currentNote.getNoteID(),lastjsonObject);
                }
                if(floatingChangeListener!=null){
                    floatingChangeListener.changeHomePage(currentNote.getNoteID());
                }
                dismiss();
                break;
        }
    }

    private int startY;
    private int startX;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int moveY = (int) event.getRawY();
                int move_bigX = moveX - startX;
                int move_bigY = moveY - startY;
                Log.e("手指移动距离的大小", move_bigX + "    " + move_bigY);
                if (Math.abs(move_bigX) > 0 || Math.abs(move_bigY) > 0) {
                    //拿到当前控件未移动的坐标
                    int left = mView.getLeft();
                    int top = mView.getTop();
                    left += move_bigX;
                    top += move_bigY;
                    int right = left + mView.getWidth();
                    int bottom = top + mView.getHeight();
                    mView.layout(left, top, right, bottom);
                }
                startX = moveX;
                startY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = mView.getLeft();
                params.topMargin = mView.getTop();
                params.setMargins(mView.getLeft(), mView.getTop(), 0, 0);
                mView.setLayoutParams(params);
                break;
        }
        return true;
    }


    public void  closeFloating(){
        if(currentNote!=null){
            RecordNoteActionManager.getManager(mContext).sendClosePopupActons(currentNote.getNoteID());
        }
        dismiss();
    }


    public void show(final long noteid, final MeetingConfig meetingConfig) {
        this.meetingConfig = meetingConfig;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.setVisibility(View.VISIBLE);
                process(noteid, meetingConfig);
            }
        },100);
    }

    public boolean isShowing(){
        if(mView!=null){
            if(mView.getVisibility()==View.VISIBLE){
                return true;
            }
        }
        return false;

    }


    public void dismiss() {
        if (isShowing()) {
            mView.setVisibility(View.GONE);
        }
    }


    private Note currentNote;
    private JSONObject lastjsonObject=new JSONObject();


    private void process(final long noteId, final MeetingConfig meetingConfig) {
        if (meetingConfig.getDocument() == null) {
            return;
        }
        String url = AppConfig.URL_PUBLIC + "DocumentNote/Item?noteID=" + noteId;
        MeetingServiceTools.getInstance().getBlueToothNoteDetail(url, MeetingServiceTools.GETBLUETOOTHNOTEDETAIL, new ServiceInterfaceListener() {
            @Override
            public void getServiceReturnData(Object object) {
                currentNote= (Note) object;
                title.setText(currentNote.getTitle());
                String lastModifiedDate=currentNote.getLastModifiedDate();
                String localNoteBlankPage = FileUtils.getBaseDir() + "note" + File.separator + "blank_note_1.jpg";
                Log.e("floatingnote", localNoteBlankPage);
                floatwebview.loadUrl("javascript:ShowPDF('" + localNoteBlankPage + "'," +1 + ",''," + currentNote.getAttachmentID() + "," + true + ")", null);
                handleBluetoothNote(currentNote,lastModifiedDate);
            }
        });
    }


    /**
     *
     * @param noteId
     * @param noteData  编码后为  {"lines":[{"id":"D342A8CB-DB21-4990-BD62-987A4D0419CC","points":[[3004,5136,500,1583830777.256],[3014,5139,880,1583830777.2579999],
     */
    public void followDrawNewLine(long noteId,String noteData){
        if(currentNote.getNoteID()==noteId){
            if (floatwebview != null) {
                String key = "ShowDotPanData";
                try {
                    JSONObject _data = new JSONObject();
                    _data.put("LinesData", Tools.getFromBase64(noteData));
                    _data.put("ShowInCenter", true);
                    _data.put("TriggerEvent", true);
                    floatwebview.loadUrl("javascript:FromApp('" + key + "'," + _data + ")", null);
                    lastjsonObject=new JSONObject(Tools.getFromBase64(noteData));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleBluetoothNote(final Note note, final String lastModifiedDate) {
        final String url=note.getSourceFileUrl();
        //https://peertime.oss-cn-shanghai.aliyuncs.com/P49/Attachment/D80370/book_page_data.json?_=1583735802772
        Observable.just(url).observeOn(Schedulers.io()).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                String newUrl = "";
                int index = url.lastIndexOf("/");
                if (index > 0 && index < url.length() - 2) {
                    newUrl = url.substring(0, index + 1) + "book_page_data.json?="+lastModifiedDate;
                }
                return newUrl;
            }
        }).map(new Function<String, JSONObject>() {
            @Override
            public JSONObject apply(String url) throws Exception {
                JSONObject jsonObject = new JSONObject();
                if (!TextUtils.isEmpty(url)) {
                    jsonObject = ServiceInterfaceTools.getinstance().syncGetNotePageJson(url);
                    lastjsonObject=jsonObject.getJSONObject("PaintData");
                    Log.e("floatingnote", "url:" + url+"   "+jsonObject.toString());
                }
                return jsonObject;
            }
        }).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<JSONObject>() {
            @Override
            public void accept(JSONObject jsonObject) throws Exception {
                String key = "ShowDotPanData";
                JSONObject _data = new JSONObject();
                _data.put("LinesData", jsonObject);
                _data.put("ShowInCenter", false);
                _data.put("TriggerEvent", false);
                Log.e("floatingnote", "ShowDotPanData");
                floatwebview.loadUrl("javascript:FromApp('" + key + "'," + _data + ")", null);
                RecordNoteActionManager.getManager(mContext).sendDisplayPopupActions(note.getNoteID(),lastjsonObject);
            }
        }).subscribe();
    }




    public class FloatNoteJavascriptInterface {

        @JavascriptInterface
        public void afterLoadPageFunction() {
            Log.e("floatingnote", "afterLoadPageFunction");
        }


        @JavascriptInterface
        public void afterChangePageFunction(final int pageNum, int type) {
//            Log.e("JavascriptInterface", "note_afterChangePageFunction,pageNum:  " + pageNum + ", type:" + type);
//            NoteViewManager.getInstance().getNotePageActionsToShow(meetingConfig);
        }

        @JavascriptInterface
        public void reflect(String result) {
            Log.e("JavascriptInterface", "reflect,result:  " + result);

        }

    }


}
