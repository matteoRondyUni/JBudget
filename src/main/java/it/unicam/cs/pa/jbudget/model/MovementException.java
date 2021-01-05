package it.unicam.cs.pa.jbudget.model;

/**
 * Eccezione che rappresenta gli errori che possono verificarsi con la creazione di un {@link Movement}.
 *
 * @author Matteo Rondini
 */
public class MovementException extends Exception {
    /**
     * Errore che si verifica quando si prova a creare/modificare un {@link Movement} con il campo
     * {@code value} negativo.
     */
    public static final String E0_NEGATIVE_VALUE = "Il movimento non puo' avere valore negativo!";

    /**
     * Errore che si verifica quando si prova a creare un {@link Movement}
     * con l'attributo {@link Transaction} {@code null}.
     */
    public static final String E1_NULL_TRANSACTION = "La transazione passata e' nulla!";

    /**
     * Errore che si verifica quando si prova ad aggiungere un {@link Movement} appartenente ad una {@link Transaction}
     * ad un'altra Transazione.
     */
    public static final String E2_DIFFERENT_TRANSACTION = "La transazione del movimento e' diversa dalla transazione attuale!";

    /**
     * Errore che si verifica quando si prova ad aggiungere 2 volte lo stesso {@link Movement}
     * all'interno di una {@link Transaction}.
     */
    public static final String E3_THERE_IS_ALREADY_A_MOVEMENT_IN_TRANSACTION = "Non si puo' inserire 2 volte un movimento in una transazione!";

    /**
     * Errore che si verifica quando si prova a creare un {@link Movement}
     * con l'attributo {@link Account} {@code null}.
     */
    public static final String E4_NULL_ACCOUNT = "L'Account passato e' nullo!";

    /**
     * Errore che si verifica quando si prova ad aggiungere un {@link Movement}
     * che appartiene ad un altro {@link Account}.
     */
    public static final String E5_DIFFERENT_ACCOUNT = "L'Account del movimento e' diverso dall'Account attuale!";

    /**
     * Errore che si verifica quando si prova ad aggiungere lo stesso {@link Movement} 2 volte
     * all'interno di un {@link Account}.
     */
    public static final String E6_THERE_IS_ALREADY_A_MOVEMENT_IN_ACCOUNT = "Non si puo' inserire 2 volte un movimento in un Conto!";

    public MovementException(String message) {
        super(message);
    }

}
