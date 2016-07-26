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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象接口实例构建器。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月3日
 *
 */
public class AbstractInterfaceBeanBuilder
{
	public AbstractInterfaceBeanBuilder()
	{
		super();
	}

	/**
	 * 解析{@linkplain ImplementMethodBeanInfo}映射表。
	 * 
	 * @param interfacee
	 * @param implementation
	 * @param implementorBeans
	 * @return
	 */
	protected Map<Method, ImplementMethodBeanInfo[]> resolveImplementMethodBeanInfosMap(Class<?> interfacee,
			Implementation implementation, Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
	{
		Map<Method, ImplementMethodBeanInfo[]> implementMethodBeanInfosMap = new HashMap<Method, ImplementMethodBeanInfo[]>();

		ImplementInfo[] implementInfos = implementation.getImplementInfos();

		for (ImplementInfo implementInfo : implementInfos)
		{
			Method interfaceMethod = implementInfo.getInterfaceMethod();
			ImplementMethodInfo[] implementMethodInfos = implementInfo.getImplementMethodInfos();

			ImplementMethodBeanInfo[] implementMethodBeanInfos = new ImplementMethodBeanInfo[implementMethodInfos.length];

			for (int i = 0; i < implementMethodInfos.length; i++)
			{
				ImplementMethodInfo implementMethodInfo = implementMethodInfos[i];
				Collection<?> implementorBeans = implementorBeansMap.get(implementMethodInfo.getImplementor());

				implementMethodBeanInfos[i] = new ImplementMethodBeanInfo(implementMethodInfo, implementorBeans);
			}

			implementMethodBeanInfosMap.put(interfaceMethod, implementMethodBeanInfos);
		}

		return implementMethodBeanInfosMap;
	}
}
