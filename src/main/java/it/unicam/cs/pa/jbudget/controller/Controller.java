package it.unicam.cs.pa.jbudget.controller;

import it.unicam.cs.pa.jbudget.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la classe ha la responsabilita' di
 * controllare i dati passati dalla View verso il Model. Ha inoltre la responsabilita' di controllare e
 * ricreare gli oggetti con le informazioni prese da File.
 *
 * @author Matteo Rondini
 * @see it.unicam.cs.pa.jbudget.model
 */
public interface Controller {
    /**
     * Crea un {@link Account} e lo aggiunge al {@link Ledger}.
     *
     * @param type        Tipologia dell'Account
     * @param name        Nome dell'Account
     * @param description Descrizione dell'Account
     * @param opening     Valore iniziale dell'Account
     */
    void addAccount(AccountType type, String name, String description, double opening);

    /**
     * Crea un {@link Account} e lo aggiunge al {@link Ledger}.
     *
     * @param type        Tipologia dell'Account
     * @param name        Nome dell'Account
     * @param description Descrizione dell'Account
     * @param ID          Codice Identificativo dell'Account
     * @param opening     Valore iniziale dell'Account
     * @throws LedgerException Viene lanciata quando l'ID dell'{@link Account} che si vuole inserire
     *                         e' gia' presente all'interno del Ledger.
     */
    void addAccount(AccountType type, String name, String description, int ID, double opening) throws LedgerException;

    /**
     * Crea una {@link Category} e la aggiunge al {@link Ledger}.
     *
     * @param name        Nome della Categoria
     * @param description Descrizione della Categoria
     */
    void addCategory(String name, String description);

    /**
     * Crea una {@link Category} e la aggiunge al Ledger.
     *
     * @param name        Nome della Categoria
     * @param description Descrizione della Categoria
     * @param ID          ID della Categoria
     * @throws LedgerException Viene lanciata quando l'ID della {@link Category} che si vuole inserire
     *                         e' gia' presente all'interno del Ledger.
     */
    void addCategory(String name, String description, int ID) throws LedgerException;

    /**
     * Crea un {@link Movement} e lo aggiunge nel {@link Ledger}.
     *
     * @param type        Tipologia del movimento
     * @param value       Valore del movimento
     * @param date        Data del movimento
     * @param description Descrizione del movimento
     * @param transaction Transazione di cui fa parte questo movimento
     * @param account     Account collegato al movimento
     * @throws MovementException Se si verifica una MovementException durante la creazione
     *                           del Movimento.
     */
    void addMovement(MovementType type, double value, Date date, String description, Transaction transaction, Account account)
            throws MovementException;

    /**
     * Crea ed aggiunge il primo {@link Movement} di un {@link Transaction} nel {@link Ledger},
     * ritorna la Transazione appena creata.
     *
     * @param type        Tipologia del movimento
     * @param value       Valore del movimento
     * @param date        Data del movimento
     * @param description Descrizione del movimento
     * @param account     Account collegato al movimento
     * @return la {@link Transaction} del movimento creato.
     * @throws MovementException Se si verifica una MovementException durante la creazione
     *                           del Movimento.
     * @throws LedgerException   Se si verifica una LedgerException durante l'inserimento
     *                           del Movimento.
     */
    Transaction addMovement(MovementType type, double value, Date date, String description, Account account)
            throws MovementException, LedgerException;

    /**
     * Aggiunge un {@link Movement} creato dal File.
     *
     * @param movement Movimento prelevato dal File
     * @throws LedgerException Viene lanciata quando si prova ad inserire un {@link Movement} che non
     *                         appartiene ad nessun {@link Account} all'interno del Ledger.
     */
    void addMovement(Movement movement) throws LedgerException;

    /**
     * Ritorna la lista degli {@link Account} all'interno del {@link Ledger}.
     *
     * @return la lista degli Account
     */
    List<Account> getAccounts();

    /**
     * Ritorna la lista dei {@link Category} all'interno del {@link Ledger}.
     *
     * @return la lista delle Categorie
     */
    List<Category> getCategory();

    /**
     * Ritorna il {@link Logger} del {@link Controller}.
     *
     * @return il Logger del Controller
     */
    Logger getLogger();

    /**
     * Ritorna la lista dei {@link Movement} all'interno del {@link Ledger}.
     *
     * @return la lista dei Movimenti
     */
    List<Movement> getMovements();

    /**
     * Ritorna la lista delle {@link Transaction} all'interno del {@link Ledger}.
     *
     * @return la lista delle Transazioni
     */
    List<Transaction> getTransactions();

    /**
     * Importa i dati dell'Applicazione.
     *
     * @param directoryPath Percorso della Cartella da dove importare i dati
     * @throws IOException       Viene lanciata quando avviene un Errore durante la lettura dei {@code Dati} da File
     * @throws LedgerException   Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Dati
     * @throws ParseException    Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Dati
     * @throws MovementException Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Dati
     */
    void importData(String directoryPath) throws IOException, ParseException, MovementException, LedgerException;

    /**
     * Elimina l'{@link Account} dal {@link Ledger}.
     *
     * @param account Account da eliminare
     * @return {@code true} se l'Account viene eliminato dal Ledger
     */
    boolean removeAccount(Account account);

    /**
     * Elimina la {@link Category} dal {@link Ledger}.
     *
     * @param category Categoria da eliminare
     * @return {@code true} se la Categoria viene eliminata dal Ledger
     */
    boolean removeCategory(Category category);

    /**
     * Elimina la {@link Category} del {@link Movement}.
     *
     * @param movement Movimento da cui eliminare la Categoria
     * @param category Categoria da eliminare
     * @return {@code true} se la Categoria viene eliminata
     */
    boolean removeCategory(Movement movement, Category category);

    /**
     * Elimina la {@link Category} del {@link Transaction}.
     *
     * @param transaction Transazione da cui eliminare la Categoria
     * @param category    Categoria da eliminare
     * @return {@code true} se la Categoria viene eliminata
     */
    boolean removeCategory(Transaction transaction, Category category);

    /**
     * Elimina il {@link Movement} dal {@link Ledger}.
     *
     * @param movement Movimento da eliminare
     * @return {@code true} se il Movimento viene eliminato dal Ledger
     */
    boolean removeMovement(Movement movement);

    /**
     * Elimina la {@link Transaction} dal {@link Ledger}.
     *
     * @param transaction Transazione da eliminare
     * @return {@code true} se la Transazione viene eliminata dal Ledger
     */
    boolean removeTransaction(Transaction transaction);

    /**
     * Salva i dati dell'Applicazione.
     *
     * @param directoryPath Percorso della Cartella dove salvare i dati
     * @throws IOException Viene lanciata quando avviene un Errore durante il Salvataggio
     */
    void saveData(String directoryPath) throws IOException;

}


