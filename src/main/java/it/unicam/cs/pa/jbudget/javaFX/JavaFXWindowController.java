package it.unicam.cs.pa.jbudget.javaFX;

import it.unicam.cs.pa.jbudget.model.LedgerException;
import it.unicam.cs.pa.jbudget.model.MovementException;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di
 * gestire una Finestra della GUI. E' inoltre responsabile della visualizzazione delle Finestre di
 * Successo, Avviso, Errore.
 *
 * @author Matteo Rondini
 */
public interface JavaFXWindowController {
    /**
     * Titolo della finestra d'errore che viene aperta dopo una
     * {@link IllegalArgumentException}.
     */
    String ERROR_TITLE_ILLEGAL_ARGUMENT = "Error - Illegal Argument Exception!";

    /**
     * Titolo della finestra d'errore che viene aperta dopo una
     * {@link MovementException}.
     */
    String ERROR_TITLE_MOVEMENT_EXCEPTION = "Error - Movement Exception";

    /**
     * Titolo della finestra d'errore che viene aperta dopo una
     * {@link LedgerException}.
     */
    String ERROR_TITLE_LEDGER_EXCEPTION = "Error - Ledger Exception";

    /**
     * Titolo della finestra d'errore che viene aperta dopo una
     * {@link NumberFormatException}.
     */
    String ERROR_TITLE_NUMBER_FORMAT_EXCEPTION = "Error - Number Format Exception";

    default void closeWindow(Stage stage) {
        stage.close();
    }

    default void errorWindow(String errorHeader, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        showAlert(alert, errorHeader, errorMessage);
    }

    default void alertWindow(String warningHeader, String warningMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        showAlert(alert, warningHeader, warningMessage);
    }

    default void successWindow(String successHeader, String successMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        showAlert(alert, successHeader, successMessage);
    }

    private void showAlert(Alert alert, String header, String message) {
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
