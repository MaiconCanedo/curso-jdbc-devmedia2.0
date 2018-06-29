package br.edu.devmedia.dto;

public class EstadoDTO {
	
	private Integer idEstado;
	private String sigla, descricao;
	
	public EstadoDTO(Integer idEstado, String sigla, String descricao) {
		this.idEstado = idEstado;
		this.sigla = sigla;
		this.descricao = descricao;
	}
	
	public EstadoDTO(String sigla, String descricao) {
		this(null, sigla, descricao);
	}
	
	public Integer getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return "EstadoDTO [idEstado=" + idEstado + ", sigla=" + sigla + ", descricao=" + descricao + "]";
	}
}