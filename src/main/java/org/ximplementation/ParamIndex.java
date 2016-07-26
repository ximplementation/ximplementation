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
 * 参数索引注解。
 * <p>
 * <i>参数索引注解</i>标注于<i>实现者</i>类内的<i>实现方法</i>、<i>优先级方法</i>、<i><i>有效性方法</i> ，用于指定参数对应
 * <i>接口方法</i>的参数索引。
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月3日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface ParamIndex
{
	/**
	 * 参数索引值。
	 * <p>
	 * 参数索引值以{@code 0}开始。
	 * </p>
	 * 
	 * @return
	 */
	int value();
}
