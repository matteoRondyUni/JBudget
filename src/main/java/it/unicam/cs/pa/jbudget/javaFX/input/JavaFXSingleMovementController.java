package it.unicam.cs.pa.jbudget.javaFX.input;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.*;

/**
 * La classe implementa le interfacce {@link JavaFXInputController}, {@link JavaFXInputChoiceBox}
 * ed ha la responsabilita' di gestire la Finestra per la creazione di un {@link Movement}.
 *
 * @author Matteo Rondini
 */
public class JavaFXSingleMovementController implements JavaFXInputController, JavaFXInputChoiceBox {
    /**
     * Errore che si verifica quando si prova a creare un {@link Movement}
     * senza passare un {@link Account}.
     */
    public static final String EXCEPTION_ACCOUNT_VOID = "Il campo Account non puo' essere vuoto!";
    /**
     * Errore che si verifica quando si prova a creare un {@link Movement}
     * senza passare una {@link Date}.
     */
    public static final String EXCEPTION_DATE_VOID = "Il campo Date non puo' essere vuoto!";

    private final Controller controller;

    @FXML
    ChoiceBox<MovementType> movementType;
    @FXML
    TextField movementValue;
    @FXML
    DatePicker movementDate;
    @FXML
    TextField movementDescription;
    @FXML
    ChoiceBox<Account> movementAccountID;

    public JavaFXSingleMovementController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Visualizza gli {@link Account} nella ChoiceBox. Nel caso non ci siano Account viene visualizzata
     * una Finestra di Dialogo.
     */
    @FXML
    public void showAccountID() {
        List<Account> idList = new ArrayList<>(controller.getAccounts());
        if (idList.isEmpty()) {
            alertWindow("Error - No Account!", "Non ci sono Account da collegare al Movimento!");
            cancel();
        }
        movementAccountID.setItems(FXCollections.observableArrayList(idList));
    }

    /**
     * Visualizza i {@link MovementType} nella ChoiceBox.
     */
    @FXML
    public void showMovementType() {
        ObservableList<MovementType> availableChoices = FXCollections.observableArrayList(MovementType.values());
        movementType.setItems(availableChoices);
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void loadChoiceBox() {
        if (Objects.isNull(movementAccountID.getValue()))
            showAccountID();
        if (Objects.isNull(movementType.getValue()))
            showMovementType();
    }

    /**
     * Crea il {@link Movement} con i dati passati dall'Utente. In caso di Errore viene
     * visualizzata una Finestra di Dialogo.
     */
    @FXML
    public void submit() {
        try {
            controller.addMovement(getMovementType(), getMovementValue(), getMovementDate(),
                    getMovementDescription(), getMovementAccount());
            cancel();
        } catch (NumberFormatException exception) {
            errorWindow(ERROR_TITLE_NUMBER_FORMAT_EXCEPTION, exception.getMessage());
        } catch (LedgerException exception) {
            errorWindow(ERROR_TITLE_LEDGER_EXCEPTION, exception.getMessage());
        } catch (MovementException exception) {
            errorWindow(ERROR_TITLE_MOVEMENT_EXCEPTION, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            errorWindow(ERROR_TITLE_ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void cancel() {
        closeWindow((Stage) movementType.getScene().getWindow());
    }

    protected MovementType getMovementType() {
        if (Objects.isNull(movementType.getValue()))
            return MovementType.CREDITS;
        return movementType.getValue();
    }

    protected double getMovementValue() {
        if (movementValue.getText().equals(""))
            return 0;
        return Double.parseDouble(movementValue.getText());
    }

    protected Date getMovementDate() {
        if (Objects.isNull(movementDate.getValue()))
            throw new IllegalArgumentException(EXCEPTION_DATE_VOID);
        return java.sql.Date.valueOf(movementDate.getValue());
    }

    protected String getMovementDescription() {
        return movementDescription.getText();
    }

    protected Account getMovementAccount() {
        if (Objects.isNull(movementAccountID.getValue()))
            throw new IllegalArgumentException(EXCEPTION_ACCOUNT_VOID);
        return movementAccountID.getValue();
    }

}
