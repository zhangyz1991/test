package com.vick.test.util.jdbc.asm;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public final class MethodTooLargeException extends IndexOutOfBoundsException {
    private static final long serialVersionUID = 6807380416709738314L;

    private final String className;
    private final String methodName;
    private final String descriptor;
    private final int codeSize;

    /**
     * Constructs a new {@link MethodTooLargeException}.
     *
     * @param className the internal name of the owner class.
     * @param methodName the name of the method.
     * @param descriptor the descriptor of the method.
     * @param codeSize the size of the method's Code attribute, in bytes.
     */
    public MethodTooLargeException(
            final String className,
            final String methodName,
            final String descriptor,
            final int codeSize) {
        super("Method too large: " + className + "." + methodName + " " + descriptor);
        this.className = className;
        this.methodName = methodName;
        this.descriptor = descriptor;
        this.codeSize = codeSize;
    }

    /**
     * Returns the internal name of the owner class.
     *
     * @return the internal name of the owner class.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Returns the name of the method.
     *
     * @return the name of the method.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Returns the descriptor of the method.
     *
     * @return the descriptor of the method.
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Returns the size of the method's Code attribute, in bytes.
     *
     * @return the size of the method's Code attribute, in bytes.
     */
    public int getCodeSize() {
        return codeSize;
    }
}
