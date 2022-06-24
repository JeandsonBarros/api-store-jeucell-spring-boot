package com.apiloja.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.apiloja.model.user.Login;
import com.apiloja.model.user.UserLogin;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public static final int TOKEN_EXPIRACAO = 600_000;

	private final AuthenticationManager authenticationManager;

	private String secretToken;

	public JWAuthenticationFilter(String secretToken, AuthenticationManager authenticationManager) {
		this.secretToken = secretToken;
		this.authenticationManager = authenticationManager;
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {


		try {

			UserLogin user  = new  ObjectMapper().readValue(request.getInputStream(), UserLogin.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					user.getEmail(),
					user.getPassword(),

					new ArrayList<>()

			));
		}
		catch (IOException e) {

			throw new RuntimeException("Falha ao autenticar usuário", e);
		}

	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {


		Login user = (Login) authResult.getPrincipal();

		String token  = JWT.create()
				.withSubject(user.getUsername())
				//.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRACAO))
				.sign(Algorithm.HMAC512(secretToken));

		response.getWriter().write(token);
		response.getWriter().flush();




	}





}
