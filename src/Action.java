import java.util.Dictionary;

public class Action {
	private String name;
	private Condition[] preconditions;
	private Condition[] add;
	private Condition[] delete;

    public Action(String name,
            String[] values,
            Condition[] preconditions,
            Condition[] add,
            Condition[] delete) {
    	this.name = name;
    	this.preconditions = preconditions;
    	this.add = add;
    	this.delete = delete;
    }
    
	public String getName() {
		return this.name;
	}

	public Condition[] getPreconditions() {
		return this.preconditions;
	}

	public Condition[] getAdd() {
		return this.add;
	}

	public Condition[] getDelete() {
		return this.delete;
	}
}
