package com.edgardleal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Contem um mapa (<code>HashMap</code>) de mensagens estatico para uso da
 * aplicação <br>
 * Para uso, deverá existir um arquivo chamado "messages" na pasta da aplicação<br>
 * As mensagens devem ser escritas contanto que satisfaçam a este reges
 * <code>.*=.*</code><br>
 * a única regra, é que seja separado por um "=", o primeiro valor é a chave<br>
 * Exemplo de arquivo de mansagem:<br>
 * <code>
 * Cliente.cpfInvalido=CPF inválido<br>
 * Produto.estoqueVazio=Não há estoque disponível deste produto
 * </code>
 * 
 * @author Edgard Leal
 * @since 24/01/2013
 * 
 */
public class Messages {
	private static Map<String, String> map = new HashMap<String, String>();

	/**
	 * Inicialização do arquivo de mensagens
	 */
	static {
		BufferedReader reader = null;
		try {
			File file = new File("messages");
			if (!file.exists()) {
				reader = new BufferedReader(new FileReader(file));
				String line = null;
				String[] values;
				while ((line = reader.readLine()) != null) {
					if (line.matches(".*=.*")) {
						values = line.split("=");
						if (map.get(values[0]) == null)
							map.put(values[0], values[1]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
	}

	public static void put(String key, String message) {
		map.put(key, message);
	}

	/**
	 * Retorna o valor <code>alternative</code> caso não exista a chave
	 * informada
	 * 
	 * @param key
	 * @param alternative
	 * @return
	 */
	public static String getMessageIfNull(String key, String alternative) {
		String result = map.get(key);
		return result == null ? alternative : result;
	}

	public static String getMessage(Class<? extends Object> c, String key) {
		return map.get(c.getSimpleName() + key);
	}

	public static String getMessage(String key) {
		return map.get(key);
	}
}
