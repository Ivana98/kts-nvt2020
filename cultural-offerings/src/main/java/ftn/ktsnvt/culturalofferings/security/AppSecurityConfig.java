package ftn.ktsnvt.culturalofferings.security;

import ftn.ktsnvt.culturalofferings.security.jwt.JwtConfig;
import ftn.ktsnvt.culturalofferings.security.jwt.JwtIssuerFilter;
import ftn.ktsnvt.culturalofferings.security.jwt.JwtVerifierFilter;
import ftn.ktsnvt.culturalofferings.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserDetailsService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public AppSecurityConfig(
            PasswordEncoder passwordEncoder,
            ApplicationUserDetailsService applicationUserService,
            SecretKey secretKey,
            JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                .authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/auth/register").permitAll()
                .antMatchers("/auth/confirm-registration").permitAll()
                .antMatchers("/auth/resend-token").permitAll()
                .antMatchers("/cultural-offerings/{id}").permitAll()
                .antMatchers("/cultural-offerings/by-page").permitAll()
                .antMatchers("/cultural-offerings/search-filter/by-page/guest").permitAll()
                .antMatchers("/cultural-offerings-types/by-page").permitAll()
                .antMatchers("/cultural-offering-subtypes/byTypeId/{typeId}").permitAll()
                .antMatchers("/subscriptions/all/query").permitAll()
                .antMatchers("/comments/by-page/{culturalOfferingId}").permitAll()
                .antMatchers("/rating/by-page/{culturalOfferingId}").permitAll()
                .antMatchers("/users/{id}").permitAll()
                .antMatchers("/images/{imageId}").permitAll()
                
                .anyRequest().authenticated()

                .and()

                .cors()
                .and()

                .addFilter(new JwtIssuerFilter(authenticationManager(), jwtConfig, secretKey, "/auth/login", "email"))
                .addFilterBefore(new JwtVerifierFilter(jwtConfig, secretKey), JwtIssuerFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        /*
            Custom filters will ignore register endpoint
         */
        web.ignoring().antMatchers(HttpMethod.POST,
                "/auth/register", "/auth/confirm-registration", "/auth/resend-token", "/cultural-offerings/search-filter/by-page/guest")
                .antMatchers(HttpMethod.GET,
                "/cultural-offerings/by-page", "/cultural-offerings-types/by-page", "/cultural-offering-subtypes/byTypeId/{typeId}", "/subscriptions/all/query");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
