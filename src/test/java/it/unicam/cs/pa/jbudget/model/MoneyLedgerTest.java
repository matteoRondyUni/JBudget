package it.unicam.cs.pa.jbudget.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Controlla che:
 * <ul>
 * <li> 1) L'Account venga creato e inserito correttamente all'interno del Ledger,
 *      controlla anche che venga rispettato l'ordine tra i vari Account; </li>
 * <li> 2) La Transazione venga inserita correttamente; controlla anche
 *      che venga rispettato l'ordine tra le varie Transazioni; </li>
 * <li> 3) Una Transazione all'interno del Ledger sia rimossa correttamente; </li>
 * <li> 4) Una Categoria sia aggiunta all'interno del Ledger; </li>
 * <li> 5) Una Categoria all'interno del Ledger sia rimossa correttamente; </li>
 * <li> 6) Un Account all'interno del Ledger sia rimosso correttamente;</li>
 * </ul>
 */
class MoneyLedgerTest {
    MoneyLedger ledger;
    MoneyTransaction t1;

    @BeforeEach
    void initialize() {
        ledger = new MoneyLedger();
        t1 = new MoneyTransaction(1);
    }

    @Test
    void addAccount() {
        assertTrue(ledger.getAccount().isEmpty());

        ledger.addAccount(AccountType.ASSETS, "wallet test", "", 0);
        assertFalse(ledger.getAccount().isEmpty());

        ledger.addAccount(AccountType.ASSETS, "wallet test", "", 2);
        ledger.addAccount(AccountType.ASSETS, "wallet test", "", 1);

        assertEquals(0, ledger.getAccount().get(0).getID());
        assertEquals(1, ledger.getAccount().get(1).getID());
        assertEquals(2, ledger.getAccount().get(2).getID());
    }

    @Test
    void addTransaction() {
        Exception e1 = assertThrows(LedgerException.class,
                () -> ledger.addTransaction(t1));
        assertEquals(LedgerException.E1_NO_TRANSACTION_IN_LEDGER, e1.getMessage());

        ledger.addAccount(AccountType.ASSETS, "wallet test", "", 0);
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, ledger.getAccount().get(0)));

        assertDoesNotThrow(() -> ledger.addTransaction(t1));
        assertTrue(ledger.getTransactions().contains(t1));

        Transaction t2 = new MoneyTransaction(2);
        Transaction t3 = new MoneyTransaction(3);

        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 2, 10, Date.from(Instant.now()), "", t3, ledger.getAccount().get(0)));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 3, 10, Date.from(Instant.now()), "", t2, ledger.getAccount().get(0)));
        assertDoesNotThrow(() -> ledger.addTransaction(t3));
        assertDoesNotThrow(() -> ledger.addTransaction(t2));

        assertEquals(1, ledger.getTransactions().get(0).getID());
        assertEquals(2, ledger.getTransactions().get(1).getID());
        assertEquals(3, ledger.getTransactions().get(2).getID());
    }

    @Test
    void removeTransaction() throws MovementException {
        ledger.addAccount(AccountType.ASSETS, "wallet test", "", 0);
        ledger.addAccount(AccountType.ASSETS, "wallet 2", "", 0);
        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, ledger.getAccount().get(0));
        new MoneyMovement(MovementType.DEBITS, 2, 10, Date.from(Instant.now()), "", t1, ledger.getAccount().get(1));

        assertDoesNotThrow(() -> ledger.addTransaction(t1));

        assertTrue(ledger.removeTransaction(t1));
        assertFalse(ledger.getTransactions().contains(t1));
        assertNull(m1.getAccount());
        assertNull(m1.getTransaction());
        assertTrue(ledger.getAccount().get(0).getMovements().isEmpty());
        assertTrue(ledger.getAccount().get(1).getMovements().isEmpty());
    }

    @Test
    void addCategory() {
        assertTrue(ledger.getCategories().isEmpty());
        ledger.addCategory("Sport", "");
        assertFalse(ledger.getCategories().isEmpty());
    }

    @Test
    void removeCategory() throws MovementException {
        ledger.addAccount(AccountType.ASSETS, "wallet test", "", 0);
        Category c1 = ledger.addCategory("Sport", "");
        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, ledger.getAccount().get(0));
        assertDoesNotThrow(() -> ledger.addTransaction(t1));

        m1.addCategory(c1);
        assertFalse(m1.categories().isEmpty());

        assertTrue(ledger.removeCategory(c1));
        assertTrue(ledger.getCategories().isEmpty());
        assertTrue(m1.categories().isEmpty());

        Category c2 = ledger.addCategory("Vacanza", "");
        t1.addCategory(c2);
        assertFalse(ledger.getCategories().isEmpty());
        assertEquals(c2, t1.categories().get(0));
        assertEquals(c2, m1.categories().get(0));
    }

    @Test
    void removeAccount() throws MovementException {
        ledger.addAccount(AccountType.ASSETS, "wallet 1", "", 0);
        Account wallet2 = ledger.addAccount(AccountType.ASSETS, "wallet 2", "", 0);

        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, ledger.getAccount().get(0));
        MoneyMovement m2 = new MoneyMovement(MovementType.CREDITS, 2, 10, Date.from(Instant.now()), "", t1, wallet2);

        assertDoesNotThrow(() -> ledger.addTransaction(t1));

        ledger.removeAccount(wallet2);
        assertFalse(ledger.getAccount().isEmpty());
        assertFalse(ledger.getTransactions().isEmpty());
        assertEquals(1, ledger.getAccount().size());
        assertEquals(1, ledger.getTransactions().size());
        assertNull(m2.getAccount());
        assertNull(m2.getTransaction());
        assertNotNull(m1.getAccount());
        assertNotNull(m1.getTransaction());
    }
}