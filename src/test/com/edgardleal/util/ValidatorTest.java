package test.edgardleal.util;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.edgardleal.util.DateUtil;
import com.edgardleal.util.Validator;

public class ValidatorTest {

	@Test
	public void TestIsvalidNumber() {
		Validator validator = new Validator();
		assertTrue(validator.isValidNumber("1"));
		assertTrue(validator.isValidNumber("5.5"));
		assertTrue(!validator.isValidNumber(" 1"));
		assertTrue(!validator.isValidNumber("1 "));
		assertTrue(validator.isValidNumber("54545.5687"));
		assertTrue(!validator.isValidNumber("1 1"));
		assertTrue(validator.isValidNumber("11"));
		assertTrue(validator.isValidNumber("9"));
		assertTrue(!validator.isValidNumber("nove"));
	}

	@Test
	public void TestIsValidDate() {
		Validator validator = new Validator();
		assertTrue(validator.isValidDate("11/11/2012"));
		assertTrue(!validator.isValidDate("1/1/2012"));
		assertTrue(!validator.isValidDate("111/11/2012"));
		assertTrue(!validator.isValidDate("11/111/2012"));
		assertTrue(validator.isValidDate("11/11/2012"));
		assertTrue(!validator.isValidDate("11/11/12012"));
		assertTrue(!validator.isValidDate("11/11/212"));
		assertTrue(validator.isValidDate("11/11/22"));
		assertTrue(validator.isValidDate("11/11/2012"));
		assertTrue(!validator.isValidDate("11-11-2012"));
		assertTrue(validator.isValidDate("2012-11-01"));
		assertTrue(!validator.isValidDate("2011-111-11"));
		assertTrue(!validator.isValidDate("201-11-11"));
		assertTrue(!validator.isValidDate("2000-11-111"));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(0L));
		for (int i = 0; i < 50000; i++) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			assertTrue(validator.isValidDate(DateUtil.format(calendar)));
		}

	}
}
