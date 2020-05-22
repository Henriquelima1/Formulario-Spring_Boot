package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

/**
 * 
 * @author henrique.lima
 *"OncePerRequestFilter" ELE É UM FILTRO DO SPRING QUE É CHAMADO UMA UNICA VEZ A CADA REQUISIÇÃO 
 */
//
/**
 * 
 * @author henrique.lima
 *Eu só criei o filter, mas não tem anotação nenhuma. Embora eu tenha herdado de uma classe, ele não registrou. 
 *Precisamos registrar esse filtro para o Spring.
 */
/**
 * 
 * @author henrique.lima
 *Para fazer isso, não é via anotação. Tem que ser na nossa classe Security Configuration. No nosso método configure, 
 *que tem as URLs, depois que eu configurei que a autenticação é stateless,
 */
public class AutenticacaoViaTokenFilter  extends OncePerRequestFilter{

	private TokenService tokenService;
	private UsuarioRepository repository;
	
	
	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository repository) {
		this.tokenService = tokenService;
		this.repository =repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		/**
		 * Eu preciso pegar o token. O primeiro passo da nossa lógica é recuperar o token do cabeçalho. Para fazer isso,
		 *  vou criar uma variável string token = recuperarToken. Nesse método passo o request, e ele já me devolve o token.
		 *   O request tem um método chamado getHeader. Nele, passo o nome do cabeçalho que quero recuperar. No caso, 
		 *   vimos que é authorization.
		 */
		
		String token = recuperarToken(request);
		/**
		 * O próximo passo é validar o token, ver se ele está correto. Aí vamos ter que usar nossa biblioteca,
		 *  o jjwt, para fazer essa validação. Se estiver ok, o terceiro passo é falar para o Spring autenticar o usuário.
		 */
		//
		/**
		 * Preciso verificar se o token está válido, se é um token gerado pela aplicação ou se algum cliente mandou um token,
		 *  mas digitou qualquer coisa aleatoriamente.
		 *
		 * Para fazer isso, preciso usar aquela API naquela biblioteca que estamos usando, 
		 * do JSON web token. Tínhamos isolado o código dela naquela classe token service. 
		 * Nessa a classe vamos criar um método que recebe um token e me devolve um boolean me dizendo se está válido ou não.
		 * 
		 * Eu vou simplesmente fazer a chamada para essa classe e guardar o boolean que foi devolvido.
		 *  Vou criar uma variável boolean, chamar de valido = tokenservice.istokenvalido(token).
		 *  
		 *  Dá um erro porque não existe esse atributo. Vou precisar criar um atributo do tipo TokenService.
		 *   A ideia seria injetar esse TokenService. Porém tem um problema. 
		 *   Nesse tipo de classe não conseguimos fazer injeção de dependências. 
		 *   Não dá para colocar um @AutoWired, 
		 *   até porque na classe de security Configuration nós que instanciamos manualmente a classe.
		 *   
		 *   Eu posso receber via construtor. Já que não posso injetar pelo atributo, vou injetar da mesma maneira,
		 *    só que via construtor. Quem for dar new no nosso filter vai ter que passar o token service como parâmetro.
		 *     Só que na verdade quem está dando new na classe token filter somos nós mesmos pela classe security Configuration.
		 *     
		 *     Mas aqui estou na classe security Configuration.
		 *      Nessa a classe eu consigo fazer injeção de dependências. Resolvemos o problema.
		 *       Não consigo injetar no filter, mas na classe Configuration eu consigo. Na hora de dar new no filter,
		 *        eu passo o token service que foi injetado. Problema resolvido.
		 *         Ele está só reclamando porque o método isTokenValido não existe. Temos que criar. 
		 *     
		 */
		boolean valido = tokenService.isTokenValido(token);
		//
		/**
		 * Por enquanto, a única coisa que preciso fazer é devolver se está válido ou se não está válido.
		 *  Só isso. Salvei. Vou voltar para o meu filter. Nesse exercício, só vamos validar se está ok ou não.
		 *   Vamos fazer aquele teste via Postman. Vou mandar um token correto, ver se está válido e se vai imprimir true.
		 *    Depois mando um token aleatório, ver se vai imprimir falso. E vou mandar vazio. 
		 */
		System.out.println(valido);
		//
		/**
		 * Mas como são vários passos e é muito código, vamos quebrar isso e fazer aos poucos.
		 *  só vamos pegar o token e imprimir no console para ver se está chegando corretamente.
		 */
		System.out.println(token);
		//
		/**
		 * caso o token esteja invalido não vamos chamar esse código para autenticar.
		 * eu preciso fazer um if. Se o token estiver válido, tenho que autenticar o usuário. Se estiver inválido,
		 *  não vai entrar no if, ele vai chamar o filterChain e vai seguir o fluxo da requisição, barrando o usuário.
		 *   Só para não ficar com essa lógica, vou criar um método privado também autenticarCliente(token)
		 */
		if(valido) {
			autenticarCliente(token);
		}
		//
		/**
		 * "filterChain.doFilter(request, response);" ESSA LINHA FALA PARA O SPRING QUE JÁ RODOU O QUE TINHA 
		 */
		filterChain.doFilter(request, response);
		
	}

	/**
	 * Diferente do AutenticacaoController, que utilizamos o authentication manager, no filtro não vou usar isso,
	 *  porque ele é para disparar o processo de autenticação, com usuário e senha. 
	 *  Aqui não quero fazer autenticação via usuário e senha, porque esse processo já foi feito. 
	 *  Só quero falar para o Spring autenticar o usuário. Para fazer isso, 
	 *  o Spring tem uma classe chamada SecurityContextHolder.getContext().setAuthentication.
	 *   Esse é o método para falar para o Spring considerar que está autenticado. Só que aí preciso passar os dados do usuário.
	 *    Então, na hora de passar esse método preciso passar o objeto authentication, 
	 *    que tem as informações do usuário que está logado.
	 *    
	 *    Ele recebe um objeto authentication, e preciso criar uma variável do tipo authentication, 
	 *    é aquela mesma variável que usamos no AutenticacaoController. Podemos dar um new, 
	 *    mas aí vamos usar outro construtor. Em um passo os dados do usuário, as credenciais, que vai ser nulo, 
	 *    porque já validei antes, e preciso passar também os perfis de acesso dele. 
	 *    
	 *    Só vai ter um probleminha, porque não temos a variável usuário. Para dar new no authentication preciso do usuário.
	 *     De onde ele veio? Precisamos recuperar. A única informação que tenho na mão é o token.
	 *      E não sei se vocês lembram, mas na classe TokenService, quando escrevemos aquele código para gerar o token, 
	 *      nós setamos uma linha setSubject(logado.getId().toString()). Dentro do token, temos o id do usuário.
	 *       Se eu tenho o id, consigo carregar o objeto do banco de dados. 
	 *       
	 *       Eu vou fazer o seguinte. Estou com o token, dentro do token vou ter o id do usuário, preciso recuperar esse id,
	 *        com algo como Long idUsuario = TokenService.getIdUsuario(token). Vou criar mais esse método no TokenService,
	 *         em que passo um token e ele me devolve um id que está dentro desse token.
	 *         
	 *  Vou voltar para o meu filter. Agora já tenho o id do usuário. Só que eu preciso do objeto usuário completo.
	 *   Na sequência, preciso carregar esse usuário do banco de dados. Para carregar do banco de dados tem o repository.
	 *    Mas não tenho acesso a ele. Vou ter que declarar mais um atributo. Vou chamar a variável de repository. 
	 *    Tem aquele problema de não conseguir injetar. Vou ter que passar esse parâmetro para o construtor,
	 *    na hora de dar new no nosso filtro. Lembre-se que quem está dando new nesse objeto é a classe security configurations.
	 *    
	 *   Eu não tenho essa variável, mas consigo injetar. 
	 *   Vou declarar mais um atributo na classe de configuração, que é o usuarioRepository. Só não posso esquecer o //@AutoWired, 
	 *   senão o Spring não vai injetar. 
	 *   
	 *   Injetei o usuarioRepository, dei o new. No meu filter, quando dei new, 
	 *   eu injetei no construtor o token service e o usuarioRepository. Agora, 
	 *   consigo recuperar o objeto usuário da seguinte maneira: usuário = repository.getOne(idUsuario). 
	 *   Pronto, recuperei o usuário. É ela que passo como parâmetro. Vou salvar, e agora está tudo compilando.
	 * @param token
	 */
	//RESUMO DO QUE EU FIZ
	/**
	 * Peguei o id do token, recuperei o objeto usuário passando o id, criei o usernameauthenticationtoken passando o usuário, 
	 * passando nulo na senha, porque não preciso dela, passando os perfis, 
	 * e aí por fim chamei a classe do Spring que força a autenticação. Essa autenticação é só para esse request.
	 *  Na próxima requisição ele vai passar no filter de novo, pegar o token e fazer todo o processo.
	 *   A autenticação é stateless. Em cada requisição eu reautentico o usuário só para executar aquela requisição. 
	 * @param token
	 */
	private void autenticarCliente(String token) {
		Long idUsuario = tokenService.getIdUsuario(token);
		Usuario usuario = repository.findById(idUsuario).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); 
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		
		/**
		 * Vou fazer um if, se o token for igual a nulo, ou se token está vazio, ou se o token não começa com bearer,
		 *  eu vou devolver nulo. É só para verificar se está vindo o token, se não é vazio, e se começa com bearer. 
		 *  Se nenhuma dessas condições forem ok, eu devolvo nulo. Senão, devolvo o token. 
		 *  Só que se eu devolver o token assim vai o conteúdo inteiro. 
		 */
		if(token ==null || token.isEmpty() || !token.startsWith("Bearer ")) {
		return null;
		}
		/**
		 * Mas eu não quero esse começo. 
		 *  Quero só o token em si, então vai ser token.substring(7, tolen.length). 
		 *  Sete porque vai começar a pegar a partir do espaço até o final da string, que é o conteúdo do token.
		 */
		return token.substring(7, token.length());
	}

	
}
