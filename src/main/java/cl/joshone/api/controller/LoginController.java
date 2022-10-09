package cl.joshone.api.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cl.joshone.api.domain.User;
import cl.joshone.api.model.UserAuth;
import cl.joshone.api.model.UserAuthResponse;
import cl.joshone.api.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@CrossOrigin("*")
@RestController
public class LoginController {
	
	@Autowired
	private UserRepository userRepositoryr;
	
	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserAuthResponse> login (@RequestBody UserAuth userAuth) {
		
		String token = getJWTToken(userAuth.getUser(), userAuth.getPasswd());
		
		if (token == null) {
			return new ResponseEntity<UserAuthResponse> (HttpStatus.UNAUTHORIZED);
		}
		
		UserAuthResponse userAuthResp = UserAuthResponse.builder()
				.token(token)
				.build();
		
		return new ResponseEntity<UserAuthResponse> (userAuthResp, HttpStatus.OK);
	}

	private String getJWTToken(String username, String password) {
		String secretKey = "J0shIsC00l!-asdk!!!32xzQWDASasd_as";
		
		Optional<User> optUser = userRepositoryr.findByEmail(username);
		
		User user = null;
		if (optUser.isPresent()) {
			user = optUser.get();
		} else {
			return null;
		}
		
		if (!password.equals(user.getPassword())) {
			return null;
		}
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("rol", user.getRol())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS256,
						secretKey.getBytes()).compact();

		return token;
	}
	
}
