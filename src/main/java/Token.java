public class Token {
    String identifier;
    Type type;
    Object value;
    public Token(String identifier, Type type, String value) {
        this.identifier = identifier;
        this.type = type;
        this.value = convert(value);
    }

    public Object convert(String first) {
        if(type.equals(Type.DECIMAL)) {
            return Long.parseLong(first);
        }
        else if(type.equals(Type.BINARY)) {
            return Long.parseLong(first, 2);
        }
        else if(type.equals(Type.HEXADECIMAL)) {
            return Long.parseLong(first, 16);
        }
        else if(type.equals(Type.STRING)) {
            return first.substring(1, first.length() - 1);
        }
        else if(type.equals(Type.VOID)) {
            return "null";
        }
        return first;
    }

    public String toString() {
        return identifier + " = " + value.toString() + " :: " + type;
    }
}