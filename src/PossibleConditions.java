import java.util.List;
import java.util.ArrayList;

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
}
