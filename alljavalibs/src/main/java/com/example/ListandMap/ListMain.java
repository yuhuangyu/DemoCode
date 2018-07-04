package com.example.ListandMap;

/**
 * Created by anye6488 on 2017/6/28.
 * 注释暂时没有,BUG应该会有
 * 请耐心细心用心体会 ╮(╯▽╰)╭
 */

public class ListMain
{
    public static class ArrayList
    {
        private final static int CAP = 10;

        private int _size = 0;
        private Object[] _values;

        public ArrayList()
        {
            _values = new Object[CAP];
        }

        public void add(Object value)
        {
            if (_size >= _values.length)
            {
                Object[] newArray = new Object[_values.length + CAP];
                System.arraycopy(_values, 0, newArray, 0, _values.length);
                _values = newArray;
            }

            _values[_size] = value;
            _size++;
        }

        public void insert(int index, Object value)
        {
            if(index <0 || index > _size)
            {
                throw  new IndexOutOfBoundsException();
            }

            if (_size >= _values.length)
            {
                Object[] newArray = new Object[_values.length + CAP];
                System.arraycopy(_values, 0, newArray, 0, index);
                System.arraycopy(_values, index, newArray, index + 1, _values.length - index);

                _values = newArray;
            }
            else
            {
                System.arraycopy(_values, index, _values, index + 1, _size - index);
            }

            _values[index] = value;

            _size++;
        }

        public void remove(int index)
        {
            if(index <0 || index >= _size)
            {
                throw  new IndexOutOfBoundsException();
            }

            if (index != _size - 1)
                System.arraycopy(_values, index + 1, _values, index, _size - index - 1);
            _size--;
        }

        public Object get(int index)
        {
            if(index <0 || index >= _size)
            {
                throw  new IndexOutOfBoundsException();
            }

            return _values[index];
        }

        public int size()
        {
            return _size;
        }
    }

    public static class LinkedList
    {
        public class Node
        {
            public Object Value;
            public Node Next;

            public Node(Object value)
            {
                Value = value;
            }
        }

        private int _size = 0;
        private Node _head;
        private Node _tail;


        public void add(Object value)
        {
            if (_head == null)
            {
                _head = new Node(value);
                _tail = _head;
            }
            else
            {
                _tail.Next = new Node(value);
                _tail = _tail.Next;
            }

            _size++;
        }

        public void insert(int index, Object value)
        {
            if(index <0 || index > _size)
            {
                throw  new IndexOutOfBoundsException();
            }

            if (index == 0)
            {
                Node node = new Node(value);
                node.Next = _head;
                _head = node;
                return;
            }
            else if (index == _size)
            {
                add(value);
                return;
            }
            Node node = find(index - 1);
            Node temp = node.Next;
            node.Next = new Node(value);
            node.Next.Next = temp;

            _size++;
        }

        public void remove(int index)
        {
            if(index <0 || index >= _size)
            {
                throw  new IndexOutOfBoundsException();
            }

            if (index == 0)
            {
                _head = _head.Next;
                return;
            }
            Node node = find(index - 1);

            if (node.Next != null)
                node.Next = node.Next.Next;

            _size--;
        }

        private Node find(int index)
        {
            int i = index;

            Node node = _head;
            while ((i--) > 0)
            {
                node = node.Next;
            }

            return node;
        }

        public Object get(int index)
        {
            if(index <0 || index >= _size)
            {
                throw  new IndexOutOfBoundsException();
            }

            return find(index).Value;
        }

        public int size()
        {
            return _size;
        }
    }

    public static void main(String[] args)
    {

        ArrayList arrayList = new ArrayList();

        for (int i = 0; i < 10; i++)
            arrayList.add(i);

        arrayList.insert(9, 1000);

        LinkedList linkedList = new LinkedList();

        for (int i = 0; i < 10; i++)
            linkedList.add(i);

        linkedList.get(5);
        System.out.print("");
    }
}
