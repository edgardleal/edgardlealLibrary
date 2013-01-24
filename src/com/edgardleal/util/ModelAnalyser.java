package com.edgardleal.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Date;

import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.servlet.ServletRequest;

import org.apache.catalina.util.URLEncoder;
import org.apache.log4j.Logger;

/**
 * 
 * @author edgardr
 * 
 */
public class ModelAnalyser {
	private final Validator validator = new Validator();

	public ModelAnalyser() {

	}

	/**
	 * Os nomes e valores codificados para serem usados como parâmetros de urls<br>
	 * EX.:<br>
	 * nome=Teste&telefone=12345678
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public String getFieldParameters(Object o, boolean enconding)
			throws Exception {
		StringBuilder result = new StringBuilder();
		URLEncoder encoder = new URLEncoder();

		Field[] fields = o.getClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getClass().isAnnotationPresent(Transient.class)
					|| field.getClass().isAnnotationPresent(OneToMany.class))
				continue;
			String name = field.getName();

			String value = Str.emptyIfNull(getValueToView(o, field));

			result.append(String.format("&%s=%s", name,
					(enconding ? encoder.encode(value) : value)));
		}
		if (result.length() > 0)
			return result.toString().substring(1);
		else
			return Str.EMPTY;
	}

	/**
	 * Retorna o valor de um objeto se este conter um método get correspondente.<br>
	 * EX.: <code>Object value = getValue(obj, field);</code><br>
	 * para que funcione, casoo nome do <code>field</code> seja
	 * <code>nome</code>, devera existir um método <br>
	 * <code>public <?> getNome()</code>
	 * 
	 * @param o
	 * @param field
	 * @param name
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private Object getValue(Object o, Field field)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Method m = o.getClass().getMethod(
				"get" + toPascalCase(field.getName()), null);
		return m.invoke(o, new Object[0]);
	}

	public String getValueToView(Object obj, Field field) {
		String result = Str.EMPTY;
		try {
			Object value = getValue(obj, field);
			if (value == null)
				return result;
			result = value.toString();
			if (field.getType().equals(java.util.Date.class)
					|| field.getType().equals(java.sql.Date.class))
				result = DateUtil.format((Date) value);

			else if (isFloatField(field)) {
				if (validator.isValidNumber(result))
					return NumberUtil.format(result);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	public boolean isNumberField(Field f) {
		@SuppressWarnings("rawtypes")
		Class _class = f.getType();
		return _class.equals(int.class) || _class.equals(float.class)
				|| _class.equals(double.class) || _class.equals(Integer.class)
				|| _class.equals(Float.class) || _class.equals(Double.class);
	}

	public boolean isFloatField(Field f) {
		@SuppressWarnings("rawtypes")
		Class _class = f.getType();
		return _class.equals(float.class) || _class.equals(double.class)
				|| _class.equals(Float.class) || _class.equals(Double.class);
	}

	public Object getValueByClassToView(Object value, Class<?> _class,
			String dateFormat) {
		if (_class.equals(Integer.class)) {
			return Integer.valueOf(value.toString());
		} else if (_class.equals(Date.class)
				|| _class.equals(java.sql.Date.class)) {
			try {
				if (validator.isValidDate(value))
					return DateUtil.format(value.toString(), dateFormat);
				else
					return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (_class.equals(Double.class)) {
			return Double.valueOf(value.toString());
		} else if (_class.equals(Float.class)) {
			return Float.valueOf(value.toString());
		}
		return value;
	}

	public String getFieldParameters(Object o, ResultSet rs) throws Exception {
		return getFieldParameters(o, rs, true);
	}

	public String getFieldParameters(Object o, ResultSet rs, boolean enconding)
			throws Exception {
		assignResultSet(o, rs);
		return getFieldParameters(o, enconding);
	}

	private String toPascalCase(final String value) {
		return value.substring(0, 1).toUpperCase()
				+ value.substring(1).toLowerCase();
	}

	/**
	 * verifica se o nome da classe esta correto ( remove espaços indevidos ) <br>
	 * e faz a transposição para a classe de encapsulamento ( wrapper)<br>
	 * int = Integer boolean = Boolean double = Double float = Float
	 * 
	 * @return
	 */
	private String verifyClassName(String value) {
		value = value.trim().replaceAll(".* (.*)", "$1");
		switch (value) {
		case "int":
			return "java.lang.Integer";
		case "boolean":
			return "java.lang.Boolean";
		case "float":
			return "java.lang.Float";
		case "double":
			return "java.lang.Double";
		}
		return value;
	}

	public Object getObjectValue(Field field, Object obj) {
		Method m;
		try {
			m = obj.getClass().getMethod("get" + toPascalCase(field.getName()),
					null);
			return m.invoke(obj, new Object[0]);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			e.printStackTrace();
			return null;
		}
	}

	public void setObjectValue(Object obj, Field field, String value)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Method method = null;
		Object _value = null;
		String methodName = "set" + toPascalCase(field.getName());
		if (field.getType().equals(int.class)) {
			method = obj.getClass().getMethod(methodName, int.class);
			_value = Integer.valueOf(value);
		} else if (field.getType().equals(java.util.Date.class)) {
			method = obj.getClass().getMethod(methodName, java.util.Date.class);
			_value = DateUtil.getDate(value);
		} else if (field.getType().equals(String.class)) {
			method = obj.getClass().getMethod(methodName, String.class);
			_value = value;
		} else if (field.getType().equals(float.class)) {
			method = obj.getClass().getMethod(methodName, float.class);
			if (validator.isValidNumber(value))
				_value = Float.valueOf(value.replace(",", "."));
			else
				_value = new Float(0);

		} else if (field.getType().equals(double.class)) {
			method = obj.getClass().getMethod(methodName, double.class);
			if (validator.isValidNumber(value))
				_value = Double.valueOf(value);
			else
				_value = new Double(0);

		}

		method.invoke(obj, _value);
	}

	public void assignRequest(Object o, ServletRequest request)
			throws Exception {

		Field fields[] = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			String className = verifyClassName(field.getType().toString());

			String fieldValue = Str.ifNullOrEmpty(
					request.getParameter(fieldName), "0");
			Method method = o.getClass().getMethod(
					"set" + toPascalCase(fieldName), field.getType());
			try {
				setObjectValue(o, field, fieldValue);
			} catch (Exception ex) {
				Logger.getLogger(this.getClass()).error(ex.getMessage(), ex);
				throw ex;
			}
		}
	}

	protected Object getValueByClass(Object value, Class<?> _class) {
		return getValueByClass(value, _class, Config.DATE_FORMAT);
	}

	protected Object getValueByClass(Object value, Class<?> _class,
			String dateFormat) {
		if (_class.equals(Integer.class)) {
			return Integer.valueOf(value.toString());
		} else if (_class.equals(Date.class)
				|| _class.equals(java.sql.Date.class)) {
			try {
				if (validator.isValidDate(value))
					return DateUtil.getDate(value.toString());
				else
					return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (_class.equals(Double.class)) {
			return Double.valueOf(value.toString());
		} else if (_class.equals(Float.class)) {
			return Float.valueOf(value.toString().replaceAll(",", "."));
		}
		return value;
	}

	public void assignResultSet(Object o, ResultSet rs) throws Exception {

		Method methods[] = o.getClass().getMethods();
		for (Method method : methods) {
			if (!method.getName().substring(0, 3).equals("set"))
				continue;
			String fieldName = method.getName().substring(3);
			String className = method.getParameterTypes()[0].toString()
					.replaceAll("(.*\\.)(\\w+)", "$2");
			Method tempMethod = rs.getClass().getMethod(
					"get" + toPascalCase(className),
					new Class[] { fieldName.getClass() });
			Object fieldValue = tempMethod.invoke(rs, fieldName);
			method.invoke(o, fieldValue);
		}
	}
}
