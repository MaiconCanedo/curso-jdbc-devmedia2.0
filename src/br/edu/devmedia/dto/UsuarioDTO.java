package br.edu.devmedia.dto;

public class UsuarioDTO implements Comparable<UsuarioDTO>{
	
	private Integer idUsuario, ativo, tipo;
	private String nome, login, senha;
	
	public UsuarioDTO() {}
	
	public UsuarioDTO(Integer idUsuario, Integer ativo, Integer tipo, String nome, String login, String senha) {
		this.idUsuario = idUsuario;
		this.ativo = ativo;
		this.tipo = tipo;
		this.nome = nome;
		this.login = login;
		this.senha = senha;
	}
	
	@Override
	public String toString() {
		return "UsuarioDTO [idUsuario=" + idUsuario + ", ativo=" + ativo + ", tipo=" + tipo + ", nome=" + nome + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		return idUsuario == ((UsuarioDTO)obj).idUsuario;
	}
	
	@Override
	public int compareTo(UsuarioDTO usuarioDTO) {
		return idUsuario.compareTo(usuarioDTO.idUsuario);
	}
	
	public String[] getArrayString() {
		return new String[] {String.valueOf(idUsuario), nome, (tipo == 0 || tipo > 1 ? "padrão" : "adimn"), (ativo == 0 ? "desativo" : "ativo")};
	}
	
	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getAtivo() {
		return ativo;
	}

	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}