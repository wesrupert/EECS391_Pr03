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
			
			actions.add(PossibleConditions.getMoveAction());
			actions.add(PossibleConditions.getHarvestAction());
			actions.add(PossibleConditions.getDepositAction());
			break;
		case 3:
		case 4:
			// Multiple peasant actions
			actions.add(PossibleConditions.getMoveAction(2));
			actions.add(PossibleConditions.getMoveAction(2));
			actions.add(PossibleConditions.getHarvestAction(2));
			actions.add(PossibleConditions.getHarvestAction(3));
			actions.add(PossibleConditions.getDepositAction(2));
			actions.add(PossibleConditions.getDepositAction(3));
			break;
		}
		return actions;
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
    
    public List<Value> getConstants() {
    	return constants;
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
		case "Move1":
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
		case "Harvest1":
			for (Value unit : units) {
				for (Value position : positions) {
					for (Value type : types) {
						variableCombos.add(new Value[]{new Value(unit), new Value(position), new Value(type)});
					}
				}
			}
			break;
		case "Deposit1":
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
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
    		return false;
    	}
    	if (o == this) {
    		return true;
    	}
    	if (!(o instanceof PlanAction)) {
    		return false;
    	}
    	PlanAction action = (PlanAction) o;
    	
    	if (!action.getName().equals(name) || action.isAppliedAction() != isAppliedAction || action.getVariables().size() != variables.size()) {
    		return false;
    	}
    	
    	if (isAppliedAction) {
    		if (action.getConstants().size() != constants.size()) {
    			return false;
    		}
    		
    		for (int i = 0; i < constants.size(); i++) {
    			if (!constants.get(i).equals(action.getConstants().get(i))) {
    				return false;
    			}
    		}
    	} else {
    		for (int i = 0; i < variables.size(); i++) {
    			if (!constants.get(i).equals(action.getVariables().get(i))) {
    				return false;
    			}
    		}
    	}
    	
    	return true;
	}
}