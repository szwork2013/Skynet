package com.okar.service.runnable;

import android.content.Context;

import com.okar.service.MsgParser;
import com.works.skynet.common.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executor;

/**
 * Created by wangfengchen on 15/1/13.
 */
public class ChatReceiveRunnable implements Runnable {

    private InputStream reader;

//    private Socket client;

    private MsgParser msgParser;

    public ChatReceiveRunnable(Context context, Socket client) {
        msgParser = new MsgParser(context);
//        this.client = client;
        try {
            reader = client.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("receive start ->");
        try {

            boolean isHead = true;
            byte[] buff = new byte[4];
            while (reader.read(buff) != -1) {
                if (isHead) {
                    int plen = Utils.bytesToInt2(buff, 0);
                    System.out.println("receive len -> " + plen);
                    buff = new byte[plen];
                    isHead = false;
                } else {
                    msgParser.parseJson(new String(buff));
                    isHead = true;
                    buff = new byte[4];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
