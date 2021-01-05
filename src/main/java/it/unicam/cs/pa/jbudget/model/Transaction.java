package it.unicam.cs.pa.jbudget.model;

import java.util.Date;
import java.util.List;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di gestire una transazione.
 * Permette di accedere e modificare la informazioni associate ad una transazione: lista delle {@link Category} e
 * lista dei {@link Movement}. La data di una Transazione corrisponde alla data dell'ultimo Movimento in essa.
 * La transazione ha anche un saldo (ottenibile tramite il metodo {@code getTotalAmount()}) che permette di ottenere
 * la variazione totale dei movimenti della transazione.
 */
public interface Transaction extends Comparable<Transaction>{
    /**
     * Aggiunge una {@link Category} alla Transazione.
     *
     * @param category Categoria da aggiungere
     */
    void addCategory(Category category);

    /**
     * Aggiunge un {@link Movement} alla Transazione.
     *
     * @param movement Movimento da aggiungere.
     * @throws MovementException Viene lanciata una MovementException se la transazione del movimento e' diversa
     *                           dalla transazione attuale o se il movimento e' gia' stato inserito.
     */
    void addMovement(Movement movement) throws MovementException;

    /**
     * Aggiunge una lista di {@link Movement} alla Transazione.
     *
     * @param movements Lista di Movimento da aggiungere.
     * @throws MovementException Viene lanciata un MovementException se la transazione del movimento e' diversa
     *                           dalla transazione attuale o se il movimento e' gia' stato inserito.
     */
    void addMovements(List<? extends Movement> movements) throws MovementException;

    /**
     * Ritorna la lista delle {@link Category} della Transazione.
     *
     * @return la {@code List} delle Categorie
     */
    List<Category> categories();

    /**
     * Ritorna la data di una Transazione, che corrisponde alla data dell'ultimo movimento in essa.
     *
     * @return ritorna la {@code Date} dell'ultimo movimento della Transazione;
     * se la Transazione e' vuota ritorna {@code null}.
     */
    Date getDate();

    /**
     * Ritorna il Codice Identificativo della Transazione.
     *
     * @return l'{@code ID} della Transazione.
     */
    int getID();

    /**
     * Calcola il saldo della Transazione, cioe' la variazione totale dei movimenti della transazione.
     *
     * @return il saldo della transazione.
     */
    double getTotalAmount();

    /**
     * Ritorna la lista dei {@link Movement} della Transazione.
     *
     * @return la {@code List} dei Movimenti
     */
    List<Movement> movements();

    /**
     * Rimuove la {@link Category} da tutti i {@link Movement} della {@link Transaction}.
     * La rimozione viene fatta solo la Categoria da eliminare apparteneva alla Transazione.
     *
     * @param category Categoria da eliminare
     * @return true se la Categoria viene eliminata
     */
    boolean removeCategory(Category category);
}
