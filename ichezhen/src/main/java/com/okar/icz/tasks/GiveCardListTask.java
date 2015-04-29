package com.okar.icz.tasks;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.caucho.hessian.client.HessianProxyFactory;
import com.okar.icz.android.GiveCardFragmentList;
import com.okar.icz.base.IczBaseFragmentList;
import com.okar.icz.model.Account;
import com.okar.icz.model.ApplyMemberCardRecord;
import com.okar.icz.model.PageResult;
import com.okar.icz.service.AccountService;
import com.okar.icz.utils.RemoteServiceFactory;

import java.net.MalformedURLException;
import java.util.List;


/**
 * Created by wangfengchen on 15/4/28.
 */
public class GiveCardListTask extends BaseAsyncTask<ApplyMemberCardRecord> {

    public GiveCardListTask(IczBaseFragmentList<ApplyMemberCardRecord> fragmentList) {
        super(fragmentList);
    }

    @Override
    protected PageResult<ApplyMemberCardRecord> doInBackground(Object... params) {
        refresh = (Integer) params[0];
        Integer accountId = (Integer) params[1];
        Integer p = (Integer) params[2];
        System.out.println("accountId "+accountId);
        System.out.println(p);
        AccountService accountService;
        try {
            accountService = RemoteServiceFactory.getAccountService();
            return accountService.getApplyMemberRecords(new Account(accountId), p, 10);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(PageResult<ApplyMemberCardRecord> pageResult) {
        super.onPostExecute(pageResult);
    }
}
