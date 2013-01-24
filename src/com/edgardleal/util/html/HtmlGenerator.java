package com.edgardleal.util.html;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.edgardleal.annotation.HtmlVisibility;
import com.edgardleal.data.Accessibility;
import com.edgardleal.data.ForeignKey;
import com.edgardleal.util.DateUtil;
import com.edgardleal.util.ModelAnalyser;
import com.edgardleal.util.Str;

public class HtmlGenerator {
	private final ModelAnalyser modelAnalyser = new ModelAnalyser();
	private final String formLine = "<div class=\"formLine\">%s%s</div>";
	String deleteLine = "<div class='col'><img src='/FinanceManager/img/erase.png' onclick=\"return __delete('%s')\" /></div>",
			editLine = "<div class='col'><img src='/FinanceManager/img/edit.png' onclick=\"return __alter('%s')\"/></div>";

	private String pageNewRegister;

	public String getPageNewRegister() {
		return pageNewRegister;
	}

	/**
	 * Incida a página para qual será redirecionado para adicionar um novo
	 * registro
	 * 
	 * @param pageNewRegister
	 */
	public void setPageNewRegister(String pageNewRegister) {
		this.pageNewRegister = pageNewRegister;
	}

	public String getForm(Object obj, String page)
			throws IllegalArgumentException, IllegalAccessException {
		return getForm(obj, page, "insert");
	}

	public String getForm(Object obj, String page, String action)
			throws IllegalArgumentException, IllegalAccessException {

		action = action.equals("new") ? "insert" : "update";
		StringBuilder result = new StringBuilder();

		Field fields[] = obj.getClass().getDeclaredFields();

		result.append(String.format(
				"<form name='form1' action='%s%s' method='post'>", "", page));

		for (Field field : fields) {
			if (!isVisibleToForm(field))
				continue;
			if (!field.isAnnotationPresent(Accessibility.class)
					|| (field.isAnnotationPresent(Accessibility.class) && field
							.getAnnotation(Accessibility.class).Visible()))
				if (field.isAnnotationPresent(ForeignKey.class)) {
					result.append(String.format(formLine, getLabel(field, obj),
							getInput(field, obj)));
				} else
					result.append(String.format(formLine, getLabel(field, obj),
							getInput(field, obj)));
		}
		result.append(String
				.format("<div class=\"formLine\"><input type=\"submit\" value=\"Salvar\" style=\"height: 24px\" class=\"linkButton\" /> "
						+ "<a class=\"linkButton\" href=\"%s%s\">Voltar</a></div>"
						+ "</fieldset><input type=\"hidden\" name=\"action\" value=\"%s\" /> <input type=\"hidden\" name=\"type\" value=\"1\" />",
						Str.EMPTY, page, action));
		result.append("</form>");
		return result.toString();
	}

	/**
	 * 
	 * @param field
	 * @param obj
	 * @return Retorna uma string contendo o código html para incluir um label
	 *         no formulario
	 */
	public Object getLabel(Field field, Object obj) {
		return String.format("<label class='formLabel' for='%s'>%s</label>",
				field.getName(), field.getName().toUpperCase());
	}

	public Object getInput(Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		String readonly = (field.isAnnotationPresent(Id.class) || field
				.isAnnotationPresent(Transient.class)) ? "readonly" : Str.EMPTY;
		String fieldName = obj.getClass().getSimpleName().toLowerCase() + "."
				+ field.getName().toLowerCase();
		return String
				.format("<input type='text' id='%s' name='%s' class='formField' %s value=\"%s\"/>",
						fieldName, fieldName, readonly,
						modelAnalyser.getValueToView(obj, field));
	}

	public String getGrid(Object obj, ResultSet rs, String page)
			throws Exception {
		String _page = page;
		HTMLStringBuffer result = new HTMLStringBuffer();

		result.append("<script type='text/javascript'>");

		result.append("function __delete(code){\n\tif(! confirm(\"Deseja realmente excluir este registro?\")) return false;\n\t");
		result.append(String
				.format("location.assign('%s%s?type=0&' + code + '&action=remove');}\n\n",
						Str.EMPTY, page));

		result.append("function __alter(code){\n\t");
		result.append(String
				.format("location.assign('%s%s?type=0&' + code + '&action=alter');}\n\n",
						Str.EMPTY, page));

		result.elementEnd("script");

		result.append(getButtonNew());
		result.append(String
				.format("<a class=\"linkButton\" onClick='var value = prompt(\"Digite o valor a ser localizado:\"); if(value) location.assign(\"%s%s?action=find&term=\" + value); return false;' href=\"#\">Pesquisar</a></div>",
						Str.EMPTY, page));

		ModelAnalyser analyser = new ModelAnalyser();
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		result.append("<div id=\"dataGrid\"><div class='linha' id='gridHeader' style='background:#E8E8E8'>");
		result.append("<div class='col' style='50px'></div>");
		result.append("<div class='col' style='50px'></div>");

		for (int i = 1; i <= columnCount; i++)
			result.append(String
					.format("<div class='col' style='width:%s' ><a href='%s?order=%s'>%s</a></div>",
							i == 1 ? "50px" : "100px", _page,
							meta.getColumnLabel(i), meta.getColumnLabel(i)));
		result.elementEnd("div");
		int j = 0;
		while (rs.next()) {
			result.append(String.format(
					"<a href='#' class='linha linha%s linkLine'>",
					j++ % 2 == 0 ? "Par" : "Impar"));
			result.append(String.format(editLine,
					analyser.getFieldParameters(obj, rs, true)));
			result.append(String.format(deleteLine,
					analyser.getFieldParameters(obj, rs)));
			for (int i = 1; i <= columnCount; i++)
				result.append(String.format(
						"<div class='col' style='width:%s'>%s</div>",
						i == 1 ? "50px" : "100px",
						getFormatedValue(rs.getString(i),
								meta.getColumnClassName(i))));
			result.append("</a>");
		}
		return result.toString() + "</div>";
	}

	public String getGrid(List<? extends Object> items, String page)
			throws Exception {
		if (items.size() == 0)
			return Str.EMPTY;
		String _page = page;
		HTMLStringBuffer result = new HTMLStringBuffer();

		result.append("<script type='text/javascript'>");

		result.append("function __delete(code){\n\tif(! confirm(\"Deseja realmente excluir este registro?\")) return false;\n\t");
		result.append(String
				.format("location.assign('%s%s?type=0&' + code + '&action=remove');}\n\n",
						Str.EMPTY, page));

		result.append("function __alter(code){\n\t");
		result.append(String
				.format("location.assign('%s%s?type=0&' + code + '&action=alter');}\n\n",
						Str.EMPTY, page));

		result.elementEnd("script");
		result.append(getButtonNew());
		result.append(String
				.format("<a class=\"linkButton\" onClick='var value = prompt(\"Digite o valor a ser localizado:\"); if(value) location.assign(\"%s%s?action=find&term=\" + value); return false;' href=\"#\">Pesquisar</a></div>",
						Str.EMPTY, page));

		ModelAnalyser analyser = new ModelAnalyser();
		result.append("<div id=\"dataGrid\"><div class='linha' id='gridHeader' style='background:#E8E8E8'>");
		result.append("<div class='col' style='50px'></div>");
		result.append("<div class='col' style='50px'></div>");

		Field fields[] = items.get(0).getClass().getDeclaredFields();
		int i = 0;
		for (Field f : fields)
			if (isVisibleToGrid(f))
				result.append(String
						.format("<div class='col' style='width:%s' ><a href='%s?order=%s'>%s</a></div>",
								++i == 1 ? "50px" : "100px", _page, f.getName()
										.toUpperCase(), f.getName()
										.toUpperCase()));

		result.append("</div>");

		int j = 00000000000000000000000000;
		for (Object obj : items) {
			result.append(String.format(
					"<a href='#' class='linha linha%s linkLine'>",
					j++ % 2 == 0 ? "Par" : "Impar"));
			result.append(String.format(editLine,
					analyser.getFieldParameters(obj, true)));

			result.append(String.format(deleteLine,
					analyser.getFieldParameters(obj, true)));

			for (Field f : fields) {
				if (!isVisibleToGrid(f))
					continue;
				Object _value = modelAnalyser.getValueByClassToView(
						modelAnalyser.getObjectValue(f, obj), f.getClass(),
						"dd/MM/yy");
				result.append(String.format(
						"<div class='col' style='width:%s'>%s</div>",
						i == 1 ? "50px" : "100px", _value == null ? Str.EMPTY
								: _value.toString()));
			}
			result.append("</a>");
		}
		return result.toString() + "</div>";
	}

	/**
	 * @param f
	 * @return
	 */
	private boolean isVisibleToForm(Field f) {
		if (f.isAnnotationPresent(HtmlVisibility.class)) {
			HtmlVisibility annotation = f.getAnnotation(HtmlVisibility.class);
			return annotation.showInForm();
		} else
			return true;
	}

	/**
	 * Verifica se um campos será exibido em um grid<br>
	 * Caso haja a anotação HtmlVisibility present, será considerado apenas o
	 * valor do campo <br>
	 * <code>showInGrid()</code> caso não exista, o campo será exibido se não
	 * houver a anotação <br>
	 * <code>Transient</code> de javax.persist ou a <code>OneToMany</code>
	 * 
	 * @param f
	 * @return
	 */
	private boolean isVisibleToGrid(Field f) {
		if (f.isAnnotationPresent(HtmlVisibility.class)) {
			HtmlVisibility htmlVisibility = f.getClass().getAnnotation(
					HtmlVisibility.class);
			return htmlVisibility != null && htmlVisibility.showInGrid();
		} else
			return !(f.isAnnotationPresent(Transient.class) || f
					.isAnnotationPresent(OneToMany.class));
	}

	/**
	 * @param page
	 * @return
	 */
	private String getButtonNew() {
		return String
				.format("<div style=\"height: 35px\"><a class=\"linkButton\" href=\"%s?action=new\">Novo</a>",
						getPageNewRegister());
	}

	private String getFormatedValue(Object value, String className) {
		switch (className) {
		case "java.sql.Date":
			return DateUtil.format(Str.emptyIfNull(value));
		}
		return Str.emptyIfNull(value);
	}

	public String getSimpleGrid(ResultSet rs, String _page) {
		StringBuilder result = new StringBuilder();
		ResultSetMetaData meta = null;
		try {
			meta = rs.getMetaData();

			int columnCount = meta.getColumnCount();
			result.append("<div id=\"dataGrid\"><div class='linha' id='gridHeader' style='background:#E8E8E8'>");
			for (int i = 1; i <= columnCount; i++)
				result.append(String
						.format("<div class='col' style='width:%s' ><a href='%s?order=%s'>%s</a></div>",
								i == 1 ? "50px" : "100px", _page,
								meta.getColumnLabel(i), meta.getColumnLabel(i)));
			result.append("</div>");
			int j = 0;
			while (rs.next()) {
				result.append(String.format(
						"<a href='#' class='linha linha%s linkLine'>",
						j++ % 2 == 0 ? "Par" : "Impar"));

				for (int i = 1; i <= columnCount; i++)
					result.append(String.format(
							"<div class='col' style='width:%s'>%s</div>",
							i == 1 ? "50px" : "100px",
							getFormatedValue(rs.getString(i),
									meta.getColumnClassName(i))));
				result.append("</a>");
			}
			return result.toString() + "</div>";
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
