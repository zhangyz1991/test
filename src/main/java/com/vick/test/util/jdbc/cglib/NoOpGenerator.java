package com.vick.test.util.jdbc.cglib;

import java.util.Iterator;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
class NoOpGenerator implements CallbackGenerator {
    public static final NoOpGenerator INSTANCE = new NoOpGenerator();

    NoOpGenerator() {
    }

    public void generate(ClassEmitter ce, Context context, List methods) {
        Iterator it = methods.iterator();

        while(true) {
            MethodInfo method;
            do {
                if (!it.hasNext()) {
                    return;
                }

                method = (MethodInfo)it.next();
            } while(!TypeUtils.isBridge(method.getModifiers()) && (!TypeUtils.isProtected(context.getOriginalModifiers(method)) || !TypeUtils.isPublic(method.getModifiers())));

            CodeEmitter e = EmitUtils.begin_method(ce, method);
            e.load_this();
            context.emitLoadArgsAndInvoke(e, method);
            e.return_value();
            e.end_method();
        }
    }

    public void generateStatic(CodeEmitter e, Context context, List methods) {
    }
}
