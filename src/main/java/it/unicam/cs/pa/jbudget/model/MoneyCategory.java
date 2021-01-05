package it.unicam.cs.pa.jbudget.model;

import java.util.Objects;

/**
 * La classe implementa l'interfaccia {@link Category} ed ha la responsabilita' di definire una categoria
 * di spesa/guadagno.
 *
 * @author Matteo Rondini
 */
public class MoneyCategory implements Category {
    private final int ID;
    private String name;
    private String description;

    /**
     * Crea una MoneyCategory
     *
     * @param name        Nome della Categoria
     * @param description Descrizione della Categoria
     * @param ID          Codice Identificativo della Categoria
     */
    public MoneyCategory(String name, String description, int ID) {
        controlDescription(description);
        controlName(name);

        this.name = name;
        this.description = description;
        this.ID = ID;
    }

    @Override
    public int compareTo(Category category) {
        if (Objects.isNull(category))
            throw new NullPointerException();
        if (this.equals(category))
            return 0;
        if (this.getID() > category.getID())
            return 1;
        else return -1;
    }

    private void controlDescription(String description) {
        if (description == null)
            throw new NullPointerException(EXCEPTION_DESCRIPTION_NULL);
    }

    private void controlName(String name) {
        if (name == null)
            throw new NullPointerException(EXCEPTION_NAME_NULL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyCategory category = (MoneyCategory) o;
        return getID() == category.getID();
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
        controlDescription(description);
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
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        controlName(name);
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    @Override
    public String toString() {
        return ID +
                " - " + name;
    }
}
