package com.kloudsync.techexcel5.help;

public class ApiTask {

    public ApiTask(Runnable task) {
        this.task = task;
    }

    Runnable task;

    public void start(ThreadManager threadManager) {
        if (task != null) {
            threadManager.execute(task);
        }
    }
}
