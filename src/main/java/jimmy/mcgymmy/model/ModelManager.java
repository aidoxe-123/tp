package jimmy.mcgymmy.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.util.Pair;
import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.util.CollectionUtil;
import jimmy.mcgymmy.model.food.Food;


/**
 * Represents the in-memory model of mcgymmmy data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final History history = new History();

    private final McGymmy mcGymmy;
    private final UserPrefs userPrefs;
    private final FilteredList<Food> filteredFoodItems;
    private Predicate<Food> filterPredicate;

    /**
     * Initializes a ModelManager with the given mcGymmy and userPrefs.
     */
    public ModelManager(ReadOnlyMcGymmy mcGymmy, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(mcGymmy, userPrefs);

        logger.fine("Initializing with food list: " + mcGymmy + " and user prefs " + userPrefs);

        this.mcGymmy = new McGymmy(mcGymmy);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredFoodItems = new FilteredList<>(this.mcGymmy.getFoodList());
        filterPredicate = PREDICATE_SHOW_ALL_FOODS;
        filteredFoodItems.setPredicate(filterPredicate);
    }

    public ModelManager() {
        this(new McGymmy(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getMcGymmyFilePath() {
        return userPrefs.getMcGymmyFilePath();
    }

    @Override
    public void setMcGymmyFilePath(Path mcGymmyFilePath) {
        requireNonNull(mcGymmyFilePath);
        userPrefs.setMcGymmyFilePath(mcGymmyFilePath);
    }

    //=========== McGymmy ================================================================================

    @Override
    public ReadOnlyMcGymmy getMcGymmy() {
        return mcGymmy;
    }

    @Override
    public void setMcGymmy(ReadOnlyMcGymmy mcGymmy) {
        saveCurrentStateToHistory();
        this.mcGymmy.resetData(mcGymmy);
        showAllFoods();
    }

    @Override
    public boolean hasFood(Food food) {
        requireNonNull(food);
        return mcGymmy.hasFood(food);
    }

    @Override
    public void deleteFood(Index index) {
        logger.fine("Delete food at index: " + index.getOneBased());
        saveCurrentStateToHistory();
        mcGymmy.removeFood(index);
    }

    @Override
    public void addFood(Food food) {
        logger.fine("Add food:\n" + food.toString());
        saveCurrentStateToHistory();
        mcGymmy.addFood(food);
        showAllFoods();
    }

    @Override
    public void setFood(Index index, Food editedFood) {
        CollectionUtil.requireAllNonNull(index, editedFood);
        logger.fine("Change food at index " + index.getOneBased() + "to food:\n" + editedFood.toString());
        saveCurrentStateToHistory();
        mcGymmy.setFood(index, editedFood);
        showAllFoods();
    }

    @Override
    public boolean canUndo() {
        return !history.empty();
    }

    /**
     * Undo the previous change to mcGymmy
     */
    @Override
    public void undo() {
        if (canUndo()) {
            assert !history.empty() : "McGymmyStack is empty";
            Pair<McGymmy, Predicate<Food>> prevMcGymmyPredicatePair = history.pop();
            McGymmy prevMcGymmy = prevMcGymmyPredicatePair.getKey();
            Predicate<Food> prevPredicate = prevMcGymmyPredicatePair.getValue();
            mcGymmy.resetData(prevMcGymmy);
            filteredFoodItems.setPredicate(prevPredicate);
        }
    }

    private void saveCurrentStateToHistory() {
        history.save(this);
    }

    @Override
    public void clearFilteredFood() {
        saveCurrentStateToHistory();
        List<Food> lst = new ArrayList<>();
        // prevent traversal error
        for (Food filteredFood : mcGymmy.getFoodList()) {
            if (!filterPredicate.test(filteredFood)) {
                lst.add(filteredFood);
            }
        }
        mcGymmy.setFoodItems(lst);

        filteredFoodItems.clear();
    }

    // package-private on purpose
    Predicate<Food> getPredicate() {
        return filterPredicate;
    }

    //=========== Filtered Food List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Food} backed by the internal list of
     * {@code versionedMcGymmy}
     */
    @Override
    public ObservableList<Food> getFilteredFoodList() {
        return filteredFoodItems;
    }

    // This method is for command usage only
    // Do record the state of the model
    // If anyone want to implement a method that show all food after changing the mcgymmy,
    // use showAllFoods() instead
    @Override
    public void updateFilteredFoodList(Predicate<Food> predicate) {
        requireNonNull(predicate);
        logger.fine("Update predicate for filtered food list");
        saveCurrentStateToHistory();
        filterPredicate = predicate;
        filteredFoodItems.setPredicate(filterPredicate);
    }

    /**
     * Updates the predicate to show all food, does not record the state of the model
     */
    private void showAllFoods() {
        filterPredicate = PREDICATE_SHOW_ALL_FOODS;
        filteredFoodItems.setPredicate(filterPredicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;

        return mcGymmy.equals(other.mcGymmy)
                && userPrefs.equals(other.userPrefs)
                && filteredFoodItems.equals(other.filteredFoodItems);
    }

}
