package br.edu.devmedia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.devmedia.dto.EnderecoDTO;
import br.edu.devmedia.exception.PersistenciaException;
import br.edu.devmedia.jdbc.ConexaoUtil;

public class EnderecoDAO implements GenericoDAO<EnderecoDTO> {
	
	private static final String SQL_SELECT = "SELECT * FROM endereco";
	private static final String SQL_INSERT = "INSERT INTO endereco (logradouro, bairro, numero, cep, id_estado) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE endereco SET logradouro=?, bairro=?, numero=?, cep=?, id_pessoa=?, id_estado=? WHERE id_endereco=?";
	private static final String SQL_DELETE =  "DELETE FROM endereco WHERE id_endereco=?";
	
	private EnderecoDTO alimentarEndereco(ResultSet resultSet) throws SQLException, PersistenciaException {
		return new EnderecoDTO(resultSet.getInt("id_endereco"), resultSet.getInt("cep"), resultSet.getInt("numero"), 
				resultSet.getString("logradouro"), resultSet.getString("bairro"), new EstadoDAO().getById(resultSet.getInt("id_estado")));
	}
	
	private PreparedStatement alimentarStatement(EnderecoDTO enderecoDTO, Connection connection, String sql, int argumento) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql, argumento);
		preparedStatement.setString(1, enderecoDTO.getLogradouro());
		preparedStatement.setString(2, enderecoDTO.getBairro());
		preparedStatement.setInt(3, enderecoDTO.getNumero());
		preparedStatement.setInt(4, enderecoDTO.getCep());
		preparedStatement.setInt(5, enderecoDTO.getEstado().getIdEstado());
		return preparedStatement;
	}
	
	private PreparedStatement alimentarStatement(EnderecoDTO enderecoDTO, Connection connection, String sql) throws SQLException {
		return alimentarStatement(enderecoDTO, connection, sql, 0);
	}
	
	@Override
	public Integer inserir(EnderecoDTO enderecoDTO) throws PersistenciaException {
		Integer chave = null;
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = alimentarStatement(enderecoDTO, connection, SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				chave = resultSet.getInt(1);
			}
			connection.close();
		} catch (Exception e) {
                        e.printStackTrace();
			throw new PersistenciaException("inserir ", e);
		}
		return chave;
	}

	@Override
	public void atualizar(EnderecoDTO enderecoDTO) throws PersistenciaException {
		try {
			EnderecoDTO enderecoTemp = getById(enderecoDTO.getIdEndereco());
			
			if(enderecoTemp == null) {
				throw new PersistenciaException("O endereço codigo " + enderecoDTO.getIdEndereco() + " não foi encontrado!");
			}
			
			if (enderecoDTO.getLogradouro() != null) {
				enderecoTemp.setLogradouro(enderecoDTO.getLogradouro());
			}
			
			if (enderecoDTO.getBairro() != null) {
				enderecoTemp.setBairro(enderecoDTO.getBairro());
			}
			
			if (enderecoDTO.getNumero() != null) {
				enderecoTemp.setNumero(enderecoDTO.getNumero());
			}
			
			if (enderecoDTO.getCep() != null) {
				enderecoTemp.setCep(enderecoDTO.getCep());
			}
			
			if (enderecoDTO.getEstado().getIdEstado() != null) {
				enderecoTemp.setEstado(enderecoDTO.getEstado());
			}
			
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = alimentarStatement(enderecoTemp, connection, SQL_UPDATE);
			preparedStatement.setInt(6, enderecoTemp.getIdEndereco());
			preparedStatement.execute();
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("atualizar ", e);
		}
	}
	
	public void atualizarEspc(EnderecoDTO enderecoDTO) throws PersistenciaException {
		try {
			
			
			Connection connection = ConexaoUtil.getInstance().getConnection();
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void atualizarIdPessoa(Integer idEndereco, Long idPessoa) throws PersistenciaException {
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE endereco SET id_pessoa=? WHERE id_endereco=?");
			preparedStatement.setLong(1, idPessoa);
			preparedStatement.setInt(2, idEndereco);
			preparedStatement.executeUpdate();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException("atualizar id_pessoa", e);
		}
	}
	
	@Override
	public void excluir(Integer idEndereco) throws PersistenciaException {
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
			preparedStatement.setInt(1, idEndereco);
			preparedStatement.execute();
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("excluir ", e);
		}
	}

	@Override
	public ArrayList<EnderecoDTO> listarTodos() throws PersistenciaException {
		ArrayList<EnderecoDTO> lista = null;
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			ResultSet resultSet = connection.prepareStatement(SQL_SELECT).executeQuery();
			lista = new ArrayList<>();
			while(resultSet.next()) {
				lista.add(alimentarEndereco(resultSet));
			}
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("listar todos ", e);
		}
		return lista;
	}

	@Override
	public EnderecoDTO getById(Integer idEndereco) throws PersistenciaException {
		EnderecoDTO enderecoDTO = null;
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT + " WHERE id_endereco=?");
			preparedStatement.setInt(1, idEndereco);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				enderecoDTO = alimentarEndereco(resultSet);
			}
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("get by id ", e);
		}
		return enderecoDTO;
	}

	@Override
	public EnderecoDTO getById(Long id) throws PersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}
}