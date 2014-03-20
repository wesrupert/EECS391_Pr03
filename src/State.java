import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class State {
    public static int GoldValue;
    public static int WoodValue;

    private State parent;
    private PlanAction fromParent;
    private Map<String, Value> valuesFromParent;
    private List<PlanAction> actions;
    private List<Condition> state;

    public State(List<PlanAction> actions, List<Condition> initialState) {
        this.parent = null;
        this.fromParent = null;
        this.valuesFromParent = null;
        this.actions = actions;
        this.state = initialState;
    }

    public State(State parent, PlanAction action, Map<String, Value> values, List<Condition> state) {
        this.parent = parent;
        this.fromParent = action;
        this.valuesFromParent = values;
        this.actions = parent.actions;
        this.state = state;
    }

    public State getParent() {
        return this.parent;
    }

    public PlanAction getFromParent() {
        return this.fromParent;
    }

    public Map<String, Value> getValuesFromParent() {
        return valuesFromParent;
    }

    public List<Condition> getState() {
        return this.state;
    }
    
    public List<PlanAction> generatePossibleActions() {
        List<String> variables = new ArrayList<>();
        for (Condition condition : state) {
            variables.addAll(condition.getVariables().keySet());
        }
        
        for (PlanAction action : actions) {
            //TODO plug variables into actions, see if they are valid
            
        }
        
        return null;
    }

    public int getHeuristicWeight(bool isGold) {
        int weight = isGold ? GoldValue : WoodValue;
        String type;

        // Find how much gold we have.
        Value has = null;
        for (Condition c : state) {
            if (c.getName().equals("Has") && {
                has = c.getValue(type);
            }
        }
        if (has == null) {
            return -1;
        }

        return -1;
    }
}
