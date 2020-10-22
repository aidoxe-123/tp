package jimmy.mcgymmy.model;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.function.Predicate;

import javafx.util.Pair;
import jimmy.mcgymmy.model.food.Food;

class History {
    private final Stack<Pair<McGymmy, Predicate<? super Food>>> stack;

    History() {
        stack = new Stack<>();
    }

    private boolean checkIfSameWithPreviousState(McGymmy otherMcGymmy, Predicate<? super Food> otherPredicate) {
        if (stack.empty()) {
            return true;
        }

        McGymmy mcGymmy = stack.peek().getKey();
        Predicate<? super Food> predicate = stack.peek().getValue();

        boolean isSameMcGymmy = otherMcGymmy.equals(mcGymmy);
        boolean isSamePredicate = otherPredicate.equals(predicate);
        return isSameMcGymmy && isSamePredicate;
    }

    void save(ModelManager modelManager) {
        McGymmy mcGymmy = new McGymmy(modelManager.getMcGymmy());
        Predicate<? super Food> predicate = modelManager.getPredicate();
        boolean isSameWithPreviousState = checkIfSameWithPreviousState(mcGymmy, predicate);
        if (!isSameWithPreviousState) {
            stack.push(new Pair<>(mcGymmy, predicate));
        }
    }

    boolean empty() {
        return stack.empty();
    }

    Pair<McGymmy, Predicate<? super Food>> pop() throws EmptyStackException {
        assert !stack.empty() : "History is empty";
        return stack.pop();
    }
}
