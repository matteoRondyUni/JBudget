package it.unicam.cs.pa.jbudget.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Controlla che:
 * 1) Un Movimento venga aggiunto correttamente all'interno del Conto;
 * 2) Una lista di Movimenti venga aggiunta al Conto;
 * 3) Il bilancio del Conto sia calcolato correttamente;
 * 4) Il Conto ritorni correttamente la lista dei movimenti filtrati da un predicate;
 */
class SimpleWalletTest {

    @Test
    void addMovement() throws MovementException {
        SimpleWallet wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);
        MoneyTransaction t1 = new MoneyTransaction(1);
        assertEquals(0, wallet.getBalance());

        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet);
        assertFalse(wallet.getMovements().isEmpty());

        SimpleWallet wallet2 = new SimpleWallet(AccountType.ASSETS, "wallet Exception", "", 2, 0);
        Exception e1 = assertThrows(MovementException.class,
                () -> wallet2.addMovement(m1));
        assertEquals(MovementException.E5_DIFFERENT_ACCOUNT, e1.getMessage());

        MoneyMovement m2 = new MoneyMovement(MovementType.DEBITS, 4, 100, Date.from(Instant.now()), "", t1, wallet);
        e1 = assertThrows(MovementException.class, () -> wallet.addMovement(m2));
        assertEquals(MovementException.E6_THERE_IS_ALREADY_A_MOVEMENT_IN_ACCOUNT, e1.getMessage());

    }

    @Test
    void addMovements() {
        SimpleWallet wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);
        MoneyTransaction t1 = new MoneyTransaction(1);
        List<MoneyMovement> list1 = new ArrayList<>();
        SimpleWallet wallet2 = new SimpleWallet(AccountType.ASSETS, "wallet Exception", "", 2, 0);

        assertDoesNotThrow(() ->
                list1.add(new MoneyMovement(MovementType.CREDITS, 1, 10, Date.from(Instant.now()), "", t1, wallet)));
        assertDoesNotThrow(() ->
                list1.add(new MoneyMovement(MovementType.CREDITS, 2, 10, Date.from(Instant.now()), "", t1, wallet)));
        assertDoesNotThrow(() ->
                list1.add(new MoneyMovement(MovementType.CREDITS, 3, 10, Date.from(Instant.now()), "", t1, wallet)));

        assertThrows(MovementException.class, () -> wallet2.addMovements(list1));
        assertEquals(list1, wallet.getMovements());
    }


    @Test
    void getBalance() throws MovementException {
        SimpleWallet wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);
        MoneyTransaction t1 = new MoneyTransaction(1);

        new MoneyMovement(MovementType.CREDITS, 1, 10, new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime(), "", t1, wallet);
        assertEquals(10, wallet.getBalance());
        new MoneyMovement(MovementType.CREDITS, 2, 10, new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime(), "", t1, wallet);
        assertEquals(20, wallet.getBalance());

        new MoneyMovement(MovementType.DEBITS, 3, 100, new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime(), "", t1, wallet);
        assertEquals(-80, wallet.getBalance());
        //Movimento Futuro
        new MoneyMovement(MovementType.DEBITS, 4, 100, new GregorianCalendar(3000, Calendar.JANUARY, 1).getTime(), "", t1, wallet);
        assertEquals(-80, wallet.getBalance());

        SimpleWallet wallet3 = new SimpleWallet(AccountType.LIABILITIES, "wallet test 2", "", 3, 0);
        new MoneyMovement(MovementType.CREDITS, 5, 10, new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime(), "", t1, wallet3);
        assertEquals(-10, wallet3.getBalance());
        new MoneyMovement(MovementType.DEBITS, 6, 10, new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime(), "", t1, wallet3);
        assertEquals(0, wallet3.getBalance());
    }

    @Test
    void getMovements() throws MovementException {
        SimpleWallet wallet = new SimpleWallet(AccountType.ASSETS, "wallet test", "", 1, 0);
        MoneyTransaction t1 = new MoneyTransaction(1);

        MoneyMovement m1 = new MoneyMovement(MovementType.CREDITS, 1, 10, new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime(), "", t1, wallet);
        MoneyMovement m2 = new MoneyMovement(MovementType.DEBITS, 2, 100, new GregorianCalendar(2000, Calendar.NOVEMBER, 1).getTime(), "", t1, wallet);

        assertTrue(wallet.getMovements(movement -> movement.getDate().equals(new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime())).contains(m1));

        assertTrue(wallet.getMovements(movement -> movement.amount() == 10).contains(m1));

        assertTrue(wallet.getMovements(movement -> movement.type().equals(MovementType.DEBITS)).contains(m2));

    }
}