package com.apiloja.repository.user;

import com.apiloja.model.user.UserShopping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends JpaRepository<UserShopping, String>{
	UserShopping findByEmail(String email);
	Page<UserShopping> findByEmailContaining(String email, Pageable page);
}
