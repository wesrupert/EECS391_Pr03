import java.util.ArrayList;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;


public class Planner {
	private List<PlanAction> availableActions;
	private State startState;
	private State goalState;
	
	public Planner(List<PlanAction> availableActions, State startState, State goalState) {
		this.availableActions = new ArrayList<>(availableActions);
		this.startState = startState;
		this.goalState = goalState;
	}
	
	public List<State> createPlan() {
		List<State> plan = new LinkedList<>();
		plan.add(startState);
		
		for (Condition goalCondition : goalState.getState()) {
			//TODO solve goal conditions one at a time
			List<State> goalPath = getPathToGoal(goalCondition, plan.get(plan.size() - 1));
			plan.addAll(goalPath);
		}
		
		return plan;
	}

	private List<State> getPathToGoal(Condition goalCondition, State currentState) {
        // Find a goal state.
        SortedSet<State> states = new TreeSet<>();
        states.add(currentState);
        State current = states.first();
		while (!current.isGoalState()) {
            states.addAll(getNextStates(current));
            current = states.first();
		}

        // Generate the list from the found state.
        List<State> path = new ArrayList<>();
        while (current.getParent() != null) {
            path.add(0, current);
            current = current.getParent();
        }

        return path;
	}
	
	private List<State> getNextStates(State currentState) {
		List<PlanAction> possibleActions = generatePossibleActions(currentState);
		List<State> nextStates = new ArrayList<>();
		for (PlanAction action : possibleActions) {
			nextStates.add(action.use(currentState));
		}
		return nextStates;
	}
	
    public List<PlanAction> generatePossibleActions(State state) {
        List<Value> variables = new ArrayList<>();
        for (Condition condition : state.getState()) {
            variables.addAll(condition.getVariables());
        }
        
        List<Value> units = new ArrayList<>();
        List<Value> positions = new ArrayList<>();
        List<Value> types = new ArrayList<>();
        
        outer:
        for (Value variable : variables) {
        	List<Value> addList = null;
        	if (variable.getName().equalsIgnoreCase("id")) {
        		addList = units;
        	} else if (variable.getName().equalsIgnoreCase("pos")) {
        		addList = positions;
        	} else if (variable.getName().equalsIgnoreCase("type")) {
        		addList = types;
        	} else if (variable.getName().equalsIgnoreCase("amt")) {
        		continue;
        	}
        	
        	if (addList == null) {
        		System.out.println("Unrecognized variable name!!!" + variable.getName());
        		continue;
        	}
        	
        	for (Value var : addList) {
        		if (variable.getName().equals(var.getName()) && variable.equals(var)) {
        			continue outer;
        		}
        	}
        	addList.add(variable);
        }

        List<PlanAction> validActions = new ArrayList<>();
        for (PlanAction actionTemplate : availableActions) {
            List<PlanAction> possibleActions = actionTemplate.getPossibleActions(units, positions, types);
            for (PlanAction action : possibleActions) {
            	if (action.isApplicableTo(state)) {
            		validActions.add(action);
            	}
            }
        }
        
        return validActions;
    }
    
//    private List<Pair<PlanAction, List<Value>>> getActions(State state, PossibleConditions p) {
//    	List<Pair<PlanAction, List<Value>>> actions = new ArrayList<>();
//    	for (List<Value> varset : p.getStates()) {
//    		for (PlanAction action : availableActions) {
//    			if (action.isApplicableTo(state, varset)) {
//    				actions.add(new Pair(action, varset));
//    			}
//    		}
//    	}
//    	return actions;
//    }

}
