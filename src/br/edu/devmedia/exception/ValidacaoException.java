package br.edu.devmedia.exception;

public class ValidacaoException extends Exception{

	private static final long serialVersionUID = 3431808801788455533L;
	
	public ValidacaoException(String message, Exception exception) {
		super(message, exception);
	}
    
	public ValidacaoException(String message) {
		super(message);
	}
}