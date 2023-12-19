package com.hawmock.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmailAndSocialType(String email, SocialType socialType);
}
