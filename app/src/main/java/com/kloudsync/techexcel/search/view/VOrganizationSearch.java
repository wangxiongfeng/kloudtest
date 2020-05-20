package com.kloudsync.techexcel5.search.view;

import com.kloudsync.techexcel5.info.School;

import java.util.List;

public interface VOrganizationSearch {
    void showLoading();

    void showEmpty(String message);

    void showOrganizations(List<School> schools, String keyword);
}
