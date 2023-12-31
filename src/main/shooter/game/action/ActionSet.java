package src.main.shooter.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import src.main.shooter.utils.ArraySet;

public class ActionSet implements Serializable {
    private static final long serialVersionUID = -4852037557772448218L;

    private final ArrayList<Action> instantActions;
    private final ArraySet<Action> longActions;

    public ActionSet() {
        instantActions = new ArrayList<Action>();
        longActions = new ArraySet<Action>();
    }

    public ArrayList<Action> getInstantActions() {
        return instantActions;
    }

    public ArraySet<Action> getLongActions() {
        return longActions;
    }

    @Override
    public String toString() {
        return "ActionSet [instantActions=" + instantActions + ", longActions=" + longActions + "]";
    }
}
