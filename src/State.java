import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State implements Comparable<State> {
    public static int GoldValue;
    public static int WoodValue;

    private State parent;
    private int depth;
    private int weight;
    private boolean weightSet;
    private PlanAction fromParent;
    private List<Value> valuesFromParent;
    private List<Condition> state;

    public State(List<Condition> initialState) {
        this.parent = null;
        this.depth = 0;
        this.weightSet = false;
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
    	if (!weightSet) {
    		weight = heuristicWeight();
    		weightSet = true;
    	}
    	return weight;
    }

    private int heuristicWeight() {
    	return 2 * -400 // TODO: Change the 2 to a peasant count
    		+ this.getHeuristicWeight(true)
    		+ this.getHeuristicWeight(false)
    		+ depth * 50;
    }

    private int getHeuristicWeight(boolean isGold) {
        int weight = isGold ? GoldValue : WoodValue;
        Value type = isGold ? Condition.GOLD : Condition.WOOD;

        for (Condition c : state) {
            if (c.getName().equals("Has") && c.getValue("type").equals(type)) {
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
        for (int i = 0; i < 3; i++) {
        	if (isHolding(i, isGold)) {
        		if (isAtTH(i)) {
        			numAt++;
        		}
        	} else {
        		if (isAt(i, isGold)) {
        			numAt++;
        		}
        	}
        }
        weight -= (weight / 100) * numAt;

        return weight;
    }
    
    private boolean isHolding(int id, boolean isGold) {
    	int type = isGold ? 11 : 12;
    	for (Condition c : state) {
    		if (c.getName().equals("Holding") &&
    			c.getValue(0).getValue() == id &&
    			c.getValue(1).getValue() == type) {
    			return true;
    		}
    	}
    	return false;
    }

    private boolean isAt(int id, boolean isGold) {
        int type = isGold ? 14 : 15;
        for (Condition c : state) {
        	if (c.getName().equals("At") &&
        		c.getValue(0).getValue() == id &&
        		c.getValue(1).getValue() == type) {
        		return true;
        	}
        }
        return false;
    }

    private boolean isAtTH(int id) {
        for (Condition c : state) {
        	if (c.getName().equals("At") &&
        		c.getValue(0).getValue() == id &&
        		c.getValue(1).getValue() == 13) {
        		return true;
        	}
        }
        return false;
    }

    public boolean isGoalState(Condition goal) {
    	return state.contains(goal);
    }

	public static State getStartState(int peasantId) {
		List<Condition> conditions = new ArrayList<>();
		
		// Add condition Holding(Peasant1, Nothing)
		conditions.add(new Condition(Condition.HOLDING, Arrays.asList(
				new Value[]{new Value("first", peasantId), new Value(Condition.NOTHING)})));
		
		// Add condition At(peasent1, Townhall)
		conditions.add(new Condition(Condition.AT, Arrays.asList(
				new Value[]{new Value("first", peasantId), new Value(Condition.TOWNHALL)})));
		
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
		State newState = new State(conditions);
		newState.GoldValue = 0;
		newState.WoodValue = 0;
		return newState;
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
		
		State newState = new State(conditions);
		newState.GoldValue = gold;
		newState.WoodValue = wood;
		return newState;
	}

	@Override
	public int compareTo(State other) {
	 	return this.getHeuristicWeight() - other.getHeuristicWeight();
	}

	@Override
	public String toString() {
		return "State depth " + depth + ", from " + fromParent + ", weight " + getHeuristicWeight() + "\n" + valuesFromParent.toString();
	}
}
