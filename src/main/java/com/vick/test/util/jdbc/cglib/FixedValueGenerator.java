package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Type;

import java.util.Iterator;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
class FixedValueGenerator implements CallbackGenerator {
    public static final FixedValueGenerator INSTANCE = new FixedValueGenerator();
    private static final Type FIXED_VALUE = TypeUtils.parseType("org.springframework.cglib.proxy.FixedValue");
    private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");

    FixedValueGenerator() {
    }

    public void generate(ClassEmitter ce, Context context, List methods) {
        Iterator it = methods.iterator();

        while(it.hasNext()) {
            MethodInfo method = (MethodInfo)it.next();
            CodeEmitter e = context.beginMethod(ce, method);
            context.emitCallback(e, context.getIndex(method));
            e.invoke_interface(FIXED_VALUE, LOAD_OBJECT);
            e.unbox_or_zero(e.getReturnType());
            e.return_value();
            e.end_method();
        }

    }

    public void generateStatic(CodeEmitter e, Context context, List methods) {
    }
}
