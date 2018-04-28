package de.deadlocker8.budgetmaster.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.inMemoryAuthentication().withUser("Default").password("1233").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.and()

			.authorizeRequests()
			.antMatchers("/static/**").permitAll()
			.antMatchers("/accounts/**").authenticated()
			.antMatchers("/payments/**").authenticated()
			.antMatchers("/charts/**").authenticated()
			.antMatchers("/reports/**").authenticated()
			.antMatchers("/categories/**").authenticated()
			.antMatchers("/settings/**").authenticated()
			.antMatchers("/about/**").authenticated()
			.and()

			.formLogin()
			.loginPage("/login")
			.successHandler((req, res, auth) -> redirectStrategy.sendRedirect(req, res, "/"))
			.failureUrl("/login?error=true")
			.permitAll()
			.and()

			.logout()
				.permitAll();
	}
}
