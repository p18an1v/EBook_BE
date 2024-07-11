package com.LoginRegisterAuth.Repository;

import com.LoginRegisterAuth.Model.contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<contact,Integer> {
}
