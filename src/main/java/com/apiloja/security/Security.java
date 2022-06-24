package com.apiloja.security;

import com.apiloja.repository.user.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

//Ajuda:https://www.baeldung.com/spring-boot-security-autoconfiguration

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		  prePostEnabled = true, 
		  securedEnabled = true, 
		  jsr250Enabled = true)
public class Security extends WebSecurityConfigurerAdapter {

	private final ImplementsUserDetailsService implementsUserDetailsService;
	UsersRepository usersRepository;
	@Value("${value.from.secrettoken}")
	private String secretToken;

	public Security(ImplementsUserDetailsService implementsUserDetailsService, UsersRepository usersRepository){
		this.usersRepository = usersRepository;
		this.implementsUserDetailsService = implementsUserDetailsService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(implementsUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/swagger-ui/**", "/v3/**").permitAll()

				.antMatchers(HttpMethod.GET, "/products/all/").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/products/").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/products/{id}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/products/{id}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/products/evaluation/{productId}").permitAll()
				.antMatchers(HttpMethod.GET, "/products/home").permitAll()
				.antMatchers(HttpMethod.GET, "/products/fast/{nameProduct}").permitAll()
				.antMatchers(HttpMethod.GET, "/products/category/{category}/").permitAll()
				.antMatchers(HttpMethod.GET, "/products/search/{name}/").permitAll()
				.antMatchers(HttpMethod.GET, "/products/{id}").permitAll()

				.antMatchers(HttpMethod.GET, "/users/all/").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/users/{cpf}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/users/").permitAll()
				.antMatchers(HttpMethod.POST, "/users/send-email").permitAll()
				.antMatchers(HttpMethod.GET, "/users/check-code/{code}").permitAll()
				.antMatchers(HttpMethod.PUT, "/users/forgotpassword/").permitAll()

				.antMatchers(HttpMethod.GET, "/ordered/all/").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/ordered/find/{email}/").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/ordered/{id}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/ordered/{id}").hasAuthority("ADMIN")


		.anyRequest().authenticated()
				.and()
				.cors()
				.configurationSource(corsConfigurationSource())
		.and()
		.addFilter(new JWAuthenticationFilter(secretToken, authenticationManager()))
		.addFilter(new JWTValidationFilter(secretToken, authenticationManager(), usersRepository))
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers();
	}

	//https://auth0.com/blog/spring-boot-authorization-tutorial-secure-an-api-java/
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedMethods(List.of(
				HttpMethod.GET.name(),
				HttpMethod.PUT.name(),
				HttpMethod.POST.name(),
				HttpMethod.DELETE.name()
		));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());

		return source;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	



}