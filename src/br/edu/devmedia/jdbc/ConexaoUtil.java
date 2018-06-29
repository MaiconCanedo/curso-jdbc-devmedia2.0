package br.edu.devmedia.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

//Padrão de projeto Singleton
public class ConexaoUtil {
	
	private static ResourceBundle config;
	private static ConexaoUtil conexaoUtil;
	private static String banco = "postgresql";
	
	private ConexaoUtil() {
		config = ResourceBundle.getBundle("config");
	}
	
	public static ConexaoUtil getInstance() {
		if (conexaoUtil == null) {
			conexaoUtil = new ConexaoUtil();
		}
		return conexaoUtil;
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
			Class.forName(config.getString("br.edu.devmedia.bd.driver." + banco));
			return DriverManager.getConnection(config.getString("br.edu.devmedia.bd.url.conexao." + banco),
					config.getString("br.edu.devmedia.bd.usuario." + banco),
					config.getString("br.edu.devmedia.bd.senha." + banco));
	}
}