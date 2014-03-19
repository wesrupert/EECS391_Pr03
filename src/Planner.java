import java.util.ArrayList;
import java.util.List;


public class Planner {
	private List<Action> availableActions;
	private List<Condition> startState;
	private List<Condition> goalState;
	
	public Planner(List<Action> availableActions, List<Condition> startState, List<Condition> goalState) {
		this.availableActions = new ArrayList<>();
		this.availableActions.addAll(availableActions);
		
		this.startState = new ArrayList<>();
		this.startState.addAll(startState);
		
		this.goalState = new ArrayList<>();
		this.goalState.addAll(goalState);
	}
	
	// TODO
	public List<Action> createPlan() {
		
		return null;
	}

}
