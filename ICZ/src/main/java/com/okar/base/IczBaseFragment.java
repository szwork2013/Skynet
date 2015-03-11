package com.okar.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.okar.dao.DatabaseHelper;
import com.okar.utils.IczLoadDataInfOps;
import com.works.skynet.base.BaseActivity;
import com.works.skynet.base.BaseFragment;

/**
 * Created by wangfengchen on 14/10/31.
 */
public abstract class IczBaseFragment extends BaseFragment implements IczLoadDataInfOps {

    public int p;

//    public DatabaseHelper databaseHelper;
//
//    public void initDatabaseHelper(){
//        databaseHelper = new DatabaseHelper(baseActivity);
//    }



    public abstract void init(View view);

    public void onServiceConnected() {}

}
