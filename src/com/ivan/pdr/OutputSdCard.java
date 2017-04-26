package com.ivan.pdr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Environment;

public class OutputSdCard {
	public static String getSDPath() {
		File sdDir = null;
		File file = null;
		boolean sdCardExit = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);		//判断sd卡是否存在
		if (sdCardExit) {
			sdDir = Environment.getExternalStorageDirectory();	//获取根目录
			String path = sdDir.getPath() + "/accelData";	//make new dir
			file = new File(path);
			if ( !file.exists())
				file.mkdir();
		}
		return file.toString();
	}
	//
	public static void method1(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
