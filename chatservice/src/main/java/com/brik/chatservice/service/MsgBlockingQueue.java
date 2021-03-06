package com.brik.chatservice.service;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
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
            log.info("放入数据");
            blockingQueue.put(msg);
        }
    }

    public String take() throws InterruptedException{
        log.info("取出数据");
        return blockingQueue.take();
    }
}
