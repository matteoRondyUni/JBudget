package it.unicam.cs.pa.jbudget.javaFX.input.delete;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.javaFX.input.JavaFXInputChoiceBox;
import it.unicam.cs.pa.jbudget.model.Account;
import it.unicam.cs.pa.jbudget.javaFX.input.JavaFXInputController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * La classe implementa le interfacce {@link JavaFXDelete}, {@link JavaFXInputController},
 * {@link JavaFXInputChoiceBox} ed ha la responsabilita' di gestire
 * la Finestra per l'eliminazione di un {@link Account}.
 *
 * @author Matteo Rondini
 */
public class JavaFXAccountDelete implements JavaFXDelete<Account>, JavaFXInputController, JavaFXInputChoiceBox {
    private final Controller controller;

    @FXML
    ChoiceBox<Account> ID;

    public JavaFXAccountDelete(Controller controller) {
        this.controller = controller;
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
    public Account getObjectToRemove() {
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

    /**
     * {@inheritDoc}
     */
    @Override
    @FXML
    public void showID() {
        ID.getItems().clear();
        ID.setItems(FXCollections.observableArrayList(controller.getAccounts()));
    }

    /**
     * Elimina l'{@link Account}. In caso di Errore viene visualizzata una Finestra di Dialogo.
     */
    @Override
    @FXML
    public void submit() {
        try {
            if (controller.removeAccount(getObjectToRemove()))
                successWindow("Rimozione Effettuata!", "Account Eliminato!");
            cancel();
        } catch (IllegalArgumentException exception) {
            errorWindow(ERROR_TITLE_ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }
}
