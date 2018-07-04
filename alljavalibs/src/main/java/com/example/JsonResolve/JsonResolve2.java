package com.example.JsonResolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by ASUS on 2018/6/25.
 */
/*
{
    "l1": {
        "l1_1": [
            "l1_1_1",
            "l1_1_2"],"l1_2": {"l1_2_1": 121}},"l2": {"l2_1": null,"l2_2": true,"l2_3": {}}}

{
    "l1": {
        "l1_1": [
            "l1_1_1",
            "l1_1_2",
            [111,"aaa"]
        ],
        "l1_2": {
            "l1_2_1": 121
        }
    },
    "l2": {
        "l2_1": null,
        "l2_2": true,
        "l2_3": {}
    }
}
* */


public class JsonResolve2 {
    private static final int TYPE_STR = 0;
    private static final int TYPE_KEY = 1;
    private static final int TYPE_VALUE = 2;
    private static final int TYPE_VALUE_STRING = 3;
    private static final int TYPE_VALUE_INT = 4;
    private static final int TYPE_VALUE_FLOAT = 5;
    private static final int TYPE_VALUE_BOOLEAN = 6;
    private static final int TYPE_VALUE_NULL = 7;

    private int nowState = -1;
    private int preState = -1;
    private boolean valueFlag = false;
    private String json;
    private List<Tokens> tokens = new ArrayList<>();
    private Map<Integer, State> stateLists = new HashMap();
    private int parseNum = 0;
    private Stack<Integer> objectOrArray = new Stack<>();  // 1 - object, 2 - Array


    public Map<String,Object> getJsonObject(String json) throws Exception{
        getTokens(json);
        return parseJsonObject();
    }


    // Resolve Object
    private Map<String, Object> parseJsonObject() throws Exception {
        Map<String,Object> map = new HashMap<>();
        String key = null;
        while (parseNum < tokens.size()){
            int type = tokens.get(parseNum).type;
            Object obj = tokens.get(parseNum).value;
            parseNum++;
            switch (type){
                case TYPE_STR:
                    char ch = (char) obj;
                    if (ch == '{') {
                        if (key != null) {
                            map.put(key,parseJsonObject());
                        }
                    }else if (ch == '}') {
                        return map;
                    }else if (ch == '[') {
                        map.put(key,getArray());
                    }else if (ch == ']') {

                    }
                    break;
                case TYPE_KEY:
                    key = (String) obj;
                    break;
                case TYPE_VALUE_STRING:
                case TYPE_VALUE_INT:
                case TYPE_VALUE_FLOAT:
                case TYPE_VALUE_BOOLEAN:
                case TYPE_VALUE_NULL:
                    map.put(key,obj);
                    break;
            }
        }
        return map;
    }
    // Resolve Array
    private List<Object> getArray() throws Exception {
        List<Object> list = new ArrayList<>();
        while (parseNum < tokens.size()){
            int type = tokens.get(parseNum).type;
            Object obj = tokens.get(parseNum).value;
            parseNum++;
            switch (type) {
                case TYPE_STR:
                    char ch = (char) obj;
                    if (ch == '{') {
                        list.add(parseJsonObject());
                    }else if (ch == '}') {

                    }else if (ch == '[') {
                        list.add(getArray());
                    }else if (ch == ']') {
                        return list;
                    }
                    break;
                case TYPE_KEY:
                    break;
                case TYPE_VALUE_STRING:
                case TYPE_VALUE_INT:
                case TYPE_VALUE_FLOAT:
                case TYPE_VALUE_BOOLEAN:
                case TYPE_VALUE_NULL:
                    list.add(obj);
                    break;

            }
        }
        return list;
    }

    // Resolve Tokens
    private void getTokens(String json) throws Exception {

        this.json = json;

        addState(TYPE_STR,new StartState());
        addState(TYPE_KEY,new KeyState());
        addState(TYPE_VALUE_STRING,new ValueStringState());
        addState(TYPE_VALUE_INT,new ValueIntState());
        addState(TYPE_VALUE_FLOAT,new ValueFloatState());
        addState(TYPE_VALUE_BOOLEAN,new ValueBooleanState());
        addState(TYPE_VALUE_NULL,new ValueNullState());
        nowState = TYPE_STR;

        int index = 0;
        while (json.length() > index){
            char c = json.charAt(index);
            State state = getState(nowState);
            state.init(index,c);

            index++;
        }
    }

    private class Tokens {
        private int type;
        private Object value;

        public Tokens(int type, Object value){
            this.type = type;
            this.value = value;
            System.out.println("Tokens :  type "+type+" ,value "+value+"         valueFlag "+valueFlag);
        }
    }

    private abstract class State {

        protected abstract void init(int index, char c) throws Exception;

        protected abstract int getInitialPosition();//初始位置
    }

    private class StartState extends State{
        int initialPosition = 0;

        @Override
        protected void init(int index, char c) throws Exception{
            this.initialPosition = index;
            switch (c){
                case '{':
                    objectOrArray.push(1);
                    tokens.add(new Tokens(TYPE_STR,c));
                    valueFlag = false;
                    break;
                case '}':
                    setToken(c);
                    break;
                case ' ':
                    break;
                case ',':
                    break;
                case '[':
                    objectOrArray.push(2);
                    tokens.add(new Tokens(TYPE_STR,c));
                    break;
                case ']':
                    setToken(c);
                    break;
                case ':':
                    valueFlag = true;
                    break;
                case '"':
                    if (valueFlag) {
                        nowState = TYPE_VALUE_STRING;
                        preState = TYPE_STR;
                    }else {
                        nowState = TYPE_KEY;
                        preState = TYPE_STR;
                    }
                    break;
                default:
                    if ('0' <= c && c <= '9') {
                        nowState = TYPE_VALUE_INT;
                        preState = TYPE_STR;
                    }else if (c == 't' || c == 'f') {
                        nowState = TYPE_VALUE_BOOLEAN;
                        preState = TYPE_STR;
                    }else if (c == 'n') {
                        nowState = TYPE_VALUE_NULL;
                        preState = TYPE_STR;
                    }
                    break;
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class KeyState extends State{
        int initialPosition = 0;

        @Override
        protected void init(int index, char c) throws Exception{
            initialPosition = index;
            if (c == '"') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                tokens.add(new Tokens(TYPE_KEY,json.substring(prePosition+1,index)));
                nowState = TYPE_STR;
                preState = TYPE_KEY;
            } else {
                nowState = TYPE_KEY;
            }

        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class ValueStringState extends State{
        int initialPosition = 0;
        @Override
        protected void init(int index, char c) throws Exception{
            initialPosition = index;
            setNowValueState();
            if (c == '"') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                if (prePosition+1 == index) {
                    tokens.add(new Tokens(TYPE_VALUE_STRING,""));
                }else {
                    tokens.add(new Tokens(TYPE_VALUE_STRING,json.substring(prePosition+1,index)));
                }
                nowState = TYPE_STR;
                preState = TYPE_VALUE_STRING;
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class ValueIntState extends State{
        int initialPosition = 0;
        @Override
        protected void init(int index, char c) throws Exception{
            initialPosition = index;
            setNowValueState();
            if ('0' <= c && c <= '9') {

            }else if (c == '.') {
                nowState = TYPE_VALUE_FLOAT;
            }else {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                int value = Integer.parseInt(json.substring(prePosition, index));
                tokens.add(new Tokens(TYPE_VALUE_INT,value));
                nowState = TYPE_STR;
                preState = TYPE_VALUE_INT;
                if (c == '}') {
                    setToken(c);
                }else if (c == ']') {
                    setToken(c);
                }
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class ValueFloatState extends State{
        int initialPosition = 0;
        @Override
        protected void init(int index, char c) throws Exception{
            initialPosition = index;
            setNowValueState();
            if ('0' <= c && c <= '9') {

            }else {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                float value = Float.parseFloat(json.substring(prePosition, index));
                tokens.add(new Tokens(TYPE_VALUE_FLOAT,value));
                nowState = TYPE_STR;
                preState = TYPE_VALUE_FLOAT;
                if (c == '}') {
                    setToken(c);
                }else if (c == ']') {
                    setToken(c);
                }
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class ValueBooleanState extends State{
        int initialPosition = 0;
        @Override
        protected void init(int index, char c) throws Exception {
            initialPosition = index;
            setNowValueState();
            if ("falsetrue".contains(c+"")) {

            }else {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                String subNull = json.substring(prePosition, index);
                if (!"false".equals(subNull) && !"true".equals(subNull)) {
                    throw new Exception("err : value boolean "+subNull+" index: "+index);
                }
                boolean value = Boolean.parseBoolean(subNull);
                tokens.add(new Tokens(TYPE_VALUE_BOOLEAN,value));
                nowState = TYPE_STR;
                preState = TYPE_VALUE_BOOLEAN;
                if (c == '}') {
                    setToken(c);
                }else if (c == ']') {
                    setToken(c);
                }
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class ValueNullState extends State{
        int initialPosition = 0;
        @Override
        protected void init(int index, char c) throws Exception {
            initialPosition = index;
            setNowValueState();
            if ("null".contains(c+"")) {

            }else {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                String subNull = json.substring(prePosition, index);
                if (!"null".equals(subNull)) {
                    throw new Exception("err : value null "+subNull+" index: "+index);
                }
                tokens.add(new Tokens(TYPE_VALUE_NULL,(Object) subNull));
                nowState = TYPE_STR;
                preState = TYPE_VALUE_NULL;
                if (c == '}') {
                    setToken(c);
                }else if (c == ']') {
                    setToken(c);
                }
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private void addState(int type, State state){
        stateLists.put(type, state);
    }

    private State getState(int type){
        return stateLists.get(type);
    }

    private void setNowValueState() {
        if (!objectOrArray.empty()) {
            if (objectOrArray.peek() == 1) {
                valueFlag = false;
            }else if (objectOrArray.peek() == 2) {
                valueFlag = true;
            }
        }else {
            System.out.println("over ... ");
        }
    }

    private void setToken(char c) {
        objectOrArray.pop();
        tokens.add(new Tokens(TYPE_STR,c));
        setNowValueState();
    }

}
