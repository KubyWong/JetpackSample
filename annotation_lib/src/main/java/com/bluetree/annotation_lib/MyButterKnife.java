package com.bluetree.annotation_lib;

import com.bluetree.annotation_lib.IBind;

/**
 * 通过接口，绑定activity和注解编译器生成的class文件建立关系
 * 涉及到
 */
public class MyButterKnife {
    public static void bind(Object activity) {
        String name = activity.getClass().getName() + "_ViewBinding";
        System.out.println("className:"+name);
        try {
            // Class.forName("ClassName")方式会执行类加载的加载、链接、初始化三个步骤
            Class<?> aClass = Class.forName(name);
            IBind iBinder = (IBind) aClass.newInstance();

            System.out.println("sdfasdfasdfasdfasdf:"+iBinder);
            iBinder.bind(activity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
