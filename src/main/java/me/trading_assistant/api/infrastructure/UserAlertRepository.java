package me.trading_assistant.api.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlert, Long> {
    List<UserAlert> findByUserId(Long userId);
    List<UserAlert> findBySymbol(String symbol);
    void deleteById(Long id);
}