package com.kloudsync.techexcel5.dialog;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.kloudsync.techexcel5.R;

public class SubConversationListActivtiy extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);

		if (RongIM.getInstance() != null)
			RongIM.getInstance().startSubConversationList(this,
					Conversation.ConversationType.GROUP);
	}
}
