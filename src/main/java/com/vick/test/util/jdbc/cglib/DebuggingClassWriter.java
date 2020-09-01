package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.ClassReader;
import com.vick.test.util.jdbc.asm.ClassVisitor;
import com.vick.test.util.jdbc.asm.ClassWriter;

import java.io.*;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class DebuggingClassWriter extends ClassVisitor {
    public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";
    private static String debugLocation = System.getProperty("cglib.debugLocation");
    private static Constructor traceCtor;
    private String className;
    private String superName;

    public DebuggingClassWriter(int flags) {
        super(Constants.ASM_API, new ClassWriter(flags));
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name.replace('/', '.');
        this.superName = superName.replace('/', '.');
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public String getClassName() {
        return this.className;
    }

    public String getSuperName() {
        return this.superName;
    }

    public byte[] toByteArray() {
        return (byte[])((byte[]) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                //此处注释了
                //byte[] b = ((ClassWriter)DebuggingClassWriter.access$001(DebuggingClassWriter.this)).toByteArray();
                byte[] b = new byte[]{};
                if (DebuggingClassWriter.debugLocation != null) {
                    String dirs = DebuggingClassWriter.this.className.replace('.', File.separatorChar);

                    try {
                        (new File(DebuggingClassWriter.debugLocation + File.separatorChar + dirs)).getParentFile().mkdirs();
                        File file = new File(new File(DebuggingClassWriter.debugLocation), dirs + ".class");
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

                        try {
                            out.write(b);
                        } finally {
                            out.close();
                        }

                        if (DebuggingClassWriter.traceCtor != null) {
                            file = new File(new File(DebuggingClassWriter.debugLocation), dirs + ".asm");
                            out = new BufferedOutputStream(new FileOutputStream(file));

                            try {
                                ClassReader cr = new ClassReader(b);
                                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                                ClassVisitor tcv = (ClassVisitor)DebuggingClassWriter.traceCtor.newInstance(null, pw);
                                cr.accept(tcv, 0);
                                pw.flush();
                            } finally {
                                out.close();
                            }
                        }
                    } catch (Exception var17) {
                        throw new CodeGenerationException(var17);
                    }
                }

                return b;
            }
        }));
    }

    static {
        if (debugLocation != null) {
            System.err.println("CGLIB debugging enabled, writing to '" + debugLocation + "'");

            try {
                Class clazz = Class.forName("org.springframework.asm.util.TraceClassVisitor");
                traceCtor = clazz.getConstructor(ClassVisitor.class, PrintWriter.class);
            } catch (Throwable var1) {
            }
        }

    }
}
