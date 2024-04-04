package org.ssafy.demotravel.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account save(Account account);

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
}
