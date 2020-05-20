package br.com.alura.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	Page<Topico> findByCurso_Nome(String nomeCurso, Pageable paginacao);
	
	//Metodo alterado para a Ultilização da paginação
	//List<Topico> findByCurso_Nome(String nomeCurso, );

}