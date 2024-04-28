package com.RS.SMS.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {
//This was for H2 database. later we are doing with SQL database.
//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
//        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
//
// This was for H2 database
// http.csrf((csrf) -> csrf.ignoringRequestMatchers(mvcMatcherBuilder.pattern("/saveMsg"))
//                        .ignoringRequestMatchers(PathRequest.toH2Console()))
//        Now we are doing with sql database
//
//        http.csrf((csrf) -> csrf.ignoringRequestMatchers("/saveMsg"))
//                .authorizeHttpRequests((requests) -> requests.requestMatchers("/dashboard").authenticated()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/displayMessages")).hasRole("ADMIN")
//                        .requestMatchers(mvcMatcherBuilder.pattern("/closeMsg/**")).hasRole("ADMIN")
//                        .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/home")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/holidays/**")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/contact")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/saveMsg")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/courses")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/about")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/assets/**")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/logout")).permitAll()
//                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
//                        .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll())
//                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/login?logout=true")
//                        .invalidateHttpSession(true).permitAll())
//                .httpBasic(Customizer.withDefaults());
//
//        http.headers(headersConfigurer -> headersConfigurer
//                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
//
//        return http.build();
//
//    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.ignoringRequestMatchers("/saveMsg").ignoringRequestMatchers("/public/**")
                        .ignoringRequestMatchers("/api/**").ignoringRequestMatchers("/data-api/**").ignoringRequestMatchers("/sms/actuator/**"))
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/displayMessages/**").hasRole("ADMIN")
                        .requestMatchers("/closeMsg/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/sms/actuator/**").hasRole("ADMIN")

                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/data-api/**").authenticated()
                        .requestMatchers("/displayProfile").authenticated()
                        .requestMatchers("/updateProfile").authenticated()
                        .requestMatchers("/student/**").hasRole("STUDENT")

                        /*.requestMatchers("/profile/**").permitAll()
                        .requestMatchers("/courseses/**").permitAll()
                        .requestMatchers("/contacts/**").permitAll()*/

                        // .requestMatchers("/data-api/**").permitAll() //for hal explorer
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/holidays/**").permitAll()

                        .requestMatchers("/contact").permitAll()
                        .requestMatchers("/saveMsg").permitAll()
                        .requestMatchers("/courses").permitAll()
                        .requestMatchers("/about").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/public/**").permitAll())
                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
                        .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll())
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true).permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }




//      We used this before having DB authenticate_user feature
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("12345")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("Shukla")
//                .password("12345678")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    //Implementing Bycrypt encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    }

