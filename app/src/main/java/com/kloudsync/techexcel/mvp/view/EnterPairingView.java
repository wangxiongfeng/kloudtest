package com.kloudsync.techexcel5.mvp.view;

import com.kloudsync.techexcel5.bean.EverPen;
import com.tqltech.tqlpencomm.BLEException;

public interface EnterPairingView extends KloudView {

	void addScanedEverPen(EverPen everPen);

	void onScanFailed(BLEException e);

	void setConnected(boolean isConnected);

	void setClick(boolean click);

	void connect();

	void setScanningBtnText(boolean enabled, int resId, float alpha);

	void removeNoConnected();

	void notifyDataSetChanged();

	void setPenName(boolean bIsSuccess);

}
