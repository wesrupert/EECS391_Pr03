import java.util.List;


public class Tester {

	public static void main(String[] args) {
		int scenario = 1;
		
		List<PlanAction> actions = PlanAction.getActions(scenario);
        State startState = State.getStartState(0);
        State goalState = State.getGoalState(scenario);
        
        Planner planner = new Planner(actions, startState, goalState);
        List<State> plan = planner.createPlan();
        
        Planner.printPlan(plan, System.out);
	}

}
