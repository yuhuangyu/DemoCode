package com.example.MathMaze;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anye6488 on 2017/7/19.
 * 注释暂时没有,BUG应该会有
 * 请耐心细心用心体会 ╮(╯▽╰)╭
 */

public class PathMain
{
    private final static char[][] map = new char[][]
            {
                    {'S', '*', '*', '*', '#', '*'},
                    {'#', '#', '#', '*', '*', '*'},
                    {'*', '*', '*', '*', '#', '#'},
                    {'#', '*', '#', '#', '*', '*'},
                    {'*', '*', '*', '*', '*', 'E'}
            };

    public static void main(String[] args)
    {
        List<Point> list = new ArrayList<>();
        boolean res = findPtah(map, 0, 0, 5, 6, list, new boolean[5][6]);
        if (res)
            System.out.println(list);

        for (Point p : list)
            if (p.x != 0 || p.y != 0)
                map[p.x][p.y] = '0';

        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 6; y++)
            {
                System.out.print(map[x][y]);
                if (y == 5)
                    System.out.println();
            }
    }

    public static class Point
    {
        public int x;
        public int y;

        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString()
        {
            return "[" + x + "," + y + "]";
        }
    }

    private static Point[] Dir = new Point[]{new Point(1, 0), new Point(-1, 0), new Point(0, 1), new Point(0, -1)};

    private static boolean findPtah(char[][] map, int x, int y, int xLimit, int yLimit, List<Point> list, boolean[][] sgin)
    {
        sgin[x][y] = true;
        System.out.println(x + "," + y);
        if (map[x][y] == 'E')
            return true;

        list.add(new Point(x, y));

        for (Point point : Dir)
        {
            int tx = x + point.x;
            int ty = y + point.y;
            if (tx >= 0 && tx < xLimit && ty >= 0 && ty < yLimit && !sgin[tx][ty] && map[tx][ty] != '#')
            {
                if (findPtah(map, tx, ty, xLimit, yLimit, list, sgin))
                    return true;
            }
        }

        return false;
    }
}
