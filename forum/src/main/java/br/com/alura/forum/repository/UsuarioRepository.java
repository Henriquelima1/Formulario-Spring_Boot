package br.com.alura.forum.repository;





import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
/**
 * metodo pela busca por e-mail
 * 
 */
	/**
	 * "Optional" VAMOS ULTILIZAR PQ ELE PODE DEVOLVER UM ERRO SE 
	 * NÃO FOR ENCONTRADO O E-MAIL E SE ENCONTRA ELE DEVOLVE O 
	 * USUARIO
	 */

	Optional<Usuario> findByEmail(String email);
	
}
