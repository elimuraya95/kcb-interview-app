package tech.elimuraya.kcbinterview.sms_provider;

import tech.elimuraya.kcbinterview.pojos.ApiResponse;

/**
 * @author eli.muraya
 * @date 28/04/2025/04/2025
 */
public interface SmsProvider {

  /**
   * Send an SMS to the customer initiating a B2C transaction
   *
   * @param phoneNumber
   * @param message
   * @return
   */
  ApiResponse sendSms(String phoneNumber, String message);
}
