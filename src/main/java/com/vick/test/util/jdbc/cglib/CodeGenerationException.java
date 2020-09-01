package com.vick.test.util.jdbc.cglib;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class CodeGenerationException extends RuntimeException {
    private Throwable cause;

    public CodeGenerationException(Throwable cause) {
        super(cause.getClass().getName() + "-->" + cause.getMessage());
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
