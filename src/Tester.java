import java.util.List;


public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<PlanAction> actions = PlanAction.getActions(1);
        State startState = State.getStartState(0);
        State goalState = State.getGoalState(1);
        
        Planner planner = new Planner(actions, startState, goalState);
        List<State> plan = planner.createPlan();
	}

}
