package org.ximplementation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非实现方法注解。
 * <p>
 * <i>非实现方法注解</i>标注于<i>实现者</i>内的<i>方法</i>，用于将方法标注为非实现方法。
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2016年8月23日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NotImplement
{

}
