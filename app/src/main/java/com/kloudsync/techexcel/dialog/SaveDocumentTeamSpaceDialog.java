package com.kloudsync.techexcel5.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.kloudsync.techexcel5.R;
import com.kloudsync.techexcel5.adapter.HeaderRecyclerAdapter;
import com.kloudsync.techexcel5.bean.Team;
import com.kloudsync.techexcel5.config.AppConfig;
import com.kloudsync.techexcel5.response.TeamsResponse;
import com.ub.kloudsync.activity.TeamSpaceBean;
import com.ub.kloudsync.activity.TeamSpaceInterfaceListener;
import com.ub.kloudsync.activity.TeamSpaceInterfaceTools;
import com.ub.techexcel.tools.ServiceInterfaceTools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SaveDocumentTeamSpaceDialog implements View.OnClickListener, HeaderRecyclerAdapter.OnItemClickListener {
	public Context mContext;
	public int width;
	public int heigth;
	public Dialog dialog;
	private View view;
	private ViewFlipper viewFlipper;
	private View selectTeamView;
	private ImageView closeImage;
	private RecyclerView teamList;
	private SingleTeamAdapter adapter;
	private View selectSpaceView;
	private LinearLayout selectTeamLayout;
	private LinearLayout selectSpaceLayout;
	private LinearLayout selectDocumentLayout;
	private TextView teamNameText;
	private RecyclerView spaceList;
	private SpaceAdapter spaceAdapter;
	private ImageView spaceCloseImage;
	private List<Team> mTeamList;

	public interface OnDocSelectedListener {
		void onDocSelected(String docId);
	}

	OnDocSelectedListener onDocSelectedListener;


	public void setOnSpaceSelectedListener(OnDocSelectedListener onDocSelectedListener) {
		this.onDocSelectedListener = onDocSelectedListener;
	}

	public void getPopwindow(Context context) {
		this.mContext = context;

	}

	public SaveDocumentTeamSpaceDialog(Context context) {
		mContext = context;
		initDialog();
		getTeamList();
	}

	public void initDialog() {
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		view = layoutInflater.inflate(R.layout.dialog_add_file_from_document, null);
		dialog = new Dialog(mContext, R.style.my_dialog);
		selectTeamView = layoutInflater.inflate(R.layout.layout_save_select_team, null);
		selectSpaceView = layoutInflater.inflate(R.layout.layout_save_select_space, null);
		viewFlipper = view.findViewById(R.id.view_flipper);
		teamList = selectTeamView.findViewById(R.id.list_team);
		teamList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
		spaceList = selectSpaceView.findViewById(R.id.list_space);
		spaceList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
		adapter = new SingleTeamAdapter();
		spaceAdapter = new SpaceAdapter();
		adapter.setOnItemClickListener(this);
		spaceAdapter.setOnItemClickListener(this);
		teamList.setAdapter(adapter);
		spaceList.setAdapter(spaceAdapter);
		spaceCloseImage = selectSpaceView.findViewById(R.id.img_close);
		spaceCloseImage = selectSpaceView.findViewById(R.id.img_close);
		spaceCloseImage.setOnClickListener(this);
		teamNameText = selectSpaceView.findViewById(R.id.txt_team_name);
		closeImage = selectTeamView.findViewById(R.id.img_close);
		selectTeamLayout = selectSpaceView.findViewById(R.id.layout_select_team);
		selectSpaceLayout = selectSpaceView.findViewById(R.id.layout_select_team);
		TextView enter = selectSpaceView.findViewById(R.id.save);
		TextView cancel = selectSpaceView.findViewById(R.id.cancel);
		enter.setOnClickListener(this);
		cancel.setOnClickListener(this);
		selectTeamLayout.setOnClickListener(this);
		closeImage.setOnClickListener(this);
		viewFlipper.addView(selectTeamView);
		viewFlipper.addView(selectSpaceView);
		width = (int) (this.mContext.getResources().getDisplayMetrics().widthPixels * (0.85f));
		heigth = (int) (this.mContext.getResources().getDisplayMetrics().heightPixels * (0.88f));
		viewFlipper = view.findViewById(R.id.view_flipper);
		dialog.setContentView(view);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = width;
		lp.height = heigth;
		dialog.getWindow().setAttributes(lp);

	}


	public boolean isShowing() {
		return dialog.isShowing();
	}

	public void dismiss() {
		if (dialog != null) {
			dialog.cancel();
			dialog = null;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.img_close:
			case R.id.cancel:
				dismiss();
				break;
			case R.id.layout_select_team:
				if (viewFlipper != null) {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
							R.anim.flipper_right_in));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
							R.anim.flipper_right_out));
					viewFlipper.showPrevious();
				}
				break;
			case R.id.save:
				Toast.makeText(mContext, mContext.getResources().getString(R.string.no_interface), Toast.LENGTH_SHORT).show();
				break;

		}
	}

	public void show() {
		if (dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}

	public void getTeamList() {
		ServiceInterfaceTools.getinstance().getCompanyTeams(AppConfig.SchoolID + "").enqueue(new Callback<TeamsResponse>() {
			@Override
			public void onResponse(Call<TeamsResponse> call, Response<TeamsResponse> response) {
				SharedPreferences sharedPreferences = mContext.getSharedPreferences(AppConfig.LOGININFO,
						Context.MODE_PRIVATE);
				if (response != null && response.isSuccessful()) {
					List<Team> list = response.body().getRetData();
					if (list == null) {
						list = new ArrayList<>();
					}
					mTeamList = list;
					adapter.setDatas(list);
				}
			}

			@Override
			public void onFailure(Call<TeamsResponse> call, Throwable t) {

			}
		});

	}

	Team currentTeam;
	TeamSpaceBean currentSpace;

	@Override
	public void onItemClick(int position, Object data) {
		if (data instanceof Team) {
			Team team = (Team) data;
			for (int i = 0; i < mTeamList.size(); i++) {
				if (i == position) {
					team.setSelected(true);
				} else {
					mTeamList.get(i).setSelected(false);
				}
			}
			currentTeam = team;
			if (viewFlipper != null) {
				teamNameText.setText(team.getName());
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.flipper_left_in));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.flipper_left_out));
				viewFlipper.showNext();
				spaceAdapter.clear();
				getSpaceList(team.getItemID() + "");
				adapter.notifyDataSetChanged();
			}
		} else if (data instanceof TeamSpaceBean) {
			TeamSpaceBean space = (TeamSpaceBean) data;
			currentSpace = space;
			spaceAdapter.setSelected(space);
		}

	}

	public class SingleTeamAdapter extends HeaderRecyclerAdapter<Team> {

		@Override
		public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
			return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_team_item, parent, false));
		}

		@Override
		public void onBind(RecyclerView.ViewHolder viewHolder, final int realPosition, Team data) {
			final Team item = mDatas.get(realPosition);
			ItemHolder holder = (ItemHolder) viewHolder;
			holder.teamNameText.setText(item.getName());
			holder.itemLayout.setSelected(item.isSelected());
		}

		class ItemHolder extends RecyclerView.ViewHolder {
			TextView teamNameText;
			LinearLayout itemLayout;

			public ItemHolder(View itemView) {
				super(itemView);
				teamNameText = itemView.findViewById(R.id.txt_team_name);
				itemLayout = itemView.findViewById(R.id.lin_favour);

			}
		}
	}

	public class SpaceAdapter extends HeaderRecyclerAdapter<TeamSpaceBean> {

		@Override
		public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
			return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_space_item, parent, false));
		}

		@Override
		public void onBind(RecyclerView.ViewHolder viewHolder, final int realPosition, TeamSpaceBean data) {
			final TeamSpaceBean item = mDatas.get(realPosition);
			ItemHolder holder = (ItemHolder) viewHolder;
			holder.spaceNameText.setText(item.getName());
           /* if (item.isSelect()) {
                holder.selectedImage.setVisibility(View.VISIBLE);
            } else {
                holder.selectedImage.setVisibility(View.INVISIBLE);
            }*/
			holder.mLlySelectSpace.setSelected(item.isSelect());
			holder.mTvItemIcon.setSelected(item.isSelect());
		}

		class ItemHolder extends RecyclerView.ViewHolder {
			TextView spaceNameText;
			private final LinearLayout mLlySelectSpace;
			private final TextView mTvItemIcon;
			//            ImageView selectedImage;

			public ItemHolder(View itemView) {
				super(itemView);
				mLlySelectSpace = itemView.findViewById(R.id.lly_item_select_space);
				mTvItemIcon = itemView.findViewById(R.id.icon);
				spaceNameText = itemView.findViewById(R.id.txt_space_name);
//                selectedImage = itemView.findViewById(R.id.image_selected);
			}
		}

		public void clear() {
			mDatas.clear();
			notifyDataSetChanged();
		}

		public void clearSelected() {
			for (TeamSpaceBean space : mDatas) {
				space.setSelect(false);
			}
		}

		public void setSelected(TeamSpaceBean space) {
			clearSelected();
			space.setSelect(true);
			notifyDataSetChanged();
		}


	}


	private void getSpaceList(String teamId) {
		String url = AppConfig.URL_PUBLIC + "TeamSpace/List?companyID=" + AppConfig.SchoolID + "&type=2&parentID=" + teamId;
		TeamSpaceInterfaceTools.getinstance().getTeamSpaceList(url,
				TeamSpaceInterfaceTools.GETTEAMSPACELIST, new TeamSpaceInterfaceListener() {
					@Override
					public void getServiceReturnData(Object object) {
						List<TeamSpaceBean> list = (List<TeamSpaceBean>) object;
						if (list == null) {
							list = new ArrayList<>();
						}
						spaceAdapter.setDatas(list);
					}
				});
	}


}
