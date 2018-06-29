package br.edu.devmedia.dto;

public class EnderecoDTO {

	private Integer idEndereco, cep, numero;
	private String logradouro, bairro;
	private EstadoDTO estadoDTO;

	public EnderecoDTO(Integer idEndereco, Integer cep, Integer numero, String logradouro, String bairro,
			EstadoDTO estadoDTO) {
		this.idEndereco = idEndereco;
		this.cep = cep;
		this.numero = numero;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.estadoDTO = estadoDTO;
	}

	public EnderecoDTO(Integer cep, Integer numero, Long idPessoa, String logradouro, String bairro,
			EstadoDTO estadoDTO) {
		this(null, cep, numero, logradouro, bairro, estadoDTO);
	}

	public EnderecoDTO(Integer cep, Integer numero, String logradouro, String bairro, EstadoDTO estadoDTO) {
		this(null, cep, numero, logradouro, bairro, estadoDTO);
	}

	public Integer getIdEndereco() {
		return idEndereco;
	}

	public void setIdEndereco(Integer idEndereco) {
		this.idEndereco = idEndereco;
	}

	public Integer getCep() {
		return cep;
	}

	public void setCep(Integer cep) {
		this.cep = cep;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public EstadoDTO getEstado() {
		return estadoDTO;
	}

	public void setEstado(EstadoDTO estadoDTO) {
		this.estadoDTO = estadoDTO;
	}
}