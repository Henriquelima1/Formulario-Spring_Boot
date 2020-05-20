package br.com.alura.forum.controller.dto;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

import br.com.alura.forum.modelo.Topico;

public class TopicoDto {

	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	
	public TopicoDto(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao =topico.getDataCriacao();
	}
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	//Metodo trocado de "List" para "Page" para paginação
	
	public static Page<TopicoDto> converter(Page<Topico> topicos) {
		//"(TopicoDto::new)" para pegar o construtor
		
		return topicos.map(TopicoDto::new);
		
		//Codigo sendo Ajustado para a Paginação
		//return topicos.stream().map(TopicoDto::new).collect(Collectors.toList());
	}
	
	

	
	
}
