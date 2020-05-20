package br.com.alura.forum.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

/**
 * "UserDetails" "GrantedAuthority" É UMA INTERFACE PARA DIZER PARA O SPRING QUE ESSA 
 * É A CLASS QUE REPRESENTA O PERFILDE ACESSO
 * @author HENRIQUE ZARDIN DE LIMA
 */

@Entity
public class Perfil implements GrantedAuthority{

	/**
	 * PRECISA DESSE ATRIBUTO
	 */
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	//PARA DEVOLVER QUAL É O ATRIBUTO QUE CONTEM O NOME DO AUTORITY(nome do perfil)
	
	@Override
	public String getAuthority() {
		
		return nome;
	}
}
