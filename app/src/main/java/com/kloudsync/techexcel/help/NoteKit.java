package com.kloudsync.techexcel5.help;

/**
 * Created by tonyan on 2019/12/3.
 */

public class NoteKit {

    private static NoteKit kit;

    public static NoteKit getInstance() {
        if (kit == null) {
            synchronized (NoteKit.class) {
                if (kit == null) {
                    kit = new NoteKit();
                }
            }
        }
        return kit;
    }
}
