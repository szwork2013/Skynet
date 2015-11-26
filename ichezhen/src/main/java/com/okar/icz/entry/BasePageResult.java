package com.okar.icz.entry;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class BasePageResult<T> {
    @Expose
    int p;
    @Expose
    int size;
    @Expose
    int np;
    @Expose
    int pp;
    @Expose
    int count;
    @Expose
    int pCount;

    @Expose
    List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNp() {
        return np;
    }

    public void setNp(int np) {
        this.np = np;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getpCount() {
        return pCount;
    }

    public void setpCount(int pCount) {
        this.pCount = pCount;
    }
}
