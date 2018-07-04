package com.example;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by LG on 2018/6/29.
 * You have to run so hard to keep the place
 */

public class MachineTwo {

    private String contextValue = "";
    private int index = 0;
    private int currentState = -1;
    private char[] jsonChar;
    private List<Token>tokens = new ArrayList<>();
    private static final int TYPE_START = 0;
    private static final int TYPE_LEFT_BRACE = -1;
    private static final int TYPE_RIGHT_BRACE = -2;
    private static final int TYPE_DOT = -3;
    private static final int TYPE_COLON = -4;
    private static final int TYPE_LEFT_BRACKET = -5;
    private static final int TYPE_RIGHT_BRACKET = -6;
    private static final int TYPE_NUMBER = 1;
    private static final int TYPE_STRING = 2;
    private static final int TYPE_FLOAT = 3;
    private static final int TYPE_SPECIAL = 4;
    private static final int TYPE_ERROR = 10;


    public MachineTwo(String json) {
        jsonChar = json.toCharArray();
        currentState = 0;
        index = 0;
        contextValue = "";
    }

    //词法解析
    public List<Token> parse() {
        for (; index < jsonChar.length; index++) {
            switch (currentState) {
                case TYPE_START:
                    startState(jsonChar[index]);
                    break;
                case TYPE_NUMBER:
                    numberState(jsonChar[index]);
                    break;
                case TYPE_STRING:
                    stringState(jsonChar[index]);
                    break;
                case TYPE_FLOAT:
                    floatState(jsonChar[index]);
                    break;
                case TYPE_SPECIAL:
                    specialState(jsonChar[index], jsonChar.length);
                    break;
                case TYPE_ERROR:
                    errorState();
                    break;
            }
        }

        if (currentState != TYPE_START && index < jsonChar.length) {
            contextValue = "The last state can not be TYPE_START";
            errorState();
        }

        return tokens;
    }

    private void startState(char curValue) {
        if (curValue >= '0' && curValue <= '9') {
            currentState = TYPE_NUMBER;
            --index;
        } else if (curValue == '{' || curValue == '}'
                || curValue == ':' || curValue == ','
                || curValue == '[' || curValue == ']') {
            int type = -1;
            switch (curValue) {
                case '{':
                    type = TYPE_LEFT_BRACE;
                    break;
                case '}':
                    type = TYPE_RIGHT_BRACE;
                    break;
                case ':':
                    type = TYPE_COLON;
                    break;
                case ',':
                    type = TYPE_DOT;
                    break;
                case '[':
                    type = TYPE_LEFT_BRACKET;
                    break;
                case ']':
                    type = TYPE_RIGHT_BRACKET;
                    break;
            }
           tokens.add(new Token(type, curValue));
            contextValue = "";
        } else if (curValue == '"') {
            currentState = TYPE_STRING;
        } else if (curValue == 'n' || curValue == 't' || curValue == 'f') {
            currentState = TYPE_SPECIAL;
            index--;
        } else if (curValue != ' ' && curValue != '\n' && curValue != '\t') {
            currentState = TYPE_ERROR;
            contextValue = "current state is startState can not match the next state";
            index--;
        }
    }

    private void numberState(char curValue) {
        if (curValue >= '0' && curValue <= '9') {
            contextValue = contextValue + curValue;
        } else if (curValue == '{' || curValue == '}'
                || curValue == ':' || curValue == ',' || curValue == '"'
                || curValue == '[' || curValue == ']' ||
                curValue == ' ' || curValue == '\n' || curValue == '\t') {
            currentState = TYPE_START;
           tokens.add(new Token(TYPE_NUMBER, contextValue));
            contextValue = "";
            --index;
        } else if (curValue == '.') {
            currentState = TYPE_FLOAT;
            contextValue = contextValue + curValue;
        } else {
            currentState = TYPE_ERROR;
            contextValue = "current state is numberState , wrong number";
            index--;
        }
    }

    private void stringState(char curValue) {
        if (curValue == '"') {
           tokens.add(new Token(TYPE_STRING, contextValue));
            contextValue = "";
            currentState = TYPE_START;
        } else
            contextValue = contextValue + curValue;
    }

    private void floatState(char curValue) {
        if (curValue >= '0' && curValue <= '9')
            contextValue = contextValue + curValue;
        else {
            currentState = TYPE_START;
           tokens.add(new Token(TYPE_FLOAT, contextValue));
            contextValue = "";
            --index;
        }
    }

    private void specialState(char curValue, int jsonLength) {

        switch (curValue) {
            case 'n':
                if ((jsonLength - index) >= 4 && new String(jsonChar).substring(index, index + 4).equals("null")) {
                    index += 3;
                    currentState = TYPE_START;
                   tokens.add(new Token(TYPE_SPECIAL, null));
                } else {
                    currentState = TYPE_ERROR;
                    contextValue = "current state is specialState wrong specialValue null";
                    index--;
                }
                break;
            case 't':
                if ((jsonLength - index) >= 4 && new String(jsonChar).substring(index, index + 4).equals("true")) {
                    index += 3;
                    currentState = TYPE_START;
                   tokens.add(new Token(TYPE_SPECIAL, true));
                } else {
                    currentState = TYPE_ERROR;
                    contextValue = "current state is specialState wrong specialValue true";
                    index--;
                }
                break;
            case 'f':
                if ((jsonLength - index) >= 5 && new String(jsonChar).substring(index, index + 5).equals("false")) {
                    index += 4;
                    currentState = TYPE_START;
                   tokens.add(new Token(TYPE_SPECIAL, false));
                } else {
                    currentState = TYPE_ERROR;
                    contextValue = "current state is specialState wrong specialValue false";
                    index--;
                }
                break;
        }
    }

    private void errorState() {
        System.out.println(String.format("The %d char is error, value:%c, reason: %s", index, jsonChar[index], contextValue));
        index = jsonChar.length;
    }

    //语法解析
    private static final int JSON_TYPE_STATR = 0;
    private static final int JSON_TYPE_KEY = 1;
    private static final int JSON_TYPE_SEPARART = 2;
    private static final int JSON_TYPE_VALUE = 3;
    private static final int JSON_TYPE_END = 5;
    private static final int JSON_TYPE_ERROR = 6;

    private Stack<Object> stack = new Stack();
    private Object json;

    public Object toJson() {
        currentState = 0;
        contextValue = null;

        for (index = 0; index <tokens.size(); index++) {
            switch (currentState) {
                case JSON_TYPE_STATR:
                    jsonStartState();
                    break;
                case JSON_TYPE_KEY:
                    jsonKeyState();
                    break;
                case JSON_TYPE_SEPARART:
                    jsonSeparateState();
                    break;
                case JSON_TYPE_VALUE:
                    jsonValueState();
                    break;
                case JSON_TYPE_END:
                    jsonEndState();
                    break;
                case JSON_TYPE_ERROR:
                    jsonErrorState();
                    break;
            }
        }

        if (!stack.isEmpty() && index ==tokens.size()) {
            contextValue = "json can not be finish";
            index--;
            jsonErrorState();
            json = null;
        }

        return json;
    }

    private void jsonStartState() {
        if (tokens.get(index).getType() == TYPE_LEFT_BRACE) {
            HashMap map = new HashMap<String, Object>();
            if (null == contextValue && stack.isEmpty())
                ;
            else if (HashMap.class.equals(stack.peek().getClass())) {
                ((HashMap) stack.peek()).put(contextValue, map);
                contextValue = null;
            } else
                ((ArrayList) stack.peek()).add(map);

            stack.push(map);

            currentState = JSON_TYPE_KEY;
        } else if (tokens.get(index).getType() == TYPE_LEFT_BRACKET) {
            ArrayList list = new ArrayList();

            if (null == contextValue && stack.isEmpty()) ;
            else if (HashMap.class.equals(stack.peek().getClass())) {
                ((HashMap) stack.peek()).put(contextValue, list);
                contextValue = null;
            } else
                ((ArrayList) stack.peek()).add(list);

            stack.push(list);

            currentState = JSON_TYPE_VALUE;
        } else {
            currentState = JSON_TYPE_ERROR;
            contextValue = "json format error,json need start with { | {";
        }
    }

    private void jsonKeyState() {
        int type =tokens.get(index).getType();
        if (type == TYPE_STRING) {
            contextValue = (String)tokens.get(index).getValue();
            currentState = JSON_TYPE_SEPARART;
        } else if (type == TYPE_RIGHT_BRACE &&tokens.get(index - 1).getType() == TYPE_LEFT_BRACE) {
            currentState = JSON_TYPE_END;
            index--;
        } else {
            currentState = JSON_TYPE_ERROR;
            contextValue = "the wrong jsonKey";
            index--;
        }
    }

    private void jsonSeparateState() {
        int type =tokens.get(index).getType();

        if (type == TYPE_COLON)
            currentState = JSON_TYPE_VALUE;
        else {
            currentState = JSON_TYPE_ERROR;
            contextValue = "the wrong jsonSeparate";
            index--;
        }
    }

    private void jsonValueState() {
        int type =tokens.get(index).getType();
        switch (type) {
            case TYPE_LEFT_BRACE:
            case TYPE_LEFT_BRACKET:
                currentState = JSON_TYPE_STATR;
                index--;
                break;
            case TYPE_RIGHT_BRACE:
            case TYPE_RIGHT_BRACKET:
                currentState = JSON_TYPE_END;
                index--;
                break;
            case TYPE_NUMBER:
            case TYPE_STRING:
            case TYPE_FLOAT:
            case TYPE_SPECIAL:
                if (HashMap.class.equals(stack.peek().getClass()))
                    ((HashMap) stack.peek()).put(contextValue,tokens.get(index).getValue());
                else
                    ((ArrayList) stack.peek()).add(tokens.get(index).getValue());
                currentState = JSON_TYPE_END;
                break;
            default:
                currentState = JSON_TYPE_ERROR;
                contextValue = "the wrong jsonValue";
                index--;
                break;
        }
    }

    private void jsonEndState() {
        int type =tokens.get(index).getType();
        if (type == TYPE_DOT) {
            if (HashMap.class.equals(stack.peek().getClass()))
                currentState = JSON_TYPE_KEY;
            else
                currentState = JSON_TYPE_VALUE;
        } else if (!stack.isEmpty() && ((type == TYPE_RIGHT_BRACE && HashMap.class.equals(stack.peek().getClass())) ||
                (type == TYPE_RIGHT_BRACKET && ArrayList.class.equals(stack.peek().getClass())))) {
            json = stack.pop();
        } else {
            json = null;
            currentState = JSON_TYPE_ERROR;
            contextValue = "the wrong jsonEnd";
            index--;
        }
    }

    private void jsonErrorState() {
        System.out.println(String.format("The %dToken is error, value:%s, reason: %s", index,tokens.get(index).getValue(), contextValue));
        index =tokens.size();
    }

    private class Token {
        private int type;
        private Object value;

        public int getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Token(int type, Object value){
            this.type = type;
            this.value = value;
//            System.out.println("tokens :  type "+type+" ,value "+value+"         valueFlag "+valueFlag);
        }
    }

    public static void main(String args[]) {
        String json = "{\n" +
                "    \"string\": \"bb\", \n" +
                "    \"emptyString\": \"\",  \n" +
                "    \"number\": 1222, \n" +
                "    \"float\": 66.88, \n" +
                "    \"nullKey\": null,\n" +
                "    \"false\":false,\n" +
                "    \"array\":[1, \"one\", null, true, {\"key\":\"VALUE\", \"kk\":[null, true, false]},[8, 8, 8]], \n" +
                "    \"true\":true,\n" +
                "    \"jsonObject\": {\n" +
                "    \"key\": \"value\",\n" +
                "    \"emptyJsonObject\": {}\n" +
                "  }" +
                "}";

        MachineTwo machineTwo = new MachineTwo(json);
        List<Token>tokens = machineTwo.parse();

        for (Token Token :tokens) {
            System.out.println("type:" +Token.getType() + "    value:" +Token.getValue());
        }

        Object myJson = machineTwo.toJson();

        System.out.println(myJson);
    }
}
