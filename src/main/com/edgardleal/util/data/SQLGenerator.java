package com.edgardleal.util.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;

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
		Object result = BeanUtils.getProperty(obj, f.getName());
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

	public String getUpdate(Object obj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		Class<? extends Object> classe = obj.getClass();
		StringBuilder result = new StringBuilder();
		StringBuilder condition = new StringBuilder();
		String tableName = Str.EMPTY;

		tableName = getTableName(classe);

		result.append(String.format("UPDATE %s SET ", tableName));
		boolean started = false;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				condition
						.append((Str.isNullOrEmpty(condition) ? Str.EMPTY
								: " AND ")).append(field.getName())
						.append(equalString)
						.append(getMethodValueToSQL(field, obj));
			} else {
				result.append((started ? "," : Str.EMPTY))
						.append(field.getName()).append(equalString)
						.append(getMethodValueToSQL(field, obj));
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
				fieldList.append((started ? COMMA : Str.EMPTY)).append(
						field.getName());
				try {
					valueList.append((started ? COMMA : Str.EMPTY)).append(
							getMethodValueToSQL(field, obj));
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
					condition.append(String.format("%s%s%s%s",
							(started ? " AND " : Str.EMPTY), field.getName(),
							equalString, getMethodValueToSQL(field, obj)));
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
