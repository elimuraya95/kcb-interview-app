package tech.elimuraya.kcbinterview.momo_providers.pojos;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author eli.muraya
 * @date 28/04/2025/04/2025
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class MomoRequestPayload {
	// phone number to receive the funds
	private String phoneNumber;

	// amount to be sent to the customer
	private double amount;

	// narration of the transaction
	private String narration;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MomoRequestPayload that = (MomoRequestPayload) o;
		return Double.compare(that.amount, amount) == 0 &&
				Objects.equals(phoneNumber, that.phoneNumber) &&
				Objects.equals(narration, that.narration);
	}

	@Override
	public int hashCode() {
		return Objects.hash(phoneNumber, amount, narration);
	}

}

