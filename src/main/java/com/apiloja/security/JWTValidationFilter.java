package com.apiloja.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apiloja.repository.user.UsersRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTValidationFilter extends BasicAuthenticationFilter {

	public static final String HEADER_ATRIBUTO = "Authorization";
	public static final String ATRIBUTO_PREFIXO = "Bearer ";

	private String secretToken;

	UsersRepository usersRepository;

	public JWTValidationFilter(String secretToken, AuthenticationManager authenticationManager, UsersRepository usersRepository) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
		this.usersRepository =  usersRepository;
		this.secretToken = secretToken;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String atributo = request.getHeader(HEADER_ATRIBUTO);

		if (atributo == null) {
			chain.doFilter(request, response);
			return;
		}

		if (!atributo.startsWith(ATRIBUTO_PREFIXO)) {
			chain.doFilter(request, response);
			return;
		}

		String token = atributo.replace(ATRIBUTO_PREFIXO, "");

		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);

	}

	public UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

		String usuario = JWT.require(Algorithm.HMAC512(secretToken)).build().verify(token)
				.getSubject();

		if (usuario == null) {

			return null;
		}

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usersRepository.findByEmail(usuario).getRole());

		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(authority);

		return new UsernamePasswordAuthenticationToken(usuario, null, authorities);
	}

}
