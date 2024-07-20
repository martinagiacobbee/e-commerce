package org.example.rodeodrivediner_webapp.configurations;


import lombok.RequiredArgsConstructor;
import org.example.rodeodrivediner_webapp.security.JwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity

@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationConverter jwtAuthConverter;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests((authz) ->
                        authz
                                //.requestMatchers(HttpMethod.GET, "/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/products/all").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/register", "users/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/products/search/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/products/paged/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/products/add").hasRole("Admin")
                                .requestMatchers(HttpMethod.DELETE, "/products/delete").hasRole("Admin")
                                .requestMatchers(HttpMethod.PUT, "/products/update").hasRole("Admin")

                                .requestMatchers(HttpMethod.GET, "/test/admin").hasRole("Admin")
                                .requestMatchers(HttpMethod.GET, "/users/getUser").hasRole("Admin")
                                .requestMatchers(HttpMethod.GET, "/users/getName").hasRole("Admin")
                                .requestMatchers(HttpMethod.DELETE, "/users/delete").hasRole("Admin")
                                .requestMatchers(HttpMethod.PUT, "/users/update").hasRole("Admin")
                                .requestMatchers(HttpMethod.GET, "/purchases/all/**").hasAnyRole("Admin", "User")

                                .requestMatchers(HttpMethod.GET, "/test/user").hasRole("User")
                                .requestMatchers(HttpMethod.POST, "/purchases/add/in").hasRole("User")
                                .requestMatchers(HttpMethod.PUT, "/purchases/update").hasRole("User")
                                .requestMatchers(HttpMethod.DELETE, "/purchases/delete").hasRole("User")
                               // .requestMatchers(HttpMethod.PUT, "/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .anyRequest().authenticated()
                );

        http.sessionManagement(sess -> sess.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        /*Il filtro CORS è importante perchè permette di inoltrare richieste http provenienti da domini diversi
        da quello entro cui risiede il backend. Non implementando il filtro, ogni richiesta proveniente da client
        situati su domini diversi verrebbe automaticamente bloccata.
         */

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        //configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedHeader("Access-Control-Allow-Headers");
        configuration.addAllowedHeader("Access-Control-Allow-Origin");
        configuration.addAllowedHeader("Access-Control-Allow-Methods");
        configuration.addExposedHeader("Authorization"); // Allowing specific headers to be exposed to the client
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

}

