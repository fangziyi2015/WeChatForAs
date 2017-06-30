package com.juns.wechat;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class App extends Application {

	private static Context _context;

	public static String currentUserNick = "";

	@Override
	public void onCreate() {
		super.onCreate();
		_context = getApplicationContext();

		CrashHandler crashHandler = CrashHandler.getInstance();// 全局异常捕捉
		crashHandler.init(_context);

		EMOptions options = new EMOptions();
		// 	默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		//	初始化
		EMClient.getInstance().init(_context, options);
		//	在做打包混淆时，关闭debug模式，避免消耗不必要的资源
		EMClient.getInstance().setDebugMode(true);
	}

	public static Context getContext(){
		return _context;
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		try {
			deleteCacheDirFile(getHJYCacheDir(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.gc();
	}

	public static Context getInstance() {
		return _context;
	}

	private static App instance;

	// 构造方法
	// 实例化一次
	public synchronized static App getInstance2() {
		if (null == instance) {
			instance = new App();
		}
		return instance;
	}

	public static String getHJYCacheDir() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().toString()
					+ "/Health/Cache";
		else
			return "/System/com.juns.Walk/Walk/Cache";
	}

	public static String getHJYDownLoadDir() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().toString()
					+ "/Walk/Download";
		else {
			return "/System/com.Juns.Walk/Walk/Download";
		}
	}

	public static void deleteCacheDirFile(String filePath,
			boolean deleteThisPath) throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteCacheDirFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}
}
