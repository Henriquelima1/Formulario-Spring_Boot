package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario logado =(Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime()+Long.parseLong(expiration));
		
		/**
		 * "Jwts.builder()" ESSE METODO É AONDE VOCÊ PODE CETAR
		 */
		return Jwts.builder()
				.setIssuer("API do Fórum da Alura")
				.setSubject(logado.getId().toString())
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	/**
	 * Preciso ter esse método para fazer a validação, para validar se o token que está chegando está ok ou não.
	 *  Para fazer isso, vamos usar de novo o tal de jwts. Só que não vou chamar o builder,
	 *   porque não quero criar um novo token. Vou chamar o método parser, 
	 *   que é o método que tem a lógica para fazer o passe de um token. Você passa para ele um token, 
	 *   ele vai descriptografar e verificar se está ok. 
	 * @param token
	 * 
	 * Na sequência, temos que chamar primeiro setSigningKey. 
	 * Tenho que passar aquele secret da nossa aplicação, que é a chave que ele usa para criptografar e descriptografar.
	 *  Tem um método chamado parseClaimsJws. Esse é o método que vamos chamar passando como parâmetro o token. 
	 * @return
	 */
	public boolean isTokenValido(String token) {
		/**
		 * Esse método devolve o Jws claims, 
		 * que é um objeto onde consigo recuperar o token e as informações que setei dentro do token. 
		 * Mas quando eu fizer essa chamada, se o token estiver válido, ele devolve o objeto. 
		 * Se estiver inválido ou nulo, ele joga uma exception. 
		 */
		//
		/**
		 * Eu vou fazer um try catch, vou colocar o código dentro do try. Se ele rodou tudo ok,
		 *  o token está válido. Se chegou na linha de baixo é porque o token está válido, retorna true,
		 *   porque não quero recuperar nenhuma informação do token nesse método. Se deu alguma exception,
		 *    ele vai entrar no false.
		 */
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		}catch (Exception e) {
			return false;
		}
		
		

	}

	/**
	 * E aí, como faço para recuperar os dados do token? É parecido com o método isTokenValido. 
	 * Vamos precisar desse código, da classe que faz o parser do token.
	 *  Vou colocar jwts.parser().setSigningKey(this.secret).parseClaimsJws(token). 
	 *  Esse objeto tem um método na sequência chamado getBody, que devolve o objeto do token em si. 
	 *  Vou chamar esse método e vou pedir para ele guardar isso numa variável local. 
	 *  
	 *  Ele devolve esse objeto do tipo claims, que é uma classe também da API do JSON web token.
	 *   Na próxima linha, dentro desse objeto claims, tenho vários métodos get para recuperar as informações do token.
	 *    Na hora em que geramos o token, setamos o subject. Então aqui consigo chamar o getSubject, para pegar o id de volta. 
	 *    
	 *    Só que dentro do token tudo é string, mas eu não quero isso como string, quero como long.
	 *     Mas eu sei que é um long que está lá dentro, que é o id do usuário, então vou fazer um parse aqui.
	 *      Com isso eu consigo recuperar o id do usuário que está setado dentro do token.
	 *  
	 * @param token
	 * @return
	 */
	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());


		
	}
	
}
