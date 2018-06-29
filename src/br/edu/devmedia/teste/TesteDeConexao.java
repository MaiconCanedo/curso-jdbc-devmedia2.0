package br.edu.devmedia.teste;

import java.util.ArrayList;
import java.util.Date;

import br.edu.devmedia.dao.EstadoDAO;
import br.edu.devmedia.dao.LoginDAO;
import br.edu.devmedia.dao.PessoaDAO;
import br.edu.devmedia.dto.PessoaDTO;
import br.edu.devmedia.util.MensagensUtil;

public class TesteDeConexao {
	
	public static void main(String[] args) {
		PessoaDAO pessoaDAO = new PessoaDAO();
		PessoaDTO pessoaDTO = new PessoaDTO();
		EstadoDAO estadoDAO = new EstadoDAO();
		LoginDAO loginDAO = new LoginDAO();
		try {
//			pessoaDTO.setIdPessoa(200);
//			pessoaDTO.setNome("Claudia");
//			pessoaDTO.setDataNascimento(new Date("1985/01/12"));
//			pessoaDTO.setSexo('f');
//			pessoaDTO.setCpf(12345678925L);
//			
//			pessoaDAO.inserir(pessoaDTO);
//			pessoaDAO.atualizar(pessoaDTO);
			
			//pessoaDAO.excluir(13);
//			pessoaDAO.listarTodos().forEach((pessoa)-> System.out.println(pessoa));
			//System.out.println("202cb962ac59075b964b07152d234b70".length());
			//System.out.println("Maicon Canedo Rocha Paradela".length());
//			int i = 1234567890;
//			System.out.println("e668096a5ad4e0343e8556b762e75f15".length());
//			//System.out.println((99999999999L / Math.pow(10, 11)));
//			System.out.println(estadoDAO.getBySigla("RJ"));
			System.out.println(loginDAO.logar("maicon.canedo-1995", "07moba.br"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
}
