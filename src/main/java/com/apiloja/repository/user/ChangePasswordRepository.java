package com.apiloja.repository.user;

import com.apiloja.model.user.ChangePassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangePasswordRepository extends JpaRepository<ChangePassword, Integer> {

}
