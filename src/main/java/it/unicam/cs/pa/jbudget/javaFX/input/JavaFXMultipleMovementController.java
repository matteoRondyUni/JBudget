package it.unicam.cs.pa.jbudget.javaFX.input;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Date;
import java.util.Objects;

/**
 * La classe estende la classe {@link JavaFXSingleMovementController}
 * ed ha la responsabilita' di gestire la Finestra per la creazione di una {@link Transaction}
 * con 1 o piu' {@link Movement}.
 *
 * @author Matteo Rondini
 */
public class JavaFXMultipleMovementController extends JavaFXSingleMovementController {
    private final Controller controller;
    private Transaction transaction;

    @FXML
    TabPane transactionPane;
    @FXML
    Tab transactionMovement;

    /**
     * Tabella dei Movimenti della Transazione
     */
    @FXML
    TableView<Movement> movementTable;
    @FXML
    TableColumn<Movement, Double> movementTableValue;
    @FXML
    TableColumn<Movement, String> movementTableDescription;
    @FXML
    TableColumn<Movement, MovementType> movementTableType;
    @FXML
    TableColumn<Movement, Date> movementTableDate;
    @FXML
    TableColumn<Movement, Integer> movementTableAccount;

    public JavaFXMultipleMovementController(Controller controller) {
        super(controller);
        this.controller = controller;
    }

    /**
     * Crea il {@link Movement} appartenente alla {@link Transaction} con i dati passati dall'Utente.
     * In caso di Errore viene visualizzata una Finestra di Dialogo.
     */
    @FXML
    public void submit() {
        try {
            if (Objects.isNull(transaction))
                transaction = controller.addMovement(getMovementType(), getMovementValue(), getMovementDate(), getMovementDescription(), getMovementAccount());
            else
                controller.addMovement(getMovementType(), getMovementValue(), getMovementDate(), getMovementDescription(), transaction, getMovementAccount());
            transactionPane.getSelectionModel().select(transactionMovement);
            refresh();
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
     * Aggiorna il contenuto della tabella.
     */
    public void refresh() {
        setMovementCellValueFactory();
        movementTable.getItems().clear();
        for (Movement movement : transaction.movements())
            movementTable.getItems().add(movement);
    }

    private void setMovementCellValueFactory() {
        movementTableValue.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().amount()));
        movementTableDescription.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getDescription()));
        movementTableType.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().type()));
        movementTableDate.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getDate()));
        movementTableAccount.setCellValueFactory(movement -> new SimpleObjectProperty<>(movement.getValue().getAccount().getID()));
    }
}
