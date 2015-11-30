package com.okar.icz.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.okar.icz.android.R;
import com.okar.icz.test.StringListAdapter;
import com.okar.icz.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 15/11/27.
 */
public class PhotoView extends RecyclerView {

    List<Photo> photos = new ArrayList<>();

    LayoutInflater inflater;

    Context mContext;

    private PhotoViewCallback callback;

    public PhotoView(Context context) {
        super(context);
        init(context);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    void init(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        photos.add(new Photo());//增加按钮
        setLayoutManager(new GridLayoutManager(context, 4));
        setAdapter(mAdapter);
    }

    private Adapter<RecyclerView.ViewHolder> mAdapter = new Adapter<ViewHolder>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PhotoViewHolder(inflater.inflate(R.layout.item_photo, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            Photo photo = photos.get(position);
            PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
            if(viewType==0){//增加
                photoViewHolder.deleteIV.setVisibility(View.GONE);
                photoViewHolder.photoIV.setImageResource(R.drawable.iconfont_jiahao);
                photoViewHolder.photoIV.setPadding(
                        DensityUtils.dip2px(getContext(), 15),
                        DensityUtils.dip2px(getContext(), 15),
                        DensityUtils.dip2px(getContext(), 15),
                        DensityUtils.dip2px(getContext(), 15));
                photoViewHolder.photoIV.setBackgroundResource(R.drawable.add_photo_bg);
                photoViewHolder.photoIV.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (callback!=null)
                            callback.onClickAddPhoto();
                    }
                });
            } else {//显示
                photoViewHolder.deleteIV.setVisibility(View.VISIBLE);
                photoViewHolder.photoIV.setPadding(0, 0, 0, 0);
                ImageLoader.getInstance().displayImage(photo.filePath, photoViewHolder.photoIV);
                photoViewHolder.photoIV.setBackground(null);
            }
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        @Override
        public int getItemViewType(int position) {
            return photos.get(position).type;
        }
    };

    class PhotoViewHolder extends ViewHolder {
        public ImageView photoIV, deleteIV;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            photoIV = (ImageView) itemView.findViewById(R.id.photo);
            deleteIV = (ImageView) itemView.findViewById(R.id.delete);
        }
    }

    class Photo {
        String filePath, url;
        int type = 0;//0是按钮 1:是图片

        public Photo(String fp, String url) {
            filePath = fp;
            this.url = url;
            type = 1;
        }

        public Photo(){}
    }

    public interface PhotoViewCallback {
        void onClickAddPhoto();
    }

    public void setPhotoViewCallback(PhotoViewCallback callback) {
        this.callback = callback;
    }

    public void addPhoto(String fp, String url) {
        int index = photos.size() - 1;
        photos.add(index, new Photo(fp, url));
        mAdapter.notifyItemInserted(index);
    }

    public List<String> getPhotoUrls() {
        List<String> rs = new ArrayList<String>();
        for (Photo photo : photos) {
            if(photo.type==1) {
                rs.add(photo.url);
            }
        }
        return rs;
    }
}
