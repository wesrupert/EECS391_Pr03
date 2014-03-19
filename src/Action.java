import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Map;

import edu.cwru.sepia.action.ActionType;

public class Action {
    private String name;
    private Set<String> variables;
    private List<Condition> preconditions;
    private List<Condition> add;
    private List<Condition> delete;
    private ActionType sepiaAction;

    public Action(String name,
            Set<String> variables,
            List<Condition> preconditions,
            List<Condition> add,
            List<Condition> delete) {
        this.name = name;
        this.variables = variables;
        if (this.onlyDefinedVarsIn(preconditions)) {
            this.preconditions = preconditions;
        }
        if (this.onlyDefinedVarsIn(add)) {
            this.add = add;
        }
        if (this.onlyDefinedVarsIn(delete)) {
            this.delete = delete;
        }
    }

    public State use(State state, Map<String, Value> values) {
        if (!isApplicableTo(state) || !onlyDefinedVarsIn(values.keySet())) {
            return null;
        }
        List<Condition> newstate = new ArrayList<>(state.getState());
        for (Condition c : add) {
            if (!newstate.contains(c)) {
                newstate.add(c);
            }
        }
        for (Condition c : delete) {
            newstate.remove(c);
        }
        return new State(state, this, newstate);
    }

    public boolean isApplicableTo(State state) {
        for (Condition c : preconditions) {
            if (!state.getState().contains(c)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return this.name;
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

    private boolean onlyDefinedVarsIn(Iterable<Condition> conditions) {
        for (Condition c : conditions) {
            if (!c.usesOnly(this.variables)) {
                return false;
            }
        }
        return true;
    }

    private boolean onlyDefinedVarsIn(Set<String> variables) {
        for (String var : variables) {
            if (!variables.contains(var)) {
                return false;
            }
        }
        return true;
    }
}
