import java.util.ArrayList;
import java.util.List;

public class State {
	private State parent;
	private PlanAction fromParent;
	private List<PlanAction> actions;
	private List<Condition> state;

	public State(List<PlanAction> actions, List<Condition> initialState) {
		this.parent = null;
        this.fromParent = null;
		this.actions = actions;
		this.state = initialState;
	}

	public State(State parent, PlanAction action, List<Condition> state) {
		this.parent = parent;
        this.fromParent = action;
		this.actions = parent.actions;
		this.state = state;
	}

	public State getParent() {
		return this.parent;
	}

	public PlanAction getFromParent() {
		return this.fromParent;
	}

	public List<Condition> getState() {
		return this.state;
	}
	
	public List<PlanAction> generatePossibleActions() {
		List<String> variables = new ArrayList<>();
		for (Condition condition : state) {
			variables.addAll(condition.getVariables().keySet());
		}
		
		for (PlanAction action : actions) {
			//TODO plug variables into actions, see if they are valid
			
		}
		
		return null;
	}
}
