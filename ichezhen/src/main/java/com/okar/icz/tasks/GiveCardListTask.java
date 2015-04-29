package com.okar.icz.tasks;

import android.os.AsyncTask;

import com.caucho.hessian.client.HessianProxyFactory;
import com.okar.icz.model.Account;
import com.okar.icz.model.ApplyMemberCardRecord;
import com.okar.icz.model.PageResult;
import com.okar.icz.service.AccountService;

import java.net.MalformedURLException;
import java.util.List;


/**
 * Created by wangfengchen on 15/4/28.
 */
public class GiveCardListTask extends AsyncTask{
    @Override
    protected Object doInBackground(Object[] params) {
        HessianProxyFactory factory = new HessianProxyFactory();
        try {
            // 不设置会报 expected hessian reply at 0x48
            factory.setHessian2Reply( false);
            AccountService accountService = factory.create(AccountService.class, "http://192.168.1.3:9080/accountservice");
            PageResult result = accountService.getApplyMemberRecords(new Account(146), 0, 10);
            for(ApplyMemberCardRecord s : (List<ApplyMemberCardRecord>)result.getData()) {
                System.out.println(s);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
