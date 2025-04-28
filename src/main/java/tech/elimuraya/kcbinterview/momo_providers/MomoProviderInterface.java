package tech.elimuraya.kcbinterview.momo_providers;

import tech.elimuraya.kcbinterview.momo_providers.pojos.MomoRequestPayload;
import tech.elimuraya.kcbinterview.momo_providers.pojos.MomoResponsePayload;

/**
 * Interface containing methods for making API calls to a Momo(Mobile Money) provider
 *
 * @author eli.muraya
 * @date 28/04/2025/04/2025
 */
public interface MomoProviderInterface {

  /**
   * Initiate a B2C transaction to the Momo(Mobile Money) API
   *
   * @param requestPayload
   * @return
   */
  MomoResponsePayload initiateB2cTransaction(MomoRequestPayload requestPayload);
}
