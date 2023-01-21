package pl.gdyzapomne.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Konfiguracja serwera zasobów dla oAuth2
 * oAuth2 wymaga, żeby skonfigurować serwer zasobów - czyli określić do jakich zasobów mają jakie role dostęp
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * Tu są ustawione uprawnienia do endpointów dla podanych ról.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.headers()
                .frameOptions()
                .disable()
                .and()
                .authorizeRequests()
                .antMatchers("/","/post","/register","/login","/redactors", "/about","/showUser","/postList","/mails/contact", "users/getName").permitAll()
                .antMatchers("/mails/subscribe").authenticated()
                .antMatchers("/users").hasAuthority("ADMIN")
                .antMatchers("/userProfile", "/posts/comments/").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/posts/comments/**").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/posts/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/user/**").hasAuthority("ADMIN");
    }
}