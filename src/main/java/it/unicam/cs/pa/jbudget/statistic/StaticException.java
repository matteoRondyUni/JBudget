package it.unicam.cs.pa.jbudget.statistic;

import it.unicam.cs.pa.jbudget.model.Account;
import it.unicam.cs.pa.jbudget.model.Movement;

/**
 * Eccezione che rappresenta gli errori che possono verificarsi con l'utilizzo di una classe che
 * implementa {@link StatisticCreator}.
 *
 * @author Matteo Rondini
 */
public class StaticException extends Exception {
    /**
     * Errore che si verifica quando si prova a calcolare delle Statistiche
     * di un {@link Account} non valido.
     */
    public static final String E0_NO_ACCOUNT = "Non e' stato inserito nessun Account utilizzabile!";

    /**
     * Errore che si verifica quando si prova a calcolare delle Statistiche
     * per un {@link Account} che non ha {@link Movement} al suo interno.
     */
    public static final String E1_NO_MOVEMENT_IN_ACCOUNT = "Non ci sono Movimenti all'interno dell'Account!";

    public StaticException(String message) {
        super(message);
    }
}
