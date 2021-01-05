package it.unicam.cs.pa.jbudget.controller;

import it.unicam.cs.pa.jbudget.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Controlla che:
 * <ul>
 * <li> 1) Un Movimento venga creato e inserito correttamente all'interno del Ledger; </li>
 * <li> 2) Un Account all'interno del Ledger sia rimosso correttamente; </li>
 * <li> 3) Una Categoria all'interno del Ledger sia rimossa correttamente; </li>
 * <li> 4) Un Movimento all'interno del Ledger sia rimosso correttamente; </li>
 * <li> 5) Una Transazione all'interno del Ledger sia rimossa correttamente; </li>
 * </ul>
 */
class MoneyControllerTest {
    Controller controller;

    @BeforeEach
    void initialize() {
        controller = new MoneyController();
        controller.addAccount(AccountType.ASSETS, "wallet test", "", 0);
    }


    @Test
    void addMovement() {
        assertEquals(0, assertDoesNotThrow(() ->
                controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                        controller.getAccounts().get(0)).getID()));

        Account testEccezione = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);

        Exception e1 = assertThrows(LedgerException.class,
                () -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "", testEccezione));
        assertEquals(LedgerException.E1_NO_TRANSACTION_IN_LEDGER, e1.getMessage());

        assertEquals(1, assertDoesNotThrow(() ->
                controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                        controller.getAccounts().get(0)).getID()));

        e1 = assertThrows(MovementException.class,
                () -> controller.addMovement(MovementType.CREDITS, -10, Date.from(Instant.now()), "", controller.getAccounts().get(0)));
        assertEquals(MovementException.E0_NEGATIVE_VALUE, e1.getMessage());

        assertEquals(2, assertDoesNotThrow(() ->
                controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                        controller.getAccounts().get(0)).getID()));
    }

    @Test
    void removeAccount() {
        assertTrue(controller.getMovements().isEmpty());
        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                controller.getAccounts().get(0)));
        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                controller.getAccounts().get(0)));
        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                controller.getAccounts().get(0)));
        assertFalse(controller.getMovements().isEmpty());

        assertFalse(controller.removeAccount(null));
        assertTrue(controller.removeAccount(controller.getAccounts().get(0)));
        assertTrue(controller.getMovements().isEmpty());
        assertTrue(controller.getAccounts().isEmpty());
    }

    @Test
    void removeCategory() {
        controller.addCategory("c1", "");
        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                controller.getAccounts().get(0)));
        Movement m1 = controller.getMovements().get(0);
        m1.addCategory(controller.getCategory().get(0));
        Transaction t1 = m1.getTransaction();

        assertFalse(m1.categories().isEmpty());
        assertEquals(controller.getCategory().get(0), m1.categories().get(0));

        controller.removeCategory(m1, controller.getCategory().get(0));
        assertTrue(m1.categories().isEmpty());
        assertTrue(t1.categories().isEmpty());

        t1.addCategory(controller.getCategory().get(0));
        assertFalse(t1.categories().isEmpty());
        assertFalse(m1.categories().isEmpty());
        controller.removeCategory(t1, controller.getCategory().get(0));
        assertTrue(m1.categories().isEmpty());
        assertTrue(t1.categories().isEmpty());

        t1.addCategory(controller.getCategory().get(0));
        controller.removeCategory(controller.getCategory().get(0));
        assertTrue(m1.categories().isEmpty());
        assertTrue(t1.categories().isEmpty());
    }

    @Test
    void removeMovement() {
        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                controller.getAccounts().get(0)));
        Movement m1 = controller.getMovements().get(0);
        Transaction t1 = m1.getTransaction();

        controller.removeMovement(m1);
        assertFalse(controller.getTransactions().contains(t1));
        assertFalse(controller.getMovements().contains(m1));
        assertNull(m1.getAccount());
        assertNull(m1.getTransaction());

        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                controller.getAccounts().get(0)));
        Movement m2 = controller.getMovements().get(0);
        Transaction t2 = m2.getTransaction();
        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                t2, controller.getAccounts().get(0)));

        controller.removeMovement(m2);
        assertFalse(t2.movements().isEmpty());
        assertTrue(controller.getTransactions().contains(t2));
        assertNull(m2.getAccount());
        assertNull(m2.getTransaction());
    }

    @Test
    void removeTransaction() {
        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                controller.getAccounts().get(0)));
        Movement m1 = controller.getMovements().get(0);
        Transaction t1 = m1.getTransaction();

        controller.removeTransaction(t1);
        assertFalse(controller.getTransactions().contains(t1));
        assertFalse(controller.getMovements().contains(m1));
        assertNull(m1.getAccount());
        assertNull(m1.getTransaction());

        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                controller.getAccounts().get(0)));
        Movement m2 = controller.getMovements().get(0);
        Transaction t2 = m2.getTransaction();
        assertDoesNotThrow(() -> controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                t2, controller.getAccounts().get(0)));

        controller.removeTransaction(t2);
        assertTrue(t2.movements().isEmpty());
        assertFalse(controller.getTransactions().contains(t2));
        assertNull(m2.getAccount());
        assertNull(m2.getTransaction());
    }
}