package test.edgardleal.util;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import com.edgardleal.util.RefactorTool;

public class TestModelAnalyser {
	Cliente cliente = null;

	@Before
	public void setUp() throws Exception {
		cliente = new Cliente(1, "Maria", "8854-8745");
	}

	@Test
	public void testGetFieldParametersObjectBoolean() {
		assertTrue(true);
	}

	@Test
	public void testGetValueToView() {
		assertTrue(true);
	}

	@Test
	public void testIsNumberField() {
		try {
			assertTrue(new RefactorTool().isNumberField(cliente.getClass()
					.getDeclaredField("codigo")));
			assertTrue(!new RefactorTool().isNumberField(cliente.getClass()
					.getDeclaredField("nome")));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsFloatField() {
		try {
			assertTrue(!new RefactorTool().isNumberField(cliente.getClass()
					.getDeclaredField("codigo")));
			assertTrue(!new RefactorTool().isNumberField(cliente.getClass()
					.getDeclaredField("nome")));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetValueByClassToView() {
		try {
			Field field = cliente.getClass().getDeclaredField("codigo");
			assertTrue(new RefactorTool().getValueToView(cliente, field)
					.equals(new Integer(1)));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
}

class Cliente {
	private int codigo;
	private String nome;
	private String telefone;

	public Cliente() {

	}

	public Cliente(int codigo, String nome, String telefone) {
		setNome(nome);
		setCodigo(codigo);
		setTelefone(telefone);

	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}