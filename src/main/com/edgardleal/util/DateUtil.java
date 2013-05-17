package com.edgardleal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utilitario para datas
 * 
 * @author edgardr
 * 
 */
public class DateUtil {
	public static final String DATE_FORMAT = Config.DATE_FORMAT;

	public DateUtil() {
	}

	public static String format(Date date) {
		return new SimpleDateFormat(Config.DATE_FORMAT).format(date);
	}

	public static String format(Calendar date) {
		return new SimpleDateFormat(Config.DATE_FORMAT).format(date.getTime());
	}

	/**
	 * Retorna uma String do tipo data formatada de acordo com o formato
	 * especificado na class <code>Config</code><br>
	 * EX.: <code>result = DateUtil.format("2012-01-01");</code> <br>
	 * result = "01/01/2012"
	 * 
	 * @param date
	 * @return
	 */
	public static String format(String date) {
		return format(date, Config.DATE_FORMAT);
	}

	public static String format(final Calendar date, final String dateFormat) {
		return new SimpleDateFormat(dateFormat).format(date.getTime());
	}

	/**
	 * Retorna uma String do tipo data formatada de acordo com o formato
	 * informado em <code>dateFormat</code><br>
	 * EX.: <code>result = DateUtil.format("2012-01-01", "dd/MM/yyyy");</code> <br>
	 * result = "01/01/2012"
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String format(String date, String dateFormat) {
		try {
			if (date.matches("\\d{4}\\-\\d{2}\\-\\d{2}"))
				return new SimpleDateFormat(dateFormat)
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
			else
				return new SimpleDateFormat(dateFormat)
						.format(new SimpleDateFormat("dd/MM/yyyy").parse(date));

		} catch (ParseException e) {
			return Str.EMPTY;
		}
	}

	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static Date getDate(Calendar c) {
		return c.getTime();
	}

	public static Date getDate(String value) {
		try {
			if (value.matches("\\d\\d/\\d{2}/\\d{4}"))
				return new SimpleDateFormat("dd/MM/yyyy").parse(value);
			else
				return new SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 
	 * @param start
	 *            Primeira data e deve ser o valor menor
	 * @param end
	 *            segunda data e deve ser o valor maior
	 * @return retorna o n�mero de minutos entre as duas datas informadas
	 */
	public long minutesBetween(Date start, Date end) {
		return (end.getTime() - start.getTime()) / (1000L * 60L);
	}
	
	/**
	 * Retorna um número inteiro de 1 à 12 representando o mes atual
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	/**
	 * retorna um valor inteiro representando o ano da data atual do sistema
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}


	/**
	 * Retorna um objeto <code>Calendar</code> referente ao objeto
	 * <code>Date</code> informado
	 * 
	 * @param d
	 * @return
	 */
	public static Calendar getCalendar(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}

	/**
	 * Retorna um objeto <code>Calendar</code> com o valor da data setado <br>
	 * para o inicio do mes da data e ano informados
	 * 
	 * @param value
	 * @return
	 */
	public Calendar getStartOfMonth(final Calendar value) {
		Calendar result = Calendar.getInstance();
		result.setTime(value.getTime());
		result.set(Calendar.DAY_OF_MONTH, 1);
		return result;
	}

	/**
	 * Vide <code>public Calendar getStartOfMonth(final Calendar value)</code>
	 * 
	 * @param value
	 * @return
	 */
	public Date getStartOfMonth(final Date value) {
		Calendar _value = Calendar.getInstance();
		_value.setTime(value);
		return getStartOfMonth(_value).getTime();
	}

	/**
	 * 
	 * @param value
	 * @return Um objeto <code>Calendar</code> com o valor da data definido para
	 *         o final do mes informado
	 */
	public Calendar getEndOfMonth(final Calendar value) {
		Calendar result = Calendar.getInstance();
		result.setTime(value.getTime());
		result.add(Calendar.MONTH, 1);
		result = getStartOfMonth(result);// inicio do proximo mes
		result.add(Calendar.DAY_OF_MONTH, -1);
		return result;
	}
}
