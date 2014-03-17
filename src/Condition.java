public class Condition {
	private String name;
	private String[] variables;

	public Condition(String name, String[] variables) {
		this.name = name;
		this.variables = variables;
	}

	public String getName() {
		return this.name;
	}

	public String[] getVariables() {
		return this.variables;
	}

	@Override
	public boolean equals(Condition cond) {
		if (!this.name.equals(cond.name)) {
			return false;
		}
		if (this.variables.length() != cond.variables.length()) {
			return false;
		}
		for (String var : this.variables) {
			if (!cond.variables.contains(var)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String temp = name + "(");
		for (int i = 0; i < variables.length(); i++) {
			temp = temp + variables[i];
			if (i < variables.length() - 1) {
				temp = temp + ",";
			}
		}
		return temp + ")";
	}
}