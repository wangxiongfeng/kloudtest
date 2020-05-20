package com.kloudsync.techexcel5.tool;

import com.kloudsync.techexcel5.bean.AccountSettingAdminUserBean;
import com.kloudsync.techexcel5.bean.AccountSettingContactBean;

import java.util.Comparator;

public class AccountSettingUserPinyinComparator implements Comparator<AccountSettingAdminUserBean> {

    @Override
    public int compare(AccountSettingAdminUserBean lhs, AccountSettingAdminUserBean rhs) {

        String str1 = (String) lhs.getSortLetters();
        String str2 = (String) rhs.getSortLetters();

        if (!str1.matches("[A-Z]")) {
            return -1;
        } else if (!str2.matches("[A-Z]")) {
            return 1;
        }
        if (str1.equals(str2)) {
            return lhs.getUserName().compareTo(rhs.getUserName());
        } else {
            return str1.compareTo(str2);

        }
    }


}
