package dev.exception;

public class CalculException extends RuntimeException {

	public CalculException(String msg) {
		super(msg);
	}

	public CalculException() {

		super("Le calcul est incorrect");
	}

}
