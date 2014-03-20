import java.util.List;

public class State {
    public static int GoldValue;
    public static int WoodValue;

    private State parent;
    private PlanAction fromParent;
    private List<Value> valuesFromParent;
    private List<Condition> state;

    public State(List<Condition> initialState) {
        this.parent = null;
        this.fromParent = null;
        this.valuesFromParent = null;
        this.state = initialState;
    }

    public State(State parent, PlanAction action, List<Value> values, List<Condition> state) {
        this.parent = parent;
        this.fromParent = action;
        this.valuesFromParent = values;
        this.state = state;
    }

    public State getParent() {
        return this.parent;
    }

    public PlanAction getFromParent() {
        return this.fromParent;
    }

    public List<Value> getValuesFromParent() {
        return valuesFromParent;
    }

    public List<Condition> getState() {
        return this.state;
    }

    public int getHeuristicWeight(boolean isGold) {
        int weight = isGold ? GoldValue : WoodValue;
        Value type = isGold ? Condition.GOLD : Condition.WOOD;

        for (Condition c : state) {
            if (c.getValue("type").equals(type)) {
                // Find how much gold we have.
                if (c.getName().equals("Has")) {
                    weight -= c.getValue("amt").getValue();
                }
                // Find how much gold is in transit.
                if (c.getName().equals("Holding")) {
                    weight -= 100;
                }
            }
        }

        return weight;
    }
}
