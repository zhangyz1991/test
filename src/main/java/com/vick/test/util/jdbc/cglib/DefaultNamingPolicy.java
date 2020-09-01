package com.vick.test.util.jdbc.cglib;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class DefaultNamingPolicy implements NamingPolicy {
    public static final DefaultNamingPolicy INSTANCE = new DefaultNamingPolicy();
    private static final boolean STRESS_HASH_CODE = Boolean.getBoolean("org.springframework.cglib.test.stressHashCodes");

    public DefaultNamingPolicy() {
    }

    public String getClassName(String prefix, String source, Object key, Predicate names) {
        if (prefix == null) {
            prefix = "org.springframework.cglib.empty.Object";
        } else if (prefix.startsWith("java")) {
            prefix = "$" + prefix;
        }

        String base = prefix + "$$" + source.substring(source.lastIndexOf(46) + 1) + this.getTag() + "$$" + Integer.toHexString(STRESS_HASH_CODE ? 0 : key.hashCode());
        String attempt = base;

        for(int var7 = 2; names.evaluate(attempt); attempt = base + "_" + var7++) {
        }

        return attempt;
    }

    protected String getTag() {
        return "ByCGLIB";
    }

    public int hashCode() {
        return this.getTag().hashCode();
    }

    public boolean equals(Object o) {
        return o instanceof DefaultNamingPolicy && ((DefaultNamingPolicy)o).getTag().equals(this.getTag());
    }
}
