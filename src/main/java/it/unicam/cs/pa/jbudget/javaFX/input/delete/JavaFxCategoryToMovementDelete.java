package it.unicam.cs.pa.jbudget.javaFX.input.delete;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.javaFX.input.JavaFXInputChoiceBox;
import it.unicam.cs.pa.jbudget.model.Category;
import it.unicam.cs.pa.jbudget.model.Movement;
import it.unicam.cs.pa.jbudget.javaFX.input.JavaFXInputController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * La classe implementa le interfacce {@link JavaFXDelete}, {@link JavaFXInputController},
 * {@link JavaFXInputChoiceBox} ed ha la responsabilita' di gestire
 * la Finestra per l'eliminazione di una {@link Category} da un {@link Movement}.
 *
 * @author Matteo Rondini
 */
public class JavaFxCategoryToMovementDelete implements JavaFXDelete<Category>, JavaFXInputController, JavaFXInputChoiceBox {
    private final Controller controller;

    @FXML
    ChoiceBox<Category> IDCategory;
    @FXML
    ChoiceBox<Movement> IDMovement;

    public JavaFxCategoryToMovementDelete(Controller controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FXML
    public void showID() {
        if (!Objects.isNull(IDMovement.getValue())) {
            IDCategory.getItems().clear();
            IDCategory.setItems(FXCollections.observableArrayList(IDMovement.getValue().categories()));
        }
    }

    /**
     * Mostra l'ID del {@link Movement}.
     */
    @FXML
    public void showMovementID() {
        IDMovement.getItems().clear();
        IDMovement.setItems(FXCollections.observableArrayList(controller.getMovements()));
    }

    /**
     * Elimina la {@link Category} dal {@link Movement}.
     * In caso di Errore viene visualizzata una Finestra di Dialogo.
     */
    @Override
    @FXML
    public void submit() {
        try {
            if (!Objects.isNull(getObjectToRemove()))
                if (controller.removeCategory(getMovementFromID(), getObjectToRemove()))
                    successWindow("Rimozione Effettuata!", "Categoria Eliminata dal Movimento!");
            cancel();
        } catch (IllegalArgumentException exception) {
            errorWindow(ERROR_TITLE_ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }

    private Movement getMovementFromID() {
        if (Objects.isNull(IDMovement.getValue()))
            throw new IllegalArgumentException(ERROR_NO_ID_TO_DELETE);
        return IDMovement.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FXML
    public void cancel() {
        closeWindow((Stage) IDMovement.getScene().getWindow());
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
    @Override
    @FXML
    public void loadChoiceBox() {
        if (Objects.isNull(IDCategory.getValue()))
            showID();
        if (Objects.isNull(IDMovement.getValue()))
            showMovementID();
    }
}
