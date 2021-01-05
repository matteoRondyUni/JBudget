package it.unicam.cs.pa.jbudget.javaFX.input.delete;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.javaFX.input.JavaFXInputChoiceBox;
import it.unicam.cs.pa.jbudget.model.Category;
import it.unicam.cs.pa.jbudget.javaFX.input.JavaFXInputController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * La classe implementa le interfacce {@link JavaFXDelete}, {@link JavaFXInputController},
 * {@link JavaFXInputChoiceBox} ed ha la responsabilita' di gestire
 * la Finestra per l'eliminazione di una {@link Category}.
 *
 * @author Matteo Rondini
 */
public class JavaFXCategoryDelete implements JavaFXDelete<Category>, JavaFXInputController, JavaFXInputChoiceBox {
    private final Controller controller;

    @FXML
    ChoiceBox<Category> ID;

    public JavaFXCategoryDelete(Controller controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FXML
    public void showID() {
        ID.getItems().clear();
        ID.setItems(FXCollections.observableArrayList(controller.getCategory()));
    }

    /**
     * Elimina la {@link Category}. In caso di Errore viene visualizzata una Finestra di Dialogo.
     */
    @Override
    @FXML
    public void submit() {
        try {
            if (controller.removeCategory(getObjectToRemove()))
                successWindow("Rimozione Effettuata!", "Categoria Eliminata!");
            cancel();
        } catch (IllegalArgumentException exception) {
            errorWindow(ERROR_TITLE_ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FXML
    public void cancel() {
        closeWindow((Stage) ID.getScene().getWindow());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getObjectToRemove() {
        if (Objects.isNull(ID.getValue()))
            throw new IllegalArgumentException(ERROR_NO_ID_TO_DELETE);
        return ID.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FXML
    public void loadChoiceBox() {
        if (Objects.isNull(ID.getValue()))
            showID();
    }

}
