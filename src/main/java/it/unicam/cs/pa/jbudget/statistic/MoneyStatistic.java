package it.unicam.cs.pa.jbudget.statistic;

import it.unicam.cs.pa.jbudget.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * La classe implementa l'interfaccia {@link StatisticCreator} ed ha la responsabilita' di
 * creare le statistiche di un {@link Account}. E' responsabile di leggere tutti i {@link Movement}
 * dell'Account e ritornare per ogni {@link MovementType} il Maggiore ed il Minore.
 * Inoltre crea una {@link Map} che collega le {@link Category} con la somma dei valori
 * di tutti i Movimenti dell'Account che hanno quella Categoria.
 *
 * @author Matteo Rondini
 */
public class MoneyStatistic implements StatisticCreator {
    private final Account account;

    /**
     * Crea un MoneyStatistic con l'{@link Account} con cui calcolare le statistiche.
     *
     * @param account Account con cui calcolare le statistiche
     */
    public MoneyStatistic(Account account) {
        this.account = account;
    }

    private void controlAccount() throws StaticException {
        if (Objects.isNull(account))
            throw new StaticException(StaticException.E0_NO_ACCOUNT);
    }

    private Map<Category, Double> createCategoryMap(List<Category> categories) {
        Map<Category, Double> categoryMap = new TreeMap<>();
        for (Category category : categories)
            categoryMap.put(category, 0.0);
        return categoryMap;
    }

    private List<Category> getCategoryFromMovement(List<Movement> movements) {
        List<Category> categories = new ArrayList<>();
        movements.forEach(movement -> categories.addAll(movement.categories()));
        return categories.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Crea e ritorna una {@link Map} che collega le {@link Category} con la somma dei valori
     * di tutti i Movimenti dell'Account che hanno quella Categoria.
     *
     * @return una Mappa delle Categorie (Key) e il loro valore totale (Value)
     * @throws StaticException Viene lanciata quando si prova a calcolare delle Statistiche
     *                         di un {@link Account} non valido
     */
    @Override
    public Map<Category, Double> getCategoryValue() throws StaticException {
        controlAccount();
        List<Movement> movements = getMovementWithCategory();
        List<Category> categories = getCategoryFromMovement(movements);
        Map<Category, Double> categoryMap = createCategoryMap(categories);
        movements.forEach(movement ->
                movement.categories().forEach(category ->
                        categoryMap.put(category, categoryMap.get(category) + movement.amount())));
        return categoryMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movement getMaxCredit() throws StaticException {
        controlAccount();

        return account.getMovements().parallelStream()
                .filter(movement -> movement.type() == MovementType.CREDITS)
                .max(Comparator.comparing(Movement::amount))
                .orElseThrow(() -> new StaticException(StaticException.E1_NO_MOVEMENT_IN_ACCOUNT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movement getMaxDebit() throws StaticException {
        controlAccount();

        return account.getMovements().parallelStream()
                .filter(movement -> movement.type() == MovementType.DEBITS)
                .max(Comparator.comparing(Movement::amount))
                .orElseThrow(() -> new StaticException(StaticException.E1_NO_MOVEMENT_IN_ACCOUNT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movement getMinCredit() throws StaticException {
        controlAccount();

        return account.getMovements().parallelStream()
                .filter(movement -> movement.type() == MovementType.CREDITS)
                .min(Comparator.comparing(Movement::amount))
                .orElseThrow(() -> new StaticException(StaticException.E1_NO_MOVEMENT_IN_ACCOUNT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movement getMinDebit() throws StaticException {
        controlAccount();

        return account.getMovements().parallelStream()
                .filter(movement -> movement.type() == MovementType.DEBITS)
                .min(Comparator.comparing(Movement::amount))
                .orElseThrow(() -> new StaticException(StaticException.E1_NO_MOVEMENT_IN_ACCOUNT));
    }

    private List<Movement> getMovementWithCategory() {
        return account.getMovements()
                .parallelStream().filter(movement -> !movement.categories().isEmpty())
                .collect(Collectors.toList());
    }
}
