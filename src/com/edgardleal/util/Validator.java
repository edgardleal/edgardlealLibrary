package com.edgardleal.util;

import java.text.DecimalFormat;

public class Validator {
	DecimalFormat decimal = new DecimalFormat();

	public Validator() {

	}

	public boolean checkEmail(String email) {
		return email.matches("\\w+@\\w+\\.\\w+");
	}

	public boolean checkTelephon(String tel) {
		return tel.matches("(\\(\\d{2,3}\\) ?)?\\d{4}\\-\\d{4}");
	}

	public boolean isValidDate(Object value) {
		return value.toString().matches(
				"(\\d{2}/\\d{2}/(\\d{2}|\\d{4}))|(\\d{4}\\-\\d\\d\\-\\d\\d)");
	}

	public boolean isValidNumber(Object value) {
		return value.toString().matches("(\\d+([\\.,]\\d+)?)");
	}

}
