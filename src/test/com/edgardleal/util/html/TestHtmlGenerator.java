package test.edgardleal.util.html;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import test.edgardleal.util.GuineaPigModel;

import com.edgardleal.util.html.HtmlGenerator;

public class TestHtmlGenerator {
	private HtmlGenerator htmlGenerator = null;
	private GuineaPigModel model = null;

	@Before
	public void setUp() throws Exception {
		htmlGenerator = new HtmlGenerator();
		model = new GuineaPigModel();
	}

	@Test
	public void testGetInput() {
		try {
			String name = "guineapigmodel.code";
			String htmlInput = htmlGenerator.getInput(
					model.getClass().getDeclaredField("code"), model)
					.toString();
			assertEquals(
					String.format(
							"<input type='text' id='%s' name='%s' class='formField' readonly value=\"0\"/>",
							name, name), htmlInput);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

}
