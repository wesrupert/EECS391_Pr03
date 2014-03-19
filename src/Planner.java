import java.util.ArrayList;
import java.util.List;


public class Planner {
	private List<PlanAction> availableActions;
	private State startState;
	private State goalState;
	
	public Planner(List<PlanAction> availableActions, State startState, State goalState) {
		this.availableActions = new ArrayList<>();
		this.availableActions.addAll(availableActions);
		
		this.startState = startState;
		
		this.goalState = goalState;
	}
	
	// TODO
	public List<PlanAction> createPlan() {
		for (Condition goalCondition : goalState.getState()) {
			//TODO solve goal conditions one at a time
			
		}
		
		return null;
	}

}
