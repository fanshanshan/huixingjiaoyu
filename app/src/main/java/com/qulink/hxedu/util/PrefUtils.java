package com.qulink.hxedu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.entity.SystemSettingBean;
import com.qulink.hxedu.entity.TokenInfo;


/**
 * SharePrefrence封装
 * 
 * @author Kevin
 * 
 */
public class PrefUtils {

	private static final String PREF_NAME = "hxEdu";

	private static final String TOKEN_KEY="token";
	private static SharedPreferences sp;

	public static void putBoolean(Context ctx, String key, boolean value) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}

		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context ctx, String key, boolean defValue) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}

	public static void putString(Context ctx, String key, String value) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	public static void putLong(Context ctx, String key, long value) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putLong(key, value).commit();
	}
	public static long getLong(Context ctx, String key, long defValue) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		return sp.getLong(key, defValue);
	}
	public static void putDouble(Context ctx, String key, float value) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putFloat(key, value).commit();
	}
	public static float getDouble(Context ctx, String key, float defValue) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		return sp.getFloat(key, defValue);
	}
	public static String getString(Context ctx, String key, String defValue) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}


	public static void saveToken(Context context,TokenInfo tokenJson){
		putString(context,TOKEN_KEY,GsonUtil.GsonString(tokenJson));
	}	public static void saveSystemSetting(Context context,SystemSettingBean systemSettingBean){
		putString(context,PREF_NAME,GsonUtil.GsonString(systemSettingBean));
	}
	public static void clearToken(Context context){
		putString(context,TOKEN_KEY,"");

	}
	public static TokenInfo getTokenBean(Context context){
		String tokenStr = getString(context,TOKEN_KEY,"");
		return GsonUtil.GsonToBean(tokenStr,TokenInfo.class);
	}public static SystemSettingBean getSystemSetting(Context context){
		String tokenStr = getString(context,PREF_NAME,"");
		return GsonUtil.GsonToBean(tokenStr,SystemSettingBean.class);
	}
	public static void clearData(Context context){
		putBoolean(context,"login",false);
	//	putBoolean(context,"is_show_award",false);
		//putBoolean(context,"alias",false);
//		if (sp == null) {
//			sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//		}
//		sp.edit().clear().commit();
	}
}
