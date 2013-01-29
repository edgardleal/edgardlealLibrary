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

import com.edgardleal.util.Config;
import com.edgardleal.util.DateUtil;
import com.edgardleal.util.NumberUtil;
import com.edgardleal.util.Str;
import com.edgardleal.util.Validator;

/**
 * 
 * @author Edgard Leal
 * @since 21/01/2013
 */
public class RefactorTool {
	private final Validator validator = new Validator();

	public RefactorTool() {

	}

	/**
	 * Os nomes e valores codificados para serem usados como par�metros de urls<br>
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
	 * Retorna o valor do campo informado, preparado para exibi��o.
	 * 
	 * @param obj
	 * @param field
	 * @return
	 */
	public String getValueToView(Object obj, Field field) {
		String result = Str.EMPTY;
		try {
			Object value = getFieldValue(field, obj);
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

	/**
	 * Indica se um campo � num�rico
	 * 
	 * @param f
	 * @return <code>true</code> quando o campo for
	 *         <code>int, flot, double , long</code>
	 */
	public boolean isNumberField(Field f) {
		@SuppressWarnings("rawtypes")
		Class _class = f.getType();
		return _class.equals(int.class) || _class.equals(float.class)
				|| _class.equals(double.class) || _class.equals(Integer.class)
				|| _class.equals(long.class) || _class.equals(Long.class)
				|| _class.equals(Float.class) || _class.equals(Double.class);
	}

	/**
	 * Indica se um campo � de algum tipo de n�mero de ponto flutuante<br>
	 * <code>float, double, Double, Float</code>
	 * 
	 * @param f
	 * @return
	 */
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

	/**
	 * Retorna o valor do campo indicado<br>
	 * OBS:. A classe deve conter um m�todo <code>get</code> correspondente<br>
	 * 
	 * @param field
	 * @param obj
	 * @return Um <code>Object</code> representando o valor do campo,<br>
	 *         e <code>null</code> em caso de erro
	 */
	public Object getFieldValue(String field, Object obj) {
		Method m;
		try {
			m = obj.getClass().getMethod("get" + Str.toPascalCase(field), null);
			return m.invoke(obj, new Object[0]);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @see {@link #getFieldValue(String, Object)}
	 * @param field
	 * @param obj
	 * @return
	 */
	public Object getFieldValue(Field field, Object obj) {
		return getFieldValue(field.getName(), obj);
	}

	public void setFieldValue(Object obj, Field field, String value)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Method method = null;
		Object _value = null;
		String methodName = "set" + Str.toPascalCase(field.getName());
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
			String fieldName = field.getName().toLowerCase();
			String className = o.getClass().getSimpleName().toLowerCase();
			String fieldValue = Str.ifNullOrEmpty(
					request.getParameter(className + "." + fieldName), "0");
			try {
				setFieldValue(o, field, fieldValue);
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
					"get" + Str.toPascalCase(className),
					new Class[] { fieldName.getClass() });
			Object fieldValue = tempMethod.invoke(rs, fieldName);
			method.invoke(o, fieldValue);
		}
	}
}