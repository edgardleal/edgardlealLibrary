package test.edgardleal.util;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import com.edgardleal.annotation.HtmlVisibility;

public class TestHtmlVisibility {
	private Cliente cliente;

	@Before
	public void setUp() throws Exception {
		cliente = new Cliente();
	}

	@Test
	public void testShowInForm() {
		HtmlVisibility htmlVisibility;
		htmlVisibility = getField("codigo").getAnnotation(HtmlVisibility.class);
		assertTrue(!htmlVisibility.showInForm());
		htmlVisibility = getField("nome").getAnnotation(HtmlVisibility.class);
		assertTrue(htmlVisibility.showInForm());
	}

	@Test
	public void testShowInGrid() {
		HtmlVisibility htmlVisibility;
		htmlVisibility = getField("codigo").getAnnotation(HtmlVisibility.class);
		assertTrue(htmlVisibility.showInGrid());
		htmlVisibility = getField("nome").getAnnotation(HtmlVisibility.class);
		assertTrue(!htmlVisibility.showInGrid());
	}

	Field getField(String name) {
		try {
			return cliente.getClass().getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	class Cliente {
		@HtmlVisibility(showInForm = false, showInGrid = true)
		private int codigo;
		@HtmlVisibility(showInForm = true, showInGrid = false)
		private String nome;
	}
}
