package it.unicam.cs.pa.jbudget.read;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * La classe implementa l'interfaccia {@link Importer} ed ha la responsabilita' di gestire
 * l'import dei dati salvati nei File {@code txt},
 * e la loro trasformazione in oggetti del Model attraverso un {@link Controller}.
 *
 * @author Matteo Rondini
 * @see it.unicam.cs.pa.jbudget.model
 */
public class FileTxtImporter implements Importer {
    final Controller controller;
    final String accountPath;
    final String movementPath;
    final String categoryPath;
    final String transactionPath;

    /**
     * Crea un FileTxtImporter con il riferimento ad il {@link Controller} e il percorso
     * della Cartella dove importare i dati con estensione {@code txt}.
     *
     * @param controller MoneyController dell'Applicazione
     * @param directory  Percorso da dove importare i dati
     */
    public FileTxtImporter(Controller controller, String directory) {
        this.controller = controller;
        accountPath = directory + "/Account.txt";
        movementPath = directory + "/Movement.txt";
        categoryPath = directory + "/Category.txt";
        transactionPath = directory + "/Transaction.txt";
    }

    /**
     * Crea un {@link Movement} con le informazione lette dal File.
     */
    private void addMovement(String movementInfo) throws ParseException, MovementException, LedgerException {
        String[] info = movementInfo.split(";");
        Transaction transaction = getMovementTransaction(info[5]);
        Movement movement = new MoneyMovement(getMovementType(info[1]), getMovementID(info[0]),
                getDoubleValue(info[2]), getMovementDate(info[3]), info[4],
                transaction, getMovementAccount(info[6]));

        controller.addMovement(movement);

        if (info.length == 8 && !Objects.isNull(info[7]))
            setCategory(movement, info[7]);
    }

    private void addTransactionCategory(String infoFromFile) {
        String[] info = infoFromFile.split(";");
        if (info.length != 2)
            throw new IndexOutOfBoundsException("Errore nel Formato dei Dati della Transazione!");
        Transaction transaction = searchTransaction(info[0]);
        String[] categories = info[1].split("-");
        if (!Objects.isNull(transaction))
            for (String category : categories)
                transaction.addCategory(searchCategory(category));
    }

    private int getAccountID(String ID) {
        return Integer.parseInt(ID);
    }

    private AccountType getAccountType(String type) {
        return AccountType.valueOf(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAccounts() throws IOException, LedgerException {
        for (String account : importData(accountPath))
            if (!account.equals("")) {
                String[] info = account.split(";");
                controller.addAccount(getAccountType(info[1]), info[2], info[3],
                        getAccountID(info[0]), getDoubleValue(info[4]));
            }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getCategories() throws IOException, LedgerException {
        for (String category : importData(categoryPath))
            if (!category.equals("")) {
                String[] info = category.split(";");
                controller.addCategory(info[2], info[1], Integer.parseInt(info[0]));
            }
    }

    private double getDoubleValue(String value) {
        return Double.parseDouble(value);
    }

    private Account getMovementAccount(String accountID) {
        for (Account account : controller.getAccounts())
            if (account.getID() == Integer.parseInt(accountID))
                return account;
        return null;
    }

    private Date getMovementDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    /**
     * Controlla che l'ID del {@link Movement} non sia gia' presente nel Ledger.
     * Controlla se l'IDMovement deve essere aggiornato in base alla creazione del Movimento letto dal File.
     * Ritorna l'ID del Movimento.
     */
    private int getMovementID(String IDFromFile) throws LedgerException {
        for (Movement movement : controller.getMovements())
            if (movement.getID() == Integer.parseInt(IDFromFile))
                throw new LedgerException(LedgerException.E3_THERE_IS_ALREADY_A_MOVEMENT_IN_LEDGER);

        return Integer.parseInt(IDFromFile);
    }

    /**
     * Ritorna la Transazione del Movimento presa dal File.
     */
    private Transaction getMovementTransaction(String transactionID) {
        Transaction transaction = searchTransaction(transactionID);
        if (Objects.isNull(transaction))
            return new MoneyTransaction(Integer.parseInt(transactionID));
        else
            return transaction;
    }

    private MovementType getMovementType(String type) {
        return MovementType.valueOf(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getMovements() throws IOException, ParseException, MovementException, LedgerException {
        for (String movement : importData(movementPath))
            if (!movement.equals(""))
                addMovement(movement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getTransaction() throws IOException {
        for (String transaction : importData(transactionPath))
            if (!transaction.equals(""))
                addTransactionCategory(transaction);
    }

    private List<String> importData(String path) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        List<String> data = new ArrayList<>();
        while (fileReader.ready())
            data.add(fileReader.readLine());
        return data;
    }

    /**
     * Cerca se nel Ledger e' presente una Categoria con l'ID passato, se si ritorna quella Categoria
     * altrimenti ritorna {@code null}.
     */
    private Category searchCategory(String categoryID) {
        for (Category category : controller.getCategory())
            if (category.getID() == Integer.parseInt(categoryID))
                return category;

        return null;
    }

    /**
     * Cerca se nel Ledger e' presente una Transazione con l'ID passato, se si ritorna quella Transazione
     * altrimenti ritorna {@code null}.
     */
    private Transaction searchTransaction(String transactionID) {
        for (Transaction transaction : controller.getTransactions())
            if (transaction.getID() == Integer.parseInt(transactionID))
                return transaction;
        return null;
    }

    /**
     * Connette le Categorie al Movimento, con le informazioni importante dal File.
     */
    private void setCategory(Movement movement, String categoriesFromFile) {
        String[] categories = categoriesFromFile.split("-");
        for (String category : categories)
            movement.addCategory(searchCategory(category));
    }

}
