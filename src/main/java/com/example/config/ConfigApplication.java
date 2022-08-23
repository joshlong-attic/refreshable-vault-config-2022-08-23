package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.Collection;

@SpringBootApplication
public class ConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

	@Bean
	@RefreshScope
	DataSource dataSource(DataSourceProperties properties, //
			@Value("${spring.datasource.url}") String url, //
			@Value("${spring.datasource.username}") String username, //
			@Value("${spring.datasource.password}") String pw //
	) {
		return DataSourceBuilder.create().url(properties.getUrl()).username(properties.getUsername())
				.password(properties.getPassword()).build();
	}

}

record Customer(Integer id, String name) {
}

@Repository
class CustomerRepository {

	private final JdbcTemplate template;

	CustomerRepository(JdbcTemplate template) {
		this.template = template;
	}

	Collection<Customer> customers() {
		return this.template.query("select * from customer",
				(rs, rowNum) -> new Customer(rs.getInt("id"), rs.getString("name")));
	}

}

@Controller
@ResponseBody
@RefreshScope
class RefreshController {

	private final CustomerRepository repository;

	RefreshController(CustomerRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/customers")
	Collection<Customer> get() {
		return this.repository.customers();
	}

}

@Component
class RefreshListener {

	@EventListener({ ApplicationReadyEvent.class, RefreshScopeRefreshedEvent.class })
	public void refresh() {
		System.out.println("the environment's been changed!");
	}

}
