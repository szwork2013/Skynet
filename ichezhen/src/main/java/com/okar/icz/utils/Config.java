package com.okar.icz.utils;

/**
 * Created by wangfengchen on 15/5/21.
 */
public class Config {

    public static class URI {
        public static final String SERVICE_URL = "http://mp.ichezhen.com";

        public static final String MERCHANT_LIST_URL = SERVICE_URL+"/merchant/indexMore.htm";
        public static final String MERCHANT_SORT_URL = SERVICE_URL+"/merchant/merchantSort.htm";
        public static final String EVENT_LIST_URL = SERVICE_URL+"/dynamic/eventList1.htm";

        public static final String SET_COOKIE = SERVICE_URL+ "/test/addSession.htm";
    }
}
