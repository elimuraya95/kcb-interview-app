package tech.elimuraya.kcbinterview.momo_providers.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.elimuraya.kcbinterview.momo_providers.MomoProviderInterface;
import tech.elimuraya.kcbinterview.momo_providers.pojos.MomoRequestPayload;
import tech.elimuraya.kcbinterview.momo_providers.pojos.MomoResponsePayload;

/**
 * Implementation of Airtel money API integration
 * todo - This is to be implemented by fellow team mate later, as per the instructions
 *
 * @author eli.muraya
 * @date 28/04/2025
 */
@Service
public class AirtelApiService implements MomoProviderInterface {
  private static final Logger logger = LoggerFactory.getLogger(AirtelApiService.class);

  /**
   * Initiate a B2C transaction to the Momo(Mobile Money) API
   *
   * @param requestPayload
   * @return
   */
  @Override
  public MomoResponsePayload initiateB2cTransaction(MomoRequestPayload requestPayload) {
    logger.info("Initiation B2c transaction through Airtel API: {}", requestPayload);

    var response = new MomoResponsePayload();
    response.setStatus(200);
    response.setMessage("Initiated B2C Transaction");
    response.setData(requestPayload);

    logger.info("Completed B2c transaction through Airtel API: {}", response);
    return response;
  }
}
