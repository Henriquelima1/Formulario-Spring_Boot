package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
/**
 * 
 * @author HENRIQUE ZARDIN DE LIMA
 *
 */

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	/**
	 * "@ResquestParam" coloca como obrigatorio para a listagem.
	 * 
	 * Metodo trocado de "List" para "Page" para paginação.
	 * 
	 * "@PageableDefault(sort = "id", direction= Direction.DESC)" para ordenar caso não 
	 * venha o parametro de ordenação.
	 * 
	 * "@PageableDefault(page = 0, size =10)" para ajustar a paginação caso não venha 
	 * passar os paremetros de paginação.
	 * 
	 * "@Cacheable(value = "listaDeTopicos")" para avisar o Spring guardar o retorno em cache. 
	 * "value = "listaDeTopicos"" Identificador unico desse cache.
	 */

	@ResponseBody
	@GetMapping
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(sort = "id", direction= Direction.DESC, page = 0, size =10) Pageable paginacao){
		/**
		 * Tem que habilitar o modulo "@EnableSpringDataWebSupport" no "main' do programa 
		 * para otimizar o Sistema de paginção como está sendo usado. 
		 */
		/**
		 * Paginação com ordenação
		 * Pageable paginacao = PageRequest.of(pagina, qtd, Direction.ASC, ordenacao);
		 */
		/**
		 * Paginação sem ordenação
		 * Pageable paginacao = PageRequest.of(pagina, qtd);
		 */
		/**
		 * findAll(com paginação) faz automaticamente na consulta com o Spring, mas o retorno 
		 * não pode ser mais um "List" e sim um "Page" que contem a quantidade de numero de 
		 * paginas e quantidade da lista no total
		 */

		
		if(nomeCurso == null) {
			
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			
			//List<Topico> topicos = topicoRepository.findAll(paginacao);
			
			return TopicoDto.converter(topicos);
		} else {
			//"findByCurso_Nome" é nosso proprio metodo criado dentro da nossa Interface "TopicoRepository"
			
			Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
			
			/**
			 * Metodo alterado para a Ultilização da paginação
			 * List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
			 */

			return TopicoDto.converter(topicos);
		}
		
	}
	
	/**
	 * "@CacheEvict" ele é usado para limpar o cache
	 * "@CacheEvict(value = "listaDeTopicos",)" para limpar o cache especifico
	 * "@CacheEvict(value = "listaDeTopicos", allEntries = true)"para limpar todos os registros 
	 */

	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable Long id) {
		
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()) {
			return ResponseEntity.ok( new DetalhesTopicoDto(topico.get()));
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
		
		
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id){
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			
			topicoRepository.deleteById(id);
			
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
	
}
