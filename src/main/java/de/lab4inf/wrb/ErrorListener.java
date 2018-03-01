package de.lab4inf.wrb;

import org.antlr.v4.runtime.*;

public class ErrorListener extends BaseErrorListener {
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
		throw new IllegalArgumentException("at line " + line + ";" + charPositionInLine + ": " + msg);
	}
}
