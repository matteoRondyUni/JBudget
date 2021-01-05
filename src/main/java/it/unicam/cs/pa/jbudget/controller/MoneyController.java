package it.unicam.cs.pa.jbudget.controller;

import it.unicam.cs.pa.jbudget.read.FileTxtImporter;
import it.unicam.cs.pa.jbudget.save.FileTxtSaver;
import it.unicam.cs.pa.jbudget.read.Importer;
import it.unicam.cs.pa.jbudget.save.Saver;
import it.unicam.cs.pa.jbudget.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La classe implementa l'interfaccia {@link Controller} ed ha la responsabilita' di controllare
 * i dati passati dalla View verso il Model. Ha inoltre la responsabilita' di controllare e
 * ricreare gli oggetti con le informazioni prese da File.
 *
 * @author Matteo Rondini
 * @see it.unicam.cs.pa.jbudget.model
 */
public class MoneyController implements Controller {
    private final Ledger ledger;
    private final Logger logger;
    private int IDTransaction;
    private int IDMovement;

    public MoneyController() {
        ledger = new MoneyLedger();
        IDTransaction = 0;
        IDMovement = 0;
        logger = Logger.getLogger("MoneyController Logger");
        logger.setLevel(Level.WARNING);
    }

    /**
     * Crea un {@link Account} e lo aggiunge al {@link Ledger}.
     *
     * @param type        Tipologia dell'Account
     * @param name        Nome dell'Account
     * @param description Descrizione dell'Account
     * @param opening     Valore iniziale dell'Account
     */
    @Override
    public void addAccount(AccountType type, String name, String description, double opening) {
        ledger.addAccount(type, name, description, opening);
    }

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
    @Override
    public void addAccount(AccountType type, String name, String description, int ID, double opening) throws LedgerException {
        ledger.addAccount(type, name, description, ID, opening);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCategory(String name, String description) {
        ledger.addCategory(name, description);
    }

    /**
     * Crea una {@link Category} e la aggiunge al Ledger.
     *
     * @param name        Nome della Categoria
     * @param description Descrizione della Categoria
     * @param ID          ID della Categoria
     * @throws LedgerException Viene lanciata quando l'ID della {@link Category} che si vuole inserire
     *                         e' gia' presente all'interno del Ledger.
     */
    @Override
    public void addCategory(String name, String description, int ID) throws LedgerException {
        ledger.addCategory(name, description, ID);
    }

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
    @Override
    public void addMovement(MovementType type, double value, Date date, String description,
                            Transaction transaction, Account account) throws MovementException {
        new MoneyMovement(type, IDMovement++, value, date, description, transaction, account);
    }

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
    @Override
    public Transaction addMovement(MovementType type, double value, Date date, String description,
                                   Account account) throws MovementException, LedgerException {
        Transaction transaction = new MoneyTransaction(IDTransaction++);
        try {
            new MoneyMovement(type, IDMovement++, value, date, description, transaction, account);
        } catch (MovementException exception) {
            IDTransaction--;
            throw exception;
        }
        return addTransaction(transaction);
    }

    /**
     * Aggiunge un {@link Movement} creato dal File.
     *
     * @param movement Movimento prelevato dal File
     * @throws LedgerException Viene lanciata quando si prova ad inserire un {@link Movement} che non
     *                         appartiene ad nessun {@link Account} all'interno del Ledger.
     */
    @Override
    public void addMovement(Movement movement) throws LedgerException {
        updateIDTransaction(movement.getTransaction().getID());
        controlFirstMovement(movement);
        updateIDMovement(movement.getID());
    }

    /**
     * Controlla che una {@link Transaction} possa essere aggiunta al {@link Ledger}.
     *
     * @return la Transaction controllata
     */
    private Transaction addTransaction(Transaction transaction) throws LedgerException {
        try {
            ledger.addTransaction(transaction);
        } catch (LedgerException ledgerException) {
            if (ledgerException.getMessage().equals(LedgerException.E1_NO_TRANSACTION_IN_LEDGER))
                IDTransaction--;
            throw ledgerException;
        }
        return transaction;
    }

    /**
     * Controlla se il {@link Movement} appena creato dal File sia il primo della sua {@link Transaction}.
     */
    private void controlFirstMovement(Movement movement) throws LedgerException {
        if (!getTransactions().contains(movement.getTransaction()))
            addTransaction(movement.getTransaction());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Account> getAccounts() {
        return ledger.getAccount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> getCategory() {
        return ledger.getCategories();
    }

    /**
     * Ritorna il {@link Logger} del {@link MoneyController}.
     *
     * @return il Logger del MoneyController
     */
    @Override
    public Logger getLogger() {
        return logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movement> getMovements() {
        List<Movement> movements = new ArrayList<>();
        ledger.getTransactions().forEach(transaction -> movements.addAll(transaction.movements()));
        return movements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> getTransactions() {
        return ledger.getTransactions();
    }

    /**
     * Importa i dati dell'Applicazione.
     *
     * @param directoryPath Percorso della Cartella da dove importare i dati
     * @throws IOException       Viene lanciata quando avviene un Errore durante la lettura dei {@code Dati} da File
     * @throws LedgerException   Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Dati
     * @throws ParseException    Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Dati
     * @throws MovementException Viene lanciata quando avviene un Errore nella {@code ricreazione} dei Dati
     */
    @Override
    public void importData(String directoryPath) throws IOException, ParseException, MovementException, LedgerException {
        Importer fileImporter = new FileTxtImporter(this, directoryPath);
        fileImporter.getAccounts();
        fileImporter.getCategories();
        fileImporter.getMovements();
        fileImporter.getTransaction();
    }

    /**
     * Elimina l'{@link Account} dal {@link Ledger}.
     *
     * @param account Account da eliminare
     * @return {@code true} se l'Account viene eliminato dal Ledger
     */
    @Override
    public boolean removeAccount(Account account) {
        return ledger.removeAccount(account);
    }

    /**
     * Elimina la {@link Category} dal {@link Ledger}.
     *
     * @param category Categoria da eliminare
     * @return {@code true} se la Categoria viene eliminata dal Ledger
     */
    @Override
    public boolean removeCategory(Category category) {
        return ledger.removeCategory(category);
    }

    /**
     * Elimina la {@link Category} del {@link Movement}.
     *
     * @param movement Movimento da cui eliminare la Categoria
     * @param category Categoria da eliminare
     * @return {@code true} se la Categoria viene eliminata
     */
    @Override
    public boolean removeCategory(Movement movement, Category category) {
        if (ledger.getCategories().contains(category))
            return movement.removeCategory(category);
        return false;
    }

    /**
     * Elimina la {@link Category} del {@link Transaction}.
     *
     * @param transaction Transazione da cui eliminare la Categoria
     * @param category    Categoria da eliminare
     * @return {@code true} se la Categoria viene eliminata
     */
    @Override
    public boolean removeCategory(Transaction transaction, Category category) {
        if (ledger.getTransactions().contains(transaction) && ledger.getCategories().contains(category))
            return transaction.removeCategory(category);
        return false;
    }

    /**
     * Elimina il {@link Movement} dal {@link Ledger}.
     *
     * @param movement Movimento da eliminare
     * @return {@code true} se il Movimento viene eliminato dal Ledger
     */
    @Override
    public boolean removeMovement(Movement movement) {
        if (removeMovementFromAccount(movement) && removeMovementFromTransaction(movement)) {
            movement.delete();
            return true;
        }
        return false;
    }

    private boolean removeMovementFromAccount(Movement movement) {
        for (Account account : ledger.getAccount())
            return account.getMovements().removeIf(movementAccount -> movementAccount.equals(movement));
        return false;
    }

    private boolean removeMovementFromTransaction(Movement movement) {
        for (Transaction transaction : ledger.getTransactions()) {
            if (transaction.movements().size() == 1)
                return ledger.removeTransaction(transaction);
            else
                return transaction.movements().removeIf(movementTransaction -> movementTransaction.equals(movement));
        }
        return false;
    }

    /**
     * Elimina la {@link Transaction} dal {@link Ledger}.
     *
     * @param transaction Transazione da eliminare
     * @return {@code true} se la Transazione viene eliminata dal Ledger
     */
    @Override
    public boolean removeTransaction(Transaction transaction) {
        return ledger.removeTransaction(transaction);
    }

    /**
     * Salva i dati dell'Applicazione.
     *
     * @param directoryPath Percorso della Cartella dove salvare i dati
     * @throws IOException Viene lanciata quando avviene un Errore durante il Salvataggio
     */
    @Override
    public void saveData(String directoryPath) throws IOException {
        Saver fileSaver = new FileTxtSaver(this, directoryPath);
        fileSaver.saveAccount();
        fileSaver.saveMovement();
        fileSaver.saveCategory();
        fileSaver.saveTransactionCategory();
    }

    /**
     * Controlla se l'IDMovement deve essere aggiornato in base alla creazione del
     * Movimento letto dal File.
     */
    private void updateIDMovement(int ID) {
        if (IDMovement <= ID)
            IDMovement = ID + 1;
    }

    /**
     * Controlla se l'IDTransaction deve essere aggiornato in base alla creazione della
     * Transazione letta dal File.
     */
    private void updateIDTransaction(int ID) {
        if (IDTransaction <= ID)
            IDTransaction = ID + 1;
    }
}
