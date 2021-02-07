package org.kuehnenagel.repository;

import org.kuehnenagel.model.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Integer> {
    /**
     * Lock is used for preventing from update the same row twice for the same user
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "10000")})
    Optional<WalletEntity> findById(int id);
    
    
}
