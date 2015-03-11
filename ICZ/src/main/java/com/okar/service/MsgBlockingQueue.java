package com.okar.service;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.okar.android.IChatService;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wangfengchen on 15/1/14.
 */
public class MsgBlockingQueue {

    private final Logger log = LoggerFactory.getLogger(MsgBlockingQueue.class);

    BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<String>();

    public void put(String msg) throws InterruptedException{
        if(ChatService.hasNetwork()) {
            log.debug("放入数据");
            blockingQueue.put(msg);
        }
    }

    public String take() throws InterruptedException{
        log.debug("取出数据");
        return blockingQueue.take();
    }
}
