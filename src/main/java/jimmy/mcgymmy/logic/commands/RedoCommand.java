package jimmy.mcgymmy.logic.commands;

import jimmy.mcgymmy.model.Model;

/**
 * Redoes the last undo command.
 */
public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";
    public static final String SHORT_DESCRIPTION = "Redo the last command.";

    public static final String MESSAGE_UNDO_SUCCESS = "Successfully redid the last command.";
    public static final String MESSAGE_NOT_UNDOABLE = "Cannot redo anymore.";

    @Override
    public CommandResult execute(Model model) {
        if (!model.canUndo()) {
            return new CommandResult(MESSAGE_NOT_UNDOABLE);
        }
        model.undo();
        return new CommandResult(MESSAGE_UNDO_SUCCESS);
    }
}

