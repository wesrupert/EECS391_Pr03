import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
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
		List<State> plan = new ArrayList<>();
		plan.add(startState);
		
		for (Condition goalCondition : goalState.getState()) {
			State.isGold = goalCondition.getValue("type").getValue() == Condition.GOLD.getValue();
			
			List<State> goalPath = getPathToGoal(goalCondition, plan.get(plan.size() - 1));
//			plan.addAll(goalPath);
			plan = new ArrayList<>(goalPath);
		}
		
		return plan;
	}

	private List<State> getPathToGoal(Condition goalCondition, State currentState) {
        // Find a goal state.
        List<State> states = new ArrayList<>();
        State current = currentState;
        do {
            states.addAll(getNextStates(current));
            
            // A* is being performed here, since the State.compareTo() is comparing states based on their heuristic values
            Collections.sort(states);
            current = states.get(0);
            states.remove(current);
		} while (!current.isGoalState(goalCondition) && !states.isEmpty());

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
        	if (variable.getName().equalsIgnoreCase("first")
        			|| variable.getName().equalsIgnoreCase("second")
        			|| variable.getName().equalsIgnoreCase("third")) {
        		addList = units;
        	} else if (variable.getName().equalsIgnoreCase("pos")
        			|| variable.getName().equalsIgnoreCase("to")
        			|| variable.getName().equalsIgnoreCase("from")) {
        		addList = positions;
        	} else if (variable.getName().equalsIgnoreCase("type")) {
        		addList = types;
        	} else if (variable.getName().equalsIgnoreCase("amt")) {
        		continue;
        	}
        	
        	if (addList == null) {
        		System.out.println("Unrecognized variable name!!! " + variable.getName());
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
            inner:
            for (PlanAction action : possibleActions) {
            	if (action.isApplicableTo(state)) {
            		for (PlanAction existingAction : validActions) {
            			if (existingAction.equals(action)) {
            				continue inner;
            			}
            		}
            		validActions.add(action);
            	}
            }
        }
        
        return validActions;
    }

	public static void printPlan(List<State> plan, PrintStream writer) {
		System.out.println("PRINTING PLAN");
		for (State state : plan) {
			writer.print("Step " + state.getDepth() + " - ");
			writer.print("Action: " + state.getFromParent().getName() + " (");
			for (Value val : state.getFromParent().getConstants()) {
				writer.print(val.getConstantAsString());
				if (state.getFromParent().getConstants().indexOf(val) != state.getFromParent().getConstants().size() - 1) {
					writer.print(", ");
				}
			}
			writer.println(")");
		}
		
	}

}
