package org.cahsmun.registration;

import org.cahsmun.registration.authentication.JwtAuthenticationEntryPoint;
import org.cahsmun.registration.authentication.JwtRequestFilter;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private DataSource dataSource;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthManagerBuilder provides various authentication mechanism: DB, LDAP, in-memory.
     * Configure AuthenticationManager so that it knows from where to load user for matching credentials. (the '.inMemoryAuthentication')
     *
     * Default BCrypt rounds: 10
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());

        /*
        auth.inMemoryAuthentication().withUser(configUtil.getProperty("spring.security.user.name"))
                .password("{noop}" + configUtil.getProperty("spring.security.user.password")).roles("ADMIN");
                */
    }


    /**
     * For DEV purpose, permit all traffic and disable security.
     * http.authorizeRequests().antMatchers("/").permitAll();
     * <p>
     * For Basic Authentication Security,
     * http
     * .authorizeRequests()
     * .anyRequest().authenticated()
     * .and()
     * .formLogin().and()
     * .httpBasic().and()
     * .cors();
     * <p>
     * Make sure we use stateless session; session won't be used to store user's state.
     * Add a filter to validate the tokens with every request.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().and()
                .csrf().disable()
                // Don't authenticate this particular request
                .authorizeRequests().antMatchers("/token/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/login/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/sponsorregistration/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/registration/**").permitAll()
                // All other request need to be authenticated
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
