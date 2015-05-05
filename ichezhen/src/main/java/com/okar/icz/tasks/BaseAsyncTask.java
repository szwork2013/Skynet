package com.okar.icz.tasks;

import android.os.AsyncTask;
import com.okar.icz.base.IczBaseFragmentList;
import com.okar.icz.model.PageResult;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import java.util.List;

/**
 * Created by wangfengchen on 15/4/29.
 */
public abstract class BaseAsyncTask extends AsyncTask<Object, Object, Object> {

    protected BaseAsyncTask(TaskExecute taskExecute) {
        this.taskExecute = taskExecute;
    }

    private TaskExecute taskExecute;

    public void setTaskExecute(TaskExecute taskExecute) {
        this.taskExecute = taskExecute;
    }

    @Override
    protected void onPreExecute() {
        taskExecute.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        taskExecute.onPostExecute(o);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
        taskExecute.onCancelled(o);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        taskExecute.onProgressUpdate(values);
    }

    public interface TaskExecute {

        void onPreExecute();

        void onPostExecute(Object o);

        void onCancelled(Object o);

        void onProgressUpdate(Object... values);

    }
}
