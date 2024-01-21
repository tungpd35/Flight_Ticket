package com.example.airlineticket.security;

import com.example.airlineticket.models.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/","agency/**","*/login","/agency/register","airline/register","/dashboard/**").permitAll()
                .requestMatchers("/agency/**").hasRole("AGENCY")
                .requestMatchers("/airline/**").hasRole("AIRLINE")
                .anyRequest().permitAll()
                .and().formLogin(form-> form.loginPage("/login").loginProcessingUrl("/login").usernameParameter("email").passwordParameter("password").successHandler(new SavedRequestAwareAuthenticationSuccessHandler(){
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
                        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                        System.out.println(customUserDetails.getUsername());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String jwt = jwtTokenProvider.generateToken(customUserDetails);
                        Cookie cookie = new Cookie("token",jwt);
                        cookie.setHttpOnly(true);
                        cookie.setMaxAge(60*60*24*7);
                        response.addCookie(cookie);
                        super.onAuthenticationSuccess(request, response, authentication);
                    }
                                                                                                                                          }
                )).logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).build();
    }
}
