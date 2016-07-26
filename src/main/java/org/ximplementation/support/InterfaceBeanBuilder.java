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

package org.ximplementation.support;

import java.util.Collection;
import java.util.Map;

/**
 * 接口实例构建器。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月3日
 *
 */
public interface InterfaceBeanBuilder
{
	/**
	 * 构建接口实例。
	 * 
	 * @param interfacee
	 * @param implementorBeansMap
	 * @return
	 */
	<T> T build(Class<T> interfacee,
			Map<Class<?>, ? extends Collection<?>> implementorBeansMap);
}
