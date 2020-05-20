package com.kloudsync.techexcel5.help;

import android.text.format.DateFormat;

import com.kloudsync.techexcel5.bean.MeetingDocument;
import com.kloudsync.techexcel5.config.AppConfig;
import com.kloudsync.techexcel5.dialog.message.ShareMessage;
import com.kloudsync.techexcel5.info.MyFriend;
import com.kloudsync.techexcel5.tool.MessageTool;
import com.ub.kloudsync.activity.Document;
import com.ub.techexcel.bean.LineItem;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.model.Conversation;

public class ShareTool {
    public static void shareDocumentToFriend(String userUrl, LineItem document, MyFriend friend, IRongCallback.ISendMessageCallback callback) {
        ShareMessage sm = new ShareMessage();
        sm.setShareDocTitle(document.getFileName());
        sm.setAttachmentID(document.getAttachmentID() + "");
        String url = AppConfig.SHARE_ATTACHMENT + document.getAttachmentID();
        String thumurl = document.getSourceFileUrl();
        sm.setShareDocThumbnailUrl(thumurl);
        sm.setShareDocUrl(url);
        sm.setShareDocAvatarUrl("");
        String date = (String) DateFormat.format("yy.MM.dd", System.currentTimeMillis());
        sm.setShareDocTime("Sharee at " + date);
        sm.setShareDocUsername(AppConfig.UserName);
        MessageTool.sendMessage(sm, friend.getRongCloudUserID(), Conversation.ConversationType.PRIVATE, callback);
    }

    public static void shareDocumentToFriend(String userUrl, MeetingDocument document, MyFriend friend, IRongCallback.ISendMessageCallback callback) {
        ShareMessage sm = new ShareMessage();
        sm.setShareDocTitle(document.getFileName());
        sm.setAttachmentID(document.getAttachmentID() + "");
        String url = AppConfig.SHARE_ATTACHMENT + document.getAttachmentID();
        String thumurl = document.getSourceFileUrl();
        sm.setShareDocThumbnailUrl(thumurl);
        sm.setShareDocUrl(url);
        sm.setShareDocAvatarUrl("");
        String date = (String) DateFormat.format("yy.MM.dd", System.currentTimeMillis());
        sm.setShareDocTime("Sharee at " + date);
        sm.setShareDocUsername(AppConfig.UserName);
        MessageTool.sendMessage(sm, friend.getRongCloudUserID(), Conversation.ConversationType.PRIVATE, callback);
    }
}
