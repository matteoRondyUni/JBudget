package it.unicam.cs.pa.jbudget.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * La classe implementa l'interfaccia {@link Transaction} ed ha la responsabilita' di gestire una transazione.
 * Permette di accedere e modificare la informazioni associate ad una transazione: lista delle {@link Category},
 * e lista dei {@link Movement}. La data di una Transazione corrisponde alla data dell'ultimo Movimento in essa.
 * La transazione ha anche un saldo (ottenibile tramite il metodo {@code getTotalAmount()}) che permette di ottenere
 * la variazione totale dei movimenti della transazione.
 *
 * @author Matteo Rondini
 */
public class MoneyTransaction implements Transaction {
    private final int ID;
    private final List<Movement> movementsList = new ArrayList<>();
    private final List<Category> categoryList = new ArrayList<>();

    /**
     * Una transazione viene creata senza dei movimenti, che vengono aggiunti successivamente.
     *
     * @param ID Codice Identificativo della transazione
     */
    public MoneyTransaction(int ID) {
        this.ID = ID;
    }

    /**
     * Aggiunge una {@link Category} alla Transazione.
     *
     * @param category Categoria da aggiungere
     */
    @Override
    public void addCategory(Category category) {
        if (!categoryList.contains(category)) {
            categoryList.add(category);
            sortCategory();
            movementsList.forEach(movement -> movement.addCategory(category));
        }
    }

    /**
     * Aggiunge un {@link Movement} alla Transazione.
     *
     * @param movement Movimento da aggiungere.
     * @throws MovementException Viene lanciata una MovementException se la transazione del movimento e' diversa
     *                           dalla transazione attuale o se il movimento e' gia' stato inserito.
     */
    @Override
    public void addMovement(Movement movement) throws MovementException {
        controlMovement(movement);
        movementsList.add(movement);
        sortMovement();
    }

    /**
     * Aggiunge una lista di {@link Movement} alla Transazione.
     *
     * @param movements Lista di Movimento da aggiungere.
     * @throws MovementException Viene lanciata un MovementException se la transazione del movimento e' diversa
     *                           dalla transazione attuale o se il movimento e' gia' stato inserito.
     */
    @Override
    public void addMovements(List<? extends Movement> movements) throws MovementException {
        for (Movement movement : movements)
            addMovement(movement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> categories() {
        return categoryList;
    }

    @Override
    public int compareTo(Transaction transaction) {
        if (Objects.isNull(transaction))
            throw new NullPointerException();
        if (this.equals(transaction))
            return 0;
        if (this.getID() > transaction.getID())
            return 1;
        else return -1;
    }

    /**
     * Controlla che il movimento abbia come transazione la transazione attuale (this);
     * e che non ci siano 2 elementi uguali dentro una transazione.
     *
     * @param movement Movimento da controllare.
     */
    private void controlMovement(Movement movement) throws MovementException {
        if (movement.getTransaction() == null || !movement.getTransaction().equals(this))
            throw new MovementException(MovementException.E2_DIFFERENT_TRANSACTION);

        if (movementsList.contains(movement))
            throw new MovementException(MovementException.E3_THERE_IS_ALREADY_A_MOVEMENT_IN_TRANSACTION);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyTransaction that = (MoneyTransaction) o;
        return getID() == that.getID();
    }

    /**
     * Ritorna la data di una Transazione, che corrisponde alla data dell'ultimo movimento in essa.
     *
     * @return ritorna la {@code Date} dell'ultimo movimento della Transazione;
     * se la Transazione e' vuota ritorna {@code null}.
     */
    @Override
    public Date getDate() {
        Date dateToReturn = null;

        if (movementsList.isEmpty())
            return null;

        for (Movement movement : movementsList)
            dateToReturn = movement.getDate();

        return dateToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Calcola il saldo della Transazione, cioe' la variazione totale dei movimenti della transazione.
     *
     * @return il saldo della transazione.
     */
    @Override
    public double getTotalAmount() {
        double amount = 0;
        for (Movement movement : movementsList) {
            if (movement.type() == MovementType.CREDITS)
                amount += movement.amount();
            else
                amount -= movement.amount();
        }
        return amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    /**
     * Ritorna la lista dei {@link Movement} della Transazione.
     *
     * @return la {@code List} dei Movimenti
     */
    @Override
    public List<Movement> movements() {
        return movementsList;
    }

    /**
     * Rimuove la {@link Category} da tutti i {@link Movement} della Transazione.
     * La rimozione viene fatta solo la Categoria da eliminare apparteneva alla Transazione.
     *
     * @param category Categoria da eliminare
     * @return true se la Categoria viene eliminata
     */
    @Override
    public boolean removeCategory(Category category) {
        if (categoryList.contains(category))
            movementsList.forEach(movement -> movement.removeCategory(category));
        return categoryList.remove(category);
    }

    private void sortCategory() {
        categoryList.sort(null);
    }

    private void sortMovement() {
        movementsList.sort(null);
    }

    @Override
    public String toString() {
        return "ID = " + ID +
                ", Total Amount = " + getTotalAmount();
    }
}
