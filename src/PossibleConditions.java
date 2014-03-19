import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

class PossibleConditions {
    private int[] ids, locations, types;
    private List<Condition> holding, at, contains;
    private List<Map<String, Value>> states;

    public PossibleConditions(int[] ids, int[] locations, int[] types) {
        this.ids = ids;
        this.locations = locations;
        this.types = types;
        holding = at = contains = null;
        states = null;
    }

    public List<Condition> getHolding() {
        if (holding == null) {
            holding = condList("Holding", ids, types);
        }
        return holding;
    }

    public List<Condition> getAt() {
        if (at == null) {
            at = condList("At", ids, locations);
        }
        return at;
    }

    public List<Condition> getContains() {
        if (contains == null) {
            contains = condList("Contains", locations, types);
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

    public List<Map<String, Value>> getStates() {
        if (states == null) {
            states = statesList();
        }
        return states;
    }


    private List<Condition> condList(String name, int[] vars1, int[] vars2) {
        List<Condition> list = new ArrayList<>();
        for (int var1 : vars1) {
            for (int var2 : vars2) {
                List<String> vars = new ArrayList<>();
                vars.add(String.valueOf(var1));
                vars.add(String.valueOf(var2));
                list.add(new Condition("Holding", vars));
            }
        }
        return list;
    }

    private List<Map<String, Value>> statesList() {
        List<Map<String, Value>> list = new ArrayList<>();
        for (int id : ids) {
            for (int location : locations) {
                for (int type : types) {
                    Map<String, Value> map = new HashMap<>();
                    map.put("id", new Value(id));
                    map.put("pos", new Value(location));
                    map.put("type", new Value(type));
                    list.add(map);
                }
            }
        }
        return list;
    }
}
