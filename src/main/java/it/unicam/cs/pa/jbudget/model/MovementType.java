package it.unicam.cs.pa.jbudget.model;

/**
 * Tipologia di {@link Movement} (DEBITS, CREDITS). La tipologia di movimento determina l'effetto di un
 * movimento su un {@link Account}. Infatti, il saldo d'un conto di tipo ASSET crescera' con movimenti di tipo CREDITS
 * e diminuira' con movimenti di tipo DEBITS.  Viceversa, il saldo d'un conto di tipo LIABILITIES aumentera'
 * con movimenti di tipo DEBITS e diminuira' con movimenti di tipo CREDITS. All'interno di una transazione
 * i movimenti DEBITS saranno trattati sempre come negativi, quelli CREDITS come positivi.
 *
 * @see AccountType
 */
public enum MovementType {
    CREDITS,
    DEBITS
}
