package jimmy.mcgymmy.model;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static jimmy.mcgymmy.testutil.TypicalFoods.CHICKEN_RICE;
import static jimmy.mcgymmy.testutil.TypicalFoods.NASI_LEMAK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.logic.predicate.NameContainsKeywordsPredicate;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.testutil.FoodBuilder;
import jimmy.mcgymmy.testutil.McGymmyBuilder;
import jimmy.mcgymmy.testutil.TypicalFoods;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @BeforeEach
    public void setup() {
        modelManager = new ModelManager();
    }

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new McGymmy(), new McGymmy(modelManager.getMcGymmy()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setMcGymmyFilePath(Paths.get("jimmy/mcgymmy/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setMcGymmyFilePath(Paths.get("new/jimmy/mcgymmy/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setMcGymmyFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setMcGymmyFilePath(null));
    }

    @Test
    public void setMcGymmyFilePath_validPath_setsMcGymmyFilePath() {
        Path path = Paths.get("jimmy/mcgymmy/file/path");
        modelManager.setMcGymmyFilePath(path);
        assertEquals(path, modelManager.getMcGymmyFilePath());
    }

    @Test
    public void hasFood_nullFood_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasFood(null));
    }

    @Test
    public void hasFood_personNotInMcGymmy_returnsFalse() {
        assertFalse(modelManager.hasFood(CHICKEN_RICE));
    }

    @Test
    public void hasFood_foodInMcGymmy_returnsTrue() {
        modelManager.addFood(CHICKEN_RICE);
        assertTrue(modelManager.hasFood(CHICKEN_RICE));
    }

    @Test
    public void getFilteredFoodList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredFoodList().remove(0));
    }

    @Test
    public void canUndo_newModelManager_returnsFalse() {
        assertFalse(modelManager.canUndo());
    }

    @Test
    public void canUndo_modelGotChangedPreviously_returnsTrue() {
        modelManager.addFood(CHICKEN_RICE);
        assertTrue(modelManager.canUndo());
    }

    @Test
    public void canUndo_modelGotChangedButUndoAlready_returnsFalse() {
        modelManager.addFood(CHICKEN_RICE);
        modelManager.undo();
        assertFalse(modelManager.canUndo());
    }

    @Test
    public void undo_undoAfterAddFood_mcGymmyHasCorrectContent() {
        modelManager.addFood(CHICKEN_RICE);
        McGymmy expected = new McGymmyBuilder().withFood(TypicalFoods.CHICKEN_RICE).build();
        modelManager.addFood(NASI_LEMAK);
        modelManager.undo();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void undo_undoAfterDeleteFood_mcGymmyHasCorrectContent() {
        modelManager.addFood(CHICKEN_RICE);
        McGymmy expected = new McGymmyBuilder().withFood(TypicalFoods.CHICKEN_RICE).build();
        modelManager.deleteFood(Index.fromOneBased(1));
        modelManager.undo();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void undo_undoAfterSetMcGymmy_mcGymmyHasCorrectContent() {
        modelManager.addFood(CHICKEN_RICE);
        McGymmy expected = new McGymmyBuilder().withFood(TypicalFoods.CHICKEN_RICE).build();
        McGymmy mcGymmy = new McGymmyBuilder().withFood(TypicalFoods.CHICKEN_RICE).withFood(
                TypicalFoods.NASI_LEMAK).build();
        modelManager.setMcGymmy(mcGymmy);
        modelManager.undo();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void undo_undoAfterSetFood_mcGymmyHasCorrectContent() {
        McGymmy expected = new McGymmyBuilder().withFood(CHICKEN_RICE).build();
        modelManager.addFood(CHICKEN_RICE);
        Food editedFood = new FoodBuilder(CHICKEN_RICE).withCarb("50").build();
        modelManager.setFood(Index.fromOneBased(1), editedFood);
        modelManager.undo();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void undo_undoMultipleTimes_mcGymmyHasCorrectContent() {
        Food newChickenRice = new FoodBuilder(CHICKEN_RICE).withDate("2020-04-20").build();
        Food newNasiLemak = new FoodBuilder().withTags("Lunch").build();

        McGymmy expected1 = new McGymmyBuilder().build();
        McGymmy expected2 = new McGymmyBuilder().withFood(CHICKEN_RICE).build();
        McGymmy expected3 = new McGymmyBuilder().withFood(CHICKEN_RICE).withFood(NASI_LEMAK).build();
        McGymmy expected4 = new McGymmyBuilder().withFood(newChickenRice).withFood(NASI_LEMAK).build();
        McGymmy expected5 = new McGymmyBuilder().withFood(newChickenRice).withFood(newNasiLemak).build();

        modelManager.addFood(CHICKEN_RICE);
        modelManager.addFood(NASI_LEMAK);
        modelManager.setFood(Index.fromOneBased(1), newChickenRice);
        modelManager.setFood(Index.fromOneBased(2), newNasiLemak);
        modelManager.deleteFood(Index.fromOneBased(1));

        modelManager.undo();
        assertEquals(expected5, modelManager.getMcGymmy());

        modelManager.undo();
        assertEquals(expected4, modelManager.getMcGymmy());

        modelManager.undo();
        assertEquals(expected3, modelManager.getMcGymmy());

        modelManager.undo();
        assertEquals(expected2, modelManager.getMcGymmy());

        modelManager.undo();
        assertEquals(expected1, modelManager.getMcGymmy());

    }

    @Test
    public void equals() {
        McGymmy mcGymmy = new McGymmyBuilder().withFood(TypicalFoods.CHICKEN_RICE).withFood(
                TypicalFoods.NASI_LEMAK).build();
        McGymmy differentMcGymmy = new McGymmy();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(mcGymmy, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(mcGymmy, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different mcGymmy -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentMcGymmy, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = CHICKEN_RICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredFoodList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(mcGymmy, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredFoodList(Model.PREDICATE_SHOW_ALL_FOODS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setMcGymmyFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(mcGymmy, differentUserPrefs)));
    }
}
