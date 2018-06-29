package br.edu.devmedia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import br.edu.devmedia.dto.EstadoDTO;
import br.edu.devmedia.exception.PersistenciaException;
import br.edu.devmedia.jdbc.ConexaoUtil;

public class EstadoDAO implements GenericoDAO<EstadoDTO>{
	
	private static final String SQL_SELECT = "SELECT * FROM estado";
	
	private EstadoDTO alimentarEstado(ResultSet resultSet ) throws SQLException {
		return new EstadoDTO(resultSet.getInt("id_estado"), resultSet.getString("sigla"), resultSet.getString("descricao"));
	}
	
	@Override
	public Integer inserir(EstadoDTO estadoDTO) throws PersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void atualizar(EstadoDTO estadoDTO) throws PersistenciaException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluir(Integer idEstado) throws PersistenciaException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<EstadoDTO> listarTodos() throws PersistenciaException {
		ArrayList<EstadoDTO> lista = null;
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			ResultSet resultSet = connection.prepareStatement(SQL_SELECT).executeQuery();
			lista = new ArrayList<>();
			while(resultSet.next()) {
				lista.add(alimentarEstado(resultSet));
			}
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("listar todos ", e);
		}
		return lista;
	}

	@Override
	public EstadoDTO getById(Integer idEstado) throws PersistenciaException {
		return getEstado(idEstado, null, null);
	}
	
	public EstadoDTO getBySigla(String sigla) throws PersistenciaException {
		return getEstado(null, sigla, null);
	}
	
	public EstadoDTO getByDescricao(String descricao) throws PersistenciaException {
		return getEstado(null, null, descricao);
	}
	
	private EstadoDTO getEstado(Integer idEstado, String sigla, String descricao) throws PersistenciaException {
		EstadoDTO estadoDTO = null;
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = null;
			if(idEstado != null) {
				preparedStatement = connection.prepareStatement(SQL_SELECT + " WHERE id_estado=?");
				preparedStatement.setInt(1, idEstado);
			}
			if(sigla != null) {
				preparedStatement = connection.prepareStatement(SQL_SELECT + " WHERE sigla=?");
				preparedStatement.setString(1, sigla);
			}
			if(descricao != null) {
				preparedStatement = connection.prepareStatement(SQL_SELECT + " WHERE descricao=?");
				preparedStatement.setString(1, descricao);
			}
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				estadoDTO = alimentarEstado(resultSet);
			}
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("get estado ", e);
		}
		return estadoDTO;
	}

	@Override
	public EstadoDTO getById(Long id) throws PersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}
}
