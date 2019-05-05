package com.philippesteinbach.model;

/**
 * In our application after any reading from or writing to the hard drive an IOResult object will be generated.
 * @param <T> Any kind of data, in our case TextFile
 */
public class IOResult<T> {

    private T data; // carry any kind of data = reusable
    private boolean ok;

    public IOResult(boolean ok, T data) {
        this.ok = ok;
        this.data = data;
    }

    public boolean isOk () {
        return ok;
    }

    public boolean hasData() {
        return data != null;
    }

    public T getData() {
        return  data;
    }
}
