import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PlanAction {
    private String name;
    private List<String> variables;
    private List<Condition> preconditions;
    private List<Condition> add;
    private List<Condition> delete;
    private List<Value> constants;
    private boolean isAppliedAction;

    public PlanAction(String name, List<String> variables, List<Condition> preconditions, List<Condition> add, List<Condition> delete) {
        this.name = name;
        this.variables = variables;
        this.preconditions = preconditions;
        this.add = add;
        this.delete = delete;
    }
    
	public PlanAction(String name, Value[] variables,
			List<Condition> preconditions, List<Condition> add,
			List<Condition> delete) {
		this.name = name;
        this.preconditions = preconditions;
        this.add = add;
        this.delete = delete;
        this.isAppliedAction = true;
        
        List<String> names = new ArrayList<>();
        for (Value variable : variables) {
        	names.add(variable.getName());
        }
        
        this.variables = names;
        this.constants = Arrays.asList(variables);
	}

	public static List<PlanAction> getActions(int scenario) {
    	List<PlanAction> actions = new ArrayList<PlanAction>();
    	
		switch (scenario) {
		case 1:
		case 2:
			// Single peasant actions
			
			actions.add(getMoveAction());
			actions.add(getHarvestAction());
			actions.add(getDepositAction());
			break;
		case 3:
		case 4:
			// Multiple peasant actions
			break;
		}
		return actions;
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
		return new PlanAction("Move", Arrays.asList(new String[] {"id", "from", "to"}),
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
		return new PlanAction("Harvest", Arrays.asList(new String[] {"id", "pos", "type"}),
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
		return new PlanAction("Deposit", Arrays.asList(new String[] {"id", "type"}),
				preconditions, add, delete);
	}

    public State use(State state) {
        if (!isApplicableTo(state)) {
            return null;
        }
        List<Condition> newconditions = new ArrayList<>(state.getState());

        // Add the new state conditions.
        for (Condition c : add) {
//            Condition applied = new Condition(c, values);
            if (!newconditions.contains(c)) {
            	newconditions.add(c);
            }
        }

        // Remove the old state conditions.
        for (Condition c : delete) {
//            Condition applied = new Condition(c, values);
            if (newconditions.contains(c)) {
                newconditions.remove(c);
            }
        }

        return new State(state, this, constants, newconditions);
    }

    public boolean isApplicableTo(State state) {
    	if (!isAppliedAction) {
    		return false;
    	}
        for (Condition c : preconditions) {
            if (!state.getState().contains(c)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return this.name;
    }
    
    public List<String> getVariables() {
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
    
    public boolean isAppliedAction() {
    	return isAppliedAction;
    }
    
    public PlanAction initializeAction(Value[] constants) {
    	if (constants.length != this.variables.size()) {
    		System.out.println("Can't initialize Action! Given constants don't match the number of variables!");
    	}
    	
    	for (int i = 0; i < constants.length; i++) {
    		constants[i].setName(this.variables.get(i));
    	}
    	
    	List<Condition> newPreconditions = insertConstantsIntoConditions(preconditions, constants);
    	List<Condition> newAdd = insertConstantsIntoConditions(add, constants);
    	List<Condition> newDelete = insertConstantsIntoConditions(delete, constants);
    	return new PlanAction(name, constants, newPreconditions, newAdd, newDelete);
    }

	private List<Condition> insertConstantsIntoConditions(List<Condition> conditions, Value[] constants) {
		List<Condition> newConditions = new LinkedList<>();
		
		for (Condition condition : conditions) {
			newConditions.add(new Condition(condition, Arrays.asList(constants)));
			
		}
		
		return newConditions;
	}

	public List<PlanAction> getPossibleActions(List<Value> units, List<Value> positions, List<Value> types) {
		List<Value[]> variableCombos = new ArrayList<>();
		
		switch (name) {
		case "Move":
			for (Value unit : units) {
				for (Value position1 : positions) {
					for (Value position2 : positions) {
						if (position1.equals(position2)) {
							continue;
						}
						variableCombos.add(new Value[]{new Value(unit), new Value(position1), new Value(position2)});
					}
				}
			}
			break;
		case "Harvest":
			for (Value unit : units) {
				for (Value position : positions) {
					for (Value type : types) {
						variableCombos.add(new Value[]{new Value(unit), new Value(position), new Value(type)});
					}
				}
			}
			break;
		case "Deposit":
			for (Value unit : units) {
				for (Value type : types) {
					if (type.equals(Condition.NOTHING)) {
						continue;
					}
					variableCombos.add(new Value[]{new Value(unit), new Value(type)});
				}
				
			}
			break;
		default:
				System.out.println("Unrecognized Action Type!!");
		}
		
		List<PlanAction> possibleActions = new ArrayList<>();
		
		for (Value[] combo : variableCombos) {
			possibleActions.add(initializeAction(combo));
		}
		
		return possibleActions;
	}
}