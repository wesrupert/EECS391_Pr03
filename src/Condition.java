import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Condition {
    public static final Condition HOLDING = new Condition("Holding", Arrays.asList(new String[] {"id", "type"}));
    public static final Condition AT = new Condition("At", Arrays.asList(new String[] {"id", "pos"}));
    public static final Condition HAS = new Condition("Has", Arrays.asList(new String[] {"type"}), Arrays.asList(new String[] {"amt"}));
    public static final Condition CONTAINS = new Condition("Contains", Arrays.asList(new String[] {"pos", "type"}));
    
    public static final Value NOTHING = new Value(0);
    public static final Value GOLD = new Value(1);
    public static final Value WOOD = new Value(2);
    public static final Value TOWNHALL = new Value(3);
    public static final Value GOLDMINE = new Value(4);
    public static final Value FOREST = new Value(5);
    
    private String name;
    private Map<String, Value> variables;

    public Condition(String name, List<String> variables) {
    	this(name, variables, null);
    }
    
    public Condition(String name, List<String> variables, List<String> sums) {
        this.name = name;
        this.variables = new HashMap<>();
        for (String var : variables) {
            this.variables.put(var, new Value(0));
        }
        if (sums != null) {
	        for (String var : sums) {
	        	this.variables.put(var,  new Value(0, Value.Type.ADD));
	        }
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
