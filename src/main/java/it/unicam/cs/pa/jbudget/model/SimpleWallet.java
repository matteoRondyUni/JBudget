package it.unicam.cs.pa.jbudget.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * La classe implementa l'interfaccia {@link Account} ed ha la responsabilita' di gestire un conto.
 * Permette di accedere e modificare le informazioni del conto: nome, descrizione.
 * Consente inoltre di ottenere il saldo attuale e il saldo iniziale del conto.
 * Inoltre, e' possibile accedere alla lista dei {@link Movement} e quelli che soddisfano un determinato predicato.
 *
 * @author Matteo Rondini
 */
public class SimpleWallet implements Account {
    private final int ID;
    private final double openingBalance;
    private final List<Movement> movementsList = new ArrayList<>();
    private final AccountType type;
    private String name;
    private String description;

    /**
     * Crea un SimpleWallet.
     *
     * @param type           Tipologia del portafoglio
     * @param name           Nome del portafoglio
     * @param description    Descrizione del portafoglio
     * @param ID             Codice Identificativo del portafoglio
     * @param openingBalance Bilancio Iniziale
     */
    public SimpleWallet(AccountType type, String name, String description, int ID, double openingBalance) {
        controlName(name);
        controlDescription(description);
        this.type = type;
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.openingBalance = openingBalance;
    }

    /**
     * Aggiunge un {@link Movement} all'interno del Conto.
     *
     * @param movement Movimento da aggiungere
     * @throws MovementException Viene lanciata quando si prova
     *                           ad aggiungere un movimento che appartiene ad un altro Account o
     *                           quando si prova ad aggiungere un movimento 2 volte all'interno di un Conto.
     */
    @Override
    public void addMovement(Movement movement) throws MovementException {
        controlMovement(movement);
        movementsList.add(movement);
        sortMovement();
    }

    /**
     * Aggiunge una lista di {@link Movement} all'interno del Conto.
     *
     * @param movements Lista di Movimento da aggiungere
     * @throws MovementException Viene lanciata quando si prova
     *                           ad aggiungere un movimento che appartiene ad un altro Account o
     *                           quando si prova ad aggiungere un movimento 2 volte all'interno di un Conto.
     */
    @Override
    public void addMovements(List<? extends Movement> movements) throws MovementException {
        for (Movement movement : movements)
            addMovement(movement);
    }

    /**
     * Calcola il valore del Movimento rispetto alla sua tipologia.
     *
     * @param movementAmount Il valore del movimento
     * @param movementType   La tipologia del movimento
     * @return il valore da aggiungere al Bilancio dell'Account
     */
    private double calculateBalance(double movementAmount, MovementType movementType) {
        switch (type) {
            case ASSETS:
                if (movementType == MovementType.CREDITS)
                    return +movementAmount;
                else
                    return -movementAmount;
            case LIABILITIES:
                if (movementType == MovementType.CREDITS)
                    return -movementAmount;
                else
                    return +movementAmount;
            default:
                return 0;
        }
    }

    @Override
    public int compareTo(Account account) {
        if (Objects.isNull(account))
            throw new NullPointerException();
        if (this.equals(account))
            return 0;
        if (this.getID() > account.getID())
            return 1;
        else return -1;
    }

    private void controlDescription(String description) {
        if (description == null)
            throw new NullPointerException(Account.EXCEPTION_DESCRIPTION_NULL);
    }

    private void controlMovement(Movement movement) throws MovementException {
        if (!movement.getAccount().equals(this))
            throw new MovementException(MovementException.E5_DIFFERENT_ACCOUNT);

        if (movementsList.contains(movement))
            throw new MovementException(MovementException.E6_THERE_IS_ALREADY_A_MOVEMENT_IN_ACCOUNT);
    }

    private void controlName(String name) {
        if (name == null)
            throw new NullPointerException(Account.EXCEPTION_NAME_NULL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleWallet wallet = (SimpleWallet) o;
        return getID() == wallet.getID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getBalance() {
        double balance = getOpeningBalance();
        for (Movement movement : movementsList) {
            if (movement.getDate().compareTo(Date.from(Instant.now())) < 0)
                balance = balance + calculateBalance(movement.amount(), movement.type());
        }
        return balance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movement> getMovements() {
        return movementsList;
    }

    /**
     * Ritorna la lista dei {@link Movement} dell'Account che soddisfano un {@link Predicate}.
     *
     * @param predicate il Predicate da soddisfare
     * @return la {@code List} dei Movimenti
     */
    @Override
    public List<Movement> getMovements(Predicate<Movement> predicate) {
        return movementsList.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getOpeningBalance() {
        return openingBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccountType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    private void sortMovement() {
        movementsList.sort(null);
    }

    @Override
    public String toString() {
        return "ID = " + ID +
                ", Name = " + name +
                ", Balance = " + getBalance();
    }
}
