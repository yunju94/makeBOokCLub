package com.make.bookclub.exception;

public class OutOfStockException extends  RuntimeException{
    public  OutOfStockException(String message){
        super(message);
    }
}
