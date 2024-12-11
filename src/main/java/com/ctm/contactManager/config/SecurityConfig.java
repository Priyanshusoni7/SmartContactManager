package com.ctm.contactManager.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.ctm.contactManager.services.implementation.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    //user create and login using java code with in memory service
    // public UserDetailsService userDetailsService() {
        
    //  UserDetails user1 = User
    //  .withDefaultPasswordEncoder()
    //  .Username("admin123")
    //  .password("admin123")
    //  .roles("ADMIN", "USER")
    //  .build();

    //     UserDetails user2 = User
    //     .withDefaultPasswordEncoder()
    //     .Username("user123")
    //     .password("user123")
    //     .roles("USER")
    //     .build();

    //     var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1, user2);
    //     return inMemoryUserDetailsManager;
    // }   

    @Autowired
    private SecurityCustomUserDetailService UserDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    AuthFailureHandler failureHandler;

    //checking-authenticating user from DB
    //can also say-> Configuration of authantication provider spring security
    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    { 
        //-> userdetailservice & PasswordEncoder set kiya h 

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        //userDetails service ka obj:
        daoAuthenticationProvider.setUserDetailsService(UserDetailService);

        //password encoder ka obj:
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        //url configuration (public urls, secure urls) [Routes manage]
        httpSecurity
            .authorizeHttpRequests(authorize->{
            //authorize.requestMatchers("/home","/register","/service").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        //when try to access user urls->(access denied)so we need loginPage(sign-up) for this:-
        httpSecurity.formLogin(formLogin ->{

            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/profile");
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            // formLogin.successHandler((request, response, authentication) -> {
            //     response.sendRedirect("/user/dashboard");
            // });

            formLogin.failureHandler(failureHandler);
        }); 

        
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        
        httpSecurity.logout(logoutForm ->{
            logoutForm.logoutUrl("/logout");
            //when user logout then redirect to login page
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        //oauth2 configuration
        httpSecurity.oauth2Login(oauth ->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}