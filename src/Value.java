class Value {
    public static enum Type { ADD, REMOVE, EQUALS, CONSTANT };
    private String name;
    private int value;
    private Type type;

    public Value(String name, int initial, Type type) {
        this.name = name;
        this.value = initial;
        this.type = type;
    }

    public Value(int value) {
        this(null, value, Type.EQUALS);
    }

    public Value(String name) {
        this(name, 0, Type.EQUALS);
    }

    public Value(String name, Type type) {
        this(name, 0, type);
    }
    
    public Value(String name, int value) {
        this(name, value, Type.EQUALS);
    }

    public Value(Value other) {
        this(other.name, other.value, other.type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int updateValue(Value value) {
        if (this.type == Type.CONSTANT) {
            return -1;
        }
        switch (value.type) {
            case ADD:
                this.value += value.value;
                return value;
            case REMOVE:
                this.value -= value.value;
                return value;
            case EQUALS:
                this.value = value.value;
                return value;
            default:
                return -1;
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type newtype) {
        type = newtype;
    }

    public boolean equals(int i) {
        return this.value == i;
    }

    public boolean equals(Value v) {
        return this.value == v.value;
    }

    public int compareTo(Value v) {
        return this.value - v.value;
    }

    @Override
    public String toString() {
        String modifier = (type == Type.ADD)      ? "+" :
                          (type == Type.REMOVE)   ? "-" :
                          (type == Type.CONSTANT) ? "$" :
                                                    "";
        if (name == null) {
            return modifier + value;
        } else {
            return "[" + modifier + name + ", " + value + "]";
        }
    }
}
