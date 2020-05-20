package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 
 * @author HENRIQUE ZARDIN DE LIMA
 *
 */

/**
 * "@EnableWebSecurity" habilita o modo de segurança na nossa aplicação
 * "@Configuration" no stratup do projeto o spring vai carregar e ler as configurações aqui dentro da classe
 * "WebSecurityConfigurerAdapter" Essa classe tem alguns metodos para fazer as configurações
 */

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	
	/**
	 * O "WebSecurityConfigurerAdapter" JÁ TEM UM METODO QUE SABE CRIA O "AuthenticationManager"
	 * QUE ESTÁ NA CLASS "AutenticacaoController" ENTÃO SÓ PRECISA SOBRE ESCREVE-LO
	 */
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	//Configurações de autenticações
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/**
		 * "auth.userDetailsService" É PARA DIZER PARA O SPRING QUAL QUE É A CLASS, A SERVICE QUE TEM
		 * A LOGICA DE AUTENTICAÇÃO
		 */
		/**
		 * "BCryptPasswordEncoder()" ALGORITMO PARA GERAÇÃO DA SENHA DE SEGURANÇA
		 */
		
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/**
	 * TODAS AS "URLs" NOVAS DO PROJETO TEM QUE SER ADD NA "Configurações de Autorização"
	 */
	
	//Configurações de Autorização
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//"authorizeRequests()" para configurar quais requests vai ser autorizado e como vai ser essa 
		//autorização 
		http.authorizeRequests()
		
		/**
		 * ".antMatchers(method)"para informar qual "url" você quer filtrar e o que é para fazer, 
		 * se é para permitir ou bloquear
		 */
		
		//"HttpMethod.GET" passa qual o metodo que você quer usar	
		.antMatchers(HttpMethod.GET, "/topicos").permitAll()
		//""/topicos/*"" INFORMA QUE QUAL QUER GET DEPOIS DE 'topicos/" PODE SER FEITO
		.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
		// A URL DE LOGIN(auth) TEM QUE ESTAR LIBERADA SENÃO O USUARIO NÃO CONSEGUE NEM LOGAR NO SISTEMA
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		//".anyRequest().authenticated()" ASSIM QUALQUER OUTRA REQUISIÇÃO TEM QUE ESTAR AUTENTICADA 
		.anyRequest().authenticated()
		/**
		 * "formLogin()" PARA O SPRING GERAR UM FORMULARIO DE AUTENTICAÇÃO E UM CONTROLLER
		 * QUE RECEBE AS REQUISIÇÕES DESSE FORMULARIO
		 * 
		 * .and().formLogin();
		 */
		
	/**
	 * ".and().csrf().disable();" DESABILITANDO O "csrf"  PQ NÃO É NECESSARIO JÁ QUE VAMOS USAR TOKEN 
	 * COM "SPRING SECURITY"
	 */
		.and().csrf().disable()
		
	/**
	 * ".sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)" COM ISSO VOCÊ A VISA
	 *  O "SPRING SECURITY" QUE NO NOSSO PROJETO QUANDO FIZERMOS AUTENTICAÇÃO, NÃO É PARA CRIAR SESSÃO
	 */
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}
	//Configurações de recursos estaticos(js, css, imagens, etc..)
	@Override
	public void configure(WebSecurity web) throws Exception {
		
	}
	//SO PARA GERAR UMA SENHA SEGURA INICIAL, DEPOIS TEM QUE APAGAR
	//public static void main(String[] args) {
		//System.out.println(new BCryptPasswordEncoder().encode("123456"));
	//}
	
}
