package jimmy.mcgymmy.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Fridge;

/**
 * Wraps all data at mcgymmy level
 * Duplicates are not allowed (by .isSameFood comparison)
 */
public class McGymmy implements ReadOnlyMcGymmy {

    private final Fridge foodItems;

    public McGymmy() {
        foodItems = new Fridge();
    }

    /**
     * Creates an McGymmy using the Foods in the {@code toBeCopied}
     */
    public McGymmy(ReadOnlyMcGymmy toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    // list overwrite operations

    /**
     * Replaces the contents of the Food list with {@code Foods}.
     * {@code Foods} must not contain duplicate Foods.
     */
    public void setFoodItems(List<Food> foodItems) {
        this.foodItems.setFoods(foodItems);
    }

    /**
     * Resets the existing data of this {@code McGymmy} with {@code newData}.
     */
    public void resetData(ReadOnlyMcGymmy newData) {
        requireNonNull(newData);
        setFoodItems(newData.getFoodList());
    }

    // Food-level operations

    /**
     * Returns true if a Food with the same identity as {@code Food} exists in mcgymmy.
     */
    public boolean hasFood(Food food) {
        requireNonNull(food);
        return foodItems.contains(food);
    }

    /**
     * Adds a Food to mcgymmy.
     * The Food must not already exist in mcgymmy.
     */
    public void addFood(Food food) {
        foodItems.add(food);
    }

    /**
     * Replaces the given Food {@code target} in the list with {@code editedFood}.
     * {@code index} must be valid.
     */
    public void setFood(Index index, Food editedFood) {
        requireNonNull(editedFood);

        foodItems.setFood(index, editedFood);
    }

    /**
     * Removes {@code key} from this {@code McGymmy}.
     * {@code index} must be valid.
     */
    public void removeFood(Index index) {
        foodItems.remove(index);
    }

    // util methods

    @Override
    public String toString() {
        return foodItems.asUnmodifiableObservableList().size() + " Foods";
        // TODO: refine later
    }

    @Override
    public ObservableList<Food> getFoodList() {
        return foodItems.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof McGymmy // instanceof handles nulls
                && foodItems.equals(((McGymmy) other).foodItems));
    }

    @Override
    public int hashCode() {
        return foodItems.hashCode();
    }
}
