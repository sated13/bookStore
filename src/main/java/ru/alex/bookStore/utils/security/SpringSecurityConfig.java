package ru.alex.bookStore.utils.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import ru.alex.bookStore.utils.LogService;
import ru.alex.bookStore.utils.users.ActiveUserRepository;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")).accessDeniedPage("/accessDenied")
                .and()
                .authorizeRequests()
                .antMatchers("/VAADIN/**", "/PUSH/**", "/login",
                        "/login/**", "/error/**", "/accessDenied/**",
                        "/vaadinServlet/**", "/register", "/main")
                .permitAll()
                .antMatchers("/adminPanel", "/adminPanel/**", "/logConfigurator",
                        "/logConfigurator/**", "/h2-console", "/h2-console/**").hasAuthority("admin")
                .antMatchers("/**").fullyAuthenticated()
                .and()
                .logout().permitAll();
        /*http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);*/
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /*@Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }*/

    @Bean
    public ActiveUserRepository activeUserRepository() {
        return new ActiveUserRepository();
    }

    /*@Bean
    public LogService logService() {
        return new LogService();
    }*/
}
