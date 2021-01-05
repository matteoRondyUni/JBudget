package it.unicam.cs.pa.jbudget.javaFX.input;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.Category;
import it.unicam.cs.pa.jbudget.model.Ledger;
import it.unicam.cs.pa.jbudget.model.Movement;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * La classe implementa le interfacce {@link JavaFXInputController}, {@link JavaFXInputChoiceBox}
 * ed ha la responsabilita' di gestire la Finestra per creare il collegamento
 * di una {@link Category} con un {@link Movement}.
 *
 * @author Matteo Rondini
 */
public class JavaFXConnectorCategoryMovementController implements JavaFXInputController, JavaFXInputChoiceBox {
    /**
     * Errore che si verifica quando non sono presenti {@link Category} nel {{@link Ledger}.
     */
    public static final String ERROR_NO_CATEGORY = "Non ci sono Categorie!";
    /**
     * Errore che si verifica quando non sono presenti {@link Movement} nel {{@link Ledger}.
     */
    public static final String ERROR_NO_MOVEMENT = "Non ci sono Movimenti!";
    private final Controller controller;

    @FXML
    ChoiceBox<Category> IDCategory;
    @FXML
    ChoiceBox<Movement> IDMovement;

    public JavaFXConnectorCategoryMovementController(Controller controller) {
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
     * Visualizza i {@link Movement} nella ChoiceBox.
     */
    @FXML
    public void showMovementID() {
        IDMovement.getItems().clear();
        IDMovement.setItems(FXCollections.observableArrayList(controller.getMovements()));
    }

    /**
     * Collega il {@link Movement} alla {@link Category}. In caso di Errore viene
     * visualizzata una Finestra di Dialogo.
     */
    @Override
    public void submit() {
        try {
            if (Objects.isNull(getCategoryFromID()))
                throw new IllegalArgumentException(ERROR_NO_CATEGORY);
            if (Objects.isNull(getMovementFromID()))
                throw new IllegalArgumentException(ERROR_NO_MOVEMENT);
            getMovementFromID().addCategory(getCategoryFromID());
            cancel();
        } catch (IllegalArgumentException exception) {
            errorWindow(ERROR_TITLE_ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }

    private Movement getMovementFromID() {
        return IDMovement.getValue();
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
        if (Objects.isNull(IDMovement.getValue()))
            showMovementID();
        if (Objects.isNull(IDCategory.getValue()))
            showCategoryID();
    }
}
