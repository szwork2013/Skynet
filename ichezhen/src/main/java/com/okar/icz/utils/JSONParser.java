package com.okar.icz.utils;

import com.google.gson.JsonParser;
import com.okar.icz.po.Merchant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangfengchen on 15/5/26.
 */
public class JSONParser {

    public static Merchant parseMerchant(JSONObject jsonObject) {
        Merchant merchant = null;
        try {
            merchant = new Merchant();
            merchant.name = jsonObject.optString("name");
            merchant.cover = jsonObject.optString("cover");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return merchant;
    }

}
