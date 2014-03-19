import java.util.List;

public class Condition {
	private String name;
	private List<String> variables;
	private List<Object> values;

	public Condition(String name, List<String> variables) {
		this.name = name;
		this.variables = variables;
		this.values = null;
	}

	public Condition(Condition original, List<Object> values) {
		this.name = original.name;
		this.values = values;
		this.variables = original.variables;
	}

	public String getName() {
		return this.name;
	}

	public List<String> getVariables() {
		return this.variables;
	}

	public List<Object> getValues() {
		return this.values;
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
		for (String var : this.variables) {
            if (!cond.variables.contains(var)) {
                return false;
            }
		}
		if (this.values == null && cond.getValues() == null) {
			return true;
        } else if (this.values == null || cond.values == null) {
            return false;
		} else {
			for (Object var : this.values) {
                if (!cond.values.contains(var)) {
                    return false;
                }
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
