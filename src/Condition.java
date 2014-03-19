import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class Condition {
    private String name;
    private Map<String, Value> variables;

    public Condition(String name, List<String> variables) {
        this.name = name;
        this.variables = new HashMap<>();
        for (String var : variables) {
            this.variables.put(var, new Value(0));
        }
    }

    public Condition(Condition original, Map<String, Value> variables) {
        this.name = original.name;
        this.variables = new HashMap<>();
        for (String var : original.variables.keySet()) {
            Value value = new Value(original.variables.get(var));
            value.set(variables.get(var).get());
            this.variables.put(var, value);
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

    public Value getValue(String key) {
        if (!variables.containsKey(key)) {
            return null;
        }
        return variables.get(key);
    }

    public boolean usesOnly(Set<String> variables) {
        for (String var : variables) {
            if (!this.variables.containsKey(var)) {
                return false;
            }
        }
        return true;
    }

    public boolean compareToLoosely(Condition c) {
        if (!name.equals(c.name)) {
            return false;
        }
        for (String var : variables.keySet()) {
            if (!c.variables.containsKey(var)) {
                return false;
            }
            Value value = variables.get(var);
            Value other = c.variables.get(var);
            if (!value.compareToLoosely(other.get())) {
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
