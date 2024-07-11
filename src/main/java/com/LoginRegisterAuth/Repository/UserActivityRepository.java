package com.LoginRegisterAuth.Repository;

import com.LoginRegisterAuth.Model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findAllByOrderByLoginTimestampDesc();
    UserActivity findTopByEmailOrderByLoginTimestampDesc(String email);
    List<UserActivity> findByEmail(String email);
}