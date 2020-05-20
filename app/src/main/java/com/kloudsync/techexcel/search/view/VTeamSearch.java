package com.kloudsync.techexcel5.search.view;

import com.kloudsync.techexcel5.bean.Team;

import java.util.List;

public interface VTeamSearch {
    void showLoading();

    void showEmpty(String message);

    void showTeams(List<Team> team, String keyword);
}
