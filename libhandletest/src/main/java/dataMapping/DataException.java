package dataMapping;

public class DataException extends Exception
{
    public DataException(String msg)
    {
        super(msg);
    }

    public DataException(String msg, Throwable e)
    {
        super(msg, e);
    }
}
