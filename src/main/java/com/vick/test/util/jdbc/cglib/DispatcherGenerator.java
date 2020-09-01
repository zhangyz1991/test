package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Type;

import java.util.Iterator;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
class DispatcherGenerator implements CallbackGenerator {
    public static final DispatcherGenerator INSTANCE = new DispatcherGenerator(false);
    public static final DispatcherGenerator PROXY_REF_INSTANCE = new DispatcherGenerator(true);
    private static final Type DISPATCHER = TypeUtils.parseType("org.springframework.cglib.proxy.Dispatcher");
    private static final Type PROXY_REF_DISPATCHER = TypeUtils.parseType("org.springframework.cglib.proxy.ProxyRefDispatcher");
    private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
    private static final Signature PROXY_REF_LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject(Object)");
    private boolean proxyRef;

    private DispatcherGenerator(boolean proxyRef) {
        this.proxyRef = proxyRef;
    }

    public void generate(ClassEmitter ce, Context context, List methods) {
        Iterator it = methods.iterator();

        while(it.hasNext()) {
            MethodInfo method = (MethodInfo)it.next();
            if (!TypeUtils.isProtected(method.getModifiers())) {
                CodeEmitter e = context.beginMethod(ce, method);
                context.emitCallback(e, context.getIndex(method));
                if (this.proxyRef) {
                    e.load_this();
                    e.invoke_interface(PROXY_REF_DISPATCHER, PROXY_REF_LOAD_OBJECT);
                } else {
                    e.invoke_interface(DISPATCHER, LOAD_OBJECT);
                }

                e.checkcast(method.getClassInfo().getType());
                e.load_args();
                e.invoke(method);
                e.return_value();
                e.end_method();
            }
        }

    }

    public void generateStatic(CodeEmitter e, Context context, List methods) {
    }
}
