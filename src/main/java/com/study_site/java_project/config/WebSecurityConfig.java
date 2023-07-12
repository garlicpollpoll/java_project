package com.study_site.java_project.config;

import com.study_site.java_project.oauth.CustomOAuth2UserService;
import com.study_site.java_project.principal.PrincipalDetailService;
import com.study_site.java_project.web.repository.JpaPersistentTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailService principalDetailService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JpaPersistentTokenRepository tokenRepository;
    private final AppAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests().antMatchers(
                        "/", "/css/**", "/image/**", "/js/**", "/h2-console/**", "/login", "/auth/loginProc",
                        "/join"
                ).permitAll()
                .antMatchers("/chat/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/").loginProcessingUrl("/auth/loginProc")
                .and()
                .oauth2Login().loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);

        http.rememberMe()
                .key("remember")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(86400 * 30)
                .userDetailsService(principalDetailService)
                .tokenRepository(tokenRepository)
                .authenticationSuccessHandler(authenticationSuccessHandler);

        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);
    }
}
