package com.nikitasterlit.jwtauthentication.security;

import com.nikitasterlit.jwtauthentication.security.jwt.JwtAuthEntryPoint;
import com.nikitasterlit.jwtauthentication.security.jwt.JwtAuthTokenFilter;
import com.nikitasterlit.jwtauthentication.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService; // контейнер для хранения пользователей

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler; // выбрасывает исключения в случае форс мажора

    @Bean// будет храниться в контексте спринга
    public JwtAuthTokenFilter authenticationJwtTokenFilter() { // проверяет токен
        return new JwtAuthTokenFilter();// создает обьект
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)// передаем контейнер с пользователями
                .passwordEncoder(passwordEncoder());// класс способный зашифровать пароль
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()// авторизировать все запросы!
                .antMatchers("/api/auth/**").permitAll() // по этому адресу пускать всех
                .anyRequest().authenticated() // пользователи по всем запросам должн проходить аутентиикацию
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() // кто обрабатвает исключения
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // как работать с ссесиями
        
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // какой фильтр подключить
    }
}