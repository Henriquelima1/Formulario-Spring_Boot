package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import br.com.alura.forum.config.security.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.form.LoginForm;
/**
 * 
 * @author HENRIQUE ZARDIN DE LIMA
 * AUTENTICAÇÃO DO LOGIN DO USUARIO POR TOKEN
 */

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	/**
	 * "AuthenticationManager" PARA FAZER UMA AUTENTICAÇÃO DE MANEIRA PROGRAMATICA
	 * ELA NÃO CONSEGUE FAZER INJEÇÃO DE DEPENDENTES DELA AUTOMATICAMENTE, A NÃO SER
	 * QUE VOCÊ CONFIGURE (isso vai ser feito na class "SecurityConfigurations")
	 * 
	 */
	@Autowired
	private AuthenticationManager authManager; 
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<?> autenticar(@RequestBody @Valid LoginForm form){
		/**
		 * SÓ PARA VERIFICAR SE ESTÁ CHEGANDO DIREITINHO AS SENHA
		 * System.out.println(form.getEmail());
		 * System.out.println(form.getSenha());  
		 */
		/**
		 * "authenticate(dadosLogin)" METODO QUE VAI FAZER A AUTENTICAÇÃO, 
		 * MAS ELE PRECISA RECEBER UM OBJETO EXPECIFICO
		 */
		/**
		 * " form.converter" METODO CRIADO PARA A CONVERÇÃO
		 */
		/**
		 * "try catch" FOI CRIADO CASO TENHA INFORMAÇÕES ERRADAS DO USUARIO
		 */
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		
		try {
			
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authentication);
			System.out.println(token);
			
		}catch(AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok().build();
		
	}
	
}
