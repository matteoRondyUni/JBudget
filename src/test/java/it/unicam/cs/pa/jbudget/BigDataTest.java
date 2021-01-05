package it.unicam.cs.pa.jbudget;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.controller.MoneyController;
import it.unicam.cs.pa.jbudget.model.AccountType;
import it.unicam.cs.pa.jbudget.model.LedgerException;
import it.unicam.cs.pa.jbudget.model.MovementException;
import it.unicam.cs.pa.jbudget.model.MovementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;

/**
 * Controlla quale siano i limiti dell'Applicazione, quanti dati possa elaborare per essere definita
 * utilizzabile.
 */
class BigDataTest {
    Controller controller;
    String path;

    @BeforeEach
    void initialize() {
        controller = new MoneyController();
        path = "Da inserire...";
    }

    @Disabled("Da usare solo per testare le criticita' di JBudget nel gestire 1000 Account")
    @Test
    void save1000Accounts() throws IOException, LedgerException, MovementException {
        for (int i = 0; i < 1000; i++) {
            controller.addAccount(AccountType.ASSETS, "Test", "", 10);
            controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                    controller.getAccounts().get(i));
        }

        controller.saveData(path);
    }

    @Disabled("Da usare solo per testare le criticita' di JBudget nel gestire 10.000 Movimenti")
    @Test
    void save10000Movements() throws IOException, LedgerException, MovementException {
        controller.addAccount(AccountType.ASSETS, "Test", "", 10);

        for (int i = 0; i < 10000; i++)
            controller.addMovement(MovementType.CREDITS, 10, Date.from(Instant.now()), "",
                    controller.getAccounts().get(0));

        controller.saveData(path);
    }

}