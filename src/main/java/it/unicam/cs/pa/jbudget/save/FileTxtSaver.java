package it.unicam.cs.pa.jbudget.save;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe implementa l'interfaccia {@link Saver} ed ha la responsabilita' di gestire
 * il salvataggio dei dati del Model in vari file con estensione {@code txt}.
 *
 * @author Matteo Rondini
 * @see it.unicam.cs.pa.jbudget.model
 */
public class FileTxtSaver implements Saver {
    final Controller controller;
    final String accountPath;
    final String movementPath;
    final String categoryPath;
    final String transactionPath;

    /**
     * Crea un FileTxtSaver con il riferimento ad il {@link Controller} e il percorso
     * della Cartella dove salvare i dati con estensione {@code txt}.
     *
     * @param controller Controller dell'Applicazione
     * @param directory  Percorso dove salvare i dati
     */
    public FileTxtSaver(Controller controller, String directory) {
        this.controller = controller;
        accountPath = directory + "/Account.txt";
        movementPath = directory + "/Movement.txt";
        categoryPath = directory + "/Category.txt";
        transactionPath = directory + "/Transaction.txt";
    }

    private String accountInformation(Account account) {
        return account.getID() + ";" + account.getType() + ";" + account.getName() + ";"
                + account.getDescription() + ";" + account.getOpeningBalance();
    }

    /**
     * Genera una stringa che contiene le Categorie del Movimento passato.
     */
    private String categoryInformation(Movement movement) {
        String toReturn = "";
        for (Category category : movement.categories())
            toReturn = toReturn.concat(category.getID() + "-");
        return toReturn;
    }

    /**
     * Genera una stringa che contiene le informazioni di una Categoria.
     */
    private String categoryInformation(Category category) {
        return category.getID() + ";" + category.getDescription() + ";" + category.getName();
    }

    /**
     * Genera una stringa che contiene le Categorie della Transazione passata.
     */
    private String categoryInformation(Transaction transaction) {
        String toReturn = "";
        for (Category category : transaction.categories())
            toReturn = toReturn.concat(category.getID() + "-");
        return toReturn;
    }

    private List<String> getAccountsToSave() {
        List<String> accountToSave = new ArrayList<>();
        controller.getAccounts().forEach(account -> accountToSave.add(accountInformation(account)));
        return accountToSave;
    }

    private List<String> getCategoriesToSave() {
        List<String> categoryToSave = new ArrayList<>();
        controller.getCategory().forEach(category -> categoryToSave.add(categoryInformation(category)));
        return categoryToSave;
    }

    private List<String> getMovementsToSave() {
        List<String> movementToSave = new ArrayList<>();
        controller.getMovements().forEach(movement -> movementToSave.add(movementInformation(movement)));
        return movementToSave;
    }

    private List<String> getTransactionToSave() {
        List<String> transactionToSave = new ArrayList<>();
        controller.getTransactions().forEach(transaction -> {
            if (!transaction.categories().isEmpty())
                transactionToSave.add(transaction.getID() + ";" + categoryInformation(transaction));
        });
        return transactionToSave;
    }

    private String movementInformation(Movement movement) {
        return movement.getID() + ";" + movement.type() + ";" + movement.amount() + ";" +
                new SimpleDateFormat("yyyy-MM-dd").format(movement.getDate()) + ";"
                + movement.getDescription() + ";" + movement.getTransaction().getID() + ";"
                + movement.getAccount().getID() + ";" + categoryInformation(movement);
    }

    private void save(List<String> stringToSave, String path) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
        fileWriter.write("");
        for (String info : stringToSave) {
            fileWriter.append(info);
            fileWriter.newLine();
        }
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * {@inheritDoc}
     */
    public void saveAccount() throws IOException {
        save(getAccountsToSave(), accountPath);
    }

    /**
     * {@inheritDoc}
     */
    public void saveCategory() throws IOException {
        save(getCategoriesToSave(), categoryPath);
    }

    /**
     * {@inheritDoc}
     */
    public void saveMovement() throws IOException {
        save(getMovementsToSave(), movementPath);
    }

    /**
     * {@inheritDoc}
     */
    public void saveTransactionCategory() throws IOException {
        save(getTransactionToSave(), transactionPath);
    }

}
