package test.edgardleal.util;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.edgardleal.util.DateUtil;

public class DateUtilTest {

	@Test
	public void testFormatDate() {
		assertTrue(DateUtil.format(new Date(0l)).equals("31/12/1969"));
	}

	@Test
	public void testFormatCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(0l));
	}

	@Test
	public void testFormatString() {
		assertTrue(DateUtil.format("31/12/1969").equals("31/12/1969"));
		assertTrue(DateUtil.format("1969-12-31").equals("31/12/1969"));
	}

	@Test
	public void testFormatDateString() {
		assertTrue(DateUtil.format(new Date(0l), "dd/MM/yyyy").equals(
				"31/12/1969"));
		assertTrue(DateUtil.format(new Date(0l), "yyyy-MM-dd").equals(
				"1969-12-31"));
		assertTrue(DateUtil.format(new Date(0l), "dd/MM/yy").equals("31/12/69"));
	}

	@Test
	public void testGetDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(0l));
		assertTrue(DateUtil.format(DateUtil.getDate(c), "dd/MM/yy").equals(
				"31/12/69"));
	}

	@Test
	public void testGetCalendar() {
		assertTrue(DateUtil.format(DateUtil.getCalendar(new Date(0l))).equals(
				"31/12/1969"));
	}

	@Test
	public void testMinutesBetween() {
		try {
			Date start = new SimpleDateFormat("dd/MM/yy HH:mm:ss")
					.parse("01/01/12 12:00:00");
			Date end = new SimpleDateFormat("dd/MM/yy HH:mm:ss")
					.parse("01/01/12 12:15:00");
			long milesInADay = 1000L * 60L;
			double result = (end.getTime() - start.getTime()) / milesInADay;
			System.out.println(result);
			long valor = new DateUtil().minutesBetween(start, end);
			assertTrue("valor diferente de 15. valor = " + valor, valor == 15);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testDateSubtract() {
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {

			Date start = formater.parse("01/01/2012 12:00:00");
			Date end = formater.parse("01/01/2012 12:08:00");
			Date result = new Date(end.getTime() - start.getTime() + 60000L
					* 60L * 3L);
			System.out.println("Data inicial : "
					+ formater.format(new Date(0L)));
			System.out.println("Resultado de 12:08:00 - 12:00:00 : "
					+ formater.format(result));
			assertTrue(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void testEndOfMonth() {
		Calendar start = Calendar.getInstance();

		start.setTime(DateUtil.getDate("01/01/2012"));
		Calendar result = new DateUtil().getEndOfMonth(start);
		String formatedResult = DateUtil.format(result, "dd/MM/yyyy");
		assertTrue("Data inesperada: " + formatedResult,
				formatedResult.equals("31/01/2012"));
	}

	@Test
	public void testStartOfMonth() {
		Calendar start = Calendar.getInstance();

		start.setTime(DateUtil.getDate("15/01/2012"));
		Calendar result = new DateUtil().getStartOfMonth(start);
		String formatedResult = DateUtil.format(result, "dd/MM/yyyy");
		assertTrue("Data inesperada: " + formatedResult,
				formatedResult.equals("01/01/2012"));
	}

}
