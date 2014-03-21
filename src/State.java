import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State implements Comparable<State> {
    public static int GoldValue;
    public static int WoodValue;

    private State parent;
    private int depth;
    private PlanAction fromParent;
    private List<Value> valuesFromParent;
    private List<Condition> state;

    public State(List<Condition> initialState) {
        this.parent = null;
        this.depth = 0;
        this.fromParent = null;
        this.valuesFromParent = null;
        this.state = initialState;
    }

    public State(State parent, PlanAction action, List<Value> values, List<Condition> state) {
        this.parent = parent;
        this.depth = parent.depth + 1;
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

    public int getHeuristicWeight() {
    	return 2 * -400 // TODO: Change the 2 to a peasant count
    		+ this.getHeuristicWeight(true)
    		+ this.getHeuristicWeight(false)
    		+ depth * 100;
    }

    private int getHeuristicWeight(boolean isGold) {
        int weight = isGold ? GoldValue : WoodValue;
        Value type = isGold ? Condition.GOLD : Condition.WOOD;
        int id = isGold ? 14 : 15;

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

        // Get weight determined by proximity.
        int numAt = 0;
        for (Condition c : state) {
        	if (c.getName().equals("At") &&
        		c.getValue("pos") != null &&
        		c.getValue("pos").getValue() == id) {
        		numAt++;
        	}
        }
        weight += (weight / 100) * numAt;

        return weight;
    }
    
    public boolean isGoalState(Condition goal) {
    	return state.contains(goal);
    }

	public static State getStartState(int peasantId) {
		List<Condition> conditions = new ArrayList<>();
		
		// Add condition Holding(Peasant1, Nothing)
		conditions.add(new Condition(Condition.HOLDING, Arrays.asList(
				new Value[]{new Value("id", peasantId), new Value(Condition.NOTHING)})));
		
		// Add condition At(peasent1, Townhall)
		conditions.add(new Condition(Condition.AT, Arrays.asList(
				new Value[]{new Value("id", peasantId), new Value(Condition.TOWNHALL)})));
		
		// Add condition Has(wood, 0)
		conditions.add(new Condition(Condition.HAS, Arrays.asList(
				new Value[]{new Value(Condition.WOOD), new Value("amt", Value.Type.ADD)})));
		
		// Add condition Has(gold, 0)
		conditions.add(new Condition(Condition.HAS, Arrays.asList(
				new Value[]{new Value(Condition.GOLD), new Value("amt", Value.Type.ADD)})));
		
		// Add condition Contains(Goldmine, gold)
		conditions.add(new Condition(Condition.CONTAINS, Arrays.asList(
				new Value[]{new Value(Condition.GOLDMINE), new Value(Condition.GOLD)})));
		
		// Add condition Contains(Forest, wood)
		conditions.add(new Condition(Condition.CONTAINS, Arrays.asList(
				new Value[]{new Value(Condition.FOREST), new Value(Condition.WOOD)})));
		
		return new State(conditions);
	}
	
	public static State getGoalState(int scenario) {
		List<Condition> conditions = new ArrayList<>();
		
		int wood = 0;
		int gold = 0;
		switch (scenario) {
		case 1:
			wood = 200;
			gold = 200;
			break;
		case 2:
		case 3:
			wood = 1000;
			gold = 1000;
			break;
		case 4:
			wood = 2000;
			gold = 3000;
			break;
		}
		
		// Add condition Has(Wood, AMT)
		conditions.add(new Condition(Condition.HAS, Arrays.asList(
				new Value[]{new Value(Condition.WOOD), new Value("amt", wood)})));
		
		// Add condition Has(Gold, AMT)
		conditions.add(new Condition(Condition.HAS, Arrays.asList(
				new Value[]{new Value(Condition.GOLD), new Value("amt", gold)})));
		
		return new State(conditions);
	}

	 public int compareTo(State other) {
	 	return this.getHeuristicWeight() - other.getHeuristicWeight();
	 }
}
