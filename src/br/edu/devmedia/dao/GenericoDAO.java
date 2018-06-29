package br.edu.devmedia.dao;

import java.util.List;

import br.edu.devmedia.exception.PersistenciaException;

public interface GenericoDAO<T> {
	
	Object inserir(T objeto) throws PersistenciaException;
	
	void atualizar(T objeto) throws PersistenciaException;
	
	void excluir(Integer id) throws PersistenciaException;
	
	List<T> listarTodos() throws PersistenciaException;
	
	T getById(Integer id) throws PersistenciaException;
	
	T getById(Long id) throws PersistenciaException;
}