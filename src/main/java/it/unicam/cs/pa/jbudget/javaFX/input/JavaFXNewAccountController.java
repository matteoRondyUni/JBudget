package it.unicam.cs.pa.jbudget.javaFX.input;

import it.unicam.cs.pa.jbudget.controller.Controller;
import it.unicam.cs.pa.jbudget.model.Account;
import it.unicam.cs.pa.jbudget.model.AccountType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * La classe implementa le interfacce {@link JavaFXInputController}, {@link JavaFXInputChoiceBox}
 * ed ha la responsabilita' di gestire la Finestra per la creazione di un {@link Account}.
 *
 * @author Matteo Rondini
 */
public class JavaFXNewAccountController implements JavaFXInputController, JavaFXInputChoiceBox {
    private final Controller controller;

    @FXML
    ChoiceBox<AccountType> accountType;
    @FXML
    TextField accountName;
    @FXML
    TextField accountDescription;
    @FXML
    TextField accountOpeningBalance;

    public JavaFXNewAccountController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Crea l'{@link Account} con i dati passati dall'Utente. In caso di Errore viene
     * visualizzata una Finestra di Dialogo.
     */
    @FXML
    public void submit() {
        try {
            controller.addAccount(getAccountType(), getAccountName(), getAccountDescription(), getAccountOpeningBalance());
            cancel();
        } catch (NumberFormatException exception) {
            errorWindow(ERROR_TITLE_NUMBER_FORMAT_EXCEPTION, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            errorWindow(ERROR_TITLE_ILLEGAL_ARGUMENT, exception.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void cancel() {
        closeWindow((Stage) accountType.getScene().getWindow());
    }

    /**
     * Visualizza gli {@link AccountType} nella ChoiceBox.
     */
    @FXML
    public void showAccountType() {
        ObservableList<AccountType> availableChoices = FXCollections.observableArrayList(AccountType.values());
        accountType.setItems(availableChoices);
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void loadChoiceBox() {
        if (Objects.isNull(accountType.getValue()))
            showAccountType();
    }

    private AccountType getAccountType() {
        if (Objects.isNull(accountType.getValue()))
            return AccountType.ASSETS;
        return accountType.getValue();
    }

    private String getAccountName() {
        if (accountName.getText().equals(""))
            throw new IllegalArgumentException(EXCEPTION_NAME_VOID);
        return accountName.getText();
    }

    private String getAccountDescription() {
        return accountDescription.getText();
    }

    private double getAccountOpeningBalance() {
        if (accountOpeningBalance.getText().equals(""))
            return 0;
        return Double.parseDouble(accountOpeningBalance.getText());
    }
}
