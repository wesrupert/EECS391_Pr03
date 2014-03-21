import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class PossibleConditions {
    private int[] ids, locations, types;
    private List<Condition> holding, at, contains;
    private List<List<Value>> states;

    public PossibleConditions(int[] ids, int[] locations, int[] types) {
        this.ids = ids;
        this.locations = locations;
        this.types = types;
        holding = at = contains = null;
        states = null;
    }

    public List<Condition> getHolding() {
        if (holding == null) {
            holding = condList("Holding", "id", "type", ids, types);
        }
        return holding;
    }

    public List<Condition> getAt() {
        if (at == null) {
            at = condList("At", "id", "pos", ids, locations);
        }
        return at;
    }

    public List<Condition> getContains() {
        if (contains == null) {
            contains = condList("Contains", "pos", "type", locations, types);
        }
        return contains;
    }

    public List<Condition> getConditions() {
        List<Condition> conditions = new ArrayList<>();
        conditions.addAll(getHolding());
        conditions.addAll(getAt());
        conditions.addAll(getContains());
        return conditions;
    }

    public List<List<Value>> getStates() {
        if (states == null) {
            states = statesList();
        }
        return states;
    }

    private List<Condition> condList(String name, String var1name, String var2name, int[] vars1, int[] vars2) {
        List<Condition> list = new ArrayList<>();
        Condition template = new Condition(name, new Value[] {
            new Value(var1name),
            new Value(var2name)
        });
        for (int var1 : vars1) {
            for (int var2 : vars2) {
                List<Value> vars = new ArrayList<>();
                vars.add(new Value(var1name, var1));
                vars.add(new Value(var2name, var2));
                list.add(new Condition(template, vars));
            }
        }
        return list;
    }

    private List<List<Value>> statesList() {
        List<List<Value>> list = new ArrayList<>();
        for (int id : ids) {
            for (int location : locations) {
                for (int type : types) {
                    List<Value> values = new ArrayList<>();
                    values.add(new Value("id", id));
                    values.add(new Value("pos", location));
                    values.add(new Value("type", type));
                    list.add(values);
                }
            }
        }
        return list;
    }
    
    // Move(id, from, to):
    //     P: At(id, from)
    //     A: At(id, to)
    //     D: At(id, from)
	public static PlanAction getMoveAction() {
        List<Condition> preconditions = new ArrayList<>();
        List<Condition> add = new ArrayList<>();
        List<Condition> delete = new ArrayList<>();
        
		// Add the precondition At(id, from)
		preconditions.add(new Condition("At", new Value[] {new Value("id"), new Value("from")}));

		// Add At(id, to) to the Add list
		add.add(new Condition("At", new Value[] {new Value("id"), new Value("to")}));
		
		// Add At(id, from) to the delete list
		delete.add(new Condition("At", new Value[] {new Value("id"), new Value("from")}));
		
		// Move action is Move(id, from, to)
		return new PlanAction("Move", Arrays.asList(new String[] {"id", "from", "to"}),
				preconditions, add, delete);
	}
	
    // Harvest(id, pos, type):
    //     P: Holding(id, NOTHING), At(id, pos), Contains(pos, type)
    //     A: Holding(id, type)
    //     D: Holding(id, NOTHING)
	public static PlanAction getHarvestAction() {
        List<Condition> preconditions = new ArrayList<>();
        List<Condition> add = new ArrayList<>();
        List<Condition> delete = new ArrayList<>();
        
		// Add the precondition Holding(id, Nothing)
		preconditions.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		// Add the precondition At(id, pos)
		preconditions.add(new Condition("At", new Value[] {new Value("id"), new Value("pos")}));
		// Add the precondition Contains(pos, type)
		preconditions.add(new Condition("Contains", new Value[] {new Value("pos"), new Value("type")}));
		
		// Add Holding(id, type) to the Add list
		add.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		
		// Add Holding(id, Nothing) to the delete list
		delete.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		
		// Harvest action is Harvest(id, pos, type)
		return new PlanAction("Harvest", Arrays.asList(new String[] {"id", "pos", "type"}),
				preconditions, add, delete);
	}
	
    // Deposit(id, type):
    //     P: Holding(id, type), At(id, TOWNHALL)
    //     A: Holding(id, NOTHING), Has(type, +amt)
    //     D: Holding(id, type)
	public static PlanAction getDepositAction() {
        List<Condition> preconditions = new ArrayList<>();
        List<Condition> add = new ArrayList<>();
        List<Condition> delete = new ArrayList<>();

		// Add the precondition Holding(id, type)
		preconditions.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		// Add the precondition At(id, Townhall)
		preconditions.add(new Condition("At", new Value[] {new Value("id"), new Value(Condition.TOWNHALL)}));

		// Add Holding(id, Nothing) to the Add list
		add.add(new Condition("Holding", new Value[] {new Value("id"), new Value(Condition.NOTHING)}));
		// Add +Has(type, amt) to the Add list
		add.add(new Condition("Has", new Value[] {new Value("type"), new Value("amt", Value.Type.ADD)}));
		
		// Add Holding(id, type) to the delete list
		delete.add(new Condition("Holding", new Value[] {new Value("id"), new Value("type")}));
		
		// Deposit action is Deposit(id, type)
		return new PlanAction("Deposit", Arrays.asList(new String[] {"id", "type"}),
				preconditions, add, delete);
	}
}
