package it.unicam.cs.pa.jbudget.javaFX.input;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.Category;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * La classe implementa l'interfaccia {@link JavaFXInputController} ed ha la responsabilita' di gestire
 * la Finestra per la creazione di una {@link Category}.
 *
 * @author Matteo Rondini
 */
public class JavaFXCategoryController implements JavaFXInputController {
    private final Controller controller;

    @FXML
    TextField categoryName;
    @FXML
    TextField categoryDescription;
    @FXML
    Button categorySubmit;

    public JavaFXCategoryController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Crea la {@link it.unicam.cs.pa.jbudget.model.Category} con i dati passati dall'Utente.
     * In caso di Errore viene visualizzata una Finestra di Dialogo.
     */
    @Override
    @FXML
    public void submit() {
        try {
            controller.addCategory(getName(), getDescription());
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
        closeWindow((Stage) categorySubmit.getScene().getWindow());
    }

    private String getName() {
        if (categoryName.getText().equals(""))
            throw new IllegalArgumentException(EXCEPTION_NAME_VOID);
        return categoryName.getText();
    }

    private String getDescription() {
        return categoryDescription.getText();
    }
}
