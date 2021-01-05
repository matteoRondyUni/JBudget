package it.unicam.cs.pa.jbudget.statistic;

import static org.junit.jupiter.api.Assertions.*;

import it.unicam.cs.pa.jbudget.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Controlla che:
 * <ul>
 * <li> 1) Venga creata correttamente la Map delle Categorie-Double; </li>
 * <li> 2) Venga cercato e ritornato correttamente il Movimento di tipo CREDITS con valore maggiore; </li>
 * <li> 3) Venga cercato e ritornato correttamente il Movimento di tipo DEBITS con valore maggiore; </li>
 * <li> 4) Venga cercato e ritornato correttamente il Movimento di tipo CREDITS con valore minore; </li>
 * <li> 5) Venga cercato e ritornato correttamente il Movimento di tipo DEBITS con valore minore; </li>
 * </ul>
 */
class MoneyStatisticTest {
    Account wallet;
    Transaction t1;
    StatisticCreator statisticCreator;

    @BeforeEach
    void initialize() {
        wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 0, 0);
        t1 = new MoneyTransaction(1);
        statisticCreator = new MoneyStatistic(wallet);
    }

    @Test
    void getCategoryValue() throws StaticException {
        Transaction t2 = new MoneyTransaction(2);
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 2, 20, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 3, 10, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 4, 100, Date.from(Instant.now()), "", t2, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 5, 200, Date.from(Instant.now()), "", t2, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 6, 100, Date.from(Instant.now()), "", t2, wallet));

        Category c1 = new MoneyCategory("c1", "", 1);
        Category c2 = new MoneyCategory("c2", "", 2);

        t1.addCategory(c1);
        t2.addCategory(c2);
        Map<Category, Double> map = statisticCreator.getCategoryValue();

        assertEquals(2, map.keySet().size());
        assertEquals(40, map.get(c1));
        assertEquals(400, map.get(c2));
    }

    @Test
    void getMaxCredit() {
        Exception e1 = assertThrows(StaticException.class,
                () -> statisticCreator.getMaxCredit());
        assertEquals(StaticException.E1_NO_MOVEMENT_IN_ACCOUNT, e1.getMessage());

        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 2, 100, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 3, 20, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 4, 1000, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 5, 1, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 6, 0, Date.from(Instant.now()), "", t1, wallet));

        assertDoesNotThrow(() -> assertEquals(1000, statisticCreator.getMaxCredit().amount()));
    }

    @Test
    void getMaxDebit() {
        Exception e1 = assertThrows(StaticException.class,
                () -> statisticCreator.getMaxDebit());
        assertEquals(StaticException.E1_NO_MOVEMENT_IN_ACCOUNT, e1.getMessage());

        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 1, 10, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 2, 100, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 3, 20, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 4, 1000, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 5, 1, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 6, 0, Date.from(Instant.now()), "", t1, wallet));

        assertDoesNotThrow(() -> assertEquals(1000, statisticCreator.getMaxDebit().amount()));
    }

    @Test
    void getMinCredit() {
        Exception e1 = assertThrows(StaticException.class,
                () -> statisticCreator.getMinCredit());
        assertEquals(StaticException.E1_NO_MOVEMENT_IN_ACCOUNT, e1.getMessage());

        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 2, 100, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 3, 0, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 4, 1000, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 5, 1, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 6, 20, Date.from(Instant.now()), "", t1, wallet));

        assertDoesNotThrow(() -> assertEquals(0, statisticCreator.getMinCredit().amount()));
    }

    @Test
    void getMinDebit() {
        Exception e1 = assertThrows(StaticException.class,
                () -> statisticCreator.getMinDebit());
        assertEquals(StaticException.E1_NO_MOVEMENT_IN_ACCOUNT, e1.getMessage());

        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 1, 10, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 2, 100, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 3, 0, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 4, 1000, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 5, 1, Date.from(Instant.now()), "", t1, wallet));
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 6, 20, Date.from(Instant.now()), "", t1, wallet));

        assertDoesNotThrow(() -> assertEquals(0, statisticCreator.getMinDebit().amount()));
    }
}