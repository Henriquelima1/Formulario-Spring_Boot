package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * @author HENRIQUE ZARDIN DE LIMA
 * 
 *
 * Tem que habilitar o modulo "@EnableSpringDataWebSupport" no "main' do programa 
 * para otimizar o Sistema de paginção como está sendo usado
 *
 *"@EnableCaching" para habilitar o uso de cache na aplicação
 */

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
