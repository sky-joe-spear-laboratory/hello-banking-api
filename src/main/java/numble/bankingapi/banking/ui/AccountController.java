package numble.bankingapi.banking.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import numble.bankingapi.banking.application.AccountApplicationService;
import numble.bankingapi.banking.domain.Money;
import numble.bankingapi.banking.dto.HistoryResponses;
import numble.bankingapi.banking.dto.TargetResponses;
import numble.bankingapi.banking.dto.TransferCommand;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

	private final AccountApplicationService accountApplicationService;

	@GetMapping("/{accountNumber}/history")
	public ResponseEntity<HistoryResponses> getHistory(@PathVariable String accountNumber) {
		return ResponseEntity.ok(accountApplicationService.getHistory(accountNumber));
	}

	@GetMapping("/{accountNumber}/deposit")
	public ResponseEntity<Void> depositMoney(@PathVariable String accountNumber, @RequestBody Money money) {
		accountApplicationService.deposit(accountNumber, money);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{accountNumber}/withdraw")
	public ResponseEntity<Void> withdrawMoney(@PathVariable String accountNumber, @RequestBody Money money) {
		accountApplicationService.withdraw(accountNumber, money);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{accountNumber}/transfer")
	public ResponseEntity<Void> transferMoney(@PathVariable String accountNumber,
		@RequestBody TransferCommand transferCommand) {
		accountApplicationService.transfer(accountNumber, transferCommand);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{accountNumber}/transfer/targets")
	public ResponseEntity<TargetResponses> getTargets(@PathVariable String accountNumber) {
		return ResponseEntity.ok(accountApplicationService.getTargets());
	}
}
