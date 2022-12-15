package aoc2022;


import java.util.*;

interface Data {

}

record Value(int value) implements Data {
}

record Array(List<Data> array) implements Data {
}

public class StringReader {

    public static final String OPEN_ARRAY = "[";
    public static final String CLOSE_ARRAY = "]";
    public static final String COMMA = ",";
    public static final Set<String> SPECIAL_TOKENS = Set.of(OPEN_ARRAY, CLOSE_ARRAY, COMMA);

    public static Array readString(String input) {
        Stack<Array> stack = new Stack<>();
        StringBuilder val = new StringBuilder();
        for (String c : input.split("")) {
            if (c.equals(" ")) {
                continue;
            }
            if (SPECIAL_TOKENS.contains(c) && !val.isEmpty()) {
                Array array = stack.pop();
                array.array().add(new Value(Integer.parseInt(val.toString().trim())));
                stack.push(array);
                val = new StringBuilder();
            }
            if (c.equals(OPEN_ARRAY)) {
                Array array = new Array(new ArrayList<>());
                if (!stack.isEmpty()) {
                    Array parentArray = stack.pop();
                    parentArray.array().add(array);
                    stack.push(parentArray);
                }
                stack.push(array);
                continue;
            }
            if (stack.size() > 1 && c.equals(CLOSE_ARRAY)) {
                stack.pop();
                continue;
            }
            if (!SPECIAL_TOKENS.contains(c)) {
                val.append(c);
            }
        }
        return stack.pop();
    }

    public static void main(String[] args) {
//        System.err.println(readString("[]"));
//        System.err.println(readString("[1]"));
//        System.err.println(readString("[11]"));
//        System.err.println(readString("[  12   , 15]"));
//        System.err.println(readString("[ [1,1]   , 15]"));
        Array x = readString("[ [1,[2]], [[[4,5]]], 8]");
        System.err.println(x.array());
    }
}
