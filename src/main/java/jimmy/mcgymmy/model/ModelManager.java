package jimmy.mcgymmy.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.util.CollectionUtil;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.macro.MacroList;


/**
 * Represents the in-memory model of mcgymmmy data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final History history = new History();

    private MacroList macroList;
    private final McGymmy mcGymmy;
    private final UserPrefs userPrefs;
    private final FilteredList<Food> filteredFoodItems;
    private Predicate<Food> filterPredicate;

    /**
     * Initializes a ModelManager with the given mcGymmy and userPrefs and macroList.
     */
    public ModelManager(ReadOnlyMcGymmy mcGymmy, ReadOnlyUserPrefs userPrefs, MacroList macroList) {
        super();
        CollectionUtil.requireAllNonNull(mcGymmy, userPrefs, macroList);

        logger.fine("Initializing with food list: " + mcGymmy + " and user prefs " + userPrefs);

        this.mcGymmy = new McGymmy(mcGymmy);
        this.userPrefs = new UserPrefs(userPrefs);
        this.macroList = macroList;
        filteredFoodItems = new FilteredList<>(this.mcGymmy.getFoodList());
        filterPredicate = PREDICATE_SHOW_ALL_FOODS;
        filteredFoodItems.setPredicate(filterPredicate);
    }

    /**
     * Initializes a ModelManager with the given mcGymmy and userPrefs. Creates a new macroList.
     */
    public ModelManager(ReadOnlyMcGymmy mcGymmy, ReadOnlyUserPrefs userPrefs) {
        this(mcGymmy, userPrefs, new MacroList());
    }

    public ModelManager() {
        this(new McGymmy(), new UserPrefs(), new MacroList());
    }

    //=========== MacroList ==================================================================================

    @Override
    public MacroList getMacroList() {
        return this.macroList;
    }

    @Override
    public void setMacroList(MacroList replacement) {
        saveCurrentStateToHistory();
        this.macroList = replacement;
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

    /**
     * Sets McGymmy and saves the current state to the history
     * @param mcGymmy
     */
    @Override
    public void setMcGymmy(ReadOnlyMcGymmy mcGymmy) {
        saveCurrentStateToHistory();
        this.mcGymmy.resetData(mcGymmy);
        updateFilterPredicate(PREDICATE_SHOW_ALL_FOODS);
    }

    @Override
    public boolean hasFood(Food food) {
        requireNonNull(food);
        return mcGymmy.hasFood(food);
    }

    /**
     * Deletes the given food and saves the current state to the history
     * The index must be valid
     *
     * @param index
     */
    @Override
    public void deleteFood(Index index) {
        logger.fine("Delete food at index: " + index.getOneBased());
        saveCurrentStateToHistory();

        //Delete food based on food index
        mcGymmy.removeFood(filteredFoodItems.get(index.getZeroBased()));
    }

    /**
     * Adds the given food and saves the current state to the history
     */
    @Override
    public void addFood(Food food) {
        logger.fine("Add food:\n" + food.toString());
        saveCurrentStateToHistory();
        mcGymmy.addFood(food);
        updateFilterPredicate(PREDICATE_SHOW_ALL_FOODS);
    }

    /**
     * Replaces the given food {@code target} with {@code editedFood}, and saves the current state to the history
     * The index must be valid
     */
    @Override
    public void setFood(Index index, Food editedFood) {
        CollectionUtil.requireAllNonNull(index, editedFood);
        logger.fine("Change food at index " + index.getOneBased() + "to food:\n" + editedFood.toString());
        saveCurrentStateToHistory();
        Food food = getFilteredFoodList().get(index.getZeroBased());
        Index index1 = Index.fromZeroBased(mcGymmy.getFoodList().indexOf(food));
        mcGymmy.setFood(index1, editedFood);
        updateFilterPredicate(PREDICATE_SHOW_ALL_FOODS);
    }

    @Override
    public boolean canUndo() {
        return history.canUndo();
    }

    /**
     * Undo the previous change to mcGymmy, remove that state from history
     */
    @Override
    public void undo() {
        if (canUndo()) {
            assert history.canUndo() : "UndoStack is empty";
            McGymmy prevMcGymmy = history.peekUndoMcGymmy();
            Predicate<Food> prevPredicate = history.peekUndoPredicate();
            MacroList macroList = history.peekUndoMacroList();
            history.undo();
            mcGymmy.resetData(prevMcGymmy);
            updateFilterPredicate(prevPredicate);
            this.macroList = macroList;
        }
    }

    @Override
    public boolean canRedo() {
        return history.canRedo();
    }

    @Override
    public void redo() {
        if (canRedo()) {
            assert history.canRedo() : "RedoStack is empty";
            McGymmy prevMcGymmy = history.peekRedoMcGymmy();
            Predicate<Food> prevPredicate = history.peekRedoPredicate();
            MacroList macroList = history.peekRedoMacroList();
            history.redo();
            mcGymmy.resetData(prevMcGymmy);
            updateFilterPredicate(prevPredicate);
            this.macroList = macroList;
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

    //=========== Filtered Food List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Food} backed by the internal list of
     * {@code versionedMcGymmy}
     */
    @Override
    public ObservableList<Food> getFilteredFoodList() {
        return filteredFoodItems;
    }

    @Override
    public void updateFilteredFoodList(Predicate<Food> predicate) {
        requireNonNull(predicate);
        logger.fine("Update predicate for filtered food list");
        saveCurrentStateToHistory();
        updateFilterPredicate(predicate);
    }

    private void updateFilterPredicate(Predicate<Food> predicate) {
        filterPredicate = predicate;
        filteredFoodItems.setPredicate(filterPredicate);
    }

    Predicate<Food> getFilterPredicate() {
        return filterPredicate;
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
