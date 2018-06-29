package br.edu.devmedia.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldComTamanhoEspecifico extends PlainDocument {

	private static final long serialVersionUID = -3679808840298934725L;
	
	private int limite = 1;

	public JTextFieldComTamanhoEspecifico(int limite) {
		super();
		this.limite = limite;
	}
	
	public JTextFieldComTamanhoEspecifico() {
		this(1);
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if(str == null) {
			return;
		}
		if(limite <= 0) {
			super.insertString(offs, str, a);
			return;
		}
		int tam = (getLength() + str.length());
		if(tam <= limite) {
			super.insertString(offs, str, a);
		}
	}
}
