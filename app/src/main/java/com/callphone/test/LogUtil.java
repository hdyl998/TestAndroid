package com.callphone.test;

import com.callphone.myapplication.MyLogUitls;

public class LogUtil {

	public static void sleep(int second) {
		try {
			Thread.sleep(second);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void log(String msg) {
		MyLogUitls.print(msg);
	}

	public static void log(int msg) {
		System.out.println(msg);
	}

}
