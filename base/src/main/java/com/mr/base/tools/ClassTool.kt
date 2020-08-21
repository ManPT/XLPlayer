package com.lib.tools

import java.lang.reflect.ParameterizedType

object ClassTool {
    /**
     *
     * @param o 根据泛型类型生成对象的标识类
     * @param i 泛型类型声明 从0开始
     * @param <T>  生成具体标识的泛型对象
     * @return
    </T> */
    fun <T> getT(o: Any, i: Int): T? {
        try {
            return ((o.javaClass
                .genericSuperclass as ParameterizedType).actualTypeArguments[i] as Class<T>)
                .newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        return null
    }
}