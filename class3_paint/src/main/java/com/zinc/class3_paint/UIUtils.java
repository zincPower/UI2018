package com.zinc.class3_paint;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by zinc on 16/7/15.
 */
public class UIUtils {

	public static int dip2px(Context context, float dipValue) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * density + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / density + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

}
