package it.unicam.cs.pa.jbudget.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controlla che:
 * 1) Un Movimento venga aggiunto ad un Transazione;
 * 2) Una lista di Movimenti venga aggiunta ad un Transazione;
 * 3) La Transazione calcoli correttamente il suo saldo;
 */
class MoneyTransactionTest {

    @Test
    void addMovement() throws MovementException {
        List<MoneyMovement> list1 = new ArrayList<>();
        MoneyTransaction t1 = new MoneyTransaction(1);
        SimpleWallet wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);

        // Controllo che la lista di movimenti di t1 all'inizio sia vuota
        assertEquals(list1, t1.movements());
        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet);

        Exception e1 = assertThrows(MovementException.class,
                () -> t1.addMovement(new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", null, wallet)));
        assertEquals(MovementException.E1_NULL_TRANSACTION, e1.getMessage());

        list1.add(m1);
        assertEquals(list1, t1.movements());

        MoneyTransaction t2 = new MoneyTransaction(2);
        e1 = assertThrows(MovementException.class, () -> t2.addMovement(m1));
        assertEquals(MovementException.E2_DIFFERENT_TRANSACTION, e1.getMessage());

        e1 = assertThrows(MovementException.class,
                () -> t1.addMovement(m1));
        assertEquals(MovementException.E3_THERE_IS_ALREADY_A_MOVEMENT_IN_TRANSACTION, e1.getMessage());

        e1 = assertThrows(MovementException.class,
                () -> t1.addMovement(new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet)));
        assertEquals(MovementException.E3_THERE_IS_ALREADY_A_MOVEMENT_IN_TRANSACTION, e1.getMessage());

    }


    @Test
    void addMovements() throws MovementException {
        SimpleWallet wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);
        List<Movement> list1 = new ArrayList<>();
        MoneyTransaction t1 = new MoneyTransaction(1);
        MoneyTransaction t2 = new MoneyTransaction(2);

        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet);
        MoneyMovement m2 = new MoneyMovement(MovementType.CREDITS, 2, 10, Date.from(Instant.now()), "", t1, wallet);
        MoneyMovement m3 = new MoneyMovement(MovementType.CREDITS, 3, 10, Date.from(Instant.now()), "", t1, wallet);

        list1.add(m1);
        list1.add(m2);
        list1.add(m3);

        assertThrows(MovementException.class, () -> t2.addMovements(list1));
        assertEquals(list1, t1.movements());
    }

    @Test
    void getTotalAmount() {
        SimpleWallet wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);
        MoneyTransaction t1 = new MoneyTransaction(1);

        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 1, 10.0, Date.from(Instant.now()), "", t1, wallet));
        assertEquals(10.0, t1.getTotalAmount());

        assertDoesNotThrow(() -> new MoneyMovement(MovementType.DEBITS, 2, 20.0, Date.from(Instant.now()), "Commissione", t1, wallet));
        assertEquals(-10.0, t1.getTotalAmount());

        SimpleWallet wallet2 = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 2, 0);
        assertDoesNotThrow(() -> new MoneyMovement(MovementType.CREDITS, 3, 10.0, Date.from(Instant.now()), "", t1, wallet2));
        assertEquals(0.0, t1.getTotalAmount());
    }
}