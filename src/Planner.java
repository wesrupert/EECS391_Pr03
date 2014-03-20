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
	public List<State> createPlan() {
		for (Condition goalCondition : goalState.getState()) {
			//TODO solve goal conditions one at a time
			
		}
		
		return null;
	}
	
//    public List<PlanAction> generatePossibleActions() {
//        List<String> variables = new ArrayList<>();
//        for (Condition condition : state) {
//            variables.addAll(condition.getVariables().keySet());
//        }
//        
//        for (PlanAction action : actions) {
//            //TODO plug variables into actions, see if they are valid
//            
//        }
//        
//        return null;
//    }

}
