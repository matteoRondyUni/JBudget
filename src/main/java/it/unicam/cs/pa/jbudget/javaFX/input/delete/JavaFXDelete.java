package it.unicam.cs.pa.jbudget.javaFX.input.delete;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di
 * permettere all'Utente di selezionare l'oggetto da eliminare in una Finestra di Input della GUI.
 */
public interface JavaFXDelete<T> {
    String ERROR_NO_ID_TO_DELETE = "Il campo ID non puo' essere vuoto.";

    /**
     * Mostra l'ID della dell'Oggetto da eliminare.
     */
    void showID();

    /**
     * Ritorna l'oggetto da eliminare selezionato dall'Utente tramite ChoiceBox.
     *
     * @return l'oggetto da eliminare
     */
    T getObjectToRemove();
}
