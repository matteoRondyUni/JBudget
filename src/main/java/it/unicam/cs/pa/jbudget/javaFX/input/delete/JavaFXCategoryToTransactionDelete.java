package it.unicam.cs.pa.jbudget.javaFX.input.delete;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.javaFX.input.JavaFXInputChoiceBox;
import it.unicam.cs.pa.jbudget.model.Category;
import it.unicam.cs.pa.jbudget.model.Transaction;
import it.unicam.cs.pa.jbudget.javaFX.input.JavaFXInputController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * La classe implementa le interfacce {@link JavaFXDelete}, {@link JavaFXInputController},
 * {@link JavaFXInputChoiceBox} ed ha la responsabilita' di gestire
 * la Finestra per l'eliminazione di una {@link Category} da una {@link Transaction}.
 *
 * @author Matteo Rondini
 */
public class JavaFXCategoryToTransactionDelete implements JavaFXDelete<Category>, JavaFXInputController, JavaFXInputChoiceBox {
    private final Controller controller;

    @FXML
    ChoiceBox<Category> IDCategory;
    @FXML
    ChoiceBox<Transaction> IDTransaction;

    public JavaFXCategoryToTransactionDelete(Controller controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FXML
    public void showID() {
        if (!Objects.isNull(IDTransaction.getValue())) {
            IDCategory.getItems().clear();
            IDCategory.setItems(FXCollections.observableArrayList(IDTransaction.getValue().categories()));
        }
    }

    /**
     * Mostra l'ID della {@link Transaction}.
     */
    @FXML
    public void showTransactionID() {
        IDTransaction.getItems().clear();
        IDTransaction.setItems(FXCollections.observableArrayList(controller.getTransactions()));
    }

    /**
     * Elimina la {@link Category} dalla {@link Transaction}.
     * In caso di Errore viene visualizzata una Finestra di Dialogo.
     */
    @Override
    @FXML
    public void submit() {
        try {
            if (!Objects.isNull(getTransactionFromID()) && !Objects.isNull(getObjectToRemove()))
                if (controller.removeCategory(getTransactionFromID(), getObjectToRemove()))
                    successWindow("Rimozione Effettuata!", "Categoria Eliminata dalla Transazione!");
            cancel();
        } catch (IllegalArgumentException exception) {
            errorWindow(ERROR_TITLE_ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }

    private Transaction getTransactionFromID() {
        if (Objects.isNull(IDTransaction.getValue()))
            throw new IllegalArgumentException(ERROR_NO_ID_TO_DELETE);
        return IDTransaction.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FXML
    public void cancel() {
        closeWindow((Stage) IDTransaction.getScene().getWindow());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getObjectToRemove() {
        if (Objects.isNull(IDCategory.getValue()))
            throw new IllegalArgumentException(ERROR_NO_ID_TO_DELETE);
        return IDCategory.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void loadChoiceBox() {
        if (Objects.isNull(IDCategory.getValue()))
            showID();
        if (Objects.isNull(IDTransaction.getValue()))
            showTransactionID();
    }

}
