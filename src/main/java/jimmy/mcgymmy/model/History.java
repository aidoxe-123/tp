package jimmy.mcgymmy.model;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.function.Predicate;

import javafx.util.Pair;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.macro.MacroList;

class History {
    private final Stack<Pair<McGymmy, Pair<Predicate<Food>, MacroList>>> undoStack;
    private final Stack<Pair<McGymmy, Pair<Predicate<Food>, MacroList>>> redoStack;

    History() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    /**
     * Saves the mcGymmy, predicate and macrolist of <code>ModelManager</code> to history
     */
    void save(ModelManager modelManager) {
        McGymmy mcGymmy = new McGymmy(modelManager.getMcGymmy());
        Predicate<Food> predicate = modelManager.getFilterPredicate();
        MacroList macroList = modelManager.getMacroList();
        undoStack.push(new Pair<>(mcGymmy, new Pair<>(predicate, macroList)));
        redoStack.clear();
    }

    boolean canUndo() {
        return !undoStack.empty();
    }

    boolean canRedo() {
        return !redoStack.empty();
    }

    /**
     * Moves the previous state from undoStack to redoStack
     * @throws EmptyStackException
     */
    void undo() throws EmptyStackException {
        assert !undoStack.empty() : "UndoStack is empty";
        redoStack.push(undoStack.pop());
    }

    /**
     * Moves the previous state from redoStack to undoStack
     * @throws EmptyStackException
     */
    void redo() throws EmptyStackException {
        assert !redoStack.empty() : "RedoStack is empty";
        undoStack.push(redoStack.pop());
    }

    McGymmy peekUndoMcGymmy() throws EmptyStackException {
        assert !undoStack.empty() : "UndoStack is empty";
        return undoStack.peek().getKey();
    }

    Predicate<Food> peekUndoPredicate() throws EmptyStackException {
        assert !undoStack.empty() : "UndoStack is empty";
        return undoStack.peek().getValue().getKey();
    }

    MacroList peekUndoMacroList() throws EmptyStackException {
        assert !undoStack.empty() : "UndoStack is empty";
        return undoStack.peek().getValue().getValue();
    }

    McGymmy peekRedoMcGymmy() throws EmptyStackException {
        assert !redoStack.empty() : "RedoStack is empty";
        return redoStack.peek().getKey();
    }

    Predicate<Food> peekRedoPredicate() throws EmptyStackException {
        assert !redoStack.empty() : "RedoStack is empty";
        return redoStack.peek().getValue().getKey();
    }

    MacroList peekRedoMacroList() throws EmptyStackException {
        assert !redoStack.empty() : "RedoStack is empty";
        return redoStack.peek().getValue().getValue();
    }
}

