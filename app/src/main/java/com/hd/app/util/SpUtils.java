package com.hd.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SpUtils {

    /**
     * 方法名: getString
     * <p>
     * 功能描述:得到字符串
     *
     * @param fileName 文件名
     * @param key      key
     * @return String 结果.如不存在,返回null
     * <p>
     * </br>throws
     */
    public static String getString(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public static String getString(Context context, String fileName, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    /**
     * 方法名: getInt
     * <p>
     * 功能描述:得到Int
     *
     * @param fileName 文件名
     * @param key      key
     * @return int 结果.如不存在,返回-0x10086
     * <p>
     * </br>throws
     */
    public static int getInt(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getInt(key, -0x10086);
    }

    /**
     * 方法名: getInt
     * <p>
     * 功能描述:得到Int
     *
     * @param fileName 文件名
     * @param key      key
     * @return int 结果.如不存在,返回传入的number
     * <p>
     * </br>throws
     */
    public static int getInt(Context context, String fileName, String key, int number) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getInt(key, number);
    }

    /**
     * 方法名: getLong
     * <p>
     * 功能描述:得到long
     *
     * @param fileName 文件名
     * @param key      key
     * @return long 结果.如不存在,返回-0x10086
     * <p>
     * </br>throws
     */
    public static long getLong(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getLong(key, -0x10086);
    }

    public static long getLong(Context context, String fileName, String key, long defaultVar) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getLong(key, defaultVar);
    }

    /**
     * 方法名: getFloat
     * <p>
     * 功能描述:得到float
     *
     * @param fileName 文件名
     * @param key      key
     * @return long 结果.如不存在,返回-10086.0f
     * <p>
     * </br>throws
     */
    public static float getFloat(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getFloat(key, -10086.0f);
    }

    /**
     * 方法名: getBoolean
     * <p>
     * 功能描述:得到boolean
     *
     * @param fileName 文件名
     * @param key      key
     * @return long 结果.true or false.默认返回false
     * <p>
     * </br>throws
     */
    public static boolean getBoolean(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    /**
     * 方法名: getBoolean
     * <p>
     * 功能描述:得到boolean
     *
     * @param context  上下文对象
     * @param fileName 文件名
     * @param key      key
     * @param flag     默认值
     * @return boolean long 结果.true or false.
     * <p>
     * </br>throws
     */
    public static boolean getBoolean(Context context, String fileName, String key, boolean flag) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, flag);
    }

    /**
     * 方法名: putString
     * <p>
     * 功能描述:存放String
     *
     * @param context  上下文对象
     * @param fileName 文件名
     * @param key      key
     * @param value    存放的内容
     * @return void
     * <p>
     * </br>throws
     */
    public static void putString(Context context, String fileName, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void remove(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.remove(key).commit();
    }

    /**
     * 方法名: putInt
     * <p>
     * 功能描述:存放Int
     *
     * @param context  上下文对象
     * @param fileName 文件名
     * @param key      key
     * @param value    存放的内容
     * @return void
     * <p>
     * </br>throws
     */
    public static void putInt(Context context, String fileName, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 方法名: putLong
     * <p>
     * 功能描述:存放long
     *
     * @param context  上下文对象
     * @param fileName 文件名
     * @param key      key
     * @param value    存放的内容
     * @return void
     * <p>
     * </br>throws
     */
    public static void putLong(Context context, String fileName, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 方法名: putFloat
     * <p>
     * 功能描述:存放long
     *
     * @param context  上下文对象
     * @param fileName 文件名
     * @param key      key
     * @param value    存放的内容
     * @return void
     * <p>
     * </br>throws
     */
    public static void putFloat(Context context, String fileName, String key, float value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 方法名: putBoolean
     * <p>
     * 功能描述:存放Boolean
     *
     * @param context  上下文对象
     * @param fileName 文件名
     * @param key      key
     * @param value    存放的内容
     * @return void
     * <p>
     * </br>throws
     */
    public static void putBoolean(Context context, String fileName, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}
