package br.edu.devmedia.bo;

import br.edu.devmedia.dao.LoginDAO;
import br.edu.devmedia.dto.UsuarioDTO;
import br.edu.devmedia.exception.NegocioException;

/**
 *
 * @author Maicon
 */
public class LoginBO {
    
    private LoginDAO loginDAO = new LoginDAO();
	
    public UsuarioDTO logar(String login, String senha) throws NegocioException {
    	UsuarioDTO usuarioDTO = null;
    	try {
            if (login == null || "".equals(login.trim())) {
                throw new NegocioException("Login Obrigatório!");
            } else if (senha == null || "".equals(senha.trim())) {
            	throw new NegocioException("Senha Obrigatória!");
            } else {
            	usuarioDTO = loginDAO.logar(login, senha);
            	if (usuarioDTO != null) {
            		if(usuarioDTO.getAtivo() == 0) {
            			throw new NegocioException("Este usuário está inativo!");
            		}
            	} else {
            		throw new NegocioException("Login e/ou Senha incorreto(s)!");
            	}
            }
        } catch (Exception e) {
        	throw new NegocioException(e.getMessage(), e);
        }
    	return usuarioDTO;
    }
    
    public UsuarioDTO logar(UsuarioDTO usuarioDTO) throws NegocioException {
    	return logar(usuarioDTO.getNome(), usuarioDTO.getSenha());
    }
}
