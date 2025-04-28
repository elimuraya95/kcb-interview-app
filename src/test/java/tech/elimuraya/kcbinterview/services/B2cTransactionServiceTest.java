package tech.elimuraya.kcbinterview.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class B2cTransactionServiceTest {
  @InjectMocks B2cTransactionService b2cTransactionService;
  @Mock MpesaApiService mpesaApiService;
  @Mock AirtelApiService airtelApiService;
  @Mock B2cTransactionRepository b2cTransactionRepository;
  @Mock AfricasTalkingApiService smsApiService;

  @Test
  void initiateB2cTransaction() {
    // Arrange
    var initiateB2cTransaction = this.generateInitiateB2cTransaction();
    B2cTransaction b2cTransaction = this.generateB2cTransaction(initiateB2cTransaction);

    // Mock the DB call
    Mockito.when(b2cTransactionRepository.save(Mockito.any(B2cTransaction.class)))
        .thenReturn(b2cTransaction);

    // Mock the Mpesa API call
    var momoApiRequest = new MomoRequestPayload();
    momoApiRequest.setAmount(initiateB2cTransaction.getAmount());
    momoApiRequest.setPhoneNumber(initiateB2cTransaction.getPhoneNumber());
    momoApiRequest.setNarration(initiateB2cTransaction.getNarration());

    var momoApiResponse = new MomoResponsePayload();
    momoApiResponse.setStatus(200);
    momoApiResponse.setMessage("Success");
    momoApiResponse.setData(momoApiRequest);

    Mockito.when(mpesaApiService.initiateB2cTransaction(momoApiRequest))
        .thenReturn(momoApiResponse);

    // Mock the SMS API call
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setData(null);
    apiResponse.setMessage("SMS sent successfully");
    apiResponse.setCode(200);
    Mockito.when(smsApiService.sendSms(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(apiResponse);

    // Act
    var response = b2cTransactionService.initiateB2cTransaction(initiateB2cTransaction);

    // Assert
    Assertions.assertTrue(response.getCode() == 200);
    Assertions.assertTrue(response.getMessage().equals("Processed B2C transaction successfully!"));

  }

  @Test
  void fetchB2cTransaction() {}

  @Test
  void validateB2cTransaction_withInvalidPhoneNumber() {
    // Arrange
    var request = this.generateInitiateB2cTransaction();
    request.setPhoneNumber(null);

    // Act
    var response = this.b2cTransactionService.validateB2cTransaction(request);

    // Assert
    Assertions.assertTrue(response.getCode() == 400);
    Assertions.assertTrue(response.getMessage().equals("Phone number is required"));
  }

  @Test
  void validateB2cTransaction_withInvalidAmount() {
    // Arrange
    var request = this.generateInitiateB2cTransaction();
    request.setAmount(0);

    // Act
    var response = this.b2cTransactionService.validateB2cTransaction(request);

    // Assert
    Assertions.assertTrue(response.getCode() == 400);
    Assertions.assertTrue(response.getMessage().equals("Transaction amount can not be less than or equal to zero"));
  }

  @Test
  void validateB2cTransaction_withInvalidNarration() {
    // Arrange
    var request = this.generateInitiateB2cTransaction();
    request.setNarration(null);

    // Act
    var response = this.b2cTransactionService.validateB2cTransaction(request);

    // Assert
    Assertions.assertTrue(response.getCode() == 400);
    Assertions.assertTrue(response.getMessage().equals("Narration is required"));
  }

  @Test
  void validateB2cTransaction_withInvalidMomoProvider() {
    // Arrange
    var request = this.generateInitiateB2cTransaction();
    request.setMomoProvider(MomoProvider.UNDEFINED);

    // Act
    var response = this.b2cTransactionService.validateB2cTransaction(request);

    // Assert
    Assertions.assertTrue(response.getCode() == 400);
    Assertions.assertTrue(response.getMessage().equals("Please provide Momo provider"));
  }

  /**
   * Generate a `InitiateB2cTransaction` pojo to use in the unit test
   *
   * @return
   */
  private InitiateB2cTransaction generateInitiateB2cTransaction() {
    InitiateB2cTransaction request = new InitiateB2cTransaction();
    request.setPhoneNumber("254704420756");
    request.setNarration("Test transaction");
    request.setAmount(100);
    request.setMomoProvider(MomoProvider.MPESA);
    return request;
  }

  /**
   * Generate a `B2cTransaction` pojo to use in the unit test
   * @param initiateB2cTransaction
   * @return
   */
  private B2cTransaction generateB2cTransaction(InitiateB2cTransaction initiateB2cTransaction) {
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
    return b2cTransaction;
  }
}
