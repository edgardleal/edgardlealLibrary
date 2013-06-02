package com.edgardleal.util.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.edgardleal.util.Str;
import com.edgardleal.util.Validator;

public class SQLGenerator {
	private final String WHERE = "WHERE";
	final String equalString = " = ", QUOTED = "'%s'";
	final String quotedEqualString = equalString + QUOTED + Str.SPACE;
	final String COMMA = ",", NULL = "null";

	public SQLGenerator() {

	}

	public Object getMethodValue(Method m, Object obj)
			throws InvocationTargetException, IllegalAccessException {
		return m.invoke(obj, new Object[0]);
	}

	/**
	 * Este metodo verifica se o valor e nulo para que nao seja utilizado <br>
	 * os apostofros ('null')
	 */
	public Object getMethodValueToSQL(Field f, Object obj)
			throws InvocationTargetException, IllegalAccessException,
			NoSuchMethodException {
		Object result = getMethodValue(f, obj);
		if (result.toString().equals(NULL)
				|| (isFieldNumber(f) && new Validator().isValidNumber(result)))
			return result;
		else
			return String.format(QUOTED, result.toString());
	}

	private boolean isFieldNumber(Field f) {
		@SuppressWarnings("rawtypes")
		Class _class = f.getType();
		return _class.equals(int.class) || _class.equals(float.class)
				|| _class.equals(double.class) || _class.equals(Integer.class)
				|| _class.equals(Float.class) || _class.equals(Double.class);
	}

	public Object getMethodValue(Field f, Object obj)
			throws InvocationTargetException, IllegalAccessException,
			NoSuchMethodException {
		Method m = obj.getClass().getMethod(
				"get" + f.getName().substring(0, 1).toUpperCase()
						+ f.getName().substring(1).toLowerCase(), new Class[0]);
		Object value = getMethodValue(m, obj);
		if (f.getType().equals(Date.class)) {
			if (value != null)
				return new SimpleDateFormat("yyyy-MM-dd")
						.format((Date) getMethodValue(m, obj));
			else
				return NULL;
		} else
			return value;
	}

	public String getUpdate(Object obj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		Class<? extends Object> classe = obj.getClass();
		StringBuilder result = new StringBuilder();
		String condition = Str.EMPTY;
		String tableName = Str.EMPTY;

		tableName = getTableName(classe);

		result.append(String.format("UPDATE %s SET ", tableName));
		boolean started = false;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				condition += (Str.isNullOrEmpty(condition) ? Str.EMPTY
						: " AND ")
						+ field.getName()
						+ equalString
						+ getMethodValueToSQL(field, obj);
			} else {
				result.append((started ? "," : Str.EMPTY) + field.getName()
						+ equalString + getMethodValueToSQL(field, obj));
				started = true;
			}
		}
		result.append(WHERE + Str.SPACE + condition);
		return result.toString();
	}

	private String getTableName(Class<? extends Object> classe) {
		String tableName = Str.EMPTY, schemaName = Str.EMPTY;
		if (classe.isAnnotationPresent(Table.class)) {
			tableName = classe.getAnnotation(Table.class).name();
			schemaName = classe.getAnnotation(Table.class).schema();
		} else
			tableName = classe.getSimpleName();

		tableName = schemaName.equals(Str.EMPTY) ? tableName : String.format(
				"%s.%s", schemaName, tableName);
		return tableName;
	}

	public String getInsertCommand(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		StringBuilder fieldList = new StringBuilder();
		StringBuilder valueList = new StringBuilder();

		boolean started = false;
		for (Field field : fields) {
			if (!field.isAnnotationPresent(GeneratedValue.class)) {
				fieldList.append((started ? COMMA : Str.EMPTY)
						+ field.getName());
				try {
					valueList.append((started ? COMMA : Str.EMPTY)
							+ getMethodValueToSQL(field, obj));
				} catch (InvocationTargetException | IllegalAccessException
						| NoSuchMethodException e) {
					e.printStackTrace();
				}
				started = true;
			}
		}

		return String.format("INSERT INTO %s (%s) VALUES(%s)",
				getTableName(obj.getClass()), fieldList.toString(),
				valueList.toString());
	}

	public String getDeleteCommand(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		StringBuilder condition = new StringBuilder();

		boolean started = false;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				try {
					condition.append((started ? " AND " : Str.EMPTY)
							+ field.getName() + equalString
							+ getMethodValueToSQL(field, obj));
				} catch (InvocationTargetException | IllegalAccessException
						| NoSuchMethodException e) {
					e.printStackTrace();
				}
				started = true;
			}
		}

		return String.format("DELETE FROM %s WHERE %s",
				getTableName(obj.getClass()), condition.toString());
	}
}
