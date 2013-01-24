package com.edgardleal.util;

import java.text.DecimalFormat;

public class NumberUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public static String format(double value) {
		return new DecimalFormat(Config.FLOAT_FORMAT).format(value);
	}

	public static String format(String value) {
		double _value = getDouble(value);
		return format(_value);
	}

	public static double getDouble(final String value) {
		if (new Validator().isValidNumber(value.trim()))
			return Double.valueOf(value.trim().replace(",", "."));
		else
			return 0D;
	}
}
