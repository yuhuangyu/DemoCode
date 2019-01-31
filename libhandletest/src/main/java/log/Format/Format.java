package log.Format;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2018/7/5.
 */

public class Format {
    private static final int TYPE_STR = 0;   //
    private static final int TYPE_SIGN = 1;  // 符号
    private static final int TYPE_METHOD = 2;  // 方法
    private static final int TYPE_PARAMETER = 3;
    private static final int TYPE_VALUE_STRING = 4;  // 参数 常量
    private static final int TYPE_VALUE_INT = 5;
    private static final int TYPE_VALUE_FLOAT = 6;
    private static final int TYPE_VALUE_BOOLEAN = 7;
    private static final int TYPE_VALUE_NULL = 8;
    private static final int TYPE_VALUE_OBJECT = 9;  // 参数 变量
    private int nowState = -1;
    private int preState = -1;
    private String json;
    private List<Tokens> tokens = new ArrayList<>();
    private Map<Integer, State> stateLists = new HashMap();

    private static Format format;
    private Format(String str) throws Exception {
        getToken(str);
    }
    public static Format get(String str) throws Exception {
        if (format ==null){
            synchronized (Format.class){
                if (format == null) {
                    format = new Format(str);
                }
            }
        }
        return format;
    }


    public String getformat( Map<Object, Object> map, FConfig fConfig) throws Exception {
//        getToken(str);
        return parse(map, fConfig);
    }

    private String parse(Map<Object, Object> map, FConfig fonfig) throws Exception {
        boolean isformat = false;
        StringBuilder strb = new StringBuilder("");
        String methodName = "";
        int parseNum = 0;
        List<Object> parameterList = new ArrayList<>();
        while (parseNum < tokens.size()){
            int type = tokens.get(parseNum).type;
            Object obj = tokens.get(parseNum).value;
            parseNum++;
            switch (type){
                case TYPE_STR:
                    if (isformat) {
                        throw new Exception("err : current type "+type+" obj: "+obj);
                    }
                    strb.append(parseStart(obj));
                    break;
                case TYPE_SIGN:
                    char ch = (char) obj;
                    if (ch == '$') {
                        isformat = true;
                        methodName = "";
                    }else if (ch == '{') {
                        parameterList = new ArrayList<>();
                    }else  if (ch == '}') {
                        strb.append(parseKey(methodName,parameterList, fonfig));
                        isformat = false;
                    }
                    break;
                case TYPE_METHOD:
                    methodName = (String) obj;
                    break;
                case TYPE_VALUE_OBJECT:
                    parameterList.add(map.get(obj));
                    break;
                case TYPE_VALUE_STRING:
                case TYPE_VALUE_INT:
                case TYPE_VALUE_FLOAT:
                case TYPE_VALUE_BOOLEAN:
                case TYPE_VALUE_NULL:
                    parameterList.add(obj);
                    break;
                default:
                    break;
            }
        }
        if (isformat) {
            throw new Exception("err : end error  ");
        }
        return strb.toString();
    }

    private Object parseStart(Object obj) {
        return obj;
    }

    private Object parseKey(String methodName, List<Object> parameterList, FConfig fConfig) throws Exception {
        Class[] classes = null;
        Object[] objects = null;
        if (methodName != null && methodName.length() == 0) {
            methodName = "mDefault";
            if (parameterList.size() == 1) {
                classes = new Class[]{Object.class};
                objects = new Object[]{parameterList.get(0)};
//                System.out.println("Configuration Class  "+ fConfig +" ,methodName "+methodName+"  ==  "+parameterList.get(0));
                return methodInvoke(methodName, fConfig, classes, objects);
            }
        }

//        System.out.println("Configuration Class  "+ fConfig +" ,methodName "+methodName);
        if (parameterList.size() == 0) {
            classes = new Class[]{};
            objects = new Object[]{};
        }else {
            classes = new Class[parameterList.size()];
            objects = new Object[parameterList.size()];
            for (int i = 0; i < parameterList.size(); i++) {
                classes[i] = parameterList.get(i).getClass();
                objects[i] = parameterList.get(i);
//                System.out.println("classes "+classes[i]+" ,objects "+objects[i]);
            }
        }
        return methodInvoke(methodName, fConfig, classes, objects);
    }

    private Object methodInvoke(String methodName, FConfig fConfig, Class[] classes, Object[] objects) throws Exception {
        Object invoke = invoke(fConfig, methodName, classes, objects);
//        System.out.println("invoke "+invoke);
        return invoke;
    }

    public <T> T invoke(Object host, String name, Class<?>[] clsArray, Object... pArray) throws Exception {
        if (host == null)
            throw new NullPointerException("host is null");

        Class<?> cls = host.getClass();
        while (true) {
            if (cls == null)
                throw new NoSuchMethodException("class "+host+ " not find method "+name);

            try {
                return invoke(cls, name, clsArray, host, pArray);
            } catch (Exception e) {
                cls = cls.getSuperclass();
            }
        }

    }

    public <T> T invoke(Class<?> cls, String name, Class<?>[] clsArray, Object host, Object... pArray) throws NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Method method = cls.getDeclaredMethod(name, clsArray);
        method.setAccessible(true);
        return (T) method.invoke(host, pArray);
    }


    private void getToken(String str) throws Exception {
        this.json = str;
        tokens = new ArrayList<>();

        addState(TYPE_STR,new StartState());
        addState(TYPE_METHOD,new MethodState());
        addState(TYPE_PARAMETER,new ParameterState());
        addState(TYPE_VALUE_OBJECT,new ValueObjectState());
        addState(TYPE_VALUE_STRING,new ValueStringState());
        addState(TYPE_VALUE_INT,new ValueIntState());
        addState(TYPE_VALUE_FLOAT,new ValueFloatState());
        addState(TYPE_VALUE_BOOLEAN,new ValueBooleanState());
        addState(TYPE_VALUE_NULL,new ValueNullState());
        nowState = TYPE_STR;

        int index = 0;
        while (str.length() > index){
            char c = str.charAt(index);
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
//            System.out.println("Tokens :  type "+type+" ,value "+value);
        }
    }

    private abstract class State {

        protected abstract void init(int index, char c) throws Exception;

        protected abstract int getInitialPosition();//记录位置
    }

    private class StartState extends State{
        int initialPosition = 0;

        @Override
        protected void init(int index, char c) throws Exception{
            switch (c){
                case '$':
                    this.initialPosition = index;
                    nowState = TYPE_METHOD;
                    preState = TYPE_STR;
                    tokens.add(new Tokens(TYPE_SIGN,c));
                    break;
                default:
                    tokens.add(new Tokens(TYPE_STR,c));
                    break;
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class MethodState extends State{
        int initialPosition = 0;

        @Override
        protected void init(int index, char c) throws Exception{
            if (c == '{') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                if (prePosition+1 == index) {
                    tokens.add(new Tokens(TYPE_METHOD,""));
                }else {
                    tokens.add(new Tokens(TYPE_METHOD,json.substring(prePosition+1,index)));
                }

                this.initialPosition = index;
                nowState = TYPE_PARAMETER;
                preState = TYPE_METHOD;

                tokens.add(new Tokens(TYPE_SIGN,c));
            }else if (c == '$' || c == '}' || c == '"') {
                throw new Exception("err : current char "+c+" index: "+index);
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class ParameterState extends State{
        int initialPosition = 0;

        @Override
        protected void init(int index, char c) throws Exception{
            if (c == '}') {
                nowState = TYPE_STR;
                preState = TYPE_PARAMETER;
                initialPosition = index;
                tokens.add(new Tokens(TYPE_SIGN,c));
            }else if (c == '@') {
                nowState = TYPE_VALUE_OBJECT;
                preState = TYPE_PARAMETER;
                initialPosition = index;
            }else if (c == '"') {
                nowState = TYPE_VALUE_STRING;
                preState = TYPE_PARAMETER;
                initialPosition = index;
            }else if ('0' <= c && c <= '9') {
                nowState = TYPE_VALUE_INT;
                preState = TYPE_PARAMETER;
                initialPosition = index;
            }else if (c == 't' || c == 'f') {
                nowState = TYPE_VALUE_BOOLEAN;
                preState = TYPE_PARAMETER;
                initialPosition = index;
            }else if (c == 'n') {
                nowState = TYPE_VALUE_NULL;
                preState = TYPE_PARAMETER;
                initialPosition = index;
            }else if (c == ' ' || c == ',') {

            } else {
                throw new Exception("err : current char "+c+" index: "+index);
            }
        }

        @Override
        protected int getInitialPosition() {
            return initialPosition;
        }
    }

    private class ValueObjectState extends State{
        int initialPosition = 0;
        @Override
        protected void init(int index, char c) throws Exception{
            if (c == ',' || c == '}' || c == ' ') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                if (prePosition+1 == index) {
                    throw new Exception("err : @Object is null "+c+" index: "+index);
                }else {
                    tokens.add(new Tokens(TYPE_VALUE_OBJECT,json.substring(prePosition+1,index)));
                }
                if (c == ',' || c == ' ') {
                    nowState = TYPE_PARAMETER;
                }else {
                    nowState = TYPE_STR;
                    tokens.add(new Tokens(TYPE_SIGN,c));
                }
                preState = TYPE_VALUE_OBJECT;
                initialPosition = index;
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
            if (c == '"') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                if (prePosition+1 == index) {
                    tokens.add(new Tokens(TYPE_VALUE_STRING,""));
                }else {
                    tokens.add(new Tokens(TYPE_VALUE_STRING,json.substring(prePosition+1,index)));
                }

                nowState = TYPE_PARAMETER;
                preState = TYPE_VALUE_STRING;
                initialPosition = index;
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
            if ('0' <= c && c <= '9') {

            }else if (c == '.') {
                nowState = TYPE_VALUE_FLOAT;
            }else if (c == ',' || c == '}' || c == ' ') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                int value = Integer.parseInt(json.substring(prePosition, index));
                tokens.add(new Tokens(TYPE_VALUE_INT,value));

                if (c == ',' || c == ' ') {
                    nowState = TYPE_PARAMETER;
                }else {
                    nowState = TYPE_STR;
                    tokens.add(new Tokens(TYPE_SIGN,c));
                }
                preState = TYPE_VALUE_INT;
                initialPosition = index;
            }else {
                throw new Exception("err : int "+c+" index: "+index);
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
            if ('0' <= c && c <= '9') {

            }else if (c == '.') {
                throw new Exception("err : Float "+c+" index: "+index);
            }else if (c == ',' || c == '}' || c == ' ') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                double value = Double.parseDouble(json.substring(prePosition, index));
                tokens.add(new Tokens(TYPE_VALUE_FLOAT,value));

                if (c == ',' || c == ' ') {
                    nowState = TYPE_PARAMETER;
                }else {
                    nowState = TYPE_STR;
                    tokens.add(new Tokens(TYPE_SIGN,c));
                }
                preState = TYPE_VALUE_FLOAT;
                initialPosition = index;
            }else {
                throw new Exception("err : Float "+c+" index: "+index);
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
            if ("falsetrue".contains(c+"")) {

            }else if (c == ',' || c == '}' || c == ' '){
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                String subNull = json.substring(prePosition, index);
                if (!"false".equals(subNull) && !"true".equals(subNull)) {
                    throw new Exception("err : value boolean "+subNull+" index: "+index);
                }
                boolean value = Boolean.parseBoolean(subNull);
                tokens.add(new Tokens(TYPE_VALUE_BOOLEAN,value));

                if (c == ',' || c == ' ') {
                    nowState = TYPE_PARAMETER;
                }else {
                    nowState = TYPE_STR;
                    tokens.add(new Tokens(TYPE_SIGN,c));
                }
                preState = TYPE_VALUE_BOOLEAN;
                initialPosition = index;
            }else {
                throw new Exception("err : boolean "+c+" index: "+index);
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
            if ("null".contains(c+"")) {

            }else if (c == ',' || c == '}' || c == ' ') {
                State pre = getState(preState);
                int prePosition = pre.getInitialPosition();
                String subNull = json.substring(prePosition, index);
                if (!"null".equals(subNull)) {
                    throw new Exception("err : value null "+subNull+" index: "+index);
                }
                tokens.add(new Tokens(TYPE_VALUE_NULL,(Object) subNull));

                if (c == ',' || c == ' ') {
                    nowState = TYPE_PARAMETER;
                }else {
                    nowState = TYPE_STR;
                    tokens.add(new Tokens(TYPE_SIGN,c));
                }
                preState = TYPE_VALUE_NULL;
                initialPosition = index;
            }else {
                throw new Exception("err : null "+c+" index: "+index);
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

}
