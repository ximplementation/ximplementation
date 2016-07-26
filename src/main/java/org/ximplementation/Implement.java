/**
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *  
  * 	http://www.apache.org/licenses/LICENSE-2.0
  *  
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License. 
  */

package org.ximplementation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现方法注解。
 * <p>
 * <i>实现方法注解</i>标注于<i>实现者</i>内的<i>实现方法</i>，用于指定<i>实现方法</i>所实现的<i>接口方法</i>。
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月3日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Implement
{
	/**
	 * <i>实现方法</i>所实现的<i>接口方法</i>标识。
	 * <p>
	 * 此标识可以是<i>接口方法</i>名称、<i>接口方法</i>{@linkplain Refered}注解的
	 * {@linkplain Refered#value() value}值、或者<i>接口方法</i>
	 * {@linkplain java.lang.reflect.Method#toString() 签名}。
	 * </p>
	 * 
	 * @return
	 */
	String value() default "";
}
