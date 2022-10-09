package cl.joshone.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.joshone.api.domain.User;
import cl.joshone.api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UsuarioController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> all(@RequestHeader("Authorization") String auth) {
		String token = auth.split(" ")[1];
		try {
			if (!validateToken(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<List<User>>(userRepository.findAll(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> save(@RequestHeader("Authorization") String auth, @RequestBody User user) {
		
		String token = auth.split(" ")[1];
		
		try {
			if (!validateToken(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Optional<User> optUser = userRepository.findByEmail(user.getEmail());
		
		if (optUser.isPresent()) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<User>(userRepository.save(user), HttpStatus.OK);
	}
	
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> update(@RequestHeader("Authorization") String auth, @RequestBody User user) {
		String token = auth.split(" ")[1];
		
		try {
			if (!validateToken(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Optional<User> optUser = userRepository.findById(user.getId());
		User fUser = null;
		if (optUser.isPresent()) {
			fUser = optUser.get();
			
			fUser.setNombre(user.getNombre());
			fUser.setApellido(user.getApellido());
			fUser.setEmail(user.getEmail());
			fUser.setPassword(user.getPassword());
			fUser.setGenero(user.getGenero());
			fUser.setRol(user.getRol());
			
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<User>(userRepository.save(fUser), HttpStatus.OK);
	} 
	
	@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> delete(@RequestHeader("Authorization") String auth, @PathVariable("id") Long id) {
		String token = auth.split(" ")[1];
		
		try {
			if (!validateToken(token)) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Optional<User> optUser = userRepository.findById(id);
		
		if (optUser.isPresent()) {
			userRepository.delete(optUser.get());
			return new ResponseEntity<User>(HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}
	
	private boolean validateToken (String token) {
		String secretKey = "J0shIsC00l!-asdk!!!32xzQWDASasd_as";
		String rol = "";
		Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
		if (claims.get("rol") != null) {
			rol = (String)claims.get("rol");
		}
		
		return rol.equals("admin");
	}

}
