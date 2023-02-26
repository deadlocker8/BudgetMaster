package de.deadlocker8.budgetmaster.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig
{
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private final PreLoginUrlBlacklist preLoginUrlBlacklist = new PreLoginUrlBlacklist();

	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http
				.csrf()
				.and()

				.authorizeHttpRequests()
				.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/touch_icon.png").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/**").authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.successHandler((req, res, auth) -> {
					Object preLoginURL = req.getSession().getAttribute("preLoginURL");
					if(preLoginURL == null || preLoginUrlBlacklist.isBlacklisted(preLoginURL.toString()))
					{
						preLoginURL = "/";
					}
					redirectStrategy.sendRedirect(req, res, preLoginURL.toString());
				})
				.permitAll()
				.and()

				.logout()
				.permitAll();

		return http.build();
	}
}