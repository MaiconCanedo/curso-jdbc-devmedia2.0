package br.edu.devmedia.exception;

public class NegocioException extends Exception{

	private static final long serialVersionUID = 3431808801788455533L;
	
	public NegocioException(String message, Exception exception) {
		super(message, exception);
	}
    
	public NegocioException(String message) {
		super(message);
	}
}