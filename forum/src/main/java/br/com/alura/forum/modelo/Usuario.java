package br.com.alura.forum.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



/**
 * "UserDetails"  É A INTERFACE QUE TEM DETALHES DE UM USUARIO
 * @author HENRIQUE ZARDIN DE LIMA
 *
 */

@Entity
public class Usuario implements UserDetails {

	/**
	 * PRECISA DESSE ATRIBUTO
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String senha;
	
	/**
	 * "fetch = FetchType.EAGER" QUANDO CARREGAR O USUARIO JÁ CARREGA A LISTA DE PERFIS DELE
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Perfil> perfis = new ArrayList<>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	/**
	 * ESSE METODO REPRESENTA QUAL É O PERFIL DO USUSARIO, E PARA DEVOLVER
	 * QUAL ATRIBUTO CONTEM A COLEÇÃO DESSE USUARIO 
	 */
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return this.perfis;
	}
	
	//ESSE METODO VAI PEDIR QUAL O ATRIBUTO QUE CORRESPONDE A SENHA 
	@Override
	public String getPassword() {
		
		return this.senha;
	}

	//ESSE METODO VAI PEDIR QUAL O ATRIBUTO QUE CORRESPONDE AO USUARIO 
	@Override
	public String getUsername() {
		
		return this.email;
	}

	//ESSE METODO VERIFICA SE A CONTA NÃO ESTÁ EXPIRADA
	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	//ESSE METODO VERIFICA SE A CONTA NÃO ESTÁ BLOQUEADA
	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	//ESSE METODO VERIFICA SE A CREDENCIAL NÃO ESTÁ EXPIRADA
	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	//ESSE METODO VERIFICA SE A CONTA NÃO ESTÁ ABILITADA
	@Override
	public boolean isEnabled() {
		
		return true;
	}

}
