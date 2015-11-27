package com.okar.icz.view.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.okar.icz.android.R;
import com.okar.icz.utils.FileUtils;
import com.okar.icz.view.imagefactory.ImageFactoryActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PhotoUtils {
	// 图片在SD卡中的缓存路径
	private static final String IMAGE_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ File.separator
			+ "ichezhen" + File.separator + "tmp" + File.separator;
	// 相册的RequestCode
	public static final int INTENT_REQUEST_CODE_ALBUM = 8880;
	// 照相的RequestCode
	public static final int INTENT_REQUEST_CODE_CAMERA = 8881;
	// 裁剪照片的RequestCode
	public static final int INTENT_REQUEST_CODE_CROP = 8884;
	// 滤镜图片的RequestCode
	public static final int INTENT_REQUEST_CODE_FLITER = 8883;

	/**
	 * 通过手机相册获取图片
	 * 
	 * @param activity
	 */
	public static void selectPhoto(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
	}

	/**
	 * 通过手机照相获取图片
	 * 
	 * @param activity
	 * @return 照相后图片的路径
	 */
	public static String takePicture(Activity activity) {
		FileUtils.createDirFile(IMAGE_PATH);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String path = IMAGE_PATH + UUID.randomUUID().toString() + ".jpg";
		File file = FileUtils.createNewFile(path);
		if (file != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
		return path;
	}

	/**
	 * 裁剪图片
	 * @param activity
	 * @param path
	 *            需要裁剪的图片路径
	 */
	public static Uri cropPhoto(Activity activity, String path) {
		Intent intent;
		Uri uri = null;
		try {
			intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
			intent.putExtra("crop", "true");//可裁剪
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 150);
			intent.putExtra("outputY", 150);
			intent.putExtra("scale", true);

			File dirs = new File(IMAGE_PATH);
			if(!dirs.exists()) {
				dirs.mkdirs();
			}
			uri = Uri.fromFile(new File(IMAGE_PATH + UUID.randomUUID().toString() + ".jpg"));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			intent.putExtra("return-data", false);//若为false则表示不返回数据
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true);
		}catch (Exception e) {
			e.printStackTrace();

			intent = new Intent(activity, ImageFactoryActivity.class);
			if (path != null) {
				intent.putExtra("path", path);
				intent.putExtra(ImageFactoryActivity.TYPE,
						ImageFactoryActivity.CROP);
			}
		}


		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
		return uri;
	}

	/**
	 * 滤镜图片
	 * 
	 * @param context
	 * @param activity
	 * @param path
	 *            需要滤镜的图片路径
	 */
	public static void fliterPhoto(Context context, Activity activity,
			String path) {
		Intent intent = new Intent(context, ImageFactoryActivity.class);
		if (path != null) {
			intent.putExtra("path", path);
			intent.putExtra(ImageFactoryActivity.TYPE,
					ImageFactoryActivity.FLITER);
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_FLITER);
	}

	/**
	 * 删除图片缓存目录
	 */
	public static void deleteImageFile() {
		File dir = new File(IMAGE_PATH);
		if (dir.exists()) {
			FileUtils.delFolder(IMAGE_PATH);
		}
	}

	/**
	 * 从文件中获取图片
	 * 
	 * @param path
	 *            图片的路径
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path) {
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * 从Uri中获取图片
	 * 
	 * @param cr
	 *            ContentResolver对象
	 * @param uri
	 *            图片的Uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
		try {
			return BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	/**
	 * 根据宽度和长度进行缩放图片
	 * 
	 * @param path
	 *            图片的路径
	 * @param w
	 *            宽度
	 * @param h
	 *            长度
	 * @return
	 */
	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 获取图片的长度和宽度
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
		Bundle bundle = null;
		if (bitmap != null) {
			bundle = new Bundle();
			bundle.putInt("width", bitmap.getWidth());
			bundle.putInt("height", bitmap.getHeight());
			return bundle;
		}
		return null;
	}

	/**
	 * 判断图片高度和宽度是否过大
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static boolean bitmapIsLarge(Bitmap bitmap) {
		final int MAX_WIDTH = 60;
		final int MAX_HEIGHT = 60;
		Bundle bundle = getBitmapWidthAndHeight(bitmap);
		if (bundle != null) {
			int width = bundle.getInt("width");
			int height = bundle.getInt("height");
			if (width > MAX_WIDTH && height > MAX_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据比例缩放图片
	 * 
	 * @param screenWidth
	 *            手机屏幕的宽度
	 * @param ratio
	 *            缩放比例
	 * @return
	 */
	public static Bitmap CompressionPhoto(float screenWidth, String path,
			int ratio) {

		Bitmap bitmap = BitmapFactory.decodeFile(path);
		Bitmap compressionBitmap = null;
		float scaleWidth = screenWidth / (bitmap.getWidth() * ratio);
//		float scaleHeight = screenWidth / (bitmap.getHeight() * ratio);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		try {
			compressionBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			return bitmap;
		}

		return compressImage(compressionBitmap);
	}
	
	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于5m,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}

		Log.i("image compress", baos.toByteArray().length / 1024 + "k");

		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	/***
	 * 获取所有品牌的图片
	 * 
	 * @return
	 */
	public static Map<String, Integer> getBrandImages() {

		Map<String, Integer> brands = new HashMap<String, Integer>();

		try {
			// 得到R.drawable所有的属性, 即获取drawable目录下的所有图片
			Field[] drawableFields = R.drawable.class.getFields();

			for (Field field : drawableFields) {
				// 如果该Field的名称以p_开头
				if (field.getName().indexOf("b_") != -1) {
					brands.put(field.getName(), field.getInt(R.drawable.class));
				}
			}
			return brands;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 保存图片到SD卡
	 * 
	 * @param bitmap
	 *            图片的bitmap对象
	 * @return
	 */
	public static String savePhotoToSDCard(Bitmap bitmap) {
		if (!FileUtils.isSdcardExist()) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		FileUtils.createDirFile(IMAGE_PATH);

		String fileName = UUID.randomUUID().toString() + ".jpg";
		String newFilePath = IMAGE_PATH + fileName;
		File file = FileUtils.createNewFile(newFilePath);
		if (file == null) {
			return null;
		}
		try {
			fileOutputStream = new FileOutputStream(newFilePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
		} catch (FileNotFoundException e1) {
			return null;
		} finally {
			try {
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				return null;
			}
		}
		return newFilePath;
	}

	/**
	 * @return 返回指定笔和指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}

	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}

	/**
	 * @return 返回指定笔离文字顶部的基准距离
	 */
	public static float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.leading - fm.ascent;
	}

	
	public static Bitmap genThumbnail(String srcPath){
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();
	        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
	        newOpts.inJustDecodeBounds = true;
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

	        newOpts.inJustDecodeBounds = false;
	        int w = newOpts.outWidth;
	        int h = newOpts.outHeight;
	        System.out.println("src pic height: "+h);
	        System.out.println("src pic width: "+w);
	        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//	        float hh = 800f;//这里设置高度为800f
	        float ww = 100f;//这里设置宽度为480f
	        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
	        int be = 1;//be=1表示不缩放
	        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
	            be = (int) (newOpts.outWidth / ww);
	        }
//	        else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//	            be = (int) (newOpts.outHeight / hh);
//	        }
	        if (be <= 0)
	            be = 1;
	        newOpts.inSampleSize = be;//设置缩放比例
	        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * 读取图片文件旋转的角度
	 * @param path 图片绝对路径
	 * @return 图片旋转的角度
	 */
	public static int getPicRotate(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		}
//		else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//			be = (int) (newOpts.outHeight / hh);
//		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		int degree = getPicRotate(srcPath);
		if(degree!=0){
			Matrix m = new Matrix();
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			m.setRotate(degree); // 旋转angle度
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,m, true);// 从新生成图片
		}
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}


	public static String compressionPhoto(String path) {
		Bitmap bitmap = getimage(path);
		return savePhotoToSDCard(bitmap);
	}

	public static void removeTemPhoto(String filePath) {
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		}
	}
}
