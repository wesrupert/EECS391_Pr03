import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Condition {
	private String name;
    private Map<String, Integer> variables;

	public Condition(String name, List<String> variables) {
		this.name = name;
        this.variables = new HashMap<>();
        for (String var : variables) {
            this.variables.put(var, null);
        }
	}

	public Condition(Condition original, Map<String, Integer> variables) {
		this.name = original.name;
        this.variables = new HashMap<>(variables);
	}

	public String getName() {
		return this.name;
	}

	public Map<String, Integer> getVariables() {
		return this.variables;
	}

	@Override
	public boolean equals(Object o) {
        Condition cond = (Condition)o;
		if (!this.name.equals(cond.name)) {
			return false;
		}
		if (this.variables.size() != cond.variables.size()) {
			return false;
		}
		for (String var : this.variables.keySet()) {
            if (!cond.variables.containsKey(var)) {
                return false;
            }
            Integer i1 = this.variables.get(var);
            Integer i2 = cond.variables.get(var);
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
		for (String var : this.variables.keySet()) {
            if (!c.variables.containsKey(var)) {
                return false;
            }
            Integer i1 = this.variables.get(var);
            Integer i2 = c.variables.get(var);
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
		for (String var : this.variables.keySet()) {
            if (!c.variables.containsKey(var)) {
                return false;
            }
            Integer i1 = this.variables.get(var);
            Integer i2 = c.variables.get(var);
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
