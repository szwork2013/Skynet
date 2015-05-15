package com.okar.icz.android;

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

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.okar.icz.base.ArrayRecyclerAdapter;
import com.okar.icz.base.BaseSwipeRecyclerFragmentList;
import com.okar.icz.common.uiimage.AnimateFirstDisplayListener;
import com.okar.icz.model.ApplyMemberCardRecord;
import com.okar.icz.model.Member;
import com.okar.icz.model.MemberCar;
import com.okar.icz.model.PageResult;
import com.okar.icz.tasks.BaseAsyncTask;
import com.okar.icz.tasks.GiveCardListTask;
import com.okar.icz.view.InputDialogFragment;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/4/20.
 */
public class GiveCardFragmentList extends BaseSwipeRecyclerFragmentList
        implements SwipeRefreshLayout.OnRefreshListener, InputDialogFragment.OnInputDialogClickListener, BaseAsyncTask.TaskExecute {

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @InjectView(R.id.give_card_list_swipe_ly)
    private SwipeRefreshLayout swipeRefreshLayout;


    @InjectView(R.id.give_card_list_swipe_rcv)
    private RecyclerView recyclerView;

    private ArrayRecyclerAdapter<ApplyMemberCardRecord> mRecyclerAdapter =
            new ArrayRecyclerAdapter<ApplyMemberCardRecord>() {

        @Override
        public RecyclerView.ViewHolder create(ViewGroup viewGroup, int i) {
            View v = layoutInflater.inflate(R.layout.item_give_card, viewGroup, false);
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
    public RecyclerView getRecView() {
        return recyclerView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragmentlist_give_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        loadData();
    }


    void init() {
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
    public void onRefresh() {
        setP(0);
        loadData();
    }

    @Override
    public void loadData() {
        super.loadData();
        GiveCardListTask giveCardListTask = new GiveCardListTask(this);
        giveCardListTask.execute(146, getP());
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
                il.displayImage(member.getHead(), headIV, animateFirstListener);
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

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(Object o) {
        onRefreshComplete(swipeRefreshLayout);
        if(o!=null) {
            PageResult<ApplyMemberCardRecord> result = (PageResult<ApplyMemberCardRecord>) o;
            System.out.println(result.getCount());
            setP(result.getNp());
            setPageSize(result.getpCount());
            result.getSize();
            for (ApplyMemberCardRecord item : result.getData()) {
                System.out.println("adddddd");
                mRecyclerAdapter.addRecyclerItem(new ArrayRecyclerAdapter
                        .RecyclerItem<ApplyMemberCardRecord>(ArrayRecyclerAdapter.RecyclerItem.NORMAL, item));
            }
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelled(Object o) {

    }

    @Override
    public void onProgressUpdate(Object... values) {

    }
}
