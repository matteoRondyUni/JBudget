package it.unicam.cs.pa.jbudget.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * La classe implementa l'interfaccia {@link Ledger} ed ha la responsabilita' di gestire tutti i dati dell'applicazione.
 * E' responsabile della creazione e cancellazioni  dei {@link Account}, dell'aggiunta e cancellazione
 * delle {@link Transaction}, della creazione e cancellazione delle {@link Category}.
 * Inoltre mantiene la lista delle transazione schedulate. Si occupa di schedulare le transazioni ad una certa data.
 *
 * @author Matteo Rondini
 */
public class MoneyLedger implements Ledger {
    private final List<Account> accountList = new ArrayList<>();
    private final List<Transaction> transactionList = new ArrayList<>();
    private final List<Category> categoryList = new ArrayList<>();
    private int IDAccount = 0;
    private int IDCategory = 0;

    /**
     * Crea l'{@link Account} da inserire nel Ledger.
     *
     * @param type        Tipologia dell'Account
     * @param name        Nome dell'Account
     * @param description Descrizione dell'Account
     * @param opening     Valore iniziale dell'Account
     * @return il {@code Conto} appena creato.
     */
    @Override
    public Account addAccount(AccountType type, String name, String description, double opening) {
        Account wallet = new SimpleWallet(type, name, description, IDAccount++, opening);
        accountList.add(wallet);
        sortAccount();
        return wallet;
    }

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
    @Override
    public void addAccount(AccountType type, String name, String description, int ID, double opening) throws LedgerException {
        Account wallet = new SimpleWallet(type, name, description, ID, opening);
        if (!accountList.contains(wallet)) {
            accountList.add(wallet);
            sortAccount();
            if (IDAccount <= ID)
                IDAccount = ID + 1;
        } else throw new LedgerException(LedgerException.E2_THERE_IS_ALREADY_AN_ACCOUNT_IN_LEDGER);
    }

    /**
     * Crea la {@link Category} da inserire nel Ledger.
     *
     * @param name        Nome della Categoria
     * @param description Descrizione della Categoria
     * @return la Categoria appena creata
     */
    @Override
    public Category addCategory(String name, String description) {
        Category category = new MoneyCategory(name, description, IDCategory++);
        categoryList.add(category);
        sortCategory();
        return category;
    }

    /**
     * Crea la {@link Category} da inserire nel Ledger.
     *
     * @param name        Nome della Categoria
     * @param description Descrizione della Categoria
     * @param ID          ID della Categoria
     * @throws LedgerException Viene lanciata quando l'ID della {@link Category} che si vuole inserire
     *                         e' gia' presente all'interno del Ledger.
     */
    @Override
    public void addCategory(String name, String description, int ID) throws LedgerException {
        Category category = new MoneyCategory(name, description, ID);
        if (!categoryList.contains(category)) {
            categoryList.add(category);
            sortCategory();
            if (IDCategory <= ID)
                IDCategory = ID + 1;
        } else throw new LedgerException(LedgerException.E4_THERE_IS_ALREADY_A_CATEGORY_IN_LEDGER);
    }

    @Override
    public void addScheduledTransaction(ScheduledTransaction st) {

    }

    /**
     * Aggiunge una {@link Transaction} solo se quest'ultima e' presente in un {@link Account} all'interno
     * del Ledger.
     *
     * @param transaction Transazione da aggiungere.
     * @throws LedgerException Viene lanciata quando la {@code Transazione} non e' presente in nessuno {@code Conto}
     *                         all'interno del Ledger.
     */
    @Override
    public void addTransaction(Transaction transaction) throws LedgerException {
        controlTransactionToAdd(transaction);
        transactionList.add(transaction);
        sortTransaction();
    }

    /**
     * Controlla se nella lista delle Transazioni sono presenti Transazioni senza Movimenti,
     * se sono presenti elimina queste Transazioni dalla Lista.
     */
    private void controlEmptyMovement() {
        for (Transaction transaction : transactionList)
            transaction.movements().removeIf(movement -> Objects.isNull(movement.getTransaction()));
        transactionList.removeIf(transaction -> transaction.movements().isEmpty());
    }

    /**
     * Controlla che la {@link Transaction} da aggiungere sia collegata
     * ad un {@link Account} nella lista del Ledger o che non sia gia' stata inserita.
     *
     * @param transaction Transazione da controllare
     * @throws LedgerException Viene lanciata quando: la Transazione non e' presente
     *                         in nessun conto all'interno del Ledger o quando risulta gia' inserita.
     */
    private void controlTransactionToAdd(Transaction transaction) throws LedgerException {
        if (transactionList.contains(transaction))
            throw new LedgerException(LedgerException.E0_THERE_IS_ALREADY_A_TRANSACTION_IN_LEDGER);

        boolean isContained = false;
        for (Account account : accountList)
            for (Movement movement : account.getMovements())
                if (movement.getTransaction().equals(transaction))
                    isContained = true;

        if (!isContained)
            throw new LedgerException(LedgerException.E1_NO_TRANSACTION_IN_LEDGER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Account> getAccount() {
        return accountList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> getCategories() {
        return categoryList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> getTransactions() {
        return transactionList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> getTransactions(Predicate<Transaction> predicate) {
        return transactionList.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Elimina l'{@link Account} dal Ledger.
     *
     * @param account Account da eliminare
     * @return {@code true} se l'Account e' stato eliminato
     */
    @Override
    public boolean removeAccount(Account account) {
        if (accountList.contains(account)) {
            for (Movement movement : account.getMovements())
                movement.delete();
            account.getMovements().clear();
            accountList.remove(account);
            controlEmptyMovement();
            return true;
        }
        return false;
    }

    /**
     * Elimina la {@link Category} dal Ledger.
     *
     * @param category Categoria da eliminare
     * @return {@code true} se la Categoria e' stata eliminata
     */
    @Override
    public boolean removeCategory(Category category) {
        if (categoryList.contains(category)) {
            transactionList.forEach(transaction -> transaction.removeCategory(category));
            transactionList.forEach(transaction ->
                    transaction.movements().forEach(movement -> movement.removeCategory(category)));
            categoryList.remove(category);
            return true;
        }
        return false;
    }

    /**
     * Elimina la {@link Transaction} dal Ledger.
     *
     * @param transaction Transazione da eliminare
     * @return {@code true} se la Transazione e' stata eliminata
     */
    @Override
    public boolean removeTransaction(Transaction transaction) {
        if (transactionList.contains(transaction)) {
            for (Account account : accountList)
                account.getMovements().removeIf(movement -> movement.getTransaction().equals(transaction));
            transaction.movements().forEach(Movement::delete);
            transaction.movements().clear();
            transactionList.remove(transaction);
            return true;
        }
        return false;
    }

    private void sortAccount() {
        accountList.sort(null);
    }

    private void sortCategory() {
        categoryList.sort(null);
    }

    private void sortTransaction() {
        transactionList.sort(null);
    }

}
