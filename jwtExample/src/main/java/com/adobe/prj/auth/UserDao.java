package com.adobe.prj.auth;

import java.util.Optional;

public interface UserDao {
	Optional<User> loadUserByUserName(String username);
}
