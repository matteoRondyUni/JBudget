package it.unicam.cs.pa.jbudget.read;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.Account;
import it.unicam.cs.pa.jbudget.model.Category;
import it.unicam.cs.pa.jbudget.model.LedgerException;
import it.unicam.cs.pa.jbudget.model.Movement;
import it.unicam.cs.pa.jbudget.model.MovementException;
import it.unicam.cs.pa.jbudget.model.Transaction;

import java.io.IOException;
import java.text.ParseException;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di gestire
 * l'import dei dati salvati, e la loro trasformazione in oggetti del Model attraverso
 * un {@link Controller}.
 *
 * @author Matteo Rondini
 * @see it.unicam.cs.pa.jbudget.model
 * @see it.unicam.cs.pa.jbudget.controller
 */
public interface Importer {
    /**
     * Importa i dati degli {@link Account}.
     *
     * @throws IOException     Viene lanciata quando avviene un Errore nell'Import dei dati
     * @throws LedgerException Viene lanciata quando avviene un Errore nella {@code ricreazione} dell'Account
     */
    void getAccounts() throws IOException, LedgerException;

    /**
     * Importa i dati delle {@link Category}.
     *
     * @throws IOException     Viene lanciata quando avviene un Errore nell'Import dei dati
     * @throws LedgerException Viene lanciata quando avviene un Errore nella {@code ricreazione} delle Category
     */
    void getCategories() throws IOException, LedgerException;

    /**
     * Importa i dati dei {@link Movement}.
     *
     * @throws IOException       Viene lanciata quando avviene un Errore nell'Import dei dati
     * @throws LedgerException   Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Movimenti
     * @throws ParseException    Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Movimenti
     * @throws MovementException Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Movimenti
     */
    void getMovements() throws IOException, ParseException, MovementException, LedgerException;

    /**
     * Importa i dati delle {@link Transaction}.
     *
     * @throws IOException Viene lanciata quando avviene un Errore nell'Import dei dati
     */
    void getTransaction() throws IOException;

}
