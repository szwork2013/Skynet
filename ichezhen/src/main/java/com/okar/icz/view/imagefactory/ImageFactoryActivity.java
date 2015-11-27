package com.okar.icz.view.imagefactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.okar.icz.android.R;
import com.okar.icz.common.BaseActivity;
import com.okar.icz.view.photo.PhotoUtils;


public class ImageFactoryActivity extends BaseActivity {

	private ViewFlipper mVfFlipper;
	private Button mBtnLeft;
	private Button mBtnRight;

	private ImageFactoryCrop mImageFactoryCrop;
	private String mPath;
	private String mNewPath;
	private int mIndex = 0;
	private String mType;

	public static final String TYPE = "type";
	public static final String CROP = "crop";
	public static final String FLITER = "fliter";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagefactory);
		initViews();
		initEvents();
		init();
	}

	protected void initViews() {
		mVfFlipper = (ViewFlipper) findViewById(R.id.imagefactory_vf_viewflipper);
		mBtnLeft = (Button) findViewById(R.id.imagefactory_btn_left);
		mBtnRight = (Button) findViewById(R.id.imagefactory_btn_right);
	}

	protected void initEvents() {
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(RESULT_CANCELED);
				finish();

			}
		});
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryCrop
						.cropAndSave());
				Intent intent = getIntent();
				intent.putExtra("path", mNewPath);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void init() {
		mPath = getIntent().getStringExtra("path");
		mType = getIntent().getStringExtra(TYPE);
		mNewPath = new String(mPath);
		if (CROP.equals(mType)) {
			mIndex = 0;
		} else if (FLITER.equals(mType)) {
			mIndex = 1;
			mVfFlipper.showPrevious();
		}
		initImageFactory();
	}

	private void initImageFactory() {
		switch (mIndex) {
		case 0:
			if (mImageFactoryCrop == null) {
				mImageFactoryCrop = new ImageFactoryCrop(this,
						mVfFlipper.getChildAt(0));
			}
			mImageFactoryCrop.init(mPath, getScreenWidth(), getScreenHeight());
			mBtnLeft.setText("取    消");
			mBtnRight.setText("确    认");

			break;
		}
	}

}
