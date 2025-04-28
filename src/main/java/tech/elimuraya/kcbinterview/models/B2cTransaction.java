package tech.elimuraya.kcbinterview.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author eli.muraya
 * @date 28/04/2025
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "b2c_transactions")
public class B2cTransaction {

  @Id @GeneratedValue private Long id;

  @Column(name = "transaction_id", nullable = false)
  private String transactionId;

  // phone number to receive the funds
  @Column(name = "phone_number", nullable = false, length = 12)
  private String phoneNumber;

  // amount to be sent to the customer
  @Column(name = "amount", nullable = false)
  private double amount;

  // narration of the transaction
  @Column(name = "narration", nullable = false, length = 100)
  private String narration;

  // the provider to use for the transaction
  private MomoProvider momoProvider;

  // narration of the transaction
  @Column(name = "user_id", nullable = false, length = 100)
  private String userId;

  @Column(name = "transaction_status")
  private TransactionStatus transactionStatus;

  // API response from the momo provider
  @Column(name = "api_response", length = 100)
  private String apiResponse;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "last_modified_at")
  private Instant lastModifiedAt;
}
