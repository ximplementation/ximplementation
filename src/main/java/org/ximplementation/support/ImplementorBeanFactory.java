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

/**
 * 实现者Bean工厂。
 * 
 * @author earthangry@gmail.com
 * @date 2016年8月15日
 *
 */
public interface ImplementorBeanFactory
{
	/**
	 * 获取指定实现者的Bean集合。
	 * <p>
	 * 如果不存在，此方法应该返回{@code null}或者空集合。
	 * </p>
	 * 
	 * @param implementor
	 * @return
	 */
	Collection<?> getImplementorBeans(Class<?> implementor);
}
