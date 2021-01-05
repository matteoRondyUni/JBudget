package it.unicam.cs.pa.jbudget.model;

/**
 * Ha la responsabilita' di definire una categoria di spesa/guadagno.
 */
public interface Category extends Comparable<Category>{
    /**
     * Errore che si verifica quando si prova a creare/modificare una Categoria con il nome {@code null}.
     */
    String EXCEPTION_NAME_NULL = "Il name non puo' essere null!";
    /**
     * Errore che si verifica quando si prova a creare/modificare una Categoria con la descrizione {@code null}.
     */
    String EXCEPTION_DESCRIPTION_NULL = "La description non puo' essere null!";

    /**
     * Ritorna la Descrizione della Categoria.
     *
     * @return la {@code descrizione} della Categoria
     */
    String getDescription();

    /**
     * Cambia il valore della descrizione della Categoria.
     *
     * @param description Descrizione da impostare
     */
    void setDescription(String description);

    /**
     * Ritorna l'ID della Categoria.
     *
     * @return l'{@code ID} della Categoria
     */
    int getID();

    /**
     * Ritorna il Nome della Categoria.
     *
     * @return il {@code nome} della Categoria
     */
    String getName();

    /**
     * Cambia il valore del nome della Categoria.
     *
     * @param name Nome da impostare
     */
    void setName(String name);
}
