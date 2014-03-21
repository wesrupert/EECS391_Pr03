import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlanAction {
    private String name;
    private Set<String> variables;
    private List<Condition> preconditions;
    private List<Condition> add;
    private List<Condition> delete;

    public PlanAction(String name, Set<String> variables, List<Condition> preconditions, List<Condition> add, List<Condition> delete) {
        this.name = name;
        this.variables = variables;
        this.preconditions = preconditions;
        this.add = add;
        this.delete = delete;
    }
    
	public static List<PlanAction> getActions(int scenario) {
    	List<PlanAction> actions = new ArrayList<PlanAction>();
    	
		switch (scenario) {
		case 1:
		case 2:
			// Single peasant actions
			
			actions.add(getMoveAction());
			actions.add(getHarvestAction());
		case 3:
		case 4:
			// Multiple peasant actions
			
		default:
			return actions;
		}
	}

    // Move(id, from, to):
    //     P: At(id, from)
    //     A: At(id, to)
    //     D: At(id, from)
	private static PlanAction getMoveAction() {
        List<Condition> preconditions = new ArrayList<>();
        List<Condition> add = new ArrayList<>();
        List<Condition> delete = new ArrayList<>();
        
		// Add the precondition At(id, from)
		preconditions.add(new Condition("At", new Value[] {new Value("id"), new Value("from")}));

		// Add At(id, to) to the Add list
		add.add(new Condition("At", new Value[] {new Value("id"), new Value("to")}));
		
		// Add At(id, from) to the delete list
		delete.add(new Condition("At", new Value[] {new Value("id"), new Value("from")}));
		
		// Move action is Move(id, from, to)
		return new PlanAction("Move", new HashSet<String>(Arrays.asList(new String[] {"id", "from", "to"})),
				preconditions, add, delete);
	}
	
    // Harvest(id, pos, type):
    //     P: Holding(id, NOTHING), At(id, pos), Contains(pos, type)
    //     A: Holding(id, type)
    //     D: Holding(id, NOTHING)
	private static PlanAction getHarvestAction() {
        List<Condition> preconditions = new ArrayList<>();
        List<Condition> add = new ArrayList<>();
        List<Condition> delete = new ArrayList<>();
        
		// Add the precondition Holding(id, Nothing)
		preconditions.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		// Add the precondition At(id, pos)
		preconditions.add(new Condition("At", new Value[] {new Value("id"), new Value("pos")}));
		// Add the precondition Contains(pos, type)
		preconditions.add(new Condition("Contains", new Value[] {new Value("pos"), new Value("type")}));
		
		// Add Holding(id, type) to the Add list
		add.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		
		// Add Holding(id, Nothing) to the delete list
		delete.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		
		// Harvest action is Harvest(id, pos, type)
		return new PlanAction("Harvest", new HashSet<String>(Arrays.asList(new String[] {"id", "pos", "type"})),
				preconditions, add, delete);
	}
	
    // Deposit(id, type):
    //     P: Holding(id, type), At(id, TOWNHALL)
    //     A: Holding(id, NOTHING), Has(type, +amt)
    //     D: Holding(id, type)
	private static PlanAction getDepositAction() {
        List<Condition> preconditions = new ArrayList<>();
        List<Condition> add = new ArrayList<>();
        List<Condition> delete = new ArrayList<>();

		// Add the precondition Holding(id, type)
		preconditions.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		// Add the precondition At(id, Townhall)
		preconditions.add(new Condition("At", new ValPue[] {new Value("id"), new Value(Condition.TOWNHALL)}));

		// Add Holding(id, Nothing) to the Add list
		add.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		// Add +Has(type, amt) to the Add list
		add.add(new Condition("Has", new Value[] {new Value("type"), new Value("amt", Value.Type.ADD)}));
		
		// Add Holding(id, type) to the delete list
		delete.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		
		// Deposit action is Deposit(id, type)
		return new PlanAction("Deposit", new HashSet<String>(Arrays.asList(new String[] {"id", "type"})),
				preconditions, add, delete);
	}

    public State use(State state, List<Value> values) {
        if (!isApplicableTo(state, values)) {
            return null;
        }
        List<Condition> newconditions = new ArrayList<>(state.getState());

        // Add the new state conditions.
        for (Condition c : add) {
            Condition applied = new Condition(c, values);
            if (!newconditions.contains(applied)) {
            	newconditions.add(applied);
            }
        }

        // Remove the old state conditions.
        for (Condition c : delete) {
            Condition applied = new Condition(c, values);
            if (newconditions.contains(applied)) {
                newconditions.remove(applied);
            }
        }

        return new State(state, this, values, newconditions);
    }

    public boolean isApplicableTo(State state, List<Value> values) {
        for (Condition c : preconditions) {
            Condition applied = new Condition(c, values);
            if (!state.getState().contains(applied)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return this.name;
    }
    
    public Set<String> getVariables() {
    	return this.variables;
    }

    public List<Condition> getPreconditions() {
        return this.preconditions;
    }

    public List<Condition> getAdd() {
        return this.add;
    }

    public List<Condition> getDelete() {
        return this.delete;
    }
}