package br.edu.devmedia.exception;

public class PersistenciaException extends Exception {

	private static final long serialVersionUID = 5743187231273341603L;
	
	public PersistenciaException(String message, Exception exception) {
		super(message, exception);
	}
	
	public PersistenciaException(String message) {
		super(message);
	}
}
