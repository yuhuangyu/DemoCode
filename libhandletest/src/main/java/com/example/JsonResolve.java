package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2018/6/25.
 */

public class JsonResolve {
    private static final int TYPE_STR = 0;
    private static final int TYPE_KEY = 3;
    private static final int TYPE_VALUE = 4;


    private int nowState = -1;
    private int preState = -1;
    private boolean valueFlag = false;
    private String json;
    private List<Tokens> tokens = new ArrayList<>();
    private Map<Integer, State> stateLists = new HashMap();
    private int parseNum = 0;

    private class Tokens {
        private int type;
        private Object value;

        public Tokens(int type, Object value){
            this.type = type;
            this.value = value;
        }
    }


    public Map<String,Object> getJsonObject(String json) throws Exception{
        getTokens(json);
        return parseJsonObject();
    }

    private Map<String, Object> parseJsonObject() throws Exception {
        Map<String,Object> map = new HashMap<>();
        String key = null;
        Object value = null;
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
                    }
                    break;
                case TYPE_KEY:
                    key = (String) obj;
                    break;
                case TYPE_VALUE:
                    value = getRealValue(obj);
                    map.put(key,value);
                    break;
            }
        }
        return map;
    }

    private List<Tokens> getTokens(String json){

        if (json == null) {
            return null;
        }
        this.json = json;

        addState(TYPE_STR,new StartState());
        addState(TYPE_KEY,new KeyState());
        addState(TYPE_VALUE,new ValueState());
        nowState = TYPE_STR;

        int index = 0;
        while (json.length() > index){
            char c = json.charAt(index);
            State state = getState(nowState);
            state.init(index,c);

            index++;
        }

        return tokens;
    }

    private abstract class State {

        protected abstract void init(int Index, char c);

        protected abstract int getInitialPosition();//初始位置
    }

    private class StartState extends State{
        int initialPosition = 0;

        @Override
        protected void init(int index, char c) {
            this.initialPosition = index;
            switch (c){
                case '{':
                    tokens.add(new Tokens(TYPE_STR,c));
                    valueFlag = false;
                    break;
                case '}':
                    tokens.add(new Tokens(TYPE_STR,c));
                    break;
                case ' ':
                    break;
                case ',':
                    break;
                case ':':
                    valueFlag = true;
                    break;
                case '"':
                    if (valueFlag) {
                        nowState = TYPE_VALUE;
                        preState = TYPE_STR;
                    }else {
                        nowState = TYPE_KEY;
                        preState = TYPE_STR;
                    }
                    break;
                default:
                    if ('0' <= c && c < '9') {
                        nowState = TYPE_VALUE;
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
        protected void init(int index, char c) {
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

    private class ValueState extends State{
        int initialPosition = 0;

        @Override
        protected void init(int index, char c) {
            initialPosition = index;
            valueFlag = false;
            if (c == '{') {
                nowState = TYPE_STR;
                tokens.add(new Tokens(TYPE_STR,c));
            }else if (c == ',' || c == '}') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                tokens.add(new Tokens(TYPE_VALUE,json.substring(prePosition,index)));
                nowState = TYPE_STR;
                preState = TYPE_VALUE;
                if (c == '}') {
                    tokens.add(new Tokens(TYPE_STR,c));
                }
            }else {
                nowState = TYPE_VALUE;
            }
        }

        /*
        *
        *
        *

{
    "l1": {
        "l1_1": [
            "l1_1_1",
            "l1_1_2"
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
*/

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

    private Object getRealValue(Object str) throws Exception {
        String substring = getTrim(str);
        if (substring.charAt(0) == '"') {
            if (substring.charAt(substring.length()-1) != '"') {
                throw new Exception("err : value String "+substring);
            }
            substring = substring.substring(1,substring.length()-1);
            return substring;
        }else if (substring.contains(".")) {
            Float aFloat = null;
            try {
                aFloat = Float.parseFloat(substring);
            } catch (NumberFormatException e) {
                throw new Exception("err : value Float "+substring);
            }
            return aFloat;
        }else{
            int i = 0;
            try {
                i = Integer.parseInt(substring);
            } catch (NumberFormatException e) {
                throw new Exception("err : value Integer "+substring);
            }
            return i;
        }
    }

    private String getTrim(Object str) throws Exception {
        String strs = (String) str;
        if (strs == null || strs.length()==0) {
            throw new Exception("err : value null");
        }
        int start = 0;
        int end = strs.length()-1;
        for (int i = 0; i < strs.length(); i++) {
            if (strs.charAt(i) == ' ') {
                start = i+1;
            }else {
                break;
            }
        }
        for (int i = 0; i < strs.length(); i++) {
            if (strs.charAt(strs.length()-i-1) == ' ') {
                end = strs.length()-i-2;
            }else {
                break;
            }
        }
        String substring = strs.substring(start, end+1);
        return substring;
    }
}
