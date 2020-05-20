package com.kloudsync.techexcel5.view.spinner;

import android.text.Spannable;
import android.text.SpannableString;

import com.kloudsync.techexcel5.bean.Team;
import com.kloudsync.techexcel5.bean.UserNotes;
import com.kloudsync.techexcel5.dialog.plugin.UserNotesDialog;

public class UserNoteTextFormatter implements SpinnerTextFormatter<UserNotes> {

    @Override
    public Spannable format(UserNotes user) {
        return new SpannableString(user.getUserName()  +"  (" + (user.getNoteCount()) + ")");
    }
}
