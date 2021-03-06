package com.albenw.excel.util;

import com.albenw.excel.base.IndexingField;
import com.albenw.excel.base.converter.CellConverter;
import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;

import java.lang.reflect.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alben.wong
 * @since 2019-02-02.
 */
public class ReflectionUtil {

    private static final Map<Class<?>, Method[]> declaredMethodsCache =
            new ConcurrentHashMap<>(256);

    private static final Method[] NO_METHODS = {};

    public static void makeAccessible(Field field) {
        Objects.requireNonNull(field, "No field provided");
        if ((!isAccessible(field) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static <T extends AccessibleObject & Member> boolean isAccessible(T member) {
        Objects.requireNonNull(member, "No member provided");
        return Modifier.isPublic(((Member)member).getModifiers()) && Modifier.isPublic(((Member)member).getDeclaringClass().getModifiers());
    }

    public static Object getFieldValue(Field field, Object instance) {
        makeAccessible(field);
        if (!Modifier.isStatic(field.getModifiers())) {
            Objects.requireNonNull(instance, "No instance given for non-static field");
        }

        try {
            return field.get(instance);
        } catch (IllegalAccessException var3) {
            throw new UnsupportedOperationException(var3);
        }
    }

    public static void setFieldValue(Field field, Object instance, Object value) {
        makeAccessible(field);
        if (!Modifier.isStatic(field.getModifiers())) {
            Objects.requireNonNull(instance, "No instance given for non-static field");
        }
        try {
            field.set(instance, value);
        } catch (IllegalAccessException var4) {
            throw new UnsupportedOperationException(var4);
        }
    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, new Class<?>[0]);
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
            for (Method method : methods) {
                if (name.equals(method.getName()) &&
                        (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    private static Method[] getDeclaredMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Method[] result = declaredMethodsCache.get(clazz);
        if (result == null) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
            if (defaultMethods != null) {
                result = new Method[declaredMethods.length + defaultMethods.size()];
                System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                int index = declaredMethods.length;
                for (Method defaultMethod : defaultMethods) {
                    result[index] = defaultMethod;
                    index++;
                }
            }
            else {
                result = declaredMethods;
            }
            declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
        }
        return result;
    }

    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new LinkedList<Method>();
                    }
                    result.add(ifcMethod);
                }
            }
        }
        return result;
    }

    public static Type getMethodFirstParameterGenericType(Class<?> clazz, String methodName, Class... parameterClass){
        Method method = findMethod(clazz, methodName, parameterClass);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if(genericParameterTypes == null || genericParameterTypes.length == 0){
            return null;
        }
        for(Type type : genericParameterTypes){
            if(type instanceof ParameterizedType){
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                if(actualTypeArguments != null && actualTypeArguments.length > 0){
                    return actualTypeArguments[0];
                }
            }
        }
        return null;
    }

    public static Type getGenericSuperType(CellConverter converter, int index){
        Type genericSuperclass = converter.getClass().getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType)genericSuperclass;
        return parameterized.getActualTypeArguments()[index];
    }

    public static Object convertToTypeValueFromString(Type type, String str) throws ParseException,  ExcelException{
        String typeName = type.getTypeName();
        if(Integer.class.equals(type) || "int".equals(typeName)){
            return Integer.valueOf(str);
        } else if(Double.class.equals(type) || "double".equals(typeName)){
            return Double.valueOf(str);
        } else if(Long.class.equals(type) || "long".equals(typeName)){
            return Long.valueOf(str);
        } else if(Float.class.equals(type) || "float".equals(typeName)){
            return Float.valueOf(str);
        } else if(String.class.equals(type)){
            return str;
        } else if(Date.class.equals(type)){
            return DateUtil.tryParse(str);
        } else {
            throw new ExcelException(ErrorCode.FIELD_PARSE_ERROR, String.format("不支持类型[%s]", type.getTypeName()));
        }
    }


}
