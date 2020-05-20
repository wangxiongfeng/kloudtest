package com.kloudsync.techexcel5.search.view;

import com.kloudsync.techexcel5.info.Customer;

import java.util.List;

public interface VContactSearch {
    void showLoading();

    void showEmpty(String message);

    void showContacts(List<Customer> conversations, String keyword);
}
