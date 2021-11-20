package io.security.basicsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * spring-security 의존성을 추가하고 root 페이지 요청 시 로그인 폼 페이지가 뜨는 이유
     * WebSecurityConfigureAdapter Class의 configure 함수때문
     * 아래의 설정으로 초기화 되기 때문이다.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 인가 정책
        http
                .authorizeRequests()           // 요청에 의한 보안을 실행한다.
                .anyRequest().authenticated(); // 모든 요청을 인증한다.

        // 인증 정책
        http
                .formLogin()             // formLogin 방식과 httpBasic 방식이 있다. (fromLogin 방식 사용)
                .loginPage("/loginPage") // 로그인 페이지 url (기본으로 제공되는 로그인 화면 페이지도 있다.)
                .defaultSuccessUrl("/")  // 로그인 성공시 url (우선순위 마지막)
                .failureUrl("/login")    // 로그인 실패시 url (우선순위 마지막)
                .usernameParameter("userId") // id 파라미터 명
                .passwordParameter("passwd") // password 파라미터 명
                .loginProcessingUrl("/login_proc") // form의 action 경로 rul
                .successHandler(new AuthenticationSuccessHandler() { // 기본적으로 성공시 success 핸들러를 호출한다. 이처럼 생성하여 직접 구현이 가능하다.
                    // 로그인 성공시 authentication 정보를 매개변수로 받아서 ~
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication" + authentication.getName());
                        response.sendRedirect("/");
                    }
                })
                // 기본저그올 실패시 fail 핸들러를 호출한다. 이처럼 생성하여 직접 구현이 가능하다.
                .failureHandler(new AuthenticationFailureHandler() {
                    // 로그인 실패시 exception 정보를 매개변수로 받아서 ~
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("exception" + exception.getMessage());
                        response.sendRedirect("/login");
                    }
                })
                .permitAll(); // login 화면은 인증없이 누구나 접근이 가능해야 한다.
    }
}
