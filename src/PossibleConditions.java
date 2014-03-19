import java.util.List;
import java.util.ArrayList;

class PossibleConditions {
    private List<Condition> holding;
    private List<Condition> at;
    private List<Condition> contains;

    public PossibleConditions(String[] ids, String[] locations, String[] types) {
        holding = condList("Holding", ids, types);
        at = condList("At", ids, locations);
        contains = condList("Contains", locations, types);
    }

    /**
     * @return the holding
     */
    public List<Condition> getHolding() {
        return holding;
    }

    /**
     * @return the at
     */
    public List<Condition> getAt() {
        return at;
    }

    /**
     * @return the contains
     */
    public List<Condition> getContains() {
        return contains;
    }

    public List<Condition> getConditions() {
        List<Condition> conditions = new ArrayList<>();
        conditions.addAll(holding);
        conditions.addAll(at);
        conditions.addAll(contains);
        return conditions;
    }

    private List<Condition> condList(String name, String[] vars1, String[] vars2) {
        List<Condition> list = new ArrayList<>();
        for (String var1 : vars1) {
            for (String var2 : vars2) {
                List<String> vars = new ArrayList<>();
                vars.add(var1);
                vars.add(var2);
                list.add(new Condition("Holding", vars));
            }
        }
        return list;
    }
}
