import java.util.List;
import java.util.Map;

public class State {
    public static int GoldValue;
    public static int WoodValue;

    private State parent;
    private PlanAction fromParent;
    private Map<String, Value> valuesFromParent;
    private List<Condition> state;

    public State(List<Condition> initialState) {
        this.parent = null;
        this.fromParent = null;
        this.valuesFromParent = null;
        this.state = initialState;
    }

    public State(State parent, PlanAction action, Map<String, Value> values, List<Condition> state) {
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
        Value type = isGold ? Condition.GOLD : Condition.WOOD;

        for (Condition c : state) {
            if (c.getValue("type").equals(type)) {
                // Find how much gold we have.
                if (c.getName().equals("Has")) {
                    weight -= c.getValue("amt").get();
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
