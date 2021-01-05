package it.unicam.cs.pa.jbudget.model;

import java.util.List;
import java.util.function.Predicate;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di gestire tutti i dati
 * dell'applicazione. E' responsabile della creazione e cancellazione dei {@link Account},
 * dell'aggiunta e cancellazione delle {@link Transaction}, della creazione e cancellazione delle {@link Category}.
 * Inoltre mantiene la lista delle transazione schedulate. Si occupa di schedulare le transazioni ad una certa data.
 */
public interface Ledger {
    /**
     * Crea l'{@link Account} da inserire nel Ledger.
     *
     * @param type        Tipologia dell'Account
     * @param name        Nome dell'Account
     * @param description Descrizione dell'Account
     * @param opening     Valore iniziale dell'Account
     * @return il {@code Conto} appena creato.
     */
    Account addAccount(AccountType type, String name, String description, double opening);

    /**
     * Crea l'{@link Account} da inserire nel Ledger.
     *
     * @param type        Tipologia dell'Account
     * @param name        Nome dell'Account
     * @param description Descrizione dell'Account
     * @param ID          ID dell'Account da creare
     * @param opening     Valore iniziale dell'Account
     * @throws LedgerException Viene lanciata quando l'ID dell'{@link Account} che si vuole inserire
     *                         e' gia' presente all'interno del Ledger.
     */
    void addAccount(AccountType type, String name, String description, int ID, double opening) throws LedgerException;

    /**
     * Crea la {@link Category} da inserire nel Ledger.
     *
     * @param name        Nome della Categoria
     * @param description Descrizione della Categoria
     * @param ID          ID della Categoria
     * @throws LedgerException Viene lanciata quando l'ID della {@link Category} che si vuole inserire
     *                         e' gia' presente all'interno del Ledger.
     */
    void addCategory(String name, String description, int ID) throws LedgerException;

    /**
     * Crea la {@link Category} da inserire nel Ledger.
     *
     * @param name        Nome della Categoria
     * @param description Descrizione della Categoria
     * @return la Categoria appena creata
     */
    Category addCategory(String name, String description);

    void addScheduledTransaction(ScheduledTransaction st);

    /**
     * Aggiunge una {@link Transaction} solo se quest'ultima e' presente in un {@link Account} all'interno
     * del Ledger.
     *
     * @param transaction Transazione da aggiungere.
     * @throws LedgerException Viene lanciata quando la {@code Transazione} non e' presente in nessuno {@code Conto}
     *                         all'interno del Ledger.
     */
    void addTransaction(Transaction transaction) throws LedgerException;

    /**
     * Ritorna la lista degli {@link Account} nel Ledger.
     *
     * @return la {@code List} degli Account
     */
    List<Account> getAccount();

    /**
     * Ritorna la lista delle {@link Category} del Ledger.
     *
     * @return la {@code List} delle Categorie
     */
    List<Category> getCategories();

    /**
     * Ritorna la lista delle {@link Transaction} del Ledger.
     *
     * @return la {@code List} delle Transazioni
     */
    List<Transaction> getTransactions();

    /**
     * Ritorna la lista delle {@link Transaction} del Ledger che soddisfano un {@link Predicate}.
     *
     * @param predicate il Predicate da soddisfare
     * @return la {@code List} delle Transazioni
     */
    List<Transaction> getTransactions(Predicate<Transaction> predicate);

    /**
     * Elimina l'{@link Account} dal Ledger.
     *
     * @param account Account da eliminare
     * @return {@code true} se l'Account e' stato eliminato
     */
    boolean removeAccount(Account account);

    /**
     * Elimina la {@link Category} dal Ledger.
     *
     * @param category Categoria da eliminare
     * @return {@code true} se la Categoria e' stata eliminata
     */
    boolean removeCategory(Category category);

    /**
     * Elimina la {@link Transaction} dal Ledger.
     *
     * @param transaction Transazione da eliminare
     * @return {@code true} se la Transazione e' stata eliminata
     */
    boolean removeTransaction(Transaction transaction);

}
