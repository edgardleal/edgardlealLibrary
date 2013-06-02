package com.edgardleal.util.html;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;

import com.edgardleal.util.DateUtil;
import com.edgardleal.util.Str;

public class HtmlGenerator {
	private final String formLine = "<div class=\"formLine\">%s%s</div>";
	String deleteLine = "<div class='col'><img src='%s' onclick=\"return __delete('%s')\" /></div>",
			editLine = "<div class='col'><img src='%s' onclick=\"return __alter('%s')\"/></div>";

	private String deleteIcon = Str.EMPTY;
	private String editIcon = Str.EMPTY;
	private String pageNewRegister;

	public String getDeleteIcon() {
		return this.deleteIcon;
	}

	public void setDeleteIcon(String deleteIcon) {
		this.deleteIcon = deleteIcon;
	}

	public String getEditIcon() {
		return this.editIcon;
	}

	public void setEditIcon(String editIcon) {
		this.editIcon = editIcon;
	}

	public String getPageNewRegister() {
		return pageNewRegister;
	}

	/**
	 * Incida a p�gina para qual ser� redirecionado para adicionar um novo
	 * registro
	 * 
	 * @param pageNewRegister
	 */
	public void setPageNewRegister(String pageNewRegister) {
		this.pageNewRegister = pageNewRegister;
	}

	public String getForm(Object obj, String page)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return getForm(obj, page, "insert");
	}

	public String getForm(Object obj, String page, String action)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		action = action.equals("new") ? "insert" : "update";
		StringBuilder result = new StringBuilder();

		Field fields[] = obj.getClass().getDeclaredFields();

		result.append(String.format(
				"<form name='form1' action='%s%s' method='post'>", "", page));

		for (Field field : fields) {
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
	 * @return Retorna uma string contendo o c�digo html para incluir um label
	 *         no formulario
	 */
	public Object getLabel(Field field, Object obj) {
		return String.format("<label class='formLabel' for='%s'>%s</label>",
				field.getName(), field.getName().toUpperCase());
	}

	public Object getInput(Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String readonly = (field
				.isAnnotationPresent(javax.persistence.Id.class) || field
				.isAnnotationPresent(Transient.class)) ? "readonly" : Str.EMPTY;
		String fieldName = obj.getClass().getSimpleName().toLowerCase() + "."
				+ field.getName().toLowerCase();
		return String
				.format("<input type='text' id='%s' name='%s' class='formField' %s value=\"%s\"/>",
						fieldName, fieldName, readonly,
						BeanUtils.getProperty(obj, field.getName()).toString());
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
