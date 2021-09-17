package com.adobe.prj.auth;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;


@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public Optional<User> loadUserByUserName(String username) {
		return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
	}

	private List<User> getApplicationUsers() {
        List<User> users = Arrays.asList(
                new User(
                        "Rahul",
                        passwordEncoder.encode("secret123"),
                        UserRole.STUDENT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new User(
                        "Rita",
                        passwordEncoder.encode("secret123"),
                        UserRole.ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new User(
                        "Harry",
                        passwordEncoder.encode("secret123"),
                        UserRole.ADMINTRAINEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );

        return users;
    }
}
