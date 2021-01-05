package it.unicam.cs.pa.jbudget.statistic;

import it.unicam.cs.pa.jbudget.model.Account;
import it.unicam.cs.pa.jbudget.model.Category;
import it.unicam.cs.pa.jbudget.model.Movement;
import it.unicam.cs.pa.jbudget.model.MovementType;

import java.util.Map;

/**
 * Questa interfaccia e' implementata dalle classi che hanno la responsabilita' di
 * creare le statistiche di un {@link Account}. E' responsabile di leggere tutti i {@link Movement}
 * dell'Account e ritornare per ogni {@link MovementType} il Maggiore ed il Minore.
 * Inoltre crea una {@link Map} che collega le {@link Category} con la somma dei valori
 * di tutti i Movimenti dell'Account che hanno quella Categoria.
 *
 * @author Matteo Rondini
 */
public interface StatisticCreator {
    /**
     * Crea e ritorna una {@link Map} che collega le {@link Category} con la somma dei valori
     * di tutti i Movimenti dell'Account che hanno quella Categoria.
     *
     * @return una Mappa delle Categorie (Key) e il loro valore totale (Value)
     * @throws StaticException Viene lanciata quando si prova a calcolare delle Statistiche
     *                         di un {@link Account} non valido
     */
    Map<Category, Double> getCategoryValue() throws StaticException;

    /**
     * Ritorna il {@link Movement} di tipo {@link MovementType} CREDITS con il valore Maggiore.
     *
     * @return il Movimento con valore Maggiore
     * @throws StaticException Viene lanciata quando si prova a calcolare delle Statistiche
     *                         di un {@link Account} non valido o
     *                         quando si prova a calcolare delle Statistiche
     *                         per un Account che non ha {@link Movement} al suo interno.
     */
    Movement getMaxCredit() throws StaticException;

    /**
     * Ritorna il {@link Movement} di tipo {@link MovementType} DEBITS con il valore Maggiore.
     *
     * @return il Movimento con valore Maggiore
     * @throws StaticException Viene lanciata quando si prova a calcolare delle Statistiche
     *                         di un {@link Account} non valido o
     *                         quando si prova a calcolare delle Statistiche
     *                         per un Account che non ha {@link Movement} al suo interno.
     */
    Movement getMaxDebit() throws StaticException;

    /**
     * Ritorna il {@link Movement} di tipo {@link MovementType} CREDITS con il valore Minore.
     *
     * @return il Movimento con valore Minore
     * @throws StaticException Viene lanciata quando si prova a calcolare delle Statistiche
     *                         di un {@link Account} non valido o
     *                         quando si prova a calcolare delle Statistiche
     *                         per un Account che non ha {@link Movement} al suo interno.
     */
    Movement getMinCredit() throws StaticException;

    /**
     * Ritorna il {@link Movement} di tipo {@link MovementType} DEBITS con il valore Minore.
     *
     * @return il Movimento con valore Minore
     * @throws StaticException Viene lanciata quando si prova a calcolare delle Statistiche
     *                         di un {@link Account} non valido o
     *                         quando si prova a calcolare delle Statistiche
     *                         per un Account che non ha {@link Movement} al suo interno.
     */
    Movement getMinDebit() throws StaticException;
}
