package com.kloudsync.techexcel5.bean.params;

import com.kloudsync.techexcel5.bean.InviteInfo;

import java.util.List;

public class InviteMultipleParams {
    private List<InviteInfo> inviteInfos;

    public List<InviteInfo> getInviteInfos() {
        return inviteInfos;
    }

    public void setInviteInfos(List<InviteInfo> inviteInfos) {
        this.inviteInfos = inviteInfos;
    }
}
