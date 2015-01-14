package com.okar.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wangfengchen on 15/1/14.
 */
public class MsgBlockingQueue {

    BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>();

    public void put(String msg) throws InterruptedException{
        blockingQueue.put(msg);

    }

    public String take() throws InterruptedException{
        return blockingQueue.take();
    }
}
