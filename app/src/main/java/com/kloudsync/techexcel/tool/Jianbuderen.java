package com.kloudsync.techexcel5.tool;

import com.kloudsync.techexcel5.config.AppConfig;
import com.kloudsync.techexcel5.info.Customer;
import com.kloudsync.techexcel5.service.UploadService;
import com.kloudsync.techexcel5.ui.ReceiveWeChatDataActivity;

import java.util.Collections;
import java.util.List;

public class Jianbuderen {

    /**
     * 消灭隐形activity
     */
    public static void Heihei(){
        AppConfig.OUTSIDE_PATH = "";
        if (ReceiveWeChatDataActivity.instance != null && !ReceiveWeChatDataActivity.instance.isFinishing()) {
            ReceiveWeChatDataActivity.instance.finish();
        }
        if(UploadService.instance != null) {
            UploadService.instance.stopSelf();
        }
    }

    /**
     * Customer排序
     * @param mlist
     */
    public static void SortCustomers(List<Customer> mlist) {
        Collections.sort(mlist, new PinyinComparator());

    }

}
