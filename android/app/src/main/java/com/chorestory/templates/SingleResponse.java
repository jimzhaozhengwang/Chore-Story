package com.chorestory.templates;

public class SingleResponse<T> {

    private T data;

    public Boolean hasResponse() {
        return (data != null);
    }

    public T getData() {
        return data;
    }
}
