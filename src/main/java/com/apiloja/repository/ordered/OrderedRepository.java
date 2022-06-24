package com.apiloja.repository.ordered;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apiloja.model.ordered.Ordered;
import com.apiloja.model.user.UserShopping;

public interface OrderedRepository extends JpaRepository<Ordered, Long>{
	List<Ordered> findByUser(UserShopping user);

	Page<Ordered> findByUserEmailContaining(String email, Pageable page);


}
