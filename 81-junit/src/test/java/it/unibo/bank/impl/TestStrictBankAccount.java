package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.unibo.bank.impl.SimpleBankAccount.*;
import static it.unibo.bank.impl.SimpleBankAccount.ATM_TRANSACTION_FEE;
import static it.unibo.bank.impl.StrictBankAccount.TRANSACTION_FEE;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PipedOutputStream;

public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // 1. Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    @BeforeEach
    public void setUp() {
        this.mRossi= new AccountHolder("Michele", "Rossi", 1);
        this.bankAccount= new StrictBankAccount(mRossi, INITIAL_AMOUNT);
    }

    // 2. Test the initial state of the StrictBankAccount
    @Test
    public void testInitialization() {
        Assertions.assertEquals(mRossi, bankAccount.getAccountHolder());
        Assertions.assertEquals(INITIAL_AMOUNT, bankAccount.getBalance());
        Assertions.assertEquals(0, bankAccount.getTransactionsCount());
    }


    // 3. Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
    @Test
    public void testManagementFees() {
        double expectedValue= INITIAL_AMOUNT; 
        Assertions.assertFalse(bankAccount.getTransactionsCount() > 0);
        expectedValue+=100;
        bankAccount.deposit(mRossi.getUserID(), INITIAL_AMOUNT);
        Assertions.assertTrue(bankAccount.getTransactionsCount() > 0);
        expectedValue-=(MANAGEMENT_FEE + TRANSACTION_FEE*bankAccount.getTransactionsCount());
        bankAccount.chargeManagementFees(mRossi.getUserID());
        Assertions.assertEquals(expectedValue, bankAccount.getBalance());
        Assertions.assertFalse(bankAccount.getTransactionsCount() > 0);
    }

    // 4. Test the withdraw of a negative value
    @Test
    public void testNegativeWithdraw() {
        try {
            bankAccount.withdraw(mRossi.getUserID(), -INITIAL_AMOUNT);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Cannot withdraw a negative amount", e.getMessage());
        }
    }

    // 5. Test withdrawing more money than it is in the account
    @Test
    public void testWithdrawingTooMuch() {
        try {
            bankAccount.withdraw(mRossi.getUserID(), INITIAL_AMOUNT*3);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance", e.getMessage());
        }
    }
}
