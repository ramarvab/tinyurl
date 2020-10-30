package com.cloudflare.tinyurl;

import com.cloudflare.repository.URLRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication(scanBasePackages = {"com.cloudflare.controller","com.cloudflare.entities","com.cloudflare.repository","com.cloudflare.service"})
@EnableCassandraRepositories(basePackageClasses = URLRepository.class)
@EnableAutoConfiguration
public class TinyurlApplication {

	public static void main(String[] args) {

		SpringApplication.run(TinyurlApplication.class, args);
	}

}
