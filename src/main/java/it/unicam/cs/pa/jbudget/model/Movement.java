package it.unicam.cs.pa.jbudget.model;

import java.util.Date;
import java.util.List;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di gestire
 * un singolo movimento. Permette di accedere e modificare le informazioni associate al movimento: descrizione,
 * importo, lista delle {@link Category} associate al movimento. Le operazioni di lettura e modifica
 * vengono effettuate per mezzo degli opportuni getter e setter. Il Movimento e' associato ad una {@link Transaction}
 * e ad un {@link Account}.
 */
public interface Movement extends Comparable<Movement> {
    /**
     * Errore che si verifica quando si prova a creare un Movimento con il campo type {@code null}.
     */
    String EXCEPTION_TYPE_NULL = "Il type non puo' essere null!";
    /**
     * Errore che si verifica quando si prova a creare/modificare
     * un Movimento con il campo date {@code null}.
     */
    String EXCEPTION_DATE_NULL = "La date non puo' essere null!";
    /**
     * Errore che si verifica quando si prova a creare/modificare
     * un Movimento con il campo description {@code null}.
     */
    String EXCEPTION_DESCRIPTION_NULL = "La description non puo' essere null!";

    /**
     * Aggiunge una {@link Category} al Movimento.
     *
     * @param category Categoria da aggiungere
     */
    void addCategory(Category category);

    /**
     * Ritorna il valore del Movimento.
     *
     * @return il {@code valore} del Movimento
     */
    double amount();

    /**
     * Ritorna la lista delle {@link Category} del Movimento.
     *
     * @return la {@code List} delle Categorie
     */
    List<Category> categories();

    /**
     * Elimina dal movimento i riferimenti all' {@link Account} e alla {@link Transaction}.
     */
    void delete();

    /**
     * Ritorna l'{@link Account} del Movimento.
     *
     * @return l'{@code Account} del Movimento
     */
    Account getAccount();

    /**
     * Ritorna la Data del Movimento.
     *
     * @return la {@code date} del Movimento
     */
    Date getDate();

    /**
     * Ritorna la Descrizione del Movimento.
     *
     * @return la {@code descrizione} del Movimento
     */
    String getDescription();

    /**
     * Ritorna il Codice Identificativo del Movimento.
     *
     * @return l'{@code ID} del movimento.
     */
    int getID();

    /**
     * Ritorna la {@link Transaction} del Movimento.
     *
     * @return la {@code Transazione} del Movimento
     */
    Transaction getTransaction();

    /**
     * Rimuove una {@link Category} dal Movimento.
     *
     * @param category Categoria da rimuovere
     * @return true se la Categoria viene eliminata
     */
    boolean removeCategory(Category category);

    /**
     * Ritorna il {@link MovementType} del Movimento.
     *
     * @return il {@code type} del Movimento
     */
    MovementType type();
}
