package com.example.MathMaze;

import java.util.ArrayList;
import java.util.List;



public class PathMain2
{
    private final static char[][] map = new char[][]
            {
                    {'S', '*', '*', '*', '#', '*'},
                    {'#', '#', '#', '*', '*', '*'},
                    {'*', '*', '*', '*', '#', '#'},
                    {'#', '*', '#', '*', '*', '*'},
                    {'*', '*', '*', '*', '*', 'E'}
            };
    private char[][] map2 = new char[][]
            {
                    {'S', '*', '*', '*', '#', '*'},
                    {'#', '#', '#', '*', '*', '*'},
                    {'*', '*', '*', '*', '#', '#'},
                    {'#', '*', '#', '*', '*', '*'},
                    {'*', '*', '*', '*', '*', 'E'}
            };
    public static void main(String[] args)
    {

        new PathMain2().backtrace(new Point(0,0));
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
    private List<Point> listPoint = new ArrayList<>();

    public void backtrace(Point point){

        if (isExcepetedGoal(point)) {
            System.out.println("================="+"   "+listPoint.size());
            printAllGoal();
            return;
        }
        List<Point> nextGoal = findNextGoal(point);
        for (Point i : nextGoal) {
            handle(point);
            mark(i);
            backtrace(i);
            unmark(i);
        }
    }


    private void unmark(Point point) {
        listPoint.remove(point);
    }

    private void mark(Point point) {
        listPoint.add(point);
    }

    private void handle(Point point) {

    }

    private List<Point> findNextGoal(Point p) {
        List<Point> nextPoints = new ArrayList<>();
        for (Point point : Dir)
        {
            int tx = p.x + point.x;
            int ty = p.y + point.y;
            if (tx >= 0 && tx < 5 && ty >= 0 && ty < 6 && map[tx][ty] != '#') {
                if (!isInGroup(tx,ty)) {
                    nextPoints.add(new Point(tx,ty));
                }
            }
        }
        return nextPoints;
    }

    private boolean isInGroup(int tx, int ty) {
        for (int i = 0; i < listPoint.size(); i++) {
            if (listPoint.get(i).x == tx && listPoint.get(i).y == ty) {
                return true;
            }
        }
        return false;
    }

    private void printAllGoal() {
        System.out.println(listPoint);
        for (Point p : listPoint)
            if (p.x != 0 || p.y != 0)
                map2[p.x][p.y] = '0';

        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 6; y++)
            {
                System.out.print(map2[x][y]);
                if (y == 5)
                    System.out.println();
            }

        map2 = new char[][]
                {
                        {'S', '*', '*', '*', '#', '*'},
                        {'#', '#', '#', '*', '*', '*'},
                        {'*', '*', '*', '*', '#', '#'},
                        {'#', '*', '#', '*', '*', '*'},
                        {'*', '*', '*', '*', '*', 'E'}
                };
    }

    private boolean isExcepetedGoal(Point point) {

        return map[point.x][point.y] == 'E';
//        return "E".equals(map[point.x][point.y]+"");
    }
}
