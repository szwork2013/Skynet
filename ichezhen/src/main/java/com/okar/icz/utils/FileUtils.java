package com.okar.icz.utils;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Environment;

import com.okar.icz.common.Constants;
import com.okar.icz.common.MD5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class FileUtils {

	public static File fileFolder1;
	public static SimpleDateFormat format;

	/**
	 * 将拍下来的照片存放在SD卡中
	 */
	public static String saveToSDCard(byte[] data) throws IOException {
		String fileName = getFileName("temp", ".jpg");
		fileFolder1 = new File(Constants.TEMP_ROOT_DIR + "/");
		if (!fileFolder1.exists()) {
			fileFolder1.mkdirs();
		}
		File jpgFile = new File(fileFolder1, fileName);
		FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
		outputStream.write(data); // 写入sd卡中
		outputStream.close(); // 关闭输出流

		return jpgFile.getAbsolutePath();
	}

	public static File saveToFile(InputStream is, String filepath)
			throws Exception {
		File file = createNewFile(filepath);
		FileOutputStream output = new FileOutputStream(file);
		byte[] buff = new byte[1024];
		int num = 0;
		while ((num = is.read(buff)) != -1) {
			output.write(buff, 0, num);
		}
		output.flush();
		output.close();

		return file;
	}

	/***
	 * 获取图片缓存文件的名称
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
		return MD5.encrypt(url) + ".i";
	}

	public static String hashKeyForDisk(String key) {
		return getFileName(key);

		// String cacheKey;
		// try {
		// final MessageDigest mDigest = MessageDigest.getInstance("MD5");
		// mDigest.update(key.getBytes());
		// cacheKey = bytesToHexString(mDigest.digest());
		// } catch (NoSuchAlgorithmException e) {
		// cacheKey = String.valueOf(key.hashCode());
		// }
		// return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 判断SD是否可以
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 创建根目录
	 * 
	 * @param path
	 *            目录路径
	 */
	public static void createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文件
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹的路径
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		myFilePath.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路径
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * 获取文件的Uri
	 * 
	 * @param path
	 *            文件的路径
	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 * 换算文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/***
	 * 获取文件名
	 * 
	 * @param p
	 *            文件前缀
	 * @param b
	 *            文件后缀
	 * @return 文件名
	 */
	public static String getFileName(String p, String b) {
		Date date = new Date();
		format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
		String fileName = p + format.format(date) + b;
		return fileName;
	}

	public static boolean sdcardExists() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			return true;
		else
			return false;
	}

}
