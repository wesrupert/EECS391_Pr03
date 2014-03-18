import java.util.List;
import java.util.ArrayList;

public class State {
	private State parent;
	private Action fromParent;
	private List<Action> actions;
	private List<Condition> state;

	public State(List<Action> actions, List<Condition> initialState) {
		this.parent = null;
		this.action = null;
		this.actions = actions;
		this.state = initialState;
	}

	public State(State parent, Action action, List<Object> values) {
		this.parent = parent;
		this.actions = parent.actions;
		this.action = action;
		this.state = action.use(parent.getState(), values);
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