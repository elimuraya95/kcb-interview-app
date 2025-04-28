package tech.elimuraya.kcbinterview.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.elimuraya.kcbinterview.pojos.ApiResponse;
import tech.elimuraya.kcbinterview.pojos.InitiateB2cTransaction;
import tech.elimuraya.kcbinterview.services.B2cTransactionService;
import tech.elimuraya.kcbinterview.services.B2cTransactionService.B2cTransactionResponse;

/**
 * @author eli.muraya
 * @date 28/04/2025/04/2025
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/b2c/")
public class B2cTransactionResource {
	private final B2cTransactionService b2cTransactionService;

	@PostMapping("initiate-transaction")
	public ApiResponse<B2cTransactionResponse> initiateB2cTransaction(@RequestBody InitiateB2cTransaction initiateB2cTransaction) {
		return b2cTransactionService.initiateB2cTransaction(initiateB2cTransaction);
	}

	@GetMapping("fetch-transaction/{transactionId}")
	public ApiResponse<B2cTransactionResponse> initiateB2cTransaction(@PathVariable("transactionId")  String transactionId) {
		return b2cTransactionService.fetchB2cTransaction(transactionId);
	}
}
