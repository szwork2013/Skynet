package com.okar.icz.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.okar.icz.android.R;
import com.okar.icz.base.ArrayRecyclerAdapter;
import com.okar.icz.base.BaseSwipeRecyclerFragmentList;
import com.okar.icz.common.uiimage.AnimateFirstDisplayListener;
import com.okar.icz.model.Account;
import com.okar.icz.model.ApplyMemberCardRecord;
import com.okar.icz.model.Member;
import com.okar.icz.model.MemberCar;
import com.okar.icz.model.PageResult;
import com.okar.icz.service.AccountService;
import com.okar.icz.utils.RemoteServiceFactory;
import com.okar.icz.view.InputDialogFragment;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/4/20.
 */
public class GiveCardFragmentList extends BaseSwipeRecyclerFragmentList
        implements SwipeRefreshLayout.OnRefreshListener, InputDialogFragment.OnInputDialogClickListener {

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @InjectView(R.id.give_card_list_swipe_ly)
    private SwipeRefreshLayout swipeRefreshLayout;


    @InjectView(R.id.give_card_list_swipe_rcv)
    private RecyclerView recyclerView;

    private ArrayRecyclerAdapter<ApplyMemberCardRecord> mRecyclerAdapter =
            new ArrayRecyclerAdapter<ApplyMemberCardRecord>() {

        @Override
        public RecyclerView.ViewHolder create(ViewGroup viewGroup, int i) {
            View v = inflater.inflate(R.layout.item_give_card, viewGroup, false);
            return new MyViewHolder(v);
        }

        @Override
        public void bind(RecyclerView.ViewHolder viewHolder, int i) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.setView(getItem(i));
        }
    };

    @Override
    public ArrayRecyclerAdapter getArrayRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentlist_give_card, container, false);
    }

    protected void init() {
        initLoadingMore(recyclerView);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.icz_green);
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadData() {
        super.loadData();
        new GiveCardTask().execute();
    }

    @Override
    public void onDialogDone(boolean cancelled, int type, List<String> messages, Object... params) {
        if (cancelled) {

            switch (type) {
                case 1:
                    showToast("发卡成功 " + messages.get(0));
                    break;
                case 2:
                    showToast(messages.get(0));
                    break;
            }
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView headIV;

        public TextView wxNameTV, mobileTV, nameTV, carNumberTV;

        Button tyBtn, jjBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            headIV = (ImageView) itemView.findViewById(R.id.item_give_card_head);
            wxNameTV = (TextView) itemView.findViewById(R.id.item_give_card_wxname);
            mobileTV = (TextView) itemView.findViewById(R.id.item_give_card_mobile);
            nameTV = (TextView) itemView.findViewById(R.id.item_give_card_name);
            carNumberTV = (TextView) itemView.findViewById(R.id.item_give_card_carnumber);
            tyBtn = (Button) itemView.findViewById(R.id.item_give_card_tongyi);
            jjBtn = (Button) itemView.findViewById(R.id.item_give_card_jujue);
        }

        public void setView(ApplyMemberCardRecord item) {
            final Member member = item.getMember();
            if (member != null) {
                ImageLoader.getInstance().displayImage(member.getHead(), headIV, animateFirstListener);
                wxNameTV.setText(member.getWxNickname());
                mobileTV.setText(member.getMobile());
                nameTV.setText(member.getNickname());
                List<MemberCar> memberCarList = member.getCars();
                if (memberCarList != null && !memberCarList.isEmpty()) {
                    carNumberTV.setText(memberCarList.get(0).getCarNumber());
                } else {
                    carNumberTV.setText("无");
                }

                tyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> labels = new ArrayList<String>();
                        labels.add("卡号");
                        InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance("发卡", labels);
                        inputDialogFragment.setOnInputDialogClickListener(GiveCardFragmentList.this);
                        inputDialogFragment.setType(1);
                        inputDialogFragment.show(getFragmentTransaction(), "tag");
                    }
                });
                jjBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> labels = new ArrayList<String>();
                        labels.add("拒绝理由");
                        InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance("拒绝", labels);
                        inputDialogFragment.setOnInputDialogClickListener(GiveCardFragmentList.this);
                        inputDialogFragment.setType(2);
                        inputDialogFragment.show(getFragmentTransaction(), "tag");
                    }
                });
            }
        }
    }

    class GiveCardTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                AccountService accountService = RemoteServiceFactory.createAccountService();
                return accountService.getApplyMemberRecords(new Account(146), getP(), 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Object o) {
            onLoadMoreComplete();
            if(o!=null) {
                PageResult<ApplyMemberCardRecord> result = (PageResult<ApplyMemberCardRecord>) o;
                System.out.println(result.getCount());
                setP(result.getNp());
                setPageSize(result.getpCount());
                result.getSize();
                for (ApplyMemberCardRecord item : result.getData()) {
                    mRecyclerAdapter.add(item);
                }
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }

    }

}
