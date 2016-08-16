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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 预准备的实现者Bean工厂。
 * <p>
 * 它先初始化实现者类，之后可以添加它们对应的Bean。
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2016年8月15日
 *
 */
public class PreparedImplementorBeanFactory implements ImplementorBeanFactory
{
	private Map<Class<?>, List<Object>> implementorBeansMap = new HashMap<Class<?>, List<Object>>();

	/**
	 * 以实现者套集构建。
	 * 
	 * @param implementors
	 */
	public PreparedImplementorBeanFactory(Set<Class<?>> implementors)
	{
		super();

		for (Class<?> implementor : implementors)
		{
			this.implementorBeansMap.put(implementor, new ArrayList<Object>(1));
		}
	}

	/**
	 * 以{@linkplain Implementation}的所有实现者构建。
	 * 
	 * @param implementation
	 */
	public PreparedImplementorBeanFactory(Implementation implementation)
	{
		super();

		ImplementInfo[] implementInfos = implementation.getImplementInfos();

		for (ImplementInfo implementInfo : implementInfos)
		{
			ImplementMethodInfo[] implementMethodInfos = implementInfo
					.getImplementMethodInfos();

			for (ImplementMethodInfo implementMethodInfo : implementMethodInfos)
			{
				Class<?> implementor = implementMethodInfo.getImplementor();

				if (!this.implementorBeansMap.containsKey(implementor))
					this.implementorBeansMap.put(implementor,
							new ArrayList<Object>(1));
			}
		}
	}

	/**
	 * 获取所有实现者。
	 * 
	 * @return
	 */
	public Set<Class<?>> getAllImplementors()
	{
		return this.implementorBeansMap.keySet();
	}

	/**
	 * 判断给定实现者是否可接受。
	 * 
	 * @param implementor
	 * @return
	 */
	boolean accept(Class<?> implementor)
	{
		return (this.implementorBeansMap.get(implementor) != null);
	}

	/**
	 * 判断给定实现者Bean是否可接受。
	 * 
	 * @param implementorBean
	 * @return
	 */
	boolean accept(Object implementorBean)
	{
		return (this.implementorBeansMap
				.get(implementorBean.getClass()) != null);
	}

	/**
	 * 添加实现者Bean。
	 * <p>
	 * 如果实现者Bean被接受，此方法将返回{@code true}；否则，返回{@code false}。
	 * </p>
	 * 
	 * @param implementorBean
	 * @return
	 */
	public boolean addImplementorBean(Object implementorBean)
	{
		List<Object> implementorBeans = this.implementorBeansMap
				.get(implementorBean.getClass());

		if (implementorBeans == null)
			return false;

		implementorBeans.add(implementorBean);

		return true;
	}

	@Override
	public Collection<?> getImplementorBeans(Class<?> implementor)
	{
		return this.implementorBeansMap.get(implementor);
	}
}
