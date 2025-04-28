package tech.elimuraya.kcbinterview.momo_providers.implementations;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.elimuraya.kcbinterview.momo_providers.MomoProviderInterface;
import tech.elimuraya.kcbinterview.momo_providers.pojos.MomoRequestPayload;
import tech.elimuraya.kcbinterview.momo_providers.pojos.MomoResponsePayload;
import tech.elimuraya.kcbinterview.services.B2cTransactionService;

/**
 * Implementation of Mpesa money API integration
 * todo - This is to be implemented by fellow team mate later, as per the instructions
 *
 * @author eli.muraya
 * @date 28/04/2025
 */
@Service
public class MpesaApiService implements MomoProviderInterface {
	private static final Logger logger = LoggerFactory.getLogger(MpesaApiService.class);


	/**
   * Initiate a B2C transaction to the Momo(Mobile Money) API
   *
   * @param requestPayload
   * @return
   */
  @Override
  public MomoResponsePayload initiateB2cTransaction(MomoRequestPayload requestPayload) {
		logger.info("Initiation B2c transaction through MPESA API: {}", requestPayload);

		var response =  new MomoResponsePayload();
		response.setStatus(200);
		response.setMessage("Initiated B2C Transaction");
		response.setData(requestPayload);

		logger.info("Completed B2c transaction through MPESA API: {}", response);
		return response;
  }
}
