package br.edu.devmedia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.edu.devmedia.dto.UsuarioDTO;
import br.edu.devmedia.exception.PersistenciaException;
import br.edu.devmedia.jdbc.ConexaoUtil;

public class LoginDAO implements GenericoDAO<UsuarioDTO> {
	
	private static final String SQL_ALTHENTICATE = "SELECT * FROM usuario WHERE login=? AND senha=MD5(?)";
	
	private PreparedStatement alimentarStatement(UsuarioDTO usuarioDTO, Connection connection, String sql) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, usuarioDTO.getIdUsuario());
		preparedStatement.setInt(1, usuarioDTO.getAtivo());
		preparedStatement.setInt(1, usuarioDTO.getTipo());
		preparedStatement.setString(2, usuarioDTO.getNome());
		preparedStatement.setString(2, usuarioDTO.getLogin());
		preparedStatement.setString(3, usuarioDTO.getSenha());
		return preparedStatement;
	}
	
	private UsuarioDTO alimentarUsuario(ResultSet resultSet) throws SQLException {
		return new UsuarioDTO(resultSet.getInt("id_usuario"), resultSet.getInt("ativo"), resultSet.getInt("tipo"), resultSet.getString("nome"), 
				resultSet.getString("login"), resultSet.getString("senha"));
	}
	
	public UsuarioDTO logar(String login, String senha) throws PersistenciaException{
		UsuarioDTO usuarioDTO = null;
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_ALTHENTICATE);
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, senha);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				usuarioDTO = alimentarUsuario(resultSet);
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException("logar ", e);
		}
		return usuarioDTO;
	}
	
	public UsuarioDTO logar(UsuarioDTO usuarioDTO) throws PersistenciaException {
		return logar(usuarioDTO.getNome(), usuarioDTO.getSenha());
	}

	
	@Override
	public void atualizar(UsuarioDTO objeto) throws PersistenciaException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluir(Integer id) throws PersistenciaException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UsuarioDTO> listarTodos() throws PersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsuarioDTO getById(Integer id) throws PersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer inserir(UsuarioDTO objeto) throws PersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsuarioDTO getById(Long id) throws PersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}
}
