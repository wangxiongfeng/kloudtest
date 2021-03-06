package com.kloudsync.techexcel5.response;

import com.kloudsync.techexcel5.search.ui.TeamAndDocSearchActivity;


public class TeamAndSpaceSearchResponse {
    int RetCode;
    String ErrorMessage;
    String DetailMessage;
    TeamAndDocSearchActivity.DatasForSearch RetData;

    public int getRetCode() {
        return RetCode;
    }

    public void setRetCode(int retCode) {
        RetCode = retCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getDetailMessage() {
        return DetailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        DetailMessage = detailMessage;
    }

    public TeamAndDocSearchActivity.DatasForSearch getRetData() {
        return RetData;
    }

    public void setRetData(TeamAndDocSearchActivity.DatasForSearch retData) {
        RetData = retData;
    }
}
