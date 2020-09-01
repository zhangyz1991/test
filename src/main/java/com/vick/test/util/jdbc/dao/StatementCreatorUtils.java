package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;
import com.vick.test.util.jdbc.framework.SpringProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.*;
import java.util.*;

/**
 * @author Vick Zhang
 * @create 2020/8/22
 */
public abstract class StatementCreatorUtils {
    public static final String IGNORE_GETPARAMETERTYPE_PROPERTY_NAME = "spring.jdbc.getParameterType.ignore";
    static boolean shouldIgnoreGetParameterType = SpringProperties.getFlag("spring.jdbc.getParameterType.ignore");
    private static final Logger logger = LoggerFactory.getLogger(StatementCreatorUtils.class);
    private static final Map<Class<?>, Integer> javaTypeToSqlTypeMap = new HashMap(32);

    public StatementCreatorUtils() {
    }

    public static int javaTypeToSqlParameterType(@Nullable Class<?> javaType) {
        if (javaType == null) {
            return -2147483648;
        } else {
            Integer sqlType = (Integer)javaTypeToSqlTypeMap.get(javaType);
            if (sqlType != null) {
                return sqlType;
            } else if (Number.class.isAssignableFrom(javaType)) {
                return 2;
            } else if (isStringValue(javaType)) {
                return 12;
            } else {
                return !isDateValue(javaType) && !Calendar.class.isAssignableFrom(javaType) ? -2147483648 : 93;
            }
        }
    }

    public static void setParameterValue(PreparedStatement ps, int paramIndex, SqlParameter param, @Nullable Object inValue) throws SQLException {
        setParameterValueInternal(ps, paramIndex, param.getSqlType(), param.getTypeName(), param.getScale(), inValue);
    }

    public static void setParameterValue(PreparedStatement ps, int paramIndex, int sqlType, @Nullable Object inValue) throws SQLException {
        setParameterValueInternal(ps, paramIndex, sqlType, (String)null, (Integer)null, inValue);
    }

    public static void setParameterValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName, @Nullable Object inValue) throws SQLException {
        setParameterValueInternal(ps, paramIndex, sqlType, typeName, (Integer)null, inValue);
    }

    private static void setParameterValueInternal(PreparedStatement ps, int paramIndex, int sqlType, @Nullable String typeName, @Nullable Integer scale, @Nullable Object inValue) throws SQLException {
        String typeNameToUse = typeName;
        int sqlTypeToUse = sqlType;
        Object inValueToUse = inValue;
        if (inValue instanceof SqlParameterValue) {
            SqlParameterValue parameterValue = (SqlParameterValue)inValue;
            if (logger.isDebugEnabled()) {
                logger.debug("Overriding type info with runtime info from SqlParameterValue: column index " + paramIndex + ", SQL type " + parameterValue.getSqlType() + ", type name " + parameterValue.getTypeName());
            }

            if (parameterValue.getSqlType() != -2147483648) {
                sqlTypeToUse = parameterValue.getSqlType();
            }

            if (parameterValue.getTypeName() != null) {
                typeNameToUse = parameterValue.getTypeName();
            }

            inValueToUse = parameterValue.getValue();
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Setting SQL statement parameter value: column index " + paramIndex + ", parameter value [" + inValueToUse + "], value class [" + (inValueToUse != null ? inValueToUse.getClass().getName() : "null") + "], SQL type " + (sqlTypeToUse == -2147483648 ? "unknown" : Integer.toString(sqlTypeToUse)));
        }

        if (inValueToUse == null) {
            setNull(ps, paramIndex, sqlTypeToUse, typeNameToUse);
        } else {
            setValue(ps, paramIndex, sqlTypeToUse, typeNameToUse, scale, inValueToUse);
        }

    }

    private static void setNull(PreparedStatement ps, int paramIndex, int sqlType, @Nullable String typeName) throws SQLException {
        if (sqlType == -2147483648 || sqlType == 1111 && typeName == null) {
            boolean useSetObject = false;
            Integer sqlTypeToUse = null;
            if (!shouldIgnoreGetParameterType) {
                try {
                    sqlTypeToUse = ps.getParameterMetaData().getParameterType(paramIndex);
                } catch (SQLException var9) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("JDBC getParameterType call failed - using fallback method instead: " + var9);
                    }
                }
            }

            if (sqlTypeToUse == null) {
                sqlTypeToUse = 0;
                DatabaseMetaData dbmd = ps.getConnection().getMetaData();
                String jdbcDriverName = dbmd.getDriverName();
                String databaseProductName = dbmd.getDatabaseProductName();
                if (databaseProductName.startsWith("Informix") || jdbcDriverName.startsWith("Microsoft") && jdbcDriverName.contains("SQL Server")) {
                    useSetObject = true;
                } else if (databaseProductName.startsWith("DB2") || jdbcDriverName.startsWith("jConnect") || jdbcDriverName.startsWith("SQLServer") || jdbcDriverName.startsWith("Apache Derby")) {
                    sqlTypeToUse = 12;
                }
            }

            if (useSetObject) {
                ps.setObject(paramIndex, (Object)null);
            } else {
                ps.setNull(paramIndex, sqlTypeToUse);
            }
        } else if (typeName != null) {
            ps.setNull(paramIndex, sqlType, typeName);
        } else {
            ps.setNull(paramIndex, sqlType);
        }

    }

    private static void setValue(PreparedStatement ps, int paramIndex, int sqlType, @Nullable String typeName, @Nullable Integer scale, Object inValue) throws SQLException {
        if (inValue instanceof SqlTypeValue) {
            ((SqlTypeValue)inValue).setTypeValue(ps, paramIndex, sqlType, typeName);
        } else if (inValue instanceof SqlValue) {
            ((SqlValue)inValue).setValue(ps, paramIndex);
        } else if (sqlType != 12 && sqlType != -1) {
            if (sqlType != -9 && sqlType != -16) {
                if ((sqlType == 2005 || sqlType == 2011) && isStringValue(inValue.getClass())) {
                    String strVal = inValue.toString();
                    if (strVal.length() > 4000) {
                        if (sqlType == 2011) {
                            ps.setNClob(paramIndex, new StringReader(strVal), (long)strVal.length());
                        } else {
                            ps.setClob(paramIndex, new StringReader(strVal), (long)strVal.length());
                        }

                        return;
                    }

                    if (sqlType == 2011) {
                        ps.setNString(paramIndex, strVal);
                    } else {
                        ps.setString(paramIndex, strVal);
                    }
                } else if (sqlType != 3 && sqlType != 2) {
                    if (sqlType == 16) {
                        if (inValue instanceof Boolean) {
                            ps.setBoolean(paramIndex, (Boolean)inValue);
                        } else {
                            ps.setObject(paramIndex, inValue, 16);
                        }
                    } else {
                        Calendar cal;
                        if (sqlType == 91) {
                            if (inValue instanceof Date) {
                                if (inValue instanceof java.sql.Date) {
                                    ps.setDate(paramIndex, (java.sql.Date)inValue);
                                } else {
                                    ps.setDate(paramIndex, new java.sql.Date(((Date)inValue).getTime()));
                                }
                            } else if (inValue instanceof Calendar) {
                                cal = (Calendar)inValue;
                                ps.setDate(paramIndex, new java.sql.Date(cal.getTime().getTime()), cal);
                            } else {
                                ps.setObject(paramIndex, inValue, 91);
                            }
                        } else if (sqlType == 92) {
                            if (inValue instanceof Date) {
                                if (inValue instanceof Time) {
                                    ps.setTime(paramIndex, (Time)inValue);
                                } else {
                                    ps.setTime(paramIndex, new Time(((Date)inValue).getTime()));
                                }
                            } else if (inValue instanceof Calendar) {
                                cal = (Calendar)inValue;
                                ps.setTime(paramIndex, new Time(cal.getTime().getTime()), cal);
                            } else {
                                ps.setObject(paramIndex, inValue, 92);
                            }
                        } else if (sqlType == 93) {
                            if (inValue instanceof Date) {
                                if (inValue instanceof Timestamp) {
                                    ps.setTimestamp(paramIndex, (Timestamp)inValue);
                                } else {
                                    ps.setTimestamp(paramIndex, new Timestamp(((Date)inValue).getTime()));
                                }
                            } else if (inValue instanceof Calendar) {
                                cal = (Calendar)inValue;
                                ps.setTimestamp(paramIndex, new Timestamp(cal.getTime().getTime()), cal);
                            } else {
                                ps.setObject(paramIndex, inValue, 93);
                            }
                        } else if (sqlType == -2147483648 || sqlType == 1111 && "Oracle".equals(ps.getConnection().getMetaData().getDatabaseProductName())) {
                            if (isStringValue(inValue.getClass())) {
                                ps.setString(paramIndex, inValue.toString());
                            } else if (isDateValue(inValue.getClass())) {
                                ps.setTimestamp(paramIndex, new Timestamp(((Date)inValue).getTime()));
                            } else if (inValue instanceof Calendar) {
                                cal = (Calendar)inValue;
                                ps.setTimestamp(paramIndex, new Timestamp(cal.getTime().getTime()), cal);
                            } else {
                                ps.setObject(paramIndex, inValue);
                            }
                        } else {
                            ps.setObject(paramIndex, inValue, sqlType);
                        }
                    }
                } else if (inValue instanceof BigDecimal) {
                    ps.setBigDecimal(paramIndex, (BigDecimal)inValue);
                } else if (scale != null) {
                    ps.setObject(paramIndex, inValue, sqlType, scale);
                } else {
                    ps.setObject(paramIndex, inValue, sqlType);
                }
            } else {
                ps.setNString(paramIndex, inValue.toString());
            }
        } else {
            ps.setString(paramIndex, inValue.toString());
        }

    }

    private static boolean isStringValue(Class<?> inValueType) {
        return CharSequence.class.isAssignableFrom(inValueType) || StringWriter.class.isAssignableFrom(inValueType);
    }

    private static boolean isDateValue(Class<?> inValueType) {
        return Date.class.isAssignableFrom(inValueType) && !java.sql.Date.class.isAssignableFrom(inValueType) && !Time.class.isAssignableFrom(inValueType) && !Timestamp.class.isAssignableFrom(inValueType);
    }

    public static void cleanupParameters(@Nullable Object... paramValues) {
        if (paramValues != null) {
            cleanupParameters((Collection)Arrays.asList(paramValues));
        }

    }

    public static void cleanupParameters(@Nullable Collection<?> paramValues) {
        if (paramValues != null) {
            Iterator var1 = paramValues.iterator();

            while(var1.hasNext()) {
                Object inValue = var1.next();
                if (inValue instanceof DisposableSqlTypeValue) {
                    ((DisposableSqlTypeValue)inValue).cleanup();
                } else if (inValue instanceof SqlValue) {
                    ((SqlValue)inValue).cleanup();
                }
            }
        }

    }

    static {
        javaTypeToSqlTypeMap.put(Boolean.TYPE, 16);
        javaTypeToSqlTypeMap.put(Boolean.class, 16);
        javaTypeToSqlTypeMap.put(Byte.TYPE, -6);
        javaTypeToSqlTypeMap.put(Byte.class, -6);
        javaTypeToSqlTypeMap.put(Short.TYPE, 5);
        javaTypeToSqlTypeMap.put(Short.class, 5);
        javaTypeToSqlTypeMap.put(Integer.TYPE, 4);
        javaTypeToSqlTypeMap.put(Integer.class, 4);
        javaTypeToSqlTypeMap.put(Long.TYPE, -5);
        javaTypeToSqlTypeMap.put(Long.class, -5);
        javaTypeToSqlTypeMap.put(BigInteger.class, -5);
        javaTypeToSqlTypeMap.put(Float.TYPE, 6);
        javaTypeToSqlTypeMap.put(Float.class, 6);
        javaTypeToSqlTypeMap.put(Double.TYPE, 8);
        javaTypeToSqlTypeMap.put(Double.class, 8);
        javaTypeToSqlTypeMap.put(BigDecimal.class, 3);
        javaTypeToSqlTypeMap.put(java.sql.Date.class, 91);
        javaTypeToSqlTypeMap.put(Time.class, 92);
        javaTypeToSqlTypeMap.put(Timestamp.class, 93);
        javaTypeToSqlTypeMap.put(Blob.class, 2004);
        javaTypeToSqlTypeMap.put(Clob.class, 2005);
    }
}
