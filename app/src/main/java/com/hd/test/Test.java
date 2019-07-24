package com.hd.test;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Note：None
 * Created by Liuguodong on 2019/7/22 15:55
 * E-Mail Address：986850427@qq.com
 */
public class Test {
    public static void main(String[] args) {

        System.out.println(new TypeToken<Person[]>(){}.getType());


        String string[]={};
        System.out.println(string.getClass().isArray());



//        new Person<String[]>(){}.fun();
    }
    /***
     * 得到T的class
     *
     * @return
     */
    public static Type getClassType(Object t) {
        Type superclass = t.getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            System.out.println("Missing type parameter.");




            return null;
        }
        if (!(superclass instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type[] params = parameterized.getActualTypeArguments();
        return  params[0];
    }

    public  static class Person< T> {
        public String name;

        public T obj;

        public void fun(){

            Type type=getClassType(this);

            Class<?> c = (Class<?>) type;
            System.out.println( c.isArray());

        }
    }
}
