package test.edgardleal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.interceptor.DefaultTypeNameExtractor;

public class TestDefaultNameExtractor {

	private DefaultTypeNameExtractor extractor;

	@Before
	public void setUp() throws Exception {
		extractor = new DefaultTypeNameExtractor();
	}

	@Test
	public void testOneWord() {
		assertEquals("string", extractor.nameFor(String.class));
	}

	@Test
	public void testTwoWord() {
		assertEquals("registroUsuario",
				extractor.nameFor(RegistroUsuario.class));
	}

	@Test
	public void testTreeWord() {
		assertEquals("registroDeUsuario",
				extractor.nameFor(RegistroDeUsuario.class));
	}

	@Test
	public void testWord() {
		assertEquals("rdptadController",
				extractor.nameFor(RDPTADController.class));
	}

	class RDPTADController {
	}
	class RegistroUsuario {
	}

	class RegistroDeUsuario {
	}
}
