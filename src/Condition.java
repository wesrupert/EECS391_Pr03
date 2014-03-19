import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Condition {
    private String name;
    private Map<String, Value> variables;

    public Condition(String name, Type type, List<String> variables) {
        this.name = name;
        this.variables = new HashMap<>();
        for (String var : variables) {
            this.variables.put(var, new Value(0));
        }
    }

    public Condition(Condition original, Map<String, Value> variables) {
        this.name = original.name;
        this.variables = new HashMap<>();
        for (String var : variables.keySet()) {
            this.variables.put(var, new Value(variables.get(ver)));
        }
    }

    public String getName() {
        return name;
    }

    public Map<String, Value> getVariables() {
        return variables;
    }

    @Override
    public boolean equals(Object o) {
        Condition cond = (Condition)o;
        if (!name.equals(cond.name)) {
            return false;
        }
        if (variables.size() != cond.variables.size()) {
            return false;
        }
        for (String var : variables.keySet()) {
            if (!cond.variables.containsKey(var)) {
                return false;
            }
            Value i1 = variables.get(var);
            Value i2 = cond.variables.get(var);
            if (!i1.equals(i2)) {
                return false;
            }
        }
        return true;
    }

    public boolean greaterThan(Condition c) {
        return compareGreaterThan(c, false);
    }

    public boolean strictlyGreaterThan(Condition c) {
        return compareGreaterThan(c, true);
    }

    private boolean compareGreaterThan(Condition c, boolean strictly) {
        int compareTo = 0;
        if (strictly) {
            compareTo = 1;
        }
        for (String var : variables.keySet()) {
            if (!c.variables.containsKey(var)) {
                return false;
            }
            Value i1 = variables.get(var);
            Value i2 = c.variables.get(var);
            if (i1.compareTo(i2) < compareTo) {
                return false;
            }
        }
        return true;
    }

    public boolean lessThan(Condition c) {
        return compareLessThan(c, false);
    }

    public boolean strictlyLessThan(Condition c) {
        return compareLessThan(c, true);
    }

    private boolean compareLessThan(Condition c, boolean strictly) {
        int compareTo = 0;
        if (strictly) {
            compareTo = -1;
        }
        for (String var : variables.keySet()) {
            if (!c.variables.containsKey(var)) {
                return false;
            }
            Value i1 = variables.get(var);
            Value i2 = c.variables.get(var);
            if (i1.compareTo(i2) > compareTo) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String temp = name + "(";
        for (int i = 0; i < variables.size(); i++) {
            temp = temp + variables.get(i);
            if (i < variables.size() - 1) {
                temp = temp + ",";
            }
        }
        return temp + ")";
    }
}
