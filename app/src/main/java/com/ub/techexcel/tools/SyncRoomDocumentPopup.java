package com.ub.techexcel.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kloudsync.techexcel5.R;
import com.kloudsync.techexcel5.view.MyDialog;
import com.ub.techexcel.bean.LineItem;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class SyncRoomDocumentPopup implements View.OnClickListener, TextView.OnEditorActionListener, DialogInterface.OnDismissListener {

    public Context mContext;
    public int width;
	public MyDialog mPopupWindow;
    private View view;
	private TextView adddocument;
    private RecyclerView recycleview;
    private SyncRoomTeamAdapter syncRoomTeamAdapter;
	private PopupWindow mPpwAddDoc;
	private LinearLayout mLlyAddDoc;
	private TextView mEtFileName;
	private InputMethodManager mImm;
	List<LineItem> mSearchList = new ArrayList<>();
	private List<LineItem> mLineItemList;


    public interface DocumentPopupEventListener{
        void onDocumentPopOpen();
        void onDocumentPopClose();
    }

    private DocumentPopupEventListener documentPopupEventListener;

    public void setDocumentPopupEventListener(DocumentPopupEventListener documentPopupEventListener) {
        this.documentPopupEventListener = documentPopupEventListener;
    }

    public void getPopwindow(Context context) {
        this.mContext = context;
        width = mContext.getResources().getDisplayMetrics().widthPixels;
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

    LinearLayout morell;

    public void initPopuptWindow() {
	    mImm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.syncroom_document_popup, null);

        recycleview = (RecyclerView) view.findViewById(R.id.recycleview);
	    mEtFileName = view.findViewById(R.id.et_dialog_bottom_file_file_name);
	    mEtFileName.setOnEditorActionListener(this);
	    adddocument = view.findViewById(R.id.adddocument);
        adddocument.setOnClickListener(this);
        recycleview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        morell = (LinearLayout) view.findViewById(R.id.morell);

        RelativeLayout moreshare = (RelativeLayout) view.findViewById(R.id.moreshare);
        RelativeLayout moreedit = (RelativeLayout) view.findViewById(R.id.moreedit);
        RelativeLayout moredelete = (RelativeLayout) view.findViewById(R.id.moredelete);
        moreshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morell.setVisibility(View.GONE);

            }
        });
        moreedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morell.setVisibility(View.GONE);
                webCamPopupListener.edit(selectLineItem);

            }
        });
        moredelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morell.setVisibility(View.GONE);
                webCamPopupListener.delete(selectLineItem);

            }
        });
	    mPopupWindow = new MyDialog(mContext, R.style.my_dialog);
        mPopupWindow.setContentView(view);
        mPopupWindow.getWindow().setGravity(Gravity.RIGHT);
        WindowManager.LayoutParams params = mPopupWindow.getWindow().getAttributes();
	    params.width = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_187);
	    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        mPopupWindow.getWindow().setAttributes(params);
        mPopupWindow.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(documentPopupEventListener != null){
                    documentPopupEventListener.onDocumentPopClose();
                }
            }
        });

        mPopupWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(documentPopupEventListener != null){
                    documentPopupEventListener.onDocumentPopClose();
                }
            }
        });
        mPopupWindow.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPopupWindow.getWindow().setWindowAnimations(R.style.anination3);


    }


    @SuppressLint("NewApi")
    public void StartPop(View v, List<LineItem> list) {
        if (mPopupWindow != null) {
            webCamPopupListener.open();
            if(documentPopupEventListener != null){
                documentPopupEventListener.onDocumentPopOpen();
            }
            mPopupWindow.show();
	        mLineItemList = list;
            syncRoomTeamAdapter = new SyncRoomTeamAdapter(mContext, list);
            recycleview.setAdapter(syncRoomTeamAdapter);

        }
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }


    public interface WebCamPopupListener {

        void changeOptions(LineItem syncRoomBean, int position);

        void teamDocument();

        void takePhoto();

        void importFromLibrary();

        void savedFile();

        void dismiss();

        void open();

        void delete(LineItem selectLineItem);

        void edit(LineItem selectLineItem);


    }

    public void setWebCamPopupListener(WebCamPopupListener webCamPopupListener) {
        this.webCamPopupListener = webCamPopupListener;
    }

    private WebCamPopupListener webCamPopupListener;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closebnt:
                mPopupWindow.dismiss();
                break;
            case R.id.adddocument:
                morell.setVisibility(View.GONE);
	            showAddDocPpw();
                break;
	        case R.id.syncroom_team_document:
		        addDocPpwDismiss();
		        webCamPopupListener.teamDocument();
		        break;
	        case R.id.syncroom_take_photo:
		        addDocPpwDismiss();
		        webCamPopupListener.takePhoto();
		        break;
	        case R.id.syncroom_file_library:
		        addDocPpwDismiss();
		        webCamPopupListener.importFromLibrary();
		        break;
	        case R.id.syncroom_save_file:
		        addDocPpwDismiss();
		        webCamPopupListener.savedFile();
		        break;
            default:
                break;
        }
    }

	private void showAddDocPpw() {
		if (mPpwAddDoc == null) {
			View addDocView = LayoutInflater.from(mContext).inflate(R.layout.ppw_syncroom_add_doc, null);
			mLlyAddDoc = addDocView.findViewById(R.id.lly_syncroom_ppw_add_doc);
			mLlyAddDoc.getBackground().setAlpha(204);
			LinearLayout team_file = addDocView.findViewById(R.id.syncroom_team_document);
			LinearLayout take_photo = addDocView.findViewById(R.id.syncroom_take_photo);
			LinearLayout file_library = addDocView.findViewById(R.id.syncroom_file_library);
			LinearLayout favorite_file = addDocView.findViewById(R.id.syncroom_save_file);

			take_photo.setOnClickListener(this);
			file_library.setOnClickListener(this);
			favorite_file.setOnClickListener(this);
			team_file.setOnClickListener(this);
			mPpwAddDoc = new PopupWindow(addDocView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			mPpwAddDoc.setBackgroundDrawable(new ColorDrawable());
			mPpwAddDoc.setOutsideTouchable(true);
			mPpwAddDoc.setFocusable(true);
		}
		int y = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_5);
		mLlyAddDoc.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		int width = mLlyAddDoc.getMeasuredWidth();
		int addIconWidth = adddocument.getWidth();
		mPpwAddDoc.showAsDropDown(adddocument, -width + addIconWidth, y);
	}

	private void addDocPpwDismiss() {
		if (mPpwAddDoc != null && mPpwAddDoc.isShowing()) {
			mPpwAddDoc.dismiss();
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		mEtFileName.setText("");
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH ||
				(event != null && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH && event.getAction() == KeyEvent.ACTION_DOWN)) {
			hideKeyBoard();
			String searchText = mEtFileName.getText().toString();
			if (TextUtils.isEmpty(searchText)) {
				syncRoomTeamAdapter.setSearchList(mLineItemList);
				syncRoomTeamAdapter.notifyDataSetChanged();
				return true;
			}
			mSearchList.clear();
			for (LineItem bean : mLineItemList) {
				if (bean.getFileName().contains(searchText)) {
					mSearchList.add(bean);
				}
			}
			syncRoomTeamAdapter.setSearchList(mSearchList);
			syncRoomTeamAdapter.notifyDataSetChanged();
			return true;
		}
		return false;
	}

	private void hideKeyBoard() {
		mImm.hideSoftInputFromWindow(mEtFileName.getWindowToken(), 0);
	}

    private LineItem selectLineItem = new LineItem();

    public class SyncRoomTeamAdapter extends RecyclerView.Adapter<SyncRoomTeamAdapter.RecycleHolder2> {

        private Context context;

        private List<LineItem> list = new ArrayList<>();

        private void setSelectedFile(LineItem file){
            for(LineItem fileItem : list){
                if(file.equals(fileItem)){
                    fileItem.setSelect(true);
                }else {
                    fileItem.setSelect(false);
                }
            }
            notifyDataSetChanged();
        }

        public SyncRoomTeamAdapter(Context context, List<LineItem> list) {
            this.context = context;
	        this.list.addAll(list);
        }

        @Override
        public RecycleHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.syncroom_document_popup_item, parent, false);
            RecycleHolder2 holder = new RecycleHolder2(view);
            return holder;
        }


        @Override
        public void onBindViewHolder(RecycleHolder2 holder, final int position) {
            final LineItem lineItem = list.get(position);
	        holder.name.setText(lineItem.getFileName());
	        holder.mTvCreateUserName.setText(null);
	        int syncCount = lineItem.getSyncRoomCount();
	        if (syncCount > 0) {
		        holder.mLlyYinXiangCount.setVisibility(View.VISIBLE);
		        holder.mTvYinXiangCount.setText(String.valueOf(syncCount));
	        } else {
		        holder.mLlyYinXiangCount.setVisibility(View.GONE);
		        holder.mTvYinXiangCount.setText("");
            }
	        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
	                morell.setVisibility(View.GONE);
                    setSelectedFile(lineItem);
                    webCamPopupListener.changeOptions(lineItem,position);
                }
            });
	        holder.mIvItemDocMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectLineItem = lineItem;
                    if (morell.getVisibility() == View.VISIBLE) {
                        morell.setVisibility(View.GONE);
                    } else {
                        morell.setVisibility(View.VISIBLE);
                    }
                }
            });
            String url = lineItem.getUrl();
            if (!TextUtils.isEmpty(url)) {
                url = url.substring(0, url.lastIndexOf("<")) + "1" + url.substring(url.lastIndexOf("."), url.length());
                Uri imageUri = null;
                if (!TextUtils.isEmpty(url)) {
                    imageUri = Uri.parse(url);
                }
	            holder.icon.setImageURI(imageUri);
            }
	        holder.headll.setSelected(lineItem.isSelect());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

	    public void setSearchList(List<LineItem> searchList) {
		    list.clear();
		    list.addAll(searchList);
	    }

        class RecycleHolder2 extends RecyclerView.ViewHolder {
	        SimpleDraweeView icon;
	        TextView name, mTvYinXiangCount, mTvCreateUserName;
	        LinearLayout headll, mLlyYinXiangCount;
	        ImageView mIvItemDocMore;

            public RecycleHolder2(View itemView) {
                super(itemView);
	            icon = itemView.findViewById(R.id.studenticon);
	            name = (TextView) itemView.findViewById(R.id.studentname);
	            headll = (LinearLayout) itemView.findViewById(R.id.headll);
	            mIvItemDocMore = itemView.findViewById(R.id.iv_item_doc_more);
	            mTvCreateUserName = itemView.findViewById(R.id.tv_item_create_user_name);
	            mLlyYinXiangCount = itemView.findViewById(R.id.lly_item_doc_yin_xiang_count);
	            mTvYinXiangCount = itemView.findViewById(R.id.tv_item_yin_xiang_count);
            }
        }
    }


}
