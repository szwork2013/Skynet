package com.okar.icz.dao;

import android.content.Context;
import com.okar.icz.po.Merchant;
import java.sql.SQLException;
import java.util.List;

public class MerchantDAO extends BaseDAO<Merchant> {


    public MerchantDAO(Context context) {
        super(context);
    }

    @Override
    public void initDao() throws SQLException {
        dao = getHelper().getDao(Merchant.class);
    }

    public int add(Merchant t) {
        try {
            return dao.create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新
     */
    public void edit(Merchant t) {
        try {
            dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int remove(Merchant t) {
        try {
            return dao.delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Merchant> listAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}



