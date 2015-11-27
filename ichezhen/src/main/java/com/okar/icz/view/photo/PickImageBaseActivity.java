package com.okar.icz.view.photo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.okar.icz.android.R;
import com.okar.icz.common.BaseActivity;
import com.okar.icz.utils.FileUtils;

/**
 * Created by wangfengchen on 15/9/2.
 */
public abstract class PickImageBaseActivity extends BaseActivity {

    protected static final int PICK_IMAGE_FROM_ALBUM = 0; // 从相册获取图片
    protected static final int PICK_IMAGE_FROM_CAMERA = 1; // 从相机获取图片
    protected static final int PICK_IMAGE_FROM_DISPOSE = 2; // 获取处理完成的图片


    protected boolean isDispose;

    private String takePicture = "";

    private Uri corpPhotoResultUri;

    public String getTakePicture() {
        return takePicture;
    }

    /**
     * 选择图片
     *
     * @param isDispose 是否处理
     */
    public void pickImage(boolean isDispose) {
        this.isDispose = isDispose;
        new AlertDialog.Builder(this)
                .setTitle(R.string.pick_image_title)
                .setItems(R.array.pick_image,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        pickImageFromAlbum();
                                        break;
                                    case 1:
                                        pickImageFromCamera();
                                    default:
                                        break;
                                }
                            }
                        }).create().show();
    }

    /**
     * 从相册获取图片
     */
    protected void pickImageFromAlbum() {
        PhotoUtils.selectPhoto(this);

    }

    /**
     * 从相机获取图片
     */
    protected void pickImageFromCamera() {
        takePicture = PhotoUtils.takePicture(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (data.getData() == null) {
                        return;
                    }
                    if (!FileUtils.isSdcardExist()) {
                        showToast("SD卡不可用,请检查");
                        return;
                    }
                    Uri uri = data.getData();
                    String path = null;
                    if("content".equals(uri.getScheme())) {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(uri, proj, null, null, null);
                        if (cursor != null) {
                            int column_index = cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                                path = cursor.getString(column_index);
                            }
                        }
                    } else if("file".equals(uri.getScheme())) {
                        path = uri.getPath();
                    }

                    // Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (path == null) {
                        showToast("图片不可用");
                        return;
                    }

                    if (isDispose) {
                        corpPhotoResultUri = PhotoUtils.cropPhoto(this, path);
                    } else {
                        Bitmap bitmap = PhotoUtils.genThumbnail(path);
                        if(bitmap!=null) {
                            pickImageResult(bitmap, path);
                        }
                    }
                }
                break;

            case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    String path = getTakePicture();
                    // Bitmap bitmap = BitmapFactory.decodeFile(path);

                    if (isDispose) {
                        corpPhotoResultUri = PhotoUtils.cropPhoto(this, path);
                    } else {
                        Bitmap bitmap = PhotoUtils.genThumbnail(path);
                        if(bitmap!=null) {
                            pickImageResult(bitmap, path);
                        }
                    }
                }
                break;

            case PhotoUtils.INTENT_REQUEST_CODE_CROP:
                if (resultCode == RESULT_OK) {
                    String path = null;
                    Uri uri = corpPhotoResultUri;
                    if(uri!=null) {
                        path = uri.getPath();
                    }
//                    else {
//                        String action = data.getAction();
//                        if(action!=null) {
//                            uri = Uri.parse(action);
//                            path = uri.getPath();
//                        }
//                    }
                    if(path==null) {
                        path = data.getStringExtra("path");
                    }

                    if (path != null) {
                        Bitmap bitmap = PhotoUtils.genThumbnail(path);
                        if (bitmap != null) {
                            pickImageResult(bitmap, path);
                        }
                    }
                }
                break;
        }
    }

    /***
     * 返回获取的图片 子类继承此方法来接收方法选择的图片
     *
     * @param bitmap
     *            选择的图片
     * @param filePath
     *            选择图片的路径
     */
    protected void pickImageResult(Bitmap bitmap, String filePath) {

    }

}
