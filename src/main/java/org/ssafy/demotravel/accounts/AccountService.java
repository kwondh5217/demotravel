package org.ssafy.demotravel.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .roles(String.valueOf(account.getRole()))
                .build();
    }

    public Account save(Account account) {
        boolean existsByEmail = accountRepository.existsByEmail(account.getEmail());
        if(existsByEmail){
            throw new IllegalArgumentException("중복된 이메일이 존재합니다");
        }
        account.encodePassword(passwordEncoder, account.getPassword());
        return accountRepository.save(account);
    }
}
