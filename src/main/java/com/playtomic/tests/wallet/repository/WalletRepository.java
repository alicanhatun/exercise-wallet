package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.projection.WalletProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByIdentifier(String identifier);

    Optional<WalletProjection> findWalletProjectionByIdentifier(String identifier);

    @Modifying
    @Query("UPDATE Wallet w SET w.balance = (w.balance + :amount) WHERE w.identifier = :identifier")
    void updateWalletBalanceByIdentifier(@Param("identifier") String identifier,
                                         @Param("amount") BigDecimal amount);

}
