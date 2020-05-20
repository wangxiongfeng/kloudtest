package com.kloudsync.techexcel5.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kloudsync.techexcel5.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MyAccountActivity extends SwipeBackActivity {

    private TextView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        findView();
        initView();
    }

    private void findView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
    }

    private void initView() {
        tv_back.setOnClickListener(new myOnClick());
    }

    protected class myOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_back:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }


}
