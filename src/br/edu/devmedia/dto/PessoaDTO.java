package br.edu.devmedia.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PessoaDTO implements Comparable<PessoaDTO> {
	
	//Atributos
	private Long idPessoa;
	private String nome, cpf;
	private EnderecoDTO endereco;
	private Character sexo;
	private Date dataNascimento;
	private Boolean ativo;
	//private DateFormat dateFormat = DateFormat.getDateInstance(2, Locale.getDefault());
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	//Construtores
	public PessoaDTO() {}
	    
	public PessoaDTO(Long idPessoa, String nome, EnderecoDTO endereco, String cpf, Character sexo, Date dataNascimento) {
		this.idPessoa = idPessoa;
		this.nome = nome;
		this.endereco = endereco;
		this.cpf = cpf;
		this.sexo = Character.toUpperCase(sexo);
		this.dataNascimento = dataNascimento;
	}
	
	public PessoaDTO(String nome, EnderecoDTO endereco, String cpf, Character sexo, Date dataNascimento) {
		this(null, nome, endereco, cpf, sexo, dataNascimento);
	}
	
	//Métodos
	public String[] getArrayString(){
        return new String[]{String.valueOf(idPessoa), nome, String.valueOf(sexo).toUpperCase(), cpf,
            (endereco != null ? endereco.getLogradouro() : null), (endereco != null ? String.valueOf(endereco.getCep()) : null),
            dateFormat.format(dataNascimento)};
    }
	
	public Long getIdPessoa() {
		return idPessoa;
	}
	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public EnderecoDTO getEndereco() {
		return endereco;
	}
	public void setEndereco(EnderecoDTO endereco) {
		this.endereco = endereco;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public Character getSexo() {
		return sexo;
	}
	public void setSexo(Character sexo) {
		this.sexo = Character.toUpperCase(sexo);
	}
	@Override
	public String toString() {
		return "PessoaDTO [idPessoa=" + idPessoa + ", nome=" + nome + ", endereco=" + endereco + ", cpf=" + cpf
				+ ", sexo=" + sexo + ", dataNascimento=" + dataNascimento + "]";
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int compareTo(PessoaDTO pessoaDTO) {
		return idPessoa.compareTo(pessoaDTO.idPessoa);
	}
}
