package br.edu.devmedia.util;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MensagensUtil {
	
	public static final Integer SIM_NAO = 0, SIM_NAO_CANCELAR = 1, OK_CANCELAR = 2; 
	public static final Integer ERRO = SIM_NAO, INFO = SIM_NAO_CANCELAR, ATENCAO = OK_CANCELAR; 

	public static void msgErr(Component parentComponent, String message) {
		msgErr(parentComponent, message, null);
	}
	public static void msgErr(Component parentComponent, String message, String title) {
		JOptionPane.showMessageDialog(parentComponent, message, title, ERRO);
	}
	public static void msgInfo(Component parentComponent, String message) {
		msgInfo(parentComponent, message, null);
	}
	public static void msgInfo(Component parentComponent, String message, String title) {
		JOptionPane.showMessageDialog(parentComponent, message, title, INFO);
	}
	public static Integer confirDiag(Component parentComponent, String message, String title, Integer optionType) {
		return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType, ATENCAO);
	}
}