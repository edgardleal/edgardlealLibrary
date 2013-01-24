package com.edgardleal.util;

/**
 * Classe de utilitários para trabalho com Strings.
 * 
 * @author edgardr
 * @since 09-01-2013
 */
public class Str {
	/**
	 * Contem uma string vazia "", deve ser utilizado para diminur o tamanho do
	 * pool de Strings da jvm
	 */
	public static final String EMPTY = "";
	/**
	 * Um espacço em branco.
	 */
	public static final String SPACE = " ";

	/**
	 * Retorna true caso a String informada seja nula ou vazia.<br>
	 * OBS: no caso de espaços em branco, considera-se a string como não vazia e
	 * não nula. Se for preciso fazer esta verificação utilize desta forma: <br>
	 * <br>
	 * 
	 * if(Str.isNullOrEmpty("  ".trim()))
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(Object value) {
		return value == null || value.toString().equals(EMPTY);
	}

	/**
	 * Indica se o valor informado não esta em branco
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNotBlank(final String value) {
		return !value.trim().equals(EMPTY);
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
		return (value == null || value.equals(EMPTY)) ? result : value;
	}

	/**
	 * Retorna uma String vazia caso o valor informado seja nul<br>
	 * caso não seja nulo, retorna o mesmo valor informado.
	 * 
	 * @param value
	 * @return
	 */
	public static String emptyIfNull(Object value) {
		return value == null ? EMPTY : value.toString();
	}

	/***
	 * Retorna as <code>String</code> informada sem os espaços a esquerda <br>
	 * a direita e sem os caracteres ENTER e TAB
	 * 
	 * @param value
	 * @return
	 */
	public static String clearSpaces(String value) {
		value = value.trim().replaceAll(" {2,20}", SPACE)
				.replaceAll("[\n\t]+", EMPTY);
		return value;
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
	 * Retorna uma <code>String</code> formada pelo caractere informado (
	 * <code>_char</code>) repetido o número de vezes informado <br>
	 * no parâmetro <code>times</code>
	 * 
	 * @param _char
	 * @param times
	 * @return
	 */
	public static String nchar(char _char, int times) {
		String result = EMPTY;
		for (int i = 0; i < times; i++)
			result += _char;

		return result;
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
		return nchar(_char, times - value.length()) + value;
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
		return value + nchar(_char, times - value.length());
	}

	public Str() {

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
		if (value.length() > maxlength) {
			ret = value.substring(0, maxlength - suffix.length()) + suffix;
		}
		return ret;
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
		return value.subSequence(0, 1).toString().toUpperCase()
				+ value.subSequence(1, value.length());
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
		return value.subSequence(0, 1).toString().toLowerCase()
				+ value.subSequence(1, value.length());
	}
}
