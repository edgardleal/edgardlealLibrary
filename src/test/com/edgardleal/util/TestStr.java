package test.edgardleal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.edgardleal.util.Str;

public class TestStr {

	@Test
	public void testIsNullOrEmpty() {
		String nullValue = null;
		assertTrue(Str.isNullOrEmpty(null));
		assertTrue(Str.isNullOrEmpty(Str.EMPTY));
		assertTrue(!Str.isNullOrEmpty("  "));
		assertTrue(Str.isNullOrEmpty(nullValue));
	}

	@Test
	public void testIsNotBlank() {
		assertTrue(!Str.isNotBlank(" "));
		assertTrue(!Str.isNotBlank(""));
		assertTrue(!Str.isNotBlank("\n"));
		assertTrue(!Str.isNotBlank("\t"));
		assertTrue(!Str.isNotBlank("   "));
		assertTrue(Str.isNotBlank(" n  "));
	}

	@Test
	public void testCamelCase() {
		assertEquals(Str.toCamelCase("CasaGrande"), "casaGrande");
		assertEquals(Str.toCamelCase("CAsaGrande"), "cAsaGrande");
		assertEquals(Str.toCamelCase("JFrame"), "jFrame");
	}

	@Test
	public void testPascalCase() {
		assertEquals(Str.toPascalCase("casaGrande"), "CasaGrande");
		assertEquals(Str.toPascalCase("casaGrande"), "CasaGrande");
		assertEquals(Str.toPascalCase("meta"), "Meta");
	}

}
