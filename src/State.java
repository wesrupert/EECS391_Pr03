import java.util.List;

public class State {
	private State parent;
	private Action fromParent;
	private List<Action> actions;
	private List<Condition> state;

	public State(List<Action> actions, List<Condition> initialState) {
		this.parent = null;
        this.fromParent = null;
		this.actions = actions;
		this.state = initialState;
	}

	public State(State parent, Action action, List<Condition> state) {
		this.parent = parent;
        this.fromParent = action;
		this.actions = parent.actions;
		this.state = state;
	}

	public State getParent() {
		return this.parent;
	}

	public Action getFromParent() {
		return this.fromParent;
	}

	public List<Condition> getState() {
		return this.state;
	}
}
