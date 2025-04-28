package tech.elimuraya.kcbinterview.pojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tech.elimuraya.kcbinterview.models.MomoProvider;

/**
 * @author eli.muraya
 * @date 28/04/2025
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class InitiateB2cTransaction {
  // phone number to receive the funds
  private String phoneNumber;

  // amount to be sent to the customer
  private double amount = 0.0;

  // narration of the transaction
  private String narration;

  // the provider to use for the transaction
  private MomoProvider momoProvider = MomoProvider.UNDEFINED;
}
