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
 * 实现者注解。
 * <p>
 * <i>实现者注解</i>用于将类标注为<i>实现者</i>。
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月3日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Implementor
{
	/**
	 * <i>实现者</i>所实现的<i>接口</i>。
	 * 
	 * @return
	 */
	Class<?> value() default Object.class;

	/**
	 * <i>实现者</i>所实现的<i>接口</i>数组。
	 * 
	 * @return
	 */
	Class<?>[] implementees() default Object.class;
}
