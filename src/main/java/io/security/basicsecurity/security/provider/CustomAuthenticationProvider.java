package io.security.basicsecurity.security.provider;

import io.security.basicsecurity.security.common.FormWebAuthenticationDetails;
import io.security.basicsecurity.security.service.AccountContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired private UserDetailsService userDetailsService;
    @Autowired private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // login할때 입력한 username & password
        final String username = authentication.getName();
        final String password = (String)authentication.getCredentials();

        // 검증 작업
        final AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(username);

        // password == accountContext 패스워드 일치하는지 검증
        // .matches(사용자 입력한 password, DB에 저장된 password 정보)
        if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // secret key 검증 (IF 포함되면 인증, ELSE 실퍠)
        final FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
        final String secretKey = formWebAuthenticationDetails.getSecretKey();
        if (secretKey == null || !secretKey.equals("secret")) {
            throw new InsufficientAuthenticationException("Invalid Secret");
        }

        return new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
