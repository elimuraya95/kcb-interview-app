package tech.elimuraya.kcbinterview.services;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tech.elimuraya.kcbinterview.models.B2cTransaction;
import tech.elimuraya.kcbinterview.models.MomoProvider;
import tech.elimuraya.kcbinterview.models.TransactionStatus;
import tech.elimuraya.kcbinterview.momo_providers.implementations.AirtelApiService;
import tech.elimuraya.kcbinterview.momo_providers.implementations.MpesaApiService;
import tech.elimuraya.kcbinterview.momo_providers.pojos.MomoRequestPayload;
import tech.elimuraya.kcbinterview.momo_providers.pojos.MomoResponsePayload;
import tech.elimuraya.kcbinterview.pojos.ApiResponse;
import tech.elimuraya.kcbinterview.pojos.InitiateB2cTransaction;
import tech.elimuraya.kcbinterview.repository.B2cTransactionRepository;
import tech.elimuraya.kcbinterview.sms_provider.AfricasTalkingApiService;

/**
 * @author eli.muraya
 * @date 28/04/2025/04/2025
 */
@Service
@AllArgsConstructor
public class B2cTransactionService {
  private static final Logger logger = LoggerFactory.getLogger(B2cTransactionService.class);

  private final MpesaApiService mpesaApiService;
  private final AirtelApiService airtelApiService;
  private final B2cTransactionRepository b2cTransactionRepository;
  private final AfricasTalkingApiService smsApiService;

  /**
   * Initiates a B2c Transaction to a given phone number
   *
   * @param initiateB2cTransaction payload used to process the B2c request
   * @return
   */
  public ApiResponse<B2cTransactionResponse> initiateB2cTransaction(
      InitiateB2cTransaction initiateB2cTransaction) {
    logger.info("B2c transaction initiated: {}", initiateB2cTransaction);
    // Validate the payload
    var validationResponse = validateB2cTransaction(initiateB2cTransaction);
    if (validationResponse.getCode() != 200) {
      logger.info("Validation of the received B2c transaction failed: {}", validationResponse);
      return validationResponse;
    }

    // Persist the transaction
    var b2cTransaction = new B2cTransaction();
    b2cTransaction.setTransactionId(UUID.randomUUID().toString());
    b2cTransaction.setPhoneNumber(initiateB2cTransaction.getPhoneNumber());
    b2cTransaction.setAmount(initiateB2cTransaction.getAmount());
    b2cTransaction.setNarration(initiateB2cTransaction.getNarration());
    b2cTransaction.setMomoProvider(initiateB2cTransaction.getMomoProvider());
    // todo - fetch the user is from the token
    b2cTransaction.setUserId(UUID.randomUUID().toString());
    b2cTransaction.setTransactionStatus(TransactionStatus.IN_PROGRESS);
    b2cTransaction.setCreatedAt(Instant.now());
    b2cTransaction.setLastModifiedAt(Instant.now());
    b2cTransaction = b2cTransactionRepository.save(b2cTransaction);
    logger.info("B2c transaction persisted successfully: {}", b2cTransaction);


    // Initiate the transaction
    var momoApiRequest = new MomoRequestPayload();
    momoApiRequest.setAmount(initiateB2cTransaction.getAmount());
    momoApiRequest.setPhoneNumber(initiateB2cTransaction.getPhoneNumber());
    momoApiRequest.setNarration(initiateB2cTransaction.getNarration());

    MomoResponsePayload momoResponsePayload = null;

    switch (initiateB2cTransaction.getMomoProvider()) {
      case MPESA -> momoResponsePayload = mpesaApiService.initiateB2cTransaction(momoApiRequest);
      case AIRTEL -> momoResponsePayload = airtelApiService.initiateB2cTransaction(momoApiRequest);
    }

    // Update the transaction Status
    if (momoResponsePayload.getStatus() == 200) {
      b2cTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

    } else {
      b2cTransaction.setTransactionStatus(TransactionStatus.FAILED);
    }
    b2cTransaction.setApiResponse(momoResponsePayload.getMessage());
    b2cTransaction = b2cTransactionRepository.save(b2cTransaction);

    // Send the SMS
    if (momoResponsePayload.getStatus() == 200) {
      smsApiService.sendSms(
          b2cTransaction.getPhoneNumber(),
          "B2C transaction of KES "
              .concat(String.valueOf(b2cTransaction.getAmount()))
              .concat(" to the phone number ")
              .concat(b2cTransaction.getPhoneNumber())
              .concat(" been completed sucessfully!"));
    } else {
      smsApiService.sendSms(
          b2cTransaction.getPhoneNumber(),
          "B2C transaction of KES "
              .concat(String.valueOf(b2cTransaction.getAmount()))
              .concat(" to the phone number ")
              .concat(b2cTransaction.getPhoneNumber())
              .concat(" failed! Kindly contact the customer care for assitance"));
    }


    // Return a response to the calling client
    var apiResponse = new ApiResponse<B2cTransactionResponse>();
    B2cTransactionResponse b2cResponse =
        new B2cTransactionResponse(
            b2cTransaction.getTransactionId(),
            b2cTransaction.getPhoneNumber(),
            b2cTransaction.getAmount(),
            b2cTransaction.getNarration(),
            b2cTransaction.getMomoProvider());

    if (momoResponsePayload.getStatus() == 200) {
      apiResponse.setCode(200);
      apiResponse.setMessage("Processed B2C transaction successfully!");
      apiResponse.setData(b2cResponse);
    } else {
      apiResponse.setCode(momoResponsePayload.getStatus());
      apiResponse.setMessage("Failed to process B2C transaction!");
      apiResponse.setData(b2cResponse);
    }
    return apiResponse;
  }

  /**
   * Fetch a B2c Transaction, given a transaction ID
   *
   * @param transactionId
   * @return
   */
  public ApiResponse<B2cTransactionResponse> fetchB2cTransaction(String transactionId) {
    var apiResponse = new ApiResponse<B2cTransactionResponse>();

    if (transactionId == null || transactionId.isEmpty()) {
      apiResponse.setCode(400);
      apiResponse.setMessage("Invalid transaction id provided!");
      return apiResponse;
    }

    var b2cTransaction = b2cTransactionRepository.findByTransactionId(transactionId);
    if (b2cTransaction.isEmpty()) {
      apiResponse.setCode(404);
      apiResponse.setMessage("Invalid transaction id provided!");
      return apiResponse;
    }

    B2cTransactionResponse b2cResponse =
        new B2cTransactionResponse(
            b2cTransaction.get().getTransactionId(),
            b2cTransaction.get().getPhoneNumber(),
            b2cTransaction.get().getAmount(),
            b2cTransaction.get().getNarration(),
            b2cTransaction.get().getMomoProvider());

    apiResponse.setCode(200);
    apiResponse.setMessage("Fetched B2C transaction successfully!");
    apiResponse.setData(b2cResponse);
    return apiResponse;
  }

  /**
   * Validate the request to initiate the transaction
   *
   * @param initiateB2cTransaction
   * @return
   */
  public ApiResponse<B2cTransactionResponse> validateB2cTransaction(
      InitiateB2cTransaction initiateB2cTransaction) {
    var response = new ApiResponse<B2cTransactionResponse>();
    // Validate phone number is provided
    if (StringUtils.isEmpty(initiateB2cTransaction.getPhoneNumber())) {
      response.setCode(400);
      response.setMessage("Phone number is required");
      return response;
    }

    // Validate the amount is greater than 0
    if (initiateB2cTransaction.getAmount() <= 0.0) {
      response.setCode(400);
      response.setMessage("Transaction amount can not be less than or equal to zero");
      return response;
    }

    // Validate the narration is provided
    if (StringUtils.isEmpty(initiateB2cTransaction.getNarration())) {
      response.setCode(400);
      response.setMessage("Narration is required");
      return response;
    }

    if (initiateB2cTransaction.getMomoProvider() == MomoProvider.UNDEFINED) {
      response.setCode(400);
      response.setMessage("Please provide Momo provider");
      return response;
    }

    // Validation is successful
    response.setCode(200);
    response.setMessage("Validation completed");
    return response;
  }

  //  A POJO to return a B2C transaction to the calling client
  public record B2cTransactionResponse(
      String transactionId,
      String phoneNumber,
      double amount,
      String narration,
      MomoProvider momoProvider) {}
}
