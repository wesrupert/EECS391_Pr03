import java.util.List;
class Pair {
	private PlanAction action;
	private List<Value> varset;

	public Pair(PlanAction action, List<Value> varset) {
		this.action = action;
		this.varset = varset;
	}

	public PlanAction getAction() {
		return action;
	}

	public List<Value> getVarset() {
		return varset;
	}
}