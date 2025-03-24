package io.github.sicozz.fraudsim.domain;

import static org.junit.jupiter.api.Assertions.*;

import io.github.sicozz.fraudsim.domain.model.TransactionBuilder;
import io.github.sicozz.fraudsim.domain.model.TransactionMetadata;
import io.github.sicozz.fraudsim.domain.model.money.Currency;
import io.github.sicozz.fraudsim.domain.model.money.Money;
import io.github.sicozz.fraudsim.domain.model.party.Customer;
import io.github.sicozz.fraudsim.domain.model.party.Merchant;
import io.github.sicozz.fraudsim.domain.model.payment.BankAccount;
import io.github.sicozz.fraudsim.domain.model.status.TransactionStatus;
import io.github.sicozz.fraudsim.domain.model.type.TransferTransaction;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Transfer Transaction Tests")
class TransferTransactionTest {

  @Nested
  @DisplayName("Transfer Types")
  class TransferTypesTests {

    @Test
    @DisplayName("Should create ACH transfer transaction")
    void shouldCreateAchTransfer() {
      // Given
      var txType = TransferTransaction.ach("Monthly rent payment");

      // Then
      assertEquals("TRANSFER", txType.getTypeCode());
      assertEquals("ACH", txType.getDisplayName());
      assertTrue(txType.isAchTransfer());
      assertFalse(txType.isWireTransfer());
      assertFalse(txType.isInternational());
      assertEquals("Monthly rent payment", txType.referenceMessage());
    }

    @Test
    @DisplayName("Should create domestic wire transfer transaction")
    void shouldCreateDomesticWireTransfer() {
      // Given
      var txType = TransferTransaction.wire(false, "BUSINESS");

      // Then
      assertEquals("TRANSFER", txType.getTypeCode());
      assertEquals("WIRE", txType.getDisplayName());
      assertTrue(txType.isWireTransfer());
      assertFalse(txType.isAchTransfer());
      assertFalse(txType.isInternational());
      assertEquals("BUSINESS", txType.purposeCode());
    }

    @Test
    @DisplayName("Should create international wire transfer transaction")
    void shouldCreateInternationalWireTransfer() {
      // Given
      var txType = TransferTransaction.wire(true, "FAMILY_SUPPORT");

      // Then
      assertEquals("TRANSFER", txType.getTypeCode());
      assertEquals("International WIRE", txType.getDisplayName());
      assertTrue(txType.isWireTransfer());
      assertTrue(txType.isInternational());
    }

    @Test
    @DisplayName("Should create recurring ACH transaction")
    void shouldCreateRecurringAchTransaction() {
      // Given
      var txType = TransferTransaction.recurringAch("Monthly subscription");

      // Then
      assertTrue(txType.isAchTransfer());
      assertTrue(txType.isRecurring());
      assertTrue(txType.isScheduled());
      assertEquals("RECURRING_PAYMENT", txType.purposeCode());
    }

    @Test
    @DisplayName("Should create internal transfer transaction")
    void shouldCreateInternalTransferTransaction() {
      // Given
      var txType = TransferTransaction.internalTransfer("Transfer to savings");

      // Then
      assertEquals("INTERNAL", txType.transferMethod());
      assertFalse(txType.isInternational());
      assertEquals("Transfer to savings", txType.referenceMessage());
    }
  }

  @Nested
  @DisplayName("Transfer Transaction Creation")
  class TransferTransactionCreationTests {

    @Test
    @DisplayName("Should create a valid bank transfer transaction")
    void shouldCreateValidBankTransferTransaction() {
      // Given
      var customer = new Customer(UUID.randomUUID(), "Carol Johnson", "carol@example.com");
      var recipient = new Customer(UUID.randomUUID(), "David Wilson", "david@example.com");
      var sourceAccount =
          BankAccount.checking("XXXXXXX6789", "021000021", "Carol Johnson", "Bank of America");
      var money = Money.of(1000.00, Currency.USD);
      var txType = TransferTransaction.ach("Gift");

      // When
      var transaction =
          TransactionBuilder.builder()
              .withReferenceId("ACH-7890")
              .withAmount(money)
              .withSource(customer)
              .withDestination(recipient)
              .withType(txType)
              .withPaymentMethod(sourceAccount)
              .withStatus(TransactionStatus.COMPLETED)
              .withMetadata(TransactionMetadata.of("memo", "Birthday gift"))
              .build();

      // Then
      assertNotNull(transaction.id());
      assertEquals("ACH-7890", transaction.referenceId());
      assertEquals(money, transaction.amount());
      assertEquals(customer, transaction.source());
      assertEquals(recipient, transaction.destination());
      assertEquals(txType, transaction.type());
      assertEquals(sourceAccount, transaction.paymentMethod());
      assertEquals(TransactionStatus.COMPLETED, transaction.status());

      assertEquals("Gift", txType.referenceMessage());
      assertTrue(txType.isAchTransfer());
    }

    @Test
    @DisplayName("Should create a valid bill payment transaction")
    void shouldCreateValidBillPaymentTransaction() {
      // Given
      var customer = new Customer(UUID.randomUUID(), "Eva Brown", "eva@example.com");
      var utility = new Merchant(UUID.randomUUID(), "City Power & Light", "4900");
      var sourceAccount =
          BankAccount.checking("XXXXXXX1234", "021000021", "Eva Brown", "Chase Bank");
      var money = Money.of(85.42, Currency.USD);
      var txType = TransferTransaction.ach("Utility bill");

      // When
      var transaction =
          TransactionBuilder.builder()
              .withReferenceId("BILL-456")
              .withAmount(money)
              .withSource(customer)
              .withDestination(utility)
              .withType(txType)
              .withPaymentMethod(sourceAccount)
              .withStatus(TransactionStatus.PENDING)
              .withMetadata(TransactionMetadata.of("account_number", "UTIL-789123"))
              .build();

      // Then
      assertEquals(TransactionStatus.PENDING, transaction.status());
      assertEquals("BILL-456", transaction.referenceId());
      assertEquals(utility, transaction.destination());

      // Verify metadata
      assertEquals("UTIL-789123", transaction.getMetadataValue("account_number").orElse(null));
    }
  }

  @Nested
  @DisplayName("Bank Account Payment Methods")
  class BankAccountPaymentMethodTests {

    @Test
    @DisplayName("Should create valid checking account")
    void shouldCreateValidCheckingAccount() {
      // Given
      var account = BankAccount.checking("123456789", "021000021", "George Wilson", "Chase Bank");

      // Then
      assertEquals("BANK_ACCOUNT", account.getMethodType());
      assertTrue(account.getMaskedIdentifier().contains("X"));
      assertEquals("George Wilson", account.getAccountHolderName());
      assertEquals("Chase Bank", account.bankName());
      assertTrue(account.isChecking());
      assertFalse(account.isSavings());
    }

    @Test
    @DisplayName("Should create valid savings account")
    void shouldCreateValidSavingsAccount() {
      // Given
      var account = BankAccount.savings("987654321", "021000021", "George Wilson", "Chase Bank");

      // Then
      assertEquals("BANK_ACCOUNT", account.getMethodType());
      assertTrue(account.isSavings());
      assertFalse(account.isChecking());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "9876"})
    @DisplayName("Should mask account number correctly")
    void shouldMaskAccountNumberCorrectly(String accountNumber) {
      // Given
      var account =
          new BankAccount(
              accountNumber, "021000021", "Test User", "Test Bank", "CHECKING", null, "US");

      // Then
      if (accountNumber.length() > 4) {
        // Should be masked except last 4 digits
        String lastFour = accountNumber.substring(accountNumber.length() - 4);
        assertTrue(account.maskedAccountNumber().endsWith(lastFour));
        assertTrue(account.maskedAccountNumber().contains("X"));
      } else {
        // For short account numbers, might not be masked
        assertEquals(accountNumber, account.maskedAccountNumber());
      }
    }
  }
}
