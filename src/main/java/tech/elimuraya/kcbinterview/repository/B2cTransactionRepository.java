package tech.elimuraya.kcbinterview.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.elimuraya.kcbinterview.models.B2cTransaction;

/**
 * Repository class for B2C transactions
 * @author eli.muraya
 * @date 28/04/2025
 */
@Repository
public interface B2cTransactionRepository extends JpaRepository<B2cTransaction, Long> {

	Optional<B2cTransaction> findByTransactionId(String transactionId);
}
