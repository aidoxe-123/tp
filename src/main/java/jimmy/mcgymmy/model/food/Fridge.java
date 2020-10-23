package jimmy.mcgymmy.model.food;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.util.CollectionUtil;

/**
 * A list of food items that allows repeated elements and does not allow nulls.
 * Supports a minimal set of list operations.
 */
public class Fridge implements Iterable<Food> {
    private final ObservableList<Food> internalList = FXCollections.observableArrayList();
    private final ObservableList<Food> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent food item as the given argument.
     */
    public boolean contains(Food toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Adds a food item to the list.
     */
    public void add(Food toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /**
     * Replaces the food item at the {@code index} position in the list with {@code editedFood}.
     * {@code target} must exist in the list.
     */
    public void setFood(Index index, Food editedFood) {
        CollectionUtil.requireAllNonNull(editedFood, index);
        internalList.set(index.getZeroBased(), editedFood);
    }

    /**
     * Removes the food item at the position index from the list.
     * The food item must exist in the list.
     */
    public void remove(Index removeIndex) {
        requireNonNull(removeIndex);
        internalList.remove(removeIndex.getZeroBased());
    }

    public void setFoods(Fridge replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code foods}.
     */
    public void setFoods(List<Food> foods) {
        CollectionUtil.requireAllNonNull(foods);
        internalList.setAll(foods);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Food> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Food> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Fridge // instanceof handles nulls
                && internalList.equals(((Fridge) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
