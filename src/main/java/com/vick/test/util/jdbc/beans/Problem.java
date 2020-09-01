package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class Problem {

    private final String message;

    private final Location location;

    @Nullable
    private final ParseState parseState;

    @Nullable
    private final Throwable rootCause;


    /**
     * Create a new instance of the {@link Problem} class.
     * @param message a message detailing the problem
     * @param location the location within a bean configuration source that triggered the error
     */
    public Problem(String message, Location location) {
        this(message, location, null, null);
    }

    /**
     * Create a new instance of the {@link Problem} class.
     * @param message a message detailing the problem
     * @param parseState the {@link ParseState} at the time of the error
     * @param location the location within a bean configuration source that triggered the error
     */
    public Problem(String message, Location location, ParseState parseState) {
        this(message, location, parseState, null);
    }

    /**
     * Create a new instance of the {@link Problem} class.
     * @param message a message detailing the problem
     * @param rootCause the underlying exception that caused the error (may be {@code null})
     * @param parseState the {@link ParseState} at the time of the error
     * @param location the location within a bean configuration source that triggered the error
     */
    public Problem(String message, Location location, @Nullable ParseState parseState, @Nullable Throwable rootCause) {
        Assert.notNull(message, "Message must not be null");
        Assert.notNull(location, "Location must not be null");
        this.message = message;
        this.location = location;
        this.parseState = parseState;
        this.rootCause = rootCause;
    }


    /**
     * Get the message detailing the problem.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Get the location within a bean configuration source that triggered the error.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Get the description of the bean configuration source that triggered the error,
     * as contained within this Problem's Location object.
     * @see #getLocation()
     */
    public String getResourceDescription() {
        return getLocation().getResource().getDescription();
    }

    /**
     * Get the {@link ParseState} at the time of the error (may be {@code null}).
     */
    @Nullable
    public ParseState getParseState() {
        return this.parseState;
    }

    /**
     * Get the underlying exception that caused the error (may be {@code null}).
     */
    @Nullable
    public Throwable getRootCause() {
        return this.rootCause;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Configuration problem: ");
        sb.append(getMessage());
        sb.append("\nOffending resource: ").append(getResourceDescription());
        if (getParseState() != null) {
            sb.append('\n').append(getParseState());
        }
        return sb.toString();
    }

}
