class Value {
    public static enum Type { ADD, REMOVE, EQUALS };
    private int value;
    private Type type;

    public Value(int value) {
        this(value, Type.EQUALS);
    }

    public Value(Value other) {
        this.value = other.value;
        this.type = other.type;
    }

    public Value(int initial, Type type) {
        this.value = initial;
        this.type = type;
    }

    public int get() {
        return value;
    }

    public int set(int newvalue) {
        switch (type) {
            case ADD:
                value += newvalue;
                return value;
            case REMOVE:
                value -= newvalue;
                return value;
            case EQUALS:
                value = newvalue;
                return value;
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type newtype) {
        type = newtype;
    }

    public boolean equals(integer i) {
        return this.value == i;
    }

    public boolean equals(Value v) {
        return this.value == v.value &&
            this.type == v.type;
    }

    public int compareTo(Value v) {
        return this.value - v.value;
    }
}
