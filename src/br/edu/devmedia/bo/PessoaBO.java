package br.edu.devmedia.bo;

import java.util.ArrayList;

import br.edu.devmedia.dao.EnderecoDAO;
import br.edu.devmedia.dao.PessoaDAO;
import br.edu.devmedia.dto.EnderecoDTO;
import br.edu.devmedia.dto.PessoaDTO;
import br.edu.devmedia.exception.NegocioException;
import br.edu.devmedia.exception.PersistenciaException;
import br.edu.devmedia.exception.ValidacaoException;

public class PessoaBO {

	private PessoaDAO pessoaDAO = new PessoaDAO();
	private EnderecoDAO enderecoDAO = new EnderecoDAO(); 
	
	/* Math.pow(10, 11) != 0 */
	private void validarPessoaEEndereco(PessoaDTO pessoaDTO, EnderecoDTO enderecoDTO) throws ValidacaoException, PersistenciaException {
		StringBuilder message = new StringBuilder();
		
		if (pessoaDTO.getNome() == null || pessoaDTO.getNome().length() < 3) {
			message.append("\nNome > contém menos que 3 caractéres");
		}
		
		if (pessoaDTO.getCpf() == null) {
			message.append("\nCPF > contém menos de 11 digitos");
		}
                
		if (pessoaDTO.getDataNascimento() == null) {
			message.append("\nData de Nascimento > vazia");
		}
		
		if (enderecoDTO.getLogradouro() == null) {
			message.append("\nLogradouro > contém menos que 5 caractéres");
		}
		
		if (enderecoDTO.getBairro() == null) {
			message.append("\nBairro > contém menos que 3 caractéres");
		}
		
		if (enderecoDTO.getCep() == null) {
			message.append("\nCep > contém menos de 8 digitos");
		}
		
		if (enderecoDTO.getNumero() == null) {
			message.append("\nNumero > campo vaizo");
		}
		
		if (enderecoDTO.getEstado() == null) {
			message.append("\nEstado > Nenhum Estado selecionado");
		}
		
		if (message.length() > 0) {
			throw new ValidacaoException("Os seguintes campos estão preechidos incorretamente:" + message);
		}
	}
	
	public Long cadastrar(PessoaDTO pessoaDTO) throws NegocioException, ValidacaoException, PersistenciaException {
		if (pessoaDAO.getByCpf(pessoaDTO.getCpf()) != null) {
			throw new ValidacaoException("O CPF " + pessoaDTO.getCpf() + " já foi cadastrado!");
		}
		return pessoaDAO.inserir(pessoaDTO);
	}
	
	public Integer cadastrar(EnderecoDTO enderecoDTO, PessoaDTO pessoaDTO) throws ValidacaoException, PersistenciaException {
		validarPessoaEEndereco(pessoaDTO, enderecoDTO);
		return enderecoDAO.inserir(enderecoDTO);
	}
	
	public void excluir(Long idPessoa) throws PersistenciaException {
		PessoaDTO pessoaDTO = pessoaDAO.getById(idPessoa); 
		pessoaDAO.excluir(idPessoa);	      
		if(pessoaDTO.getEndereco() != null) {
			enderecoDAO.excluir(pessoaDTO.getEndereco().getIdEndereco());
		}	
	}

	public void ativarDesativar(Long idPessoa, Integer ativo) throws NegocioException, PersistenciaException {
		if (ativo != 0 && ativo != 1) {
			throw new NegocioException("Valor diferente de 1(ativo) e 0(inativo)!");
		}
		if (pessoaDAO.getById(idPessoa) == null) {
			throw new NegocioException("Pessoa não encontrada!");
		}
		pessoaDAO.ativarDesativar(idPessoa, ativo);
	}

	public ArrayList<PessoaDTO> filtrarPessoa(String nome, String cpf, String sexo, String ordem) throws PersistenciaException, NegocioException {
//		if (cpf != null) {
//			char[] lista = cpf.toCharArray();
//			for (int i = 0; i < lista.length; i++) {
//				if (!Character.isDigit(lista[i])) {
//					throw new NegocioException("O CPF deve conter somete números");
//				}
//			}
//		}
		return pessoaDAO.filtrarPessoa(nome, cpf, sexo, ordem);
	}

	public ArrayList<PessoaDTO> listar() throws PersistenciaException {
		return pessoaDAO.listarTodos();
	}

	public ArrayList<PessoaDTO> listar(Integer ativo) throws PersistenciaException {
		return ativo == 0 || 1 == ativo ? pessoaDAO.listar(ativo) : pessoaDAO.listarTodos();
	}

	public PessoaDTO getById(Long idPessoa) throws PersistenciaException {
		return pessoaDAO.getById(idPessoa);
	}

	public void atualizar(PessoaDTO pessoaDTO) throws PersistenciaException, NegocioException {
		if (pessoaDTO.getIdPessoa() == null) {
			throw new NegocioException("Inpossível atualiziar, id_pessoa não informado!");
		}
		pessoaDAO.atualizar(pessoaDTO);
	}
	
	public void atualizar(EnderecoDTO enderecoDTO) throws PersistenciaException, NegocioException {
		if (enderecoDTO.getIdEndereco() == null) {
			throw new NegocioException("Inpossível atualiziar, id_endereco não informado!");
		}
		enderecoDAO.atualizar(enderecoDTO);
	}
}