package jimmy.mcgymmy.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.macro.MacroList;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Food> PREDICATE_SHOW_ALL_FOODS = unused -> true;

    /**
     * Returns the current MacroList
     */
    public MacroList getMacroList();

    /**
     * Replaces the current MacroList
     */
    public void setMacroList(MacroList replacement);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' mcgymmy file path.
     */
    Path getMcGymmyFilePath();

    /**
     * Sets the user prefs' mcgymmy file path.
     */
    void setMcGymmyFilePath(Path mcGymmyFilePath);

    /**
     * Returns  McGymmy
     */
    ReadOnlyMcGymmy getMcGymmy();

    /**
     * Sets McGymmy
     */
    void setMcGymmy(ReadOnlyMcGymmy mcGymmy);

    /**
     * Returns true if a food with the same identity as {@code food} exists in mcgymmy.
     */
    boolean hasFood(Food food);

    /**
     * Deletes the given food.
     * The index must be valid
     *
     * @param index
     */
    void deleteFood(Index index);

    /**
     * Adds the given food.
     */
    void addFood(Food food);

    /**
     * Replaces the given food {@code target} with {@code editedFood}.
     * The index must be valid
     */
    void setFood(Index index, Food editedFood);

    /**
     * Removes all filtered food from Fridge and clears filtered food list
     */
    void clearFilteredFood();

    /**
     * Checks if McGymmy can undo
     */
    boolean canUndo();

    /**
     * Undoes the previous change to McGymmy
     */
    void undo();

    /**
     * Checks if McGymmy can redo
     */
    boolean canRedo();

    /**
     * Redoes the last undo command
     */
    void redo();

    /**
     * Returns an unmodifiable view of the filtered food list
     */
    ObservableList<Food> getFilteredFoodList();

    /**
     * Updates the filter of the filtered food list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredFoodList(Predicate<Food> predicate);
}
