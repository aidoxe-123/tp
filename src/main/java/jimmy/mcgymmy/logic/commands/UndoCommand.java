package jimmy.mcgymmy.logic.commands;

import jimmy.mcgymmy.model.Model;

/**
 * Undo the previous action that causes changes to the ModelManager.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String SHORT_DESCRIPTION = "Undo the last command.";

    public static final String MESSAGE_UNDO_SUCCESS = "Successfully undid the last command.";
    public static final String MESSAGE_NOT_UNDOABLE = "Cannot undo anymore.";

    @Override
    public CommandResult execute(Model model) {
        if (!model.canUndo()) {
            return new CommandResult(MESSAGE_NOT_UNDOABLE);
        }
        model.undo();
        return new CommandResult(MESSAGE_UNDO_SUCCESS);
    }
}
