package com.okar.icz.android;

import android.os.Bundle;
import android.os.Handler;
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

import com.okar.icz.base.IczBaseFragmentList;
import com.okar.icz.model.Account;
import com.okar.icz.model.ApplyMemberCardRecord;
import com.okar.icz.model.Member;
import com.okar.icz.model.MemberCar;
import com.okar.icz.po.Bean;
import com.okar.icz.service.AccountService;
import com.okar.icz.tasks.GiveCardListTask;
import com.okar.icz.utils.RemoteServiceFactory;
import com.okar.icz.view.InputDialogFragment;
import com.okar.icz.view.swipe.SwipeRefreshLayout;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/4/20.
 */
public class GiveCardFragmentList extends IczBaseFragmentList<ApplyMemberCardRecord>
        implements SwipeRefreshLayout.OnRefreshListener, InputDialogFragment.OnInputDialogClickListener {

    @InjectView(R.id.give_card_list_swipe_ly)
    private SwipeRefreshLayout swipeRefreshLayout;


    @InjectView(R.id.give_card_list_swipe_rcv)
    private RecyclerView recyclerView;

    Handler handler = new Handler() {

    };

    private GiveCardListTask giveCardListTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragmentlist_give_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        loadData(p);
    }


    void init() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.icz_green);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
       loadData(p);
    }

    @Override
    public void loadData(int p) {
            giveCardListTask = new GiveCardListTask(this);
            giveCardListTask.execute(1, 146, p);
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.item_give_card, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onDialogDone(boolean cancelled, List<String> messages) {
        if(cancelled) {
            showToast(messages.get(0));
        }
    }

    class MyViewHolder extends ViewHolder {

        public ImageView headIV;

        public TextView wxNameTV,nameTV,carNumberTV;

        Button tyBtn,jjBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            headIV = (ImageView) itemView.findViewById(R.id.item_give_card_head);
            wxNameTV = (TextView) itemView.findViewById(R.id.item_give_card_wxname);
            nameTV = (TextView) itemView.findViewById(R.id.item_give_card_name);
            carNumberTV = (TextView) itemView.findViewById(R.id.item_give_card_carnumber);
            tyBtn = (Button) itemView.findViewById(R.id.item_give_card_tongyi);
            jjBtn = (Button) itemView.findViewById(R.id.item_give_card_jujue);
        }

        @Override
        public void setView(ApplyMemberCardRecord item) {
            final Member member = item.getMember();
            if(member!=null) {
                il.displayImage(member.getHead(), headIV);
                wxNameTV.setText(member.getWxNickname());
                nameTV.setText(member.getNickname());
                List<MemberCar> memberCarList = member.getCars();
                if(memberCarList!=null&&!memberCarList.isEmpty()) {
                    carNumberTV.setText(memberCarList.get(0).getCarNumber());
                }else {
                    carNumberTV.setText("无");
                }

                tyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> labels = new ArrayList<String>();
                        labels.add("卡号");
                        InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance("提示", labels);
                        inputDialogFragment.setOnInputDialogClickListener(GiveCardFragmentList.this);
                        inputDialogFragment.show( getFragmentTransaction(),"tag");
                    }
                });
                jjBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }
}
