package br.edu.devmedia.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.devmedia.dto.PessoaDTO;
import br.edu.devmedia.exception.PersistenciaException;
import br.edu.devmedia.jdbc.ConexaoUtil;

public class PessoaDAO implements GenericoDAO<PessoaDTO> {

	private static final String SQL_SELECT = "SELECT * FROM tb_pessoa";
	private static final String SQL_INSERT = "INSERT INTO tb_pessoa (nome, id_endereco, cpf, sexo, data_nascimento) values(?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE tb_pessoa SET nome=?, id_endereco=?, cpf=?, sexo=?, data_nascimento=? WHERE id_pessoa=?";
	private static final String SQL_ACTIVE_DEACTIVATED = "UPDATE tb_pessoa SET ativo=? WHERE id_pessoa=?";
	private static final String SQL_DELETE = "DELETE FROM tb_pessoa WHERE id_pessoa=?";

	private PessoaDTO alimentarPessoa(ResultSet resultSet) throws SQLException, PersistenciaException {
		return new PessoaDTO(resultSet.getLong("id_pessoa"), resultSet.getString("nome"), new EnderecoDAO().getById(resultSet.getInt("id_endereco")), 
			resultSet.getString("cpf"), resultSet.getString("sexo").charAt(0), resultSet.getDate("data_nascimento"));
	}

	private PreparedStatement alimentarStatement(PessoaDTO pessoaDTO, Connection connection, String sql)
			throws SQLException {
		return alimentarStatement(pessoaDTO, connection, sql, 0);
	}
	
	private PreparedStatement alimentarStatement(PessoaDTO pessoaDTO, Connection connection, String sql, int argumento)
			throws SQLException {
		PreparedStatement preparedStatement =  connection.prepareStatement(sql, argumento);
		preparedStatement.setString(1, pessoaDTO.getNome());
		preparedStatement.setInt(2, pessoaDTO.getEndereco().getIdEndereco());
		preparedStatement.setString(3, pessoaDTO.getCpf());
		preparedStatement.setString(4, pessoaDTO.getSexo().toString());
		preparedStatement.setDate(5, new Date(pessoaDTO.getDataNascimento().getTime()));
		return preparedStatement;
	}
	
	public void inserir(List<PessoaDTO> listaPessoas) throws PersistenciaException {
		for (PessoaDTO pessoaDTO : listaPessoas) {
			inserir(pessoaDTO);
		}
	}

	@Override
	public Long inserir(PessoaDTO pessoaDTO) throws PersistenciaException {
		Long codigo= null; 
		try (Connection connection = ConexaoUtil.getInstance().getConnection()) {
			PreparedStatement preparedStatement = alimentarStatement(pessoaDTO, connection, SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if(resultSet.next()) {
				codigo = resultSet.getLong(1);
			}
			connection.close();
		} catch (Exception e) {
                        e.printStackTrace();
			throw new PersistenciaException("inserir " + e.getMessage(), e);
		}
		return codigo;
	}
	
	@Override
	public ArrayList<PessoaDTO> listarTodos() throws PersistenciaException {
		return listarTodos(null);
	}

	public ArrayList<PessoaDTO> listar(Integer ativo) throws PersistenciaException {
		return listarTodos(ativo);
	}

	private ArrayList<PessoaDTO> listarTodos(Integer ativo) throws PersistenciaException {
		ArrayList<PessoaDTO> lista = new ArrayList<>();
		String sql = "";
		if (ativo != null) {
			sql = " WHERE ativo=?";
		}
		try (Connection connection = ConexaoUtil.getInstance().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT + sql);
			if (ativo != null) {
				preparedStatement.setInt(1, ativo);
			}
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				lista.add(alimentarPessoa(resultSet));
			}
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("listar " + e.getMessage(), e);
		}
		return lista;
	}

	public ArrayList<PessoaDTO> filtrarPessoa(String nome, String cpf, String sexo, String ordem) throws PersistenciaException {
		ArrayList<PessoaDTO> lista = null;
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			StringBuilder sql = new StringBuilder();
			Boolean ultimo = false;

			if (nome != null) {
				sql.append(" WHERE nome LIKE ?");
				ultimo = true;
			}
			if (cpf != null) {
				if (ultimo) {
					sql.append(" AND");
				} else {
					sql.append(" WHERE");
					ultimo = true;
				}
				sql.append(" cpf LIKE ?");
			}
			if (sexo != null) {
				if (ultimo) {
					sql.append(" AND");
				} else {
					sql.append(" WHERE");
				}
				sql.append(" sexo=?");
			}
			sql.append(" AND ativo=1 ORDER BY " + ordem);
			
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT + sql);
			Integer cont = 0;
			
			if (nome != null) {
				preparedStatement.setString(++cont, "%" + nome + "%");
			}
			if (cpf != null) {
				preparedStatement.setString(++cont, "%" + cpf + "%");
			}
			if (sexo != null) {
				preparedStatement.setString(++cont, sexo);
			}			
			
			ResultSet resultSet = preparedStatement.executeQuery();

			lista = new ArrayList<>();
			while (resultSet.next()) {
				lista.add(alimentarPessoa(resultSet));
			}

			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("filtrar pessoa " + e.getMessage(), e);
		}
		return lista;
	}

	public void atualizar(List<PessoaDTO> listaPessoas) throws PersistenciaException {
		for (PessoaDTO pessoaDTO : listaPessoas) {
			atualizar(pessoaDTO);
		}
	}

	@Override
	public void atualizar(PessoaDTO pessoaDTO) throws PersistenciaException {
		PessoaDTO pessoaTemp = getById(pessoaDTO.getIdPessoa());

		if (pessoaTemp == null) {
			throw new PersistenciaException("Erro 404, nenhum item com o indice " + pessoaDTO.getIdPessoa()
					+ " encontrado na tabela tb_pessoa!");
		}
		if (pessoaDTO.getNome() != null) {
			pessoaTemp.setNome(pessoaDTO.getNome());
		}
		if (pessoaDTO.getCpf() != null) {
			pessoaTemp.setCpf(pessoaDTO.getCpf());
		}
		if (pessoaDTO.getSexo() != null) {
			pessoaTemp.setSexo(pessoaDTO.getSexo());
		}
		if (pessoaDTO.getEndereco().getIdEndereco() != null) {
			pessoaTemp.setEndereco(pessoaDTO.getEndereco());
		}
		if (pessoaDTO.getDataNascimento() != null) {
			pessoaTemp.setDataNascimento(pessoaDTO.getDataNascimento());
		}

		try (Connection connection = ConexaoUtil.getInstance().getConnection()) {
			PreparedStatement preparedStatement = alimentarStatement(pessoaTemp, connection, SQL_UPDATE);
			preparedStatement.setLong(6, pessoaTemp.getIdPessoa());

			preparedStatement.executeUpdate();
			connection.close();
		} catch (Exception e) {
                        e.printStackTrace();
			throw new PersistenciaException("atualizar " + e.getMessage(), e);
		}
	}

	public void ativarDesativar(Long idPessoa, Integer ativo) throws PersistenciaException {
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_ACTIVE_DEACTIVATED);
			preparedStatement.setInt(1, ativo);
			preparedStatement.setLong(2, idPessoa);
			preparedStatement.execute();
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("alterar " + e.getMessage(), e);
		}
	}

	@Override
	public PessoaDTO getById(Long idPessoa) throws PersistenciaException {
		PessoaDTO pessoaDTO = null;
		try (Connection connection = ConexaoUtil.getInstance().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT + " WHERE id_pessoa=?");
			preparedStatement.setLong(1, idPessoa);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				pessoaDTO = alimentarPessoa(resultSet);
			}
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("get for id " + e.getMessage(), e);
		}
		return pessoaDTO;
	}

	public PessoaDTO getByCpf(String cpf) throws PersistenciaException {
		PessoaDTO pessoaDTO = null;
		try (Connection connection = ConexaoUtil.getInstance().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT + " WHERE cpf=?");
			preparedStatement.setString(1, cpf);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				pessoaDTO = alimentarPessoa(resultSet);
			}
		} catch (Exception e) {
			throw new PersistenciaException("get for id " + e.getMessage(), e);
		}
		return pessoaDTO;
	}
        
	public void excluir(Long idPessoa) throws PersistenciaException {
		try {
			Connection connection = ConexaoUtil.getInstance().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
			preparedStatement.setLong(1, idPessoa);

			preparedStatement.executeUpdate();
			connection.close();
		} catch (Exception e) {
			throw new PersistenciaException("excluir " + e.getMessage(), e);
		}
	}

	@Override
	public PessoaDTO getById(Integer id) throws PersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public void excluir(Integer id) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}