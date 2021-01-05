package it.unicam.cs.pa.jbudget.model;

import java.util.Date;
import java.util.List;

/**
 * Indica una {@link Transaction} o una serie di transazioni schedulate ad una certa data. La serie di transazioni
 * termina quando il metodo {@code isCompleted()} restituisce true.
 */
public interface ScheduledTransaction {
    String getDescription();

    List<Transaction> getTransaction(Date date);

    boolean isCompleted();

    void setDescription(String description);
}
