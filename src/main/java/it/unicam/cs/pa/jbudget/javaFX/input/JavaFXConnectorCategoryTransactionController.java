package it.unicam.cs.pa.jbudget.javaFX.input;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.Category;
import it.unicam.cs.pa.jbudget.model.Ledger;
import it.unicam.cs.pa.jbudget.model.Transaction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * La classe implementa le interfacce {@link JavaFXInputController}, {@link JavaFXInputChoiceBox}
 * ed ha la responsabilita' di gestire la Finestra per creare il collegamento
 * di una {@link Category} con una {@link Transaction}.
 *
 * @author Matteo Rondini
 */
public class JavaFXConnectorCategoryTransactionController implements JavaFXInputController, JavaFXInputChoiceBox {
    /**
     * Errore che si verifica quando non sono presenti {@link Category} nel {{@link Ledger}.
     */
    public static final String ERROR_NO_CATEGORY = "Non ci sono Categorie!";
    /**
     * Errore che si verifica quando non sono presenti {@link Transaction} nel {{@link Ledger}.
     */
    public static final String ERROR_NO_TRANSACTION = "Non ci sono Transazioni!";
    private final Controller controller;

    @FXML
    ChoiceBox<Category> IDCategory;
    @FXML
    ChoiceBox<Transaction> IDTransaction;

    public JavaFXConnectorCategoryTransactionController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Visualizza le {@link Category} nella ChoiceBox.
     */
    @FXML
    public void showCategoryID() {
        IDCategory.getItems().clear();
        IDCategory.setItems(FXCollections.observableArrayList(controller.getCategory()));
    }

    /**
     * Visualizza le {@link Transaction} nella ChoiceBox.
     */
    @FXML
    public void showTransactionID() {
        IDTransaction.getItems().clear();
        IDTransaction.setItems(FXCollections.observableArrayList(controller.getTransactions()));
    }

    /**
     * Collega la {@link Transaction} alla {@link Category}. In caso di Errore viene
     * visualizzata una Finestra di Dialogo.
     */
    @Override
    public void submit() {
        try {
            if (Objects.isNull(getCategoryFromID()))
                throw new IllegalArgumentException(ERROR_NO_CATEGORY);
            if (Objects.isNull(getTransactionFromID()))
                throw new IllegalArgumentException(ERROR_NO_TRANSACTION);
            getTransactionFromID().addCategory(getCategoryFromID());
            cancel();
        } catch (IllegalArgumentException exception) {
            errorWindow(ERROR_TITLE_ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }

    private Transaction getTransactionFromID() {
        return IDTransaction.getValue();
    }

    private Category getCategoryFromID() {
        return IDCategory.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel() {
        closeWindow((Stage) IDCategory.getScene().getWindow());
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void loadChoiceBox() {
        if (Objects.isNull(IDTransaction.getValue()))
            showTransactionID();
        if (Objects.isNull(IDCategory.getValue()))
            showCategoryID();
    }

}
