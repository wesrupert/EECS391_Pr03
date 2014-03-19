import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cwru.sepia.action.ActionType;

public class PlanAction {
    private String name;
    private Set<String> variables;
    private List<Condition> preconditions;
    private List<Condition> add;
    private List<Condition> delete;
    private ActionType sepiaAction;

    public PlanAction(String name,
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

    
	public static PlanAction getNewMoveAction(String id, String from, String to) {
		// The action is defined as Move(id, from, to)
		Set<String> variables = new HashSet<>();
		variables.add(id);
		variables.add(from);
		variables.add(to);
		
		// Add the precondition At(id, from)
		List<Condition> preconditions = new ArrayList<>();
		List<String> precVars = new ArrayList<>();
		precVars.add(id);
		precVars.add(from);
		preconditions.add(new Condition("At", precVars));

		// Add At(id, to) to the Add list
		List<Condition> add = new ArrayList<>();
		List<String> addVars = new ArrayList<>();
		addVars.add(id);
		addVars.add(to);
		add.add(new Condition("At", addVars));
		
		//Add At(id, from) to the delete list
		List<Condition> delete = new ArrayList<>();
		List<String> delVars = new ArrayList<>();
		delVars.add(id);
		delVars.add(from);
		delete.add(new Condition("At", delVars));
		
		return new PlanAction("Move", variables, preconditions, add, delete);
	}
	
	public static PlanAction getNewHarvestAction(String id, String pos, String type) {
		// The action is defined as Harvest(id, pos, type)
		Set<String> variables = new HashSet<>();
		variables.add(id);
		variables.add(pos);
		variables.add(type);
		
		// Add the precondition Holding(id, nothing)
		List<Condition> preconditions = new ArrayList<>();
		List<String> precVars = new ArrayList<>();
		precVars.add(id);
		precVars.add("Nothing");
		preconditions.add(new Condition("Holding", precVars));
		
		// Add the precondition At(id, pos)
		precVars.clear();
		precVars.add(id);
		precVars.add(pos);
		preconditions.add(new Condition("At", precVars));
		
		// Add the precondition Contains(pos, type)
		precVars.clear();
		precVars.add(pos);
		precVars.add(type);
		preconditions.add(new Condition("Contains", precVars));

		// Add Holding(id, type) to the Add list
		List<Condition> add = new ArrayList<>();
		List<String> addVars = new ArrayList<>();
		addVars.add(id);
		addVars.add(type);
		add.add(new Condition("Holding", addVars));
		
		//Add Holding(id, nothing) to the delete list
		List<Condition> delete = new ArrayList<>();
		List<String> delVars = new ArrayList<>();
		delVars.add(id);
		delVars.add("Nothing");
		delete.add(new Condition("Holding", delVars));
		
		return new PlanAction("Harvest", variables, preconditions, add, delete);
	}
	
	public static PlanAction getNewDepositAction(String id, String type) {
		// The action is defined as Deposit(id, type)
		Set<String> variables = new HashSet<>();
		variables.add(id);
		variables.add(type);
		
		// Add the precondition Holding(id, type)
		List<Condition> preconditions = new ArrayList<>();
		List<String> precVars = new ArrayList<>();
		precVars.add(id);
		precVars.add(type);
		preconditions.add(new Condition("Holding", precVars));
		
		// Add the precondition At(Townhall)
		precVars.clear();
		precVars.add("Townhall");
		preconditions.add(new Condition("At", precVars));

		// Add Holding(id, nothing) to the Add list
		List<Condition> add = new ArrayList<>();
		List<String> addVars = new ArrayList<>();
		addVars.add(id);
		addVars.add("Nothing");
		add.add(new Condition("Holding", addVars));
		
		// Add +Has(type, 100) to the Add list
		addVars.clear();
		addVars.add(type);
		List<String> addSums = new ArrayList<>();
		addSums.add("Amt");
		add.add(new Condition("Has", addVars, addSums));
		
		//Add Holding(id, type) to the delete list
		List<Condition> delete = new ArrayList<>();
		List<String> delVars = new ArrayList<>();
		delVars.add(id);
		delVars.add(type);
		delete.add(new Condition("Holding", delVars));
		
		return new PlanAction("Harvest", variables, preconditions, add, delete);
	}
}
