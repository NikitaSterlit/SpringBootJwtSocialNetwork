package com.nikitasterlit.jwtauthentication.controller;

import com.nikitasterlit.jwtauthentication.message.request.LoginForm;
import com.nikitasterlit.jwtauthentication.message.request.SignUpForm;
import com.nikitasterlit.jwtauthentication.message.response.JwtResponse;
import com.nikitasterlit.jwtauthentication.model.Role;
import com.nikitasterlit.jwtauthentication.model.RoleName;
import com.nikitasterlit.jwtauthentication.model.User;
import com.nikitasterlit.jwtauthentication.repository.RoleRepository;
import com.nikitasterlit.jwtauthentication.repository.UserRepository;
import com.nikitasterlit.jwtauthentication.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController // управляющая конфигурация
@RequestMapping("/api/auth") // конфигурация работаюя по следующему урлу
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

   // прокомментировать/ уметь работать в Postman todo
    @PostMapping("/signin") //  АУТЕНТИФИКАЦИЯ метод post по Урл адресу
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) { //запрос в оболочке с валидными полями тела

        Authentication authentication = authenticationManager.authenticate( //
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword() // передача в обьект аутентификации логина и пароля
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication); // передача обьекта аутентификации

        String jwt = jwtProvider.generateJwtToken(authentication); // генерируем токен
        return ResponseEntity.ok(new JwtResponse(jwt)); // отправляем токен обратно пользователю
    }

    // прокомментировать/ уметь работать в Postman todo
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) { // запрос на службу с валидными полями класса
        if(userRepository.existsByUsername(signUpRequest.getUsername())) { //если имя пользлователя исользуется (уже есть в базе данных)
            return new ResponseEntity<String>("Fail -> Username is already taken!", // отправка ответа с текстовым телом
                    HttpStatus.BAD_REQUEST);// и http статусом
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) { //если емаил исользуется (уже есть в базе данных)
            return new ResponseEntity<String>("Fail -> Email is already in use!", // отправка ответа с текстовым телом
                    HttpStatus.BAD_REQUEST); // и http статусом
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), // создание Пользователя с именем логином
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword())); // паролем и почтой

        Set<String> strRoles = signUpRequest.getRole(); // получаем роль от шаблона регистрации
        Set<Role> roles = new HashSet<>(); // создаем множество ролей БАЗЫ ДАННЫХ

        strRoles.forEach(role -> {
        	switch(role) { // добавление соответсвующей роли пользователю из шаблона авторизации
	    		case "admin":
	    			Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	    			roles.add(adminRole);
	    			
	    			break;
	    		case "pm":
	            	Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	            	roles.add(pmRole);
	            	
	    			break;
	    		default:
	        		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	        		roles.add(userRole);        			
        	}
        });
        
        user.setRoles(roles); // передать юзеру роль
        userRepository.save(user); // сохранить пользователя

        return ResponseEntity.ok().body("User registered successfully!");
    }
}