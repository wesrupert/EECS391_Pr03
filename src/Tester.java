import java.util.List;


public class Tester {

	public static void main(String[] args) {
		List<PlanAction> actions;
		State startState, goalState;
		Planner planner;
		List<State> plan;

		// Test single-peasant scenarios.
		actions = PlanAction.getActions(1);
        startState = State.getStartState(1);
        goalState = State.getGoalState(1);
        planner = new Planner(actions, startState, goalState);
        plan = planner.createPlan();
        Planner.printPlan(plan, System.out);

        // Test multi-peasant scenarios.
		actions = PlanAction.getActions(4);
        startState = State.getStartState(2);
        goalState = State.getGoalState(4);
        planner = new Planner(actions, startState, goalState);
        plan = planner.createPlan();
        Planner.printPlan(plan, System.out);
	}

}
