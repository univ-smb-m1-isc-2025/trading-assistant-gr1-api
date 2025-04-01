package me.trading_assistant.api.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findById(Long account_id);
    void deleteById(Long account_id);
    Optional<Account> findByEmail(String email);

}

