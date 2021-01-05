package it.unicam.cs.pa.jbudget.save;

import it.unicam.cs.pa.jbudget.model.Account;
import it.unicam.cs.pa.jbudget.model.Category;
import it.unicam.cs.pa.jbudget.model.Movement;
import it.unicam.cs.pa.jbudget.model.Transaction;

import java.io.IOException;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di gestire
 * il salvataggio dei dati del Model.
 *
 * @author Matteo Rondini
 * @see it.unicam.cs.pa.jbudget.model
 */
public interface Saver {
    /**
     * Salva i dati degli {@link Account}.
     *
     * @throws IOException Viene lanciata quando avviene un Errore nel salvataggio
     */
    void saveAccount() throws IOException;

    /**
     * Salva i dati delle {@link Category}.
     *
     * @throws IOException Viene lanciata quando avviene un Errore nel salvataggio
     */
    void saveCategory() throws IOException;

    /**
     * Salva i dati dei {@link Movement}.
     *
     * @throws IOException Viene lanciata quando avviene un Errore nel salvataggio
     */
    void saveMovement() throws IOException;

    /**
     * Salva i dati delle {@link Transaction}.
     *
     * @throws IOException Viene lanciata quando avviene un Errore nel salvataggio
     */
    void saveTransactionCategory() throws IOException;
}
