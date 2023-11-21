package io.github.itzgonza;

import io.github.itzgonza.impl.PasswordParser;

/**
 * @author ItzGonza
 */
public class Start {

	public static void main(String[] argument) {
		if (PasswordParser.instance == null)
		    PasswordParser.instance = new PasswordParser();
		PasswordParser.instance.initialize();
	}
	
}
