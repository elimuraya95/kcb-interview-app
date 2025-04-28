package tech.elimuraya.kcbinterview.sms_provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.elimuraya.kcbinterview.pojos.ApiResponse;

/**
 * Implementation for sending SMMs with the Africas Talking APIs
 *
 * <p>todo - This is to be implemented by fellow team mate later, as per the instructions
 *
 * @author eli.muraya
 * @date 28/04/2025
 */
@Service
public class AfricasTalkingApiService implements SmsProvider {
  private static final Logger logger = LoggerFactory.getLogger(AfricasTalkingApiService.class);

  /**
   * Send an SMS to the customer initiating a B2C transaction
   *
   * @param phoneNumber
   * @param message
   * @return
   */
  @Override
  public ApiResponse sendSms(String phoneNumber, String message) {
    logger.info("SMS sent successfully: {}", phoneNumber);
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setData(null);
    apiResponse.setMessage("SMS sent successfully");
    apiResponse.setCode(200);
    return apiResponse;
  }
}
