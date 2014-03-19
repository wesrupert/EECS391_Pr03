import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import edu.cwru.sepia.action.ActionType;

public class Action {
	private String name;
	private List<Condition> preconditions;
	private List<Condition> add;
	private List<Condition> delete;
	private ActionType sepiaAction;

    public Action(String name,
            String[] values,
            List<Condition> preconditions,
            List<Condition> add,
            List<Condition> delete) {
    	this.name = name;
    	this.preconditions = preconditions;
    	this.add = add;
    	this.delete = delete;
    }

    public List<Condition> use(State state, Map<String, Object> values) {
        if (!isApplicableTo(state)) {
            return state.getState();
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
        return newstate;
    }

    public boolean isApplicableTo(State state) {
    	for (Condition c : preconditions) {
    		boolean isIn = false;
	    	for (Condition o : state.getState()) {
	    		isIn |= c.equals(o);
	    		if (isIn) {
	    			break;
	    		}
	    	}
	    	if (!isIn) {
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
}
