package com.example.MathCalculate;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by anye6488 on 2017/7/5.
 * 注释暂时没有,BUG应该会有
 * 请耐心细心用心体会 ╮(╯▽╰)╭
 */

public class MathMain
{
    public static final char Add = '+';
    public static final char Sub = '-';
    public static final char Adv = '*';
    public static final char Dev = '/';
    public static final char Left = '(';
    public static final char Right = ')';


    public abstract class Expression
    {
        public abstract double value();
    }

    public abstract class BinaryExpression extends Expression
    {
        private double _left;
        private double _right;
        private int _priority;

        public double left()
        {
            return _left;
        }

        public void left(double _left)
        {
            this._left = _left;
        }

        public double right()
        {
            return _right;
        }

        public void right(double _right)
        {
            this._right = _right;
        }
    }

    public static double calculate(String exp)
    {
        Queue<Object> expList = expression(exp);

        System.out.println(expList);

        Stack<Double> value = new Stack<>();
        while (!expList.isEmpty())
        {
            Object v = expList.poll();
            if (v instanceof Double)
                value.push((double) v);
            else if (v.equals(Add))
            {
                double right = value.pop();
                double left = value.pop();
                value.push(left + right);
            }
            else if (v.equals(Sub))
            {
                double right = value.pop();
                double left = value.pop();
                value.push(left - right);
            }
            else if (v.equals(Adv))
            {
                double right = value.pop();
                double left = value.pop();
                value.push(left * right);
            }
            else if (v.equals(Dev))
            {
                double right = value.pop();
                double left = value.pop();
                value.push(left / right);
            }

            System.out.println(value);
        }
        return value.pop();
    }

    private static Queue<Object> expression(String exp)
    {
        final Queue<Object> number = new LinkedList<Object>();
        final Stack<Character> op = new Stack<Character>();

        parse(exp, new MathResult()
        {
            @Override
            public void onNumber(double value)
            {
                number.add(value);
                System.out.println(number);
                System.out.println(op);
                System.out.println();
            }

            @Override
            public void onOperation(char type)
            {
                if (op.isEmpty())
                    op.push(type);
                else
                {
                    if (type == '-' || type == '+')
                    {
                        while (true)
                        {
                            if (op.isEmpty() || op.peek() == '(')
                            {
                                op.push(type);
                                break;
                            }
                            else
                            {
                                number.add(op.pop());
                            }
                        }
                    }
                    else if (type == '(')
                        op.push(type);
                    else if (type == ')')
                    {
                        while (true)
                        {
                            if (op.peek() == '(')
                            {
                                op.pop();
                                break;
                            }
                            else
                            {
                                number.add(op.pop());
                            }
                        }
                    }
                    else if (type == '*' || type == '/')
                    {
                        while (true)
                        {
                            if (op.isEmpty() || op.peek() == '(' || op.peek() == '+' || op.peek() == '-')
                            {
                                op.push(type);
                                break;
                            }
                            else
                            {
                                number.add(op.pop());
                            }
                        }
                    }
                }

                System.out.println(number);
                System.out.println(op);
                System.out.println();
            }
        });

        while (!op.isEmpty())
            number.add(op.pop());

        return number;
    }

    public interface MathResult
    {
        void onNumber(double value);

        void onOperation(char type);
    }

    static final int STATE_NONE = 0;
    static final int STATE_INT = 1;
    static final int STATE_REAL = 2;


    private static Character isOp(char c)
    {
        if (c == '(')
            return Left;
        else if (c == ')')
            return Right;
        else if (c == '+')
            return Add;
        else if (c == '-')
            return Sub;
        else if (c == '*')
            return Adv;
        else if (c == '/')
            return Dev;
        else
            return null;
    }

    private static void parse(String exp, MathResult result)
    {
        int index = 0;
        int state = 0;
        double value = 0;
        double bit = 10;
        while (exp.length() > index)
        {
            char c = exp.charAt(index);

            if (state == STATE_NONE)
            {
                bit = 10;
                Character type = isOp(c);
                if (type != null)
                {
                    result.onOperation(type);
                }
                else if (c >= '0' && c <= '9')
                {
                    value = c - '0';
                    state = STATE_INT;
                }
            }
            else if (state == STATE_INT)
            {
                Character type;
                if (c >= '0' && c <= '9')
                {
                    value = value * 10 + c - '0';
                }
                else if (c == '.')
                {
                    state = STATE_REAL;
                }
                else if ((type = isOp(c)) != null)
                {
                    result.onNumber(value);
                    value = 0;
                    result.onOperation(type);
                    state = STATE_NONE;
                }
            }
            else if (state == STATE_REAL)
            {
                Character type;
                if (c >= '0' && c <= '9')
                {
                    value = value + (c - '0') / bit;
                    bit *= 10;
                }
                else if ((type = isOp(c)) != null)
                {
                    result.onNumber(value);
                    value = 0;
                    result.onOperation(type);
                    state = STATE_NONE;
                }

            }

            index++;
        }
    }

    public static void main(String[] args)
    {
        System.out.print(calculate("0.5+3.7*(20.2*5-10)"));
    }
}
