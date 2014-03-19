import java.util.ArrayList;
import java.util.List;


public class Planner {
	private List<Action> availableActions;
	private State startState;
	private State goalState;
	
	public Planner(List<Action> availableActions, State startState, State goalState) {
		this.availableActions = new ArrayList<>();
		this.availableActions.addAll(availableActions);
		
		this.startState = startState;
		
		this.goalState = goalState;
	}
	
	// TODO
	public List<Action> createPlan() {
		for (Condition goalCondition : goalState.getState()) {
			//TODO solve goal conditions one at a time
		}
		
		return null;
	}

}
