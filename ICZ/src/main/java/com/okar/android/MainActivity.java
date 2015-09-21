package com.okar.chatservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.okar.view.JazzyViewPager;
import com.okar.view.OutlineContainer;
import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;

public class MainActivity extends BaseActivity {
	@InjectView(R.id.jazzyPager)
	private JazzyViewPager jazzyPager;
	List<Map<String, View>> tabViews = new ArrayList<Map<String, View>>();
	Context context;
	public TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		// --------------------
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("0").setIndicator(createTab("主页", 0)).setContent(android.R.id.tabcontent));
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator(createTab("统计", 1)).setContent(android.R.id.tabcontent));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator(createTab("消息", 2)).setContent(android.R.id.tabcontent));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator(createTab("设置", 3)).setContent(android.R.id.tabcontent));
		// 点击tabHost 来切换不同的消息
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				int index = Integer.parseInt(tabId);
				setTabSelectedState(index, 4);
				tabHost.getTabContentView().setVisibility(View.GONE);// 隐藏content
			}
		});
		tabHost.setCurrentTab(0);
		initJazzyPager(JazzyViewPager.TransitionEffect.Standard);
	}

    @Override
    protected void init() {

    }

    /**
	 * 动态创建 TabWidget 的Tab项,并设置normalLayout的alpha为1，selectedLayout的alpha为0[显示normal，隐藏selected]
	 * @param tabLabelText
	 * @param tabIndex
	 * @return
	 */
	private View createTab(String tabLabelText, int tabIndex) {
//		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.main_tabwidget_layout, null);
//		TextView normalTV = (TextView) tabIndicator.findViewById(R.id.normalTV);
//		TextView selectedTV = (TextView) tabIndicator.findViewById(R.id.selectedTV);
//		normalTV.setText(tabLabelText);
//		selectedTV.setText(tabLabelText);
//		ImageView normalImg = (ImageView) tabIndicator.findViewById(R.id.normalImg);
//		ImageView selectedImg = (ImageView) tabIndicator.findViewById(R.id.selectedImage);
//		switch (tabIndex) {
//			case 0:
//				normalImg.setImageResource(R.drawable.index_nav_1);
//				selectedImg.setImageResource(R.drawable.index_nav_1);
//				break;
//			case 1:
//				normalImg.setImageResource(R.drawable.index_nav_2);
//				selectedImg.setImageResource(R.drawable.index_nav_2);
//				break;
//			case 2:
//				normalImg.setImageResource(R.drawable.index_nav_3);
//				selectedImg.setImageResource(R.drawable.index_nav_3);
//				break;
//			case 3:
//				normalImg.setImageResource(R.drawable.index_nav_4);
//				selectedImg.setImageResource(R.drawable.index_nav_4);
//				break;
//		}
//		View normalLayout = tabIndicator.findViewById(R.id.normalLayout);
//		normalLayout.setAlpha(1f);// 透明度显示
//		View selectedLayout = tabIndicator.findViewById(R.id.selectedLayout);
//		selectedLayout.setAlpha(0f);// 透明的隐藏
//		Map<String, View> map = new HashMap<String, View>();
//		map.put("normal", normalLayout);
//		map.put("selected", selectedLayout);
//		tabViews.add(map);
//		return tabIndicator;
        return null;
	}

	/**
	 * 设置tab状态选中
	 * @param index
	 */
	private void setTabSelectedState(int index, int tabCount) {
		for (int i = 0; i < tabCount; i++) {
			if (i == index) {
				tabViews.get(i).get("normal").setAlpha(0f);
				tabViews.get(i).get("selected").setAlpha(1f);
			} else {
				tabViews.get(i).get("normal").setAlpha(1f);
				tabViews.get(i).get("selected").setAlpha(0f);
			}
		}
		jazzyPager.setCurrentItem(index, false);// false表示 代码切换 pager 的时候不带scroll动画
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTabSelectedState(tabHost.getCurrentTab(), 4);
	}
	
	private void initJazzyPager(JazzyViewPager.TransitionEffect effect) {
		jazzyPager.setTransitionEffect(effect);
		jazzyPager.setAdapter(new MainAdapter());
		jazzyPager.setPageMargin(30);
		jazzyPager.setFadeEnabled(true);
		jazzyPager.setSlideCallBack(new JazzyViewPager.SlideCallback() {
			@Override
			public void callBack(int position, float positionOffset) {
				Map<String, View> map = tabViews.get(position);
				ViewHelper.setAlpha(map.get("selected"), positionOffset);
				ViewHelper.setAlpha(map.get("normal"), 1 - positionOffset);
			}
		});
		jazzyPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				tabHost.setCurrentTab(position);
			}

			@Override
			public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
			}

			@Override
			public void onPageScrollStateChanged(int paramInt) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Toggle Fade");
		String[] effects = this.getResources().getStringArray(R.array.jazzy_effects);
		for (String effect : effects)
			menu.add(effect);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().equals("Toggle Fade")) {
			jazzyPager.setFadeEnabled(!jazzyPager.getFadeEnabled());
		} else {
			JazzyViewPager.TransitionEffect effect = JazzyViewPager.TransitionEffect.valueOf(item.getTitle().toString());
			initJazzyPager(effect);
		}
		return true;
	}

    @Override
    public void onClick(View view) {

    }

    private class MainAdapter extends PagerAdapter {
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			TextView text = new TextView(MainActivity.this);
			text.setGravity(Gravity.CENTER);
			text.setTextSize(30);
			text.setTextColor(Color.WHITE);
			text.setText("Page " + position);
			text.setPadding(30, 30, 30, 30);
			int bg = Color.rgb((int) Math.floor(Math.random() * 128) + 64, (int) Math.floor(Math.random() * 128) + 64, (int) Math.floor(Math.random() * 128) + 64);
			text.setBackgroundColor(bg);
			container.addView(text, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			jazzyPager.setObjectForPosition(text, position);
			return text;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			container.removeView(jazzyPager.findViewFromObject(position));
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}
	}

}
