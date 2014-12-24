package com.hx.android.ehentai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
	
	public static File createFile(File file) {
		return createFile(file.getAbsolutePath());
	}

	public static Bitmap convertToBitmap(String path) {
		return convertToBitmap(path, 0, 0);
	}

	public static Bitmap convertToBitmap(String path, int w, int h) {
		Bitmap ret = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			BitmapFactory.Options options1 = new BitmapFactory.Options();
			options1.inSampleSize = computeSampleSize(options, -1, w * h);
			ret = BitmapFactory.decodeFile(path, options1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static Bitmap convertToBitmap(byte[] content, int w, int h) {
		Bitmap ret = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(content, 0, content.length, options);

			BitmapFactory.Options options1 = new BitmapFactory.Options();
			options1.inSampleSize = computeSampleSize(options, -1, w * h);
			ret = BitmapFactory.decodeByteArray(content, 0, content.length, options1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static String readAllText(String path, String charset) {
		return readAllText(new File(path), charset);
	}

	public static String readAllText(File file, String charset) {
		String ret = null;

		if (!file.exists())
			return ret;

		try {
			FileInputStream inputStream = new FileInputStream(file);

			byte[] buffer = new byte[(int) file.length()];

			if (inputStream.read(buffer) > 0)
				ret = new String(buffer, charset);

			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public static void writeAllText(File file, String content, String charset) {
		try {
			writeFile(file, content.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void appendAllText(File file, String content, String charset) {
		try {
			appendFile(file, content.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeFile(File file, byte[] content) {
		try {
			if(!file.exists()) createFile(file);
			FileOutputStream outputStream = new FileOutputStream(file);
			FileChannel fileChannel = outputStream.getChannel();
			fileChannel.write(ByteBuffer.wrap(content), 0);
			fileChannel.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void appendFile(File file, byte[] content) {
		try {
			if(!file.exists()) createFile(file);
			FileOutputStream outputStream = new FileOutputStream(file);
			FileChannel fileChannel = outputStream.getChannel();
			fileChannel.write(ByteBuffer.wrap(content),
					file.length());
			fileChannel.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] readAllBytes(File file){
		try {
			FileInputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			inputStream.read(buffer);
			inputStream.close();
			return buffer;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
