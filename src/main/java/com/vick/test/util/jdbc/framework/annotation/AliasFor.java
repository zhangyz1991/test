package com.vick.test.util.jdbc.framework.annotation;

import java.lang.annotation.*;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AliasFor {

    /**
     * Alias for {@link #attribute}.
     * <p>Intended to be used instead of {@link #attribute} when {@link #annotation}
     * is not declared &mdash; for example: {@code @AliasFor("value")} instead of
     * {@code @AliasFor(attribute = "value")}.
     */
    @AliasFor("attribute")
    String value() default "";

    /**
     * The name of the attribute that <em>this</em> attribute is an alias for.
     * @see #value
     */
    @AliasFor("value")
    String attribute() default "";

    /**
     * The type of annotation in which the aliased {@link #attribute} is declared.
     * <p>Defaults to {@link Annotation}, implying that the aliased attribute is
     * declared in the same annotation as <em>this</em> attribute.
     */
    Class<? extends Annotation> annotation() default Annotation.class;

}
