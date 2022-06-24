package com.apiloja.security;

import java.util.HashSet;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.apiloja.model.user.Login;
import com.apiloja.model.user.UserShopping;
import com.apiloja.repository.user.UsersRepository;


@Service("implementsUserDetailsService")
public class ImplementsUserDetailsService implements UserDetailsService {

	@Autowired
	private UsersRepository ur;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		UserShopping user = ur.findByEmail(login);
		if (user == null) {

			throw new UsernameNotFoundException("Usuario [" + login + "] não encontrado!");
		}

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(authority);
		
		return new Login(user, authorities);

	}
	
	
	

}
