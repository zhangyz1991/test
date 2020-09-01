package com.vick.test.util.jdbc.framework;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class DefaultParameterNameDiscoverer extends PrioritizedParameterNameDiscoverer {

    public DefaultParameterNameDiscoverer() {
        if (!GraalDetector.inImageCode()) {
            if (KotlinDetector.isKotlinReflectPresent()) {
                addDiscoverer(new KotlinReflectionParameterNameDiscoverer());
            }
            addDiscoverer(new StandardReflectionParameterNameDiscoverer());
            addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
        }
    }

}
