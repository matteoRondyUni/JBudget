package it.unicam.cs.pa.jbudget.model;

import java.util.List;
import java.util.function.Predicate;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di gestire un conto.
 * Permette di accedere alle informazioni del conto: nome, descrizione, saldo iniziale, tipologia.
 * Permette di modificare il nome e la descrizione. Consente inoltre di ottenere il saldo attuale.
 * Inoltre, e' possibile accedere alla lista dei movimenti e quelli che soddisfano un determinato predicato.
 */
public interface Account extends Comparable<Account> {
    /**
     * Errore che si verifica quando si prova a creare un Account con il campo name {@code null}.
     */
    String EXCEPTION_NAME_NULL = "Il type non puo' essere null!";
    /**
     * Errore che si verifica quando si prova a creare un Account con il campo date {@code null}.
     */
    String EXCEPTION_DESCRIPTION_NULL = "La date non puo' essere null!";

    /**
     * Aggiunge un {@link Movement} all'interno del Conto.
     *
     * @param movement Movimento da aggiungere
     * @throws MovementException Viene lanciata quando si prova
     *                           ad aggiungere un movimento che appartiene ad un altro Account o
     *                           quando si prova ad aggiungere un movimento 2 volte all'interno di un Conto.
     */
    void addMovement(Movement movement) throws MovementException;

    /**
     * Aggiunge una lista di {@link Movement} all'interno del Conto.
     *
     * @param movements Lista di Movimento da aggiungere
     * @throws MovementException Viene lanciata quando si prova
     *                           ad aggiungere un movimento che appartiene ad un altro Account o
     *                           quando si prova ad aggiungere un movimento 2 volte all'interno di un Conto.
     */
    void addMovements(List<? extends Movement> movements) throws MovementException;

    /**
     * Ritorna il bilancio attuale del Conto.
     *
     * @return il bilancio del Conto
     */
    double getBalance();

    /**
     * Ritorna la descrizione del Conto.
     *
     * @return la {@code description} del Conto
     */
    String getDescription();

    /**
     * Cambia la descrizione dell'Account.
     *
     * @param description Descrizione da impostare
     */
    void setDescription(String description);

    /**
     * Ritorna l'ID del Conto.
     *
     * @return l' {@code ID} del Conto
     */
    int getID();

    /**
     * Ritorna la lista dei {@link Movement} dell'Account.
     *
     * @return la {@code List} dei Movimenti
     */
    List<Movement> getMovements();

    /**
     * Ritorna la lista dei {@link Movement} dell'Account che soddisfano un {@link Predicate}.
     *
     * @param predicate il Predicate da soddisfare
     * @return la {@code List} dei Movimenti
     */
    List<Movement> getMovements(Predicate<Movement> predicate);

    /**
     * Ritorna il nome del Conto.
     *
     * @return il {@code name} del Conto
     */
    String getName();

    /**
     * Cambia il nome dell'Account.
     *
     * @param name Nome da impostare
     */
    void setName(String name);

    /**
     * Ritorna il bilancio iniziale del Conto.
     *
     * @return l' {@code openingBalance} del Conto
     */
    double getOpeningBalance();

    /**
     * Ritorna l'{@link AccountType} dell'Account.
     *
     * @return il type dell'Account
     */
    AccountType getType();
}
