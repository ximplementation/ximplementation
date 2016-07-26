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
 * 优先级注解。
 * <p>
 * <i>优先级注解</i>标注于<i>实现者</i>类内的<i>实现方法</i>，用于设置<i>实现方法</i>的<i>优先级方法</i>。
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月3日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Priority
{
	/**
	 * 优先级值。
	 * <p>
	 * 此属性的默认值为{@code 0}。
	 * </p>
	 * 
	 * @return
	 */
	int value() default 0;

	/**
	 * 优先级方法标识。
	 * 
	 * <p>
	 * 如果注解类内存在被{@linkplain Refered}注解、且值为此标识的方法，那么它将被用作优先级方法；
	 * </p>
	 * <p>
	 * 否则，方法名称为此标识、且返回值为{@code int}或者{@linkplain Integer}的方法将被用作优先级方法。
	 * </p>
	 * <p>
	 * 此属性的默认值为{@code 0}。
	 * </p>
	 * 
	 * @return
	 */
	String method() default "";
}
