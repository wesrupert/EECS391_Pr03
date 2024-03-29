import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State implements Comparable<State> {
    public static int GoldValue;
    public static int WoodValue;
    public static boolean isGold;

    private State parent;
    private int depth;
    private int weight;
    private boolean weightSet;
    private PlanAction fromParent;
    private List<Value> valuesFromParent;
    private List<Condition> state;
    private int numPeasants = 1;

    public State(List<Condition> initialState) {
        this.parent = null;
        this.depth = 0;
        this.weightSet = false;
        this.fromParent = null;
        this.valuesFromParent = null;
        this.state = initialState;
    }

    public State(State parent, PlanAction action, List<Value> values, List<Condition> state, int numPeasants) {
        this.parent = parent;
        this.depth = parent.depth + 1;
        this.fromParent = action;
        this.valuesFromParent = values;
        this.state = state;
        this.numPeasants = numPeasants;
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
    
    public int getDepth() {
    	return depth;
    }
    
    public int getNumPeasants() {
    	return numPeasants;
    }
    
    public void incrementNumPeasants() {
    	numPeasants++;
    }

    public int getHeuristicWeight() {
        if (!weightSet) {
            weight = depth + getHeuristic();
            weightSet = true;
        }
        return weight;
    }

    public int getHeuristic() {
        int goal = isGold ? GoldValue : WoodValue;
        return (goal - getValue()) / (100 * numPeasants());
    }

    private int numPeasants() {
        for (Condition c : state) {
            if (c.getName().equals("Numpeas")) {
                return c.getValue(0).getValue();
            }
        }
        return -1;
    }

    private int getValue() {
        int id = isGold ? 11 : 12;
        for (condition c : state) {
            if (c.getName().equals("Has") && c.getValue(0).getValue() == id) {
                return c.getValue(1).getValue();
            }
        }
        return -1;
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
		
		// Add condition Hall(Townhall)
		conditions.add(new Condition(Condition.HALL, Arrays.asList(
				new Value[]{new Value(Condition.TOWNHALL)})));

        // Add condition Numpeas(numpeas)
        conditions.add(new Condition(Condition.NUMPEAS, Arrays.asList(
                new Value[] { new Value("amt", 1, Value.Type.ADD) })));
				
		State newState = new State(conditions);
		State.GoldValue = 0;
		State.WoodValue = 0;
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
		
		// Add condition Has(Gold, AMT)
		conditions.add(new Condition(Condition.HAS, Arrays.asList(
				new Value[]{new Value(Condition.GOLD), new Value("amt", gold)})));
		
		// Add condition Has(Wood, AMT)
		conditions.add(new Condition(Condition.HAS, Arrays.asList(
				new Value[]{new Value(Condition.WOOD), new Value("amt", wood)})));
		
		State newState = new State(conditions);
		State.GoldValue = gold;
		State.WoodValue = wood;
		return newState;
	}

	@Override
	public int compareTo(State other) {
	 	return this.getHeuristicWeight() - other.getHeuristicWeight();
	}
	
	@Override boolean equals(Object other) {
		if (other == null || !(other instanceof State)) {
			return false;
		}
		State s = (State)other;
		List<Condition> conds = new ArrayList<>(s.state);
		for (Condition c : state) {
			boolean isIn = false;
			for (Condition o : s.state) {
				if (c.equals(o)) {
					isIn = true;
					conds.remove(o);
					break;
				}
			}
			if (!isIn) {
				return false;
			}
		}
		return conds.isEmpty();
	}

	@Override
	public String toString() {
		return "State depth " + depth + ", from " + fromParent + ", weight " + getHeuristicWeight() + "\n" + valuesFromParent.toString();
	}
}
