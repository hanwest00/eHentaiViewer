package com.hx.android.ehentai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileManager {
	public static File createDir(String path) {
		File ret = new File(path);
		if (!ret.exists())
			ret.mkdirs();
		return ret;
	}

	public static void clearDir(File dir) {

		File[] files = dir.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				clearDir(f);
				continue;
			}

			f.delete();
		}
	}

	public static void clearDir(String path) {
		File dir = new File(path);
		clearDir(dir);
	}

	public static File createFile(String dirs, String fileName) {
		File ret = new File(createDir(dirs), fileName);
		if (!ret.exists())
			try {
				ret.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return ret;
	}

	public static File createFile(String fullFileName) {
		String dir = fullFileName.substring(0, fullFileName.lastIndexOf("/"));
		String fileName = fullFileName.substring(fullFileName.lastIndexOf("/"),
				fullFileName.length());
		return createFile(dir, fileName);
	}
	
	public static Bitmap convertToBitmap(String path) {
		File f = new File(path);
		return convertToBitmap(f);
	}

	public static Bitmap convertToBitmap(File file) {
		Bitmap ret = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, options);
			int i = options.outWidth;
			int j = options.outHeight;
			int k = 1;
			while (true) {
				if ((i / 2 < 300) || (j / 2 < 300)) {
					BitmapFactory.Options options2 = new BitmapFactory.Options();
					options2.inSampleSize = k;
					ret = BitmapFactory.decodeStream(new FileInputStream(file),
							null, options2);
					break;
				}
				i /= 2;
				j /= 2;
				k *= 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
