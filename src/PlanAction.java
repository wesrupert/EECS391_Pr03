import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlanAction {
    private String name;
    private Set<String> variables;
    private List<Condition> preconditions;
    private List<Condition> add;
    private List<Condition> delete;

    public PlanAction(String name, Set<String> variables, List<Condition> preconditions, List<Condition> add, List<Condition> delete) {
        this.name = name;
        this.variables = variables;
        this.preconditions = preconditions;
        this.add = add;
        this.delete = delete;
    }

    public State use(State state, List<Value> values) {
        if (!isApplicableTo(state, values)) {
            return null;
        }
        List<Condition> newconditions = new ArrayList<>(state.getState());

        // Add the new state conditions.
        for (Condition c : add) {
            Condition applied = new Condition(c, values);
            if (!newstate.contains(applied)) {
                newstate.add(applied);
            }
        }

        // Remove the old state conditions.
        for (Condition c : delete) {
            Condition applied = new Condition(c, values);
            if (newconditions.contains(applied)) {
                newconditions.remove(applied);
            }
        }

        return new State(state, this, values, newstate);
    }

    public boolean isApplicableTo(State state, List<Value> values) {
        for (Condition c : preconditions) {
            Condition applied = new Condition(c, values);
            if (!state.getState().contains(applied)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return this.name;
    }
    
    public Set<String> getVariables() {
    	return this.variables;
    }

    public List<Condition> getPreconditions() {
        return this.preconditions;
    }

    public List<Condition> getAdd() {
        return this.add;
    }

    public List<Condition> getDelete() {
        return this.delete;
    }
}