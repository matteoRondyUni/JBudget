package it.unicam.cs.pa.jbudget.javaFX.input;

import it.unicam.cs.pa.jbudget.javaFX.JavaFXWindowController;

/**
 * Questa interfaccia estende {@link JavaFXWindowController} ed e' implementata dalle classi
 * che hanno la responsabilita' di gestire una Finestra di Input della GUI.
 */
public interface JavaFXInputController extends JavaFXWindowController {
    /**
     * Errore che si verifica quando l'Utente prova a creare un
     * oggetto senza un {@code Name}.
     */
    String EXCEPTION_NAME_VOID = "Il campo Name non puo' essere vuoto!";

    /**
     * Termina l'operazione attuale e chiude la Finestra.
     */
    void submit();

    /**
     * Annulla l'operazione attuale e chiude la Finestra.
     */
    void cancel();
}
