package it.unicam.cs.pa.jbudget.javaFX;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.controller.MoneyController;
import it.unicam.cs.pa.jbudget.javaFX.input.*;
import it.unicam.cs.pa.jbudget.javaFX.input.delete.*;
import it.unicam.cs.pa.jbudget.model.*;
import it.unicam.cs.pa.jbudget.statistic.MoneyStatistic;
import it.unicam.cs.pa.jbudget.statistic.StaticException;
import it.unicam.cs.pa.jbudget.statistic.StatisticCreator;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * La classe implementa l'interfaccia {@link JavaFXWindowController} ed ha la responsabilita' di gestire
 * la Finestra Principale dell'Applicazione. E' responsabile, inoltre, della visualizzazione delle Tabelle
 * degli {@link Account}, {@link Movement}, {@link Category}.
 *
 * @author Matteo Rondini
 */
public class JavaFXController implements JavaFXWindowController {
    private final String ERROR_NO_ACCOUNT = "Non ci sono Account!\nDevi prima aggiungere almeno un Account.";
    private final String ERROR_NO_CREDITS = "Error - No Credits Movements!";
    private final String ERROR_NO_DEBITS = "Error - No Debits Movements!";
    private final Controller controller = new MoneyController();

    /**
     * Tabella degli Account
     */
    @FXML
    TableView<Account> accountTable;
    @FXML
    TableColumn<Account, Integer> accountTableID;
    @FXML
    TableColumn<Account, String> accountTableName, accountTableDescription;
    @FXML
    TableColumn<Account, Double> accountTableOpeningBalance, accountTableBalance;
    @FXML
    TableColumn<Account, AccountType> accountTableType;

    /**
     * Tabella dei Movimenti
     */
    @FXML
    TableView<Movement> movementTable;
    @FXML
    TableColumn<Movement, Double> movementValue;
    @FXML
    TableColumn<Movement, String> movementDescription, movementDate;
    @FXML
    TableColumn<Movement, Integer> movementID, movementTransaction, movementAccount;
    @FXML
    TableColumn<Movement, MovementType> movementType;

    /**
     * Tabella delle Category
     */
    @FXML
    TableView<Category> categoryTable;
    @FXML
    TableColumn<Category, Integer> categoryID;
    @FXML
    TableColumn<Category, String> categoryName, categoryDescription;

    /**
     * Tabella dei Movimenti con delle Categorie
     */
    @FXML
    TableView<Movement> movementCAT;
    @FXML
    TableColumn<Movement, Double> movementValueCAT;
    @FXML
    TableColumn<Movement, Integer> movementIDCAT, movementTransactionCAT;
    @FXML
    TableColumn<Movement, List<Category>> movementCategory;

    @FXML
    Button saveButton;
    @FXML
    TextField savePath;

    @FXML
    Button openButton;
    @FXML
    TextField importPath;

    /**
     * Statistiche
     */
    @FXML
    TableView<String> categoryMapTable;
    @FXML
    TableColumn<String, String> categoryMapInfo;
    @FXML
    ChoiceBox<Account> accountChoiceBox;
    @FXML
    TextField maxMovementCredits;
    @FXML
    TextField minMovementCredits;
    @FXML
    TextField maxMovementDebits;
    @FXML
    TextField minMovementDebits;

    private void accountChoiceBoxRefresh() {
        accountChoiceBox.getItems().clear();
        accountChoiceBox.getItems().addAll(controller.getAccounts());
    }

    private void accountRefresh() {
        if (!controller.getAccounts().isEmpty()) {
            setAccountCellValueFactory();
            accountTable.getItems().clear();
            accountTable.getItems().addAll(controller.getAccounts());
        } else
            accountTable.getItems().clear();
        accountChoiceBoxRefresh();
    }

    /**
     * Apre la Finestra per aggiungere un {@link Account}.
     */
    @FXML
    public void addAccount() {
        try {
            openWindow("/AccountCreatorWindow.fxml", "Add Account", new JavaFXNewAccountController(controller));
            accountRefresh();
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "addAccount()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per aggiungere un {@link Category}.
     */
    @FXML
    public void addCategory() {
        try {
            openWindow("/CategoryCreatorWindow.fxml", "Add Category", new JavaFXCategoryController(controller));
            categoryRefresh();
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "addCategory()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per aggiungere un {@link Category} ad un {@link Movement}.
     */
    @FXML
    public void addCategoryToMovement() {
        try {
            if (controller.getMovements().isEmpty() || controller.getCategory().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/ConnectorCategoryMovement.fxml", "Add Category to Movement", new JavaFXConnectorCategoryMovementController(controller));
            movementCategoryRefresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Category or Movement!", "Non ci sono Categorie/Movimenti!");
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "addCategoryToMovement()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per aggiungere un {@link Category} ad una {@link Transaction}.
     */
    @FXML
    public void addCategoryToTransaction() {
        try {
            if (controller.getTransactions().isEmpty() || controller.getCategory().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/ConnectorCategoryTransaction.fxml", "Add Category to Transaction", new JavaFXConnectorCategoryTransactionController(controller));
            movementCategoryRefresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Category or Transaction!", "Non ci sono Categorie/Transazioni!");
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "addCategoryToTransaction()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per aggiungere un {@link Movement}.
     */
    @FXML
    public void addSingleMovement() {
        try {
            if (controller.getAccounts().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/SingleMovementCreatorWindow.fxml", "Add Single Movement", new JavaFXSingleMovementController(controller));
            refresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Account!", ERROR_NO_ACCOUNT);
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "addSingleMovement()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per aggiungere un {@link Transaction} con 1 o piu' {@link Movement}.
     */
    @FXML
    public void addMultipleMovement() {
        try {
            if (controller.getAccounts().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/MultipleMovementCreatorWindow.fxml", "Add Multiple Movement", new JavaFXMultipleMovementController(controller));
            refresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Account!", ERROR_NO_ACCOUNT);
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "addMultipleMovement()", exception.getMessage());
        }
    }

    private void categoryRefresh() {
        if (!controller.getCategory().isEmpty()) {
            setCategoryCellValueFactory();
            categoryTable.getItems().clear();
            categoryTable.getItems().addAll(controller.getCategory());
        } else
            categoryTable.getItems().clear();
    }

    /**
     * Apre la Finestra per rimuovere un {@link Account}.
     */
    @FXML
    public void deleteAccount() {
        try {
            if (controller.getAccounts().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/delete/AccountRemoveWindow.fxml", "Remove Account", new JavaFXAccountDelete(controller));
            refresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Account!", ERROR_NO_ACCOUNT);
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "deleteAccount()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per rimuovere una {@link Category}.
     */
    @FXML
    public void deleteCategory() {
        try {
            if (controller.getCategory().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/delete/CategoryRemoveWindow.fxml", "Remove Category", new JavaFXCategoryDelete(controller));
            refresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Category!", "Non ci sono Categorie da eliminare!");
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "deleteCategory()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per rimuovere una {@link Category} da un {@link Movement}.
     */
    @FXML
    public void deleteCategoryToMovement() {
        try {
            if (controller.getCategory().isEmpty() || controller.getMovements().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/delete/CategoryRemoveToMovementWindow.fxml", "Remove Category to Movement", new JavaFxCategoryToMovementDelete(controller));
            refresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Category/Movement!", "Non ci sono Categorie/Movimenti da eliminare!");
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "deleteCategoryToMovement()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per rimuovere una {@link Category} da un {@link Transaction}.
     */
    @FXML
    public void deleteCategoryToTransaction() {
        try {
            if (controller.getCategory().isEmpty() || controller.getTransactions().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/delete/CategoryRemoveToTransactionWindow.fxml", "Remove Category to Movement", new JavaFXCategoryToTransactionDelete(controller));
            refresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Category/Transaction!", "Non ci sono Categorie/Transazioni da eliminare!");
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "deleteCategoryToTransaction()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per rimuovere un {@link Movement}.
     */
    @FXML
    public void deleteMovement() {
        try {
            if (controller.getMovements().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/delete/MovementRemoveWindow.fxml", "Remove Movement", new JavaFXMovementDelete(controller));
            refresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Movement!", "Non ci sono Movimenti da eliminare!");
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "deleteMovement()", exception.getMessage());
        }
    }

    /**
     * Apre la Finestra per rimuovere una {@link Transaction}.
     */
    @FXML
    public void deleteTransaction() {
        try {
            if (controller.getTransactions().isEmpty())
                throw new IllegalArgumentException();
            openWindow("/delete/TransactionRemoveWindow.fxml", "Remove Transaction", new JavaFXTransactionDelete(controller));
            refresh();
        } catch (IllegalArgumentException exception) {
            alertWindow("Error - No Transaction!", "Non ci sono Transazioni da eliminare!");
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "deleteTransaction()", exception.getMessage());
        }
    }

    /**
     * Calcola le Statistiche per un {@link Account} e visualizza le informazioni.
     */
    @FXML
    public void getStatistic() {
        if (!Objects.isNull(accountChoiceBox.getValue())) {
            StatisticCreator statisticCreator = new MoneyStatistic(accountChoiceBox.getValue());
            setStatisticTextField(statisticCreator);
            populateCategoryMapTable(statisticCreator);
        }
    }

    @FXML
    public void importData() {
        if (importPath.getText().equals("")) {
            File directory = new DirectoryChooser().showDialog(openButton.getScene().getWindow());
            importPath.setText(directory.getAbsolutePath());
        }
        try {
            if (!Objects.isNull(importPath))
                controller.importData(importPath.getText());
            refresh();
        } catch (IOException exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "importData()", exception.getMessage());
            errorWindow("Error - Errore nella lettura dei dati", exception.getMessage());
        } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
            errorWindow("Error - Il Formato dei Dati e' Errato!", exception.getMessage());
        } catch (Exception exception) {
            controller.getLogger().logp(Level.WARNING, "JavaFXController", "importData()", exception.getMessage());
            errorWindow("Error!", exception.getMessage());
        }
    }

    private void movementCategoryRefresh() {
        if (!controller.getMovements().isEmpty()) {
            setMovementCATCellValueFactory();
            movementCAT.getItems().clear();
            movementCAT.getItems().addAll(controller.getMovements().stream().filter(
                    movement -> !movement.categories().isEmpty()).collect(Collectors.toList()));
        } else
            movementCAT.getItems().clear();
    }

    private void movementRefresh() {
        if (!controller.getMovements().isEmpty()) {
            setMovementCellValueFactory();
            movementTable.getItems().clear();
            movementTable.getItems().addAll(controller.getMovements());
        } else
            movementTable.getItems().clear();
    }

    /**
     * Apre la Finestra selezionata.
     *
     * @param window       il Percorso del file fxml da visualizzare
     * @param title        il Titolo della Finestra
     * @param FXController il MoneyController della Finestra
     */
    private <T extends JavaFXInputController> void openWindow(String window, String title, T FXController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(window));
        fxmlLoader.setController(FXController);
        Parent root = fxmlLoader.load();
        Image ico = new Image("/JBudget_Icon.png");
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(ico);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void populateCategoryMapTable(StatisticCreator statisticCreator) {
        setCategoryMapCellValueFactory();
        categoryMapTable.getItems().clear();
        List<String> info = new ArrayList<>();
        try {
            Map<Category, Double> map = statisticCreator.getCategoryValue();
            map.forEach((category, value) ->
                    info.add("ID: " + category.getID() + ", Name: " + category.getName() + ", Valore: " + value));
        } catch (StaticException exception) {
            info.add("Error! -" + exception.getMessage());
        }
        categoryMapTable.getItems().addAll(info);
    }

    /**
     * Aggiorna il contenuto delle Tabelle.
     */
    public void refresh() {
        accountRefresh();
        movementRefresh();
        categoryRefresh();
        movementCategoryRefresh();
    }

    @FXML
    public void saveData() {
        if (savePath.getText().equals("")) {
            File directory = new DirectoryChooser().showDialog(saveButton.getScene().getWindow());
            if (!Objects.isNull(directory))
                savePath.setText(directory.getAbsolutePath());
        }
        try {
            if (!savePath.getText().equals(""))
                controller.saveData(savePath.getText());
        } catch (IOException exception) {
            errorWindow("Error - Errore nel Salvataggio dei dati", exception.getMessage());
        }
    }

    private void setAccountCellValueFactory() {
        accountTableID.setCellValueFactory(account -> new SimpleObjectProperty<>(account.getValue().getID()));
        accountTableName.setCellValueFactory(account -> new SimpleObjectProperty<>(account.getValue().getName()));
        accountTableDescription.setCellValueFactory(account -> new SimpleObjectProperty<>(account.getValue().getDescription()));
        accountTableOpeningBalance.setCellValueFactory(account -> new SimpleObjectProperty<>(account.getValue().getOpeningBalance()));
        accountTableBalance.setCellValueFactory(account -> new SimpleObjectProperty<>(account.getValue().getBalance()));
        accountTableType.setCellValueFactory(account -> new SimpleObjectProperty<>(account.getValue().getType()));
    }

    private void setCategoryCellValueFactory() {
        categoryID.setCellValueFactory(category -> new SimpleObjectProperty<>(category.getValue().getID()));
        categoryName.setCellValueFactory(category -> new SimpleObjectProperty<>(category.getValue().getName()));
        categoryDescription.setCellValueFactory(category -> new SimpleObjectProperty<>(category.getValue().getDescription()));
    }

    private void setCategoryMapCellValueFactory() {
        categoryMapInfo.setCellValueFactory(info -> new SimpleObjectProperty<>(info.getValue()));
    }

    private void setMaxMovementsCredits(StatisticCreator statisticCreator) {
        try {
            maxMovementCredits.setText(statisticCreator.getMaxCredit().toString());
        } catch (StaticException exception) {
            maxMovementCredits.setText(ERROR_NO_CREDITS);
        }
    }

    private void setMaxMovementsDebits(StatisticCreator statisticCreator) {
        try {
            maxMovementDebits.setText(statisticCreator.getMaxDebit().toString());
        } catch (StaticException exception) {
            maxMovementDebits.setText(ERROR_NO_DEBITS);
        }
    }

    private void setMinMovementCredits(StatisticCreator statisticCreator) {
        try {
            minMovementCredits.setText(statisticCreator.getMinCredit().toString());
        } catch (StaticException exception) {
            minMovementCredits.setText(ERROR_NO_CREDITS);
        }
    }

    private void setMinMovementDebits(StatisticCreator statisticCreator) {
        try {
            minMovementDebits.setText(statisticCreator.getMinDebit().toString());
        } catch (StaticException exception) {
            minMovementDebits.setText(ERROR_NO_DEBITS);
        }
    }

    private void setMovementCATCellValueFactory() {
        movementIDCAT.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getID()));
        movementValueCAT.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().amount()));
        movementTransactionCAT.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getTransaction().getID()));
        movementCategory.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().categories()));
    }

    private void setMovementCellValueFactory() {
        movementValue.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().amount()));
        movementDescription.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getDescription()));
        movementType.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().type()));
        movementDate.setCellValueFactory(movement ->
                new SimpleObjectProperty<>(new SimpleDateFormat("yyyy-MM-dd").format(movement.getValue().getDate())));
        movementID.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getID()));
        movementTransaction.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getTransaction().getID()));
        movementAccount.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getAccount().getID()));
    }

    private void setStatisticTextField(StatisticCreator statisticCreator) {
        setMaxMovementsCredits(statisticCreator);
        setMaxMovementsDebits(statisticCreator);
        setMinMovementCredits(statisticCreator);
        setMinMovementDebits(statisticCreator);
    }

}
