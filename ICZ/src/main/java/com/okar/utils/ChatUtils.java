package com.okar.utils;

import com.works.skynet.common.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by wangfengchen on 15/1/23.
 */
public class ChatUtils {

    private static ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public static byte[] getMsgBytes(String msg) {

        try {
            byte[] len = Utils.intToBytes2(msg.getBytes("UTF-8").length);
            baos.reset();
            baos.write(len, 0, len.length);
            baos.write(msg.getBytes("UTF-8"), 0, msg.getBytes("UTF-8").length);
            return baos.toByteArray();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
