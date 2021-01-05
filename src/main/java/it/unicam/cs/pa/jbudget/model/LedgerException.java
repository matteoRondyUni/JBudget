package it.unicam.cs.pa.jbudget.model;

/**
 * Eccezione che rappresenta gli errori che possono verificarsi con l'utilizzo del {@link Ledger}.
 *
 * @author Matteo Rondini
 */
public class LedgerException extends Exception {
    /**
     * Errore che si verifica quando si prova ad inserire 2 volte
     * la stessa {@link Transaction} in un {@link Ledger}.
     */
    public static final String E0_THERE_IS_ALREADY_A_TRANSACTION_IN_LEDGER = "Non si puo' inserire 2 volte una Transazione in un Ledger!";

    /**
     * Errore che si verifica quando si prova ad inserire una {@link Transaction} che non e' presente in
     * nessun {@link Movement} all'interno del {@link Ledger}.
     */
    public static final String E1_NO_TRANSACTION_IN_LEDGER = "Non e' presente la transazione in nessuno movimento all'interno del Ledger!";

    /**
     * Errore che si verifica quando si prova ad inserire 2 volte
     * lo stesso {@link Account} in un {@link Ledger}.
     */
    public static final String E2_THERE_IS_ALREADY_AN_ACCOUNT_IN_LEDGER = "Non si puo' inserire 2 volte un Account in un Ledger!";

    /**
     * Errore che si verifica quando si prova ad inserire 2 volte
     * lo stesso {@link Movement} in un {@link Ledger}.
     */
    public static final String E3_THERE_IS_ALREADY_A_MOVEMENT_IN_LEDGER = "Non si puo' inserire 2 volte un Movimento in un Ledger!";

    /**
     * Errore che si verifica quando si prova ad inserire 2 volte
     * la stessa {@link Category} in un {@link Ledger}.
     */
    public static final String E4_THERE_IS_ALREADY_A_CATEGORY_IN_LEDGER = "Non si puo' inserire 2 volte una Categoria in un Ledger!";

    public LedgerException(String message) {
        super(message);
    }
}