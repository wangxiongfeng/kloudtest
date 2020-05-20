package com.kloudsync.techexcel5.mvp.presenter;

import com.kloudsync.techexcel5.mvp.view.MyNoteView;
import com.tqltech.tqlpencomm.Dot;

public class MyNotePresenter extends TQLPenSignalKloudPresenter<MyNoteView> {

	@Override
	public void onReceiveDot(Dot dot) {
		if (getView() != null) {
			getView().onReceiveDot(dot);
		}
	}

	@Override
	public void onReceiveOfflineStrokes(Dot dot) {
		if (getView() != null) {
			getView().onReceiveOfflineStrokes(dot);
		}
	}
}
