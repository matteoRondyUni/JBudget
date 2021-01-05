package it.unicam.cs.pa.jbudget.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Controlla che:
 * <ul>
 * <li> 1) Un Movimento venga creato correttamente e che vengano lanciate le giuste eccezioni; </li>
 * <li> 2) Il valore del Movimento venga modificato correttamente; </li>
 * <li> 3) Al Movimento venga aggiunta correttamente una Categoria; </li>
 * <li> 4) Al Movimento venga rimossa correttamente una Categoria; </li>
 * </ul>
 */
class MoneyMovementTest {
    MoneyTransaction t1;
    SimpleWallet wallet;

    @BeforeEach
    void initialize() {
        t1 = new MoneyTransaction(1);
        wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);
    }

    @Test
    void MoneyMovement() throws MovementException {
        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet);
        assertEquals(10, m1.amount());

        Exception e1 = assertThrows(MovementException.class,
                () -> new MoneyMovement(MovementType.CREDITS, 2, -10, Date.from(Instant.now()), "", t1, wallet));
        assertEquals(MovementException.E0_NEGATIVE_VALUE, e1.getMessage());

        e1 = assertThrows(MovementException.class,
                () -> new MoneyMovement(MovementType.CREDITS, 2, 10, Date.from(Instant.now()), "", null, wallet));
        assertEquals(MovementException.E1_NULL_TRANSACTION, e1.getMessage());

        e1 = assertThrows(MovementException.class,
                () -> new MoneyMovement(MovementType.CREDITS, 2, 10, Date.from(Instant.now()), "", t1, null));
        assertEquals(MovementException.E4_NULL_ACCOUNT, e1.getMessage());

        e1 = assertThrows(NullPointerException.class,
                () -> new MoneyMovement(null, 2, 10, Date.from(Instant.now()), "", t1, null));
        assertEquals(Movement.EXCEPTION_TYPE_NULL, e1.getMessage());

        e1 = assertThrows(NullPointerException.class,
                () -> new MoneyMovement(MovementType.CREDITS, 2, 10, null, "", t1, null));
        assertEquals(Movement.EXCEPTION_DATE_NULL, e1.getMessage());

        e1 = assertThrows(NullPointerException.class,
                () -> new MoneyMovement(MovementType.CREDITS, 2, 10, Date.from(Instant.now()), null, t1, null));
        assertEquals(Movement.EXCEPTION_DESCRIPTION_NULL, e1.getMessage());
    }

    @Test
    void setValue() throws MovementException {
        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet);

        Exception e1 = assertThrows(MovementException.class,
                () -> m1.setValue(-10));
        assertEquals(MovementException.E0_NEGATIVE_VALUE, e1.getMessage());

        m1.setValue(100);
        assertEquals(100, m1.amount());
    }

    @Test
    void addCategory() throws MovementException {
        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet);
        assertTrue(m1.categories().isEmpty());
        m1.addCategory(null);
        assertTrue(m1.categories().isEmpty());

        MoneyCategory c1 = new MoneyCategory("SPORT", "", 0);
        m1.addCategory(c1);
        assertFalse(m1.categories().isEmpty());
    }

    @Test
    void removeCategory() throws MovementException {
        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet);

        MoneyCategory c1 = new MoneyCategory("SPORT", "", 0);
        m1.addCategory(c1);
        m1.removeCategory(c1);
        assertTrue(m1.categories().isEmpty());
    }
}