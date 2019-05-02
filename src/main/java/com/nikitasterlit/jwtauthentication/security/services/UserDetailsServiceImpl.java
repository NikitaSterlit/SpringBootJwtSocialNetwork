package com.nikitasterlit.jwtauthentication.security.services;

import com.nikitasterlit.jwtauthentication.model.User;
import com.nikitasterlit.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { //

    @Autowired
    UserRepository userRepository; //

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	
        User user = userRepository.findByUsername(username) // ищем пользователя
                	.orElseThrow(() -> // если нулевой
                        new UsernameNotFoundException("User Not Found with -> username or email : " + username) // выбрасываем исключение
        );

        return UserPrinciple.build(user); // обьект user перегоняем в класс оболочку UserPrinciple
    }
}