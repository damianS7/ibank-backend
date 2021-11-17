package com.ibank;

import com.ibank.user.User;
import com.ibank.user.UserRepository;
import com.ibank.user.UserService;
import com.ibank.user.exception.EmailTakenException;
import com.ibank.user.exception.UsernameTakenException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class IBankApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {

		Optional<User> u = userRepository.findByUsername("demo");
		System.out.println(u.isPresent());

		User user = new User(0L, "demo", "demo@gmail.com", "demo");

		try {
			userService.register(user);
		} catch (EmailTakenException e) {
			e.printStackTrace();
		} catch (UsernameTakenException e) {
			e.printStackTrace();
		}

		u = userRepository.findByUsername("demo2");
		System.out.println(u.isPresent());
	}

}
