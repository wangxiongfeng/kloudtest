package com.kloudsync.techexcel5.pc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kloudsync.techexcel5.R;

public class EvaluationManagementAdapter extends BaseAdapter {

	private List<String> list=new ArrayList<String>();
	private Context mContext;

	public EvaluationManagementAdapter(Context mContext, List<String> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		view = LayoutInflater.from(mContext).inflate(
				R.layout.pc_evaluation_managemen_item, null);
		return view;
	}

}
