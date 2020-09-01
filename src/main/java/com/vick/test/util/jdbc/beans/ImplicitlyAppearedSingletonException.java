package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@SuppressWarnings("serial")
class ImplicitlyAppearedSingletonException extends IllegalStateException {

    public ImplicitlyAppearedSingletonException() {
        super("About-to-be-created singleton instance implicitly appeared through the " +
                "creation of the factory bean that its bean definition points to");
    }

}
