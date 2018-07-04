package com.test.huisuo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2018/6/25.
 */

public class JsonResolve {
    private static final int TYPE_STR = 0;
    private static final int TYPE_NUM = 1;
    private static final int TYPE_FLOAT = 2;
    private static final int TYPE_KEY = 3;
    private static final int TYPE_VALUE = 4;
    private static final int TYPE_JSON_ERR = 5;
    private int nowState = -1;
    private int preState = -1;
    private String json;
    private List<Tokens> tokens = new ArrayList<>();
    private Map<Integer, State> stateLists = new HashMap();
    private class Tokens {
        private int type;
        private Object value;

        public Tokens(int type, Object value){
            this.type = type;
            this.value = value;
        }
    }


    public JSONObject getJsonObject(String json) throws Exception{
        List<Tokens> tokens = getTokens(json);
        JSONObject jsonObject = new JSONObject();
//        Map<String,Object> map = new HashMap<>();
        String key = null;
        for (int i = 0; i < tokens.size(); i++) {
            Tokens tokens1 = tokens.get(i);
            if (tokens1.type == TYPE_KEY) {
                key = (String) tokens1.value;
            } else if (tokens1.type == TYPE_VALUE) {
                if (key != null) {
                    jsonObject.put(key,getRealValue(tokens1.value));
//                    map.put(key,getRealValue(tokens1.value));
                }else {
                    throw new Exception("err : Json Resolve "+tokens1.value);
                }
                key = null;
            }
        }
//        JSONArray
        return jsonObject;
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
                case '}':
                case ' ':
                case ',':
                    nowState = TYPE_STR;
                    break;
                case ':':
                    nowState = TYPE_VALUE;
                    preState = TYPE_STR;
                    break;
                case '"':
                    nowState = TYPE_KEY;
                    preState = TYPE_STR;
                    break;
                default:
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
            if (c == ',' || c == '}') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                tokens.add(new Tokens(TYPE_VALUE,json.substring(prePosition+1,index)));
                nowState = TYPE_STR;
                preState = TYPE_VALUE;
            }else {
                nowState = TYPE_VALUE;
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
                end = strs.length()-i;
            }else {
                break;
            }
        }
        String substring = strs.substring(start, end-1);
        return substring;
    }

}
