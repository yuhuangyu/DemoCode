package com.example.MathCalculate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anye6488 on 2017/7/12.
 * 注释暂时没有,BUG应该会有
 * 请耐心细心用心体会 ╮(╯▽╰)╭
 */

public class Math2Main
{
    public static void main(String[] args)
    {
        double d = new Math2Main().calculate("12+3*((13+10-20)+(9+11))",new HashMap<String, Double>());
        System.out.print(d);
    }

    public interface Operator
    {
        char label();

        int power();

        double calculate(double left, double right);
    }


    private Map<Character, Operator> _op = new HashMap<Character, Operator>()
    {
        {
            put('+', new Operator()
            {
                @Override
                public char label()
                {
                    return '+';
                }

                @Override
                public int power()
                {
                    return 1;
                }

                @Override
                public double calculate(double left, double right)
                {
                    return left + right;
                }
            });

            put('-', new Operator()
            {
                @Override
                public char label()
                {
                    return '-';
                }

                @Override
                public int power()
                {
                    return 1;
                }

                @Override
                public double calculate(double left, double right)
                {
                    return left - right;
                }
            });

            put('*', new Operator()
            {
                @Override
                public char label()
                {
                    return '*';
                }

                @Override
                public int power()
                {
                    return 2;
                }

                @Override
                public double calculate(double left, double right)
                {
                    return left * right;
                }
            });

            put('/', new Operator()
            {
                @Override
                public char label()
                {
                    return '/';
                }

                @Override
                public int power()
                {
                    return 2;
                }

                @Override
                public double calculate(double left, double right)
                {
                    return left / right;
                }
            });
        }
    };

    public double calculate(String exp, Map<String, Double> cache)
    {
        if (cache.containsKey(exp))
            return cache.get(exp);

        int minIndex = -1;
        int minOp = 1000;
        Operator op = null;
        int _state = 0;

        for (int i = 0; i < exp.length(); i++)
        {
            if (exp.charAt(i) == '(')
            {
                _state++;
            }
            else if (exp.charAt(i) == ')')
            {
                _state--;
            }

            if (_state > 0)
                continue;

            Operator o = _op.get(exp.charAt(i));
            if (o != null)
            {
                if (minOp >= o.power())
                {
                    minOp = o.power();
                    minIndex = i;
                    op = o;
                }
            }
        }

        if (exp.startsWith("(") && exp.endsWith(")") && minIndex == -1)
        {
            double res = calculate(exp.substring(1, exp.length() - 1), cache);
            cache.put(exp, res);
            return res;
        }

        if (op != null)
        {
            double left = calculate(exp.substring(0, minIndex), cache);
            double right = calculate(exp.substring(minIndex + 1, exp.length()), cache);
            System.out.println("" + left + op.label() + right);
            double res = op.calculate(left, right);
            cache.put(exp, res);
            return res;
        }
        else
        {
            double res = Double.parseDouble(exp);
            cache.put(exp, res);
            return res;
        }
    }
}
