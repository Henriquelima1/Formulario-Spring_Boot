package br.com.alura.forum.config.security;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService{

	@Autowired
	private UsuarioRepository repository;
	/**
	 * "loadUserByUsername(String username)" MEODO DE AUTENTICAÇÃO, QUE RECEBE COMO PARAMETRO O "E-MAIL"
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/**
		 * "repository.findByEmial(username);" PARA FAZER A CONSULTA DO E-MAIL E DEVOLVER
		 * O USUARIO
		 */
		 Optional<Usuario> usuario = repository.findByEmail(username);
		/**
		 * SE VIER UM USUARIO É PQ ESTÁ CERTO O E-MAIL QUE FOI DIGITADO
		 * AI SIMPLESMENTE DEVOLVE O USUSARIO
		 * SE NÃO VIER TEM QUE AVISAR AO SPRING QUE ESSE USUARIO NÃO
		 * EXISTE E MANDAR O ERRO 
		 */
		if (usuario.isPresent()) {
			return usuario.get();
		}
		
		throw new UsernameNotFoundException("Dados inválidos!!");
	}

}
