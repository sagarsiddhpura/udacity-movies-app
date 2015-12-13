package com.siddworks.android.popcorntime.util;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {

	private static final String TAG = "Log";
	static int count = 0;
	static String message = "";
	static boolean isGlobalLoggingEnabled = true;
	static boolean isFileLoggingEnabled = false;
	
	public static void d(boolean loggingEnabled, String tag, String message)
	{
		if(isGlobalLoggingEnabled && loggingEnabled)
		{
			logToFile(tag, message);
			android.util.Log.d(tag, message);
		}
	}
	
	public static void i(boolean loggingEnabled, String tag, String message)
	{
		if(isGlobalLoggingEnabled && loggingEnabled)
		{
			logToFile(tag, message);
			android.util.Log.i(tag, message);
		}
	}
	
	public static void e(boolean loggingEnabled, String tag, String message)
	{
		if(isGlobalLoggingEnabled && loggingEnabled)
		{
			logToFile(tag, message);
			android.util.Log.e(tag, message);
		}
	}

	private static void logToFile(String tag, String message2) {
//		android.util.Log.d(TAG, "logToFile() called with " + "tag = [" + tag + "], message2 = [" + message2 + "]");
		if(isFileLoggingEnabled)
		{
//			if(count > 50)
//			{
				 try {
					 File file = new File(Environment.getExternalStoragePublicDirectory(
					            Environment.DIRECTORY_DOWNLOADS), "Log.txt");
			            FileWriter out = new FileWriter(file, true);
			            out.write(message2+"\n\n");
//					 out.write(message+"\n\n");
			            out.close();
			        } catch (IOException e) {
			        }
				count = 0;
				message = "";
//			}
//			else
//			{
//				count ++;
//				message += SystemClock.elapsedRealtime() + " : " + tag+" : "+message2+"\n\n";
//			}
		}
	}
}
