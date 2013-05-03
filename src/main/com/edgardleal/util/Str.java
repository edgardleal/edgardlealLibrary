package com.edgardleal.util;

/**
 * Classe de utilit�rios para trabalho com Strings.
 * 
 * @author Edgard Leal
 * @since 09-01-2013
 */
public class Str {
	/**
	 * Contem uma string vazia "", deve ser utilizado para diminur o tamanho do <br>
	 * pool de Strings da jvm
	 */
	public static final String EMPTY = "";
	/**
	 * Um espaço em branco.
	 */
	public static final String SPACE = " ";

	/***
	 * Retorna as <code>String</code> informada sem os espaços a esquerda <br>
	 * a direita e sem os caracteres ENTER e TAB
	 *
	 * @param value
	 * @return
	 */
	public static String clearSpaces(String value) {
		value = value.trim().replaceAll(" {2,20}", Str.SPACE)
				.replaceAll("[\n\t]+", Str.EMPTY);
		return value;
	}

	public static String concat(String... args) {
		StringBuilder builder = new StringBuilder();
		for (String string : args)
			builder.append(string);
		return builder.toString();
	}

	/**
	 * Retorna uma String vazia caso o valor informado seja nul<br>
	 * caso não seja nulo, retorna o mesmo valor informado.
	 *
	 * @param value
	 * @return
	 */
	public static String emptyIfNull(Object value) {
		return value == null ? Str.EMPTY : value.toString();
	}

	/**
	 * retorna o "result" informado caso o valor informado seja nulo ou vazio<br>
	 * <br>
	 * EX:<br>
	 * String vazio = ""; <br>
	 * String _null = null;<br>
	 * String valor = "A"<br>
	 *
	 * ifNullOrEmpty(vazio, valor) // retorna "A" <br>
	 * ifNullOrEmpty(_null, valor) // retorna "A" <br>
	 * ifNullOrEmpty("B", valor) // retorna "B" <br>
	 *
	 * @param value
	 *            : valor analizado
	 * @param result
	 *            : resultado caso seja nulo ou empty
	 * @return String com o melhor valor
	 */
	public static String ifNullOrEmpty(String value, String result) {
		return value == null || value.equals(Str.EMPTY) ? result : value;
	}

	/**
	 * Indica se o valor informado não esta em branco
	 *
	 * @param value
	 * @return
	 */
	public static boolean isNotBlank(final String value) {
		return !value.trim().equals(Str.EMPTY);
	}

	/**
	 * retorna o valor informado em "ifNull" caso o valor de "value" seja igual
	 * a null
	 *
	 * @param value
	 * @param ifNull
	 * @return
	 */
	public static String isNull(String value, String ifNull) {
		return value == null ? ifNull : value;
	}

	/**
	 * Retorna true caso a String informada seja nula ou vazia.<br>
	 * OBS: no caso de espaços em branco, considera-se a string como não vazia e
	 * não nula. Se for preciso fazer esta verificação utilize desta forma: <br>
	 * <br>
	 *
	 * if(Str.isNullOrEmpty(" ".trim()))
	 *
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(Object value) {
		return value == null || value.toString().equals(Str.EMPTY);
	}

	/**
	 * Return the value string limited to maxlength characters. If the string
	 * gets curtailed and the suffix parameter is appended to it.
	 * <p/>
	 * Adapted from Velocity Tools Formatter.
	 *
	 * @param value
	 *            the string value to limit the length of
	 * @param maxlength
	 *            the maximum string length
	 * @param suffix
	 *            the suffix to append to the length limited string
	 * @return a length limited string
	 */
	public static String limitLength(String value, int maxlength, String suffix) {
		String ret = value;
		if (value.length() > maxlength)
			ret = Str.concat(value.substring(0, maxlength - suffix.length()),
					suffix);

		return ret;
	}

	/**
	 * Retorna uma string precedida do caractere <code>_char</code> repetido ate
	 * que se complete <br>
	 * o tamanho informado no parâmetro <code>times</code> <br>
	 * OBS: time é o tamnaho final da string retornada
	 *
	 * @param value
	 * @param _char
	 * @param times
	 * @return
	 */
	public static String lpad(String value, char _char, int times) {
		return Str.concat(Str.nchar(_char, times - value.length()), value);
	}

	/**
	 * Retorna uma <code>String</code> formada pelo caractere informado (
	 * <code>_char</code>) repetido o número de vezes informado <br>
	 * no parâmetro <code>times</code>
	 *
	 * @param _char
	 * @param times
	 * @return
	 */
	public static String nchar(char _char, int times) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < times; i++)
			builder.append(_char);

		return builder.toString();
	}

	/**
	 * Retorna uma string seguida ( a direita ) do caractere <code>_char</code>
	 * repetido ate que se complete <br>
	 * o tamanho informado no parâmetro <code>times</code> <br>
	 * OBS: time é o tamnaho final da string retornada
	 *
	 * @param value
	 * @param _char
	 * @param times
	 * @return
	 */
	public static String rpad(String value, char _char, int times) {
		return Str.concat(value, Str.nchar(_char, times - value.length()));
	}

	/**
	 * Retorna a palavra informaca com a primeira letra em caixa baixa<br>
	 * Ex.:<br>
	 * <code>Strign value = Str.toPascalCase("NomeFuncionario");</code><br>
	 * value = 'NomeFuncionario'
	 *
	 * @param value
	 * @return
	 */
	public static String toCamelCase(final String value) {
		return Str.concat(value.subSequence(0, 1).toString().toLowerCase(),
				String.valueOf(value.subSequence(1, value.length())));
	}

	/**
	 * Retorna a palavra informada com a primeira letra em caixa alta<br>
	 * Ex.:<br>
	 * <code>Strign value = Str.toPascalCase("nomeFuncionario");</code> <br>
	 * value = 'NomeFuncionario'
	 *
	 * @param value
	 * @return
	 */
	public static String toPascalCase(final String value) {
		return Str.concat(value.subSequence(0, 1).toString().toUpperCase(),
				String.valueOf(value.subSequence(1, value.length())));
	}

	/**
	 * Não permite isntancias
	 */
	private Str() {

	}
}
