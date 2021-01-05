package it.unicam.cs.pa.jbudget.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * La classe implementa l'interfaccia {@link Movement} ed ha la responsabilita' di gestire un singolo movimento.
 * Permette di accedere e modificare le informazioni associate al movimento: descrizione, importo, data,
 * lista delle {@link Category} associate al movimento. Le operazioni di lettura e modifica di queste operazioni
 * vengono effettuate per mezzo degli opportuni getter e setter. Per creare un movimento, quest'ultimo
 * deve essere associato ad una {@link Transaction} ed ad un {@link Account}.
 *
 * @author Matteo Rondini
 * @see MovementType
 */
public class MoneyMovement implements Movement {
    private final MovementType type;
    private final int ID;
    private final List<Category> categoryList = new ArrayList<>();
    private double value;
    private Date date;
    private String description;
    private Transaction transaction;
    private Account account;

    /**
     * Per creare un Movimento, questo l'ultimo deve essere collegato ad un {@link Account}
     * e ad una {@link Transaction}.
     *
     * @param type        Tipologia del Movimento
     * @param ID          Codice Identificativo
     * @param value       Valore del Movimento
     * @param date        Data del Movimento
     * @param description Descrizione del Movimento
     * @param transaction Transazione di cui fa parte questo Movimento
     * @param account     Account collegato al Movimento
     * @throws MovementException Viene lanciata una MovementException se si prova a creare un Movimento
     *                           con valore negativo, oppure con il campo transaction, account, date,
     *                           description, type {@code null}.
     */
    public MoneyMovement(MovementType type, int ID, double value, Date date, String description,
                         Transaction transaction, Account account)
            throws MovementException {
        this(type, ID, value, date, description);

        controlTransaction(transaction);
        controlAccount(account);
        this.transaction = transaction;
        this.account = account;
        this.transaction.addMovement(this);
        this.account.addMovement(this);
    }

    private MoneyMovement(MovementType type, int ID, double value, Date date, String description) throws MovementException {
        controlValue(value);
        controlType(type);
        controlDate(date);
        controlDescription(description);
        this.type = type;
        this.ID = ID;
        this.value = value;
        this.date = date;
        this.description = description;
    }

    /**
     * Aggiunge una {@link Category} al Movimento.
     *
     * @param category Categoria da aggiungere
     */
    @Override
    public void addCategory(Category category) {
        if (!categoryList.contains(category) && !Objects.isNull(category))
            categoryList.add(category);
        sortCategory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double amount() {
        return value;
    }

    /**
     * Ritorna la lista delle {@link Category} del Movimento.
     *
     * @return la {@code List} delle Categorie
     */
    @Override
    public List<Category> categories() {
        return categoryList;
    }

    @Override
    public int compareTo(Movement movement) {
        if (Objects.isNull(movement))
            throw new NullPointerException();
        if (this.equals(movement))
            return 0;
        if (this.getID() > movement.getID())
            return 1;
        else return -1;
    }

    private void controlAccount(Account account) throws MovementException {
        if (account == null)
            throw new MovementException(MovementException.E4_NULL_ACCOUNT);
    }

    private void controlDate(Date date) {
        if (date == null)
            throw new NullPointerException(EXCEPTION_DATE_NULL);
    }

    private void controlDescription(String description) {
        if (description == null)
            throw new NullPointerException(EXCEPTION_DESCRIPTION_NULL);
    }

    private void controlTransaction(Transaction transaction) throws MovementException {
        if (transaction == null)
            throw new MovementException(MovementException.E1_NULL_TRANSACTION);
    }

    private void controlType(MovementType type) {
        if (type == null)
            throw new NullPointerException(EXCEPTION_TYPE_NULL);
    }

    private void controlValue(double value) throws MovementException {
        if (value < 0)
            throw new MovementException(MovementException.E0_NEGATIVE_VALUE);
    }

    /**
     * Elimina dal movimento i riferimenti all' {@link Account} e alla {@link Transaction}.
     */
    @Override
    public void delete() {
        transaction = null;
        account = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyMovement that = (MoneyMovement) o;
        return getID() == that.getID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account getAccount() {
        return account;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate() {
        return date;
    }

    /**
     * Cambia la data del Movimento.
     *
     * @param date Data da impostare
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Cambia il valore della descrizione del Movimento.
     *
     * @param description Descrizione da impostare
     */
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
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    /**
     * Rimuove una {@link Category} dal Movimento.
     *
     * @param category Categoria da rimuovere
     * @return true se la Categoria viene eliminata
     */
    @Override
    public boolean removeCategory(Category category) {
        return categoryList.remove(category);
    }

    /**
     * Cambia il valore del Movimento.
     *
     * @param value Valore da impostare
     * @throws MovementException Viene lanciata una MovementException se si prova ad aggiornare il
     *                           valore di un Movimento passando un numero negativo.
     */
    public void setValue(double value) throws MovementException {
        controlValue(value);
        this.value = value;
    }

    private void sortCategory() {
        categoryList.sort(null);
    }

    @Override
    public String toString() {
        return "ID = " + ID +
                ", Value = " + value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovementType type() {
        return type;
    }
}
