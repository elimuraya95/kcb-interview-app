package tech.elimuraya.kcbinterview.momo_providers.pojos;

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
public class MomoResponsePayload {
  // Status code received from the momo provider API
  private int status;

  // Message received from the momo provider API
  private String message;

  // Data received from the momo provider API
  private MomoRequestPayload data;
}
