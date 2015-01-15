package com.shopping.list.helper;

import com.shopping.list.constant.GlobalValues;

import android.graphics.Color;

public class ProcessColorHelper {

	public static int getColorForProcess(final double countCheckedProducts,
			final double countProducts) {

		final double processPercentage = (countCheckedProducts / countProducts) * 100;

		if (processPercentage > GlobalValues.PROCESS_PERCENTAGE_YELLOW) {
			// if there is higher process than 99.99 % - it's green
			return Color.parseColor(GlobalValues.PROCESS_COLOR_LIME_GREEN);

		} else if (processPercentage > GlobalValues.PROCESS_PERCENTAGE_RED) {
			// if there is higher process than 0.00 % - it's yellow
			return Color.parseColor(GlobalValues.PROCESS_COLOR_YELLOW);

		} else {
			// it's red
			return Color.parseColor(GlobalValues.PROCESS_COLOR_RED);
		}
	}
}
