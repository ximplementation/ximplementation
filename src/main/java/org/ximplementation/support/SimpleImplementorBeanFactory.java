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

/**
 * Simple <i>implementor</i> bean factory.
 * <p>
 * It manages a {@code Map} and simply get <i>implementor</i> beans from it.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 *
 */
public class SimpleImplementorBeanFactory implements ImplementorBeanFactory
{
	private Map<Class<?>, ? extends Collection<?>> implementorBeansMap;
	
	public SimpleImplementorBeanFactory()
	{
		super();
	}

	public SimpleImplementorBeanFactory(
			Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
	{
		super();
		this.implementorBeansMap = implementorBeansMap;
	}

	public Map<Class<?>, ? extends Collection<?>> getImplementorBeansMap()
	{
		return implementorBeansMap;
	}

	public void setImplementorBeansMap(
			Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
	{
		this.implementorBeansMap = implementorBeansMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getImplementorBeans(Class<T> implementor)
	{
		return (this.implementorBeansMap == null ? null
				: (Collection<T>) this.implementorBeansMap.get(implementor));
	}

	/**
	 * Create an instance by <i>implementor</i> beans array.
	 * 
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be built.
	 * @return
	 */
	public static SimpleImplementorBeanFactory valueOf(
			Object... implementorBeans)
	{
		Map<Class<?>, List<Object>> implementorBeansMap = new HashMap<Class<?>, List<Object>>();
		
		for (Object implementorBean : implementorBeans)
		{
			Class<?> myClass = implementorBean.getClass();
	
			List<Object> myBeanList = implementorBeansMap.get(myClass);
	
			if (myBeanList == null)
			{
				myBeanList = new ArrayList<Object>(1);
				implementorBeansMap.put(myClass, myBeanList);
			}
	
			myBeanList.add(implementorBean);
		}
		
		return new SimpleImplementorBeanFactory(implementorBeansMap);
	}

	/**
	 * Create an instance by <i>implementor</i> beans collection.
	 * 
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be built.
	 * @return
	 */
	public static SimpleImplementorBeanFactory valueOf(
			Collection<?> implementorBeans)
	{
		Map<Class<?>, List<Object>> implementorBeansMap = new HashMap<Class<?>, List<Object>>();

		for (Object implementorBean : implementorBeans)
		{
			Class<?> myClass = implementorBean.getClass();

			List<Object> myBeanList = implementorBeansMap.get(myClass);

			if (myBeanList == null)
			{
				myBeanList = new ArrayList<Object>(1);
				implementorBeansMap.put(myClass, myBeanList);
			}

			myBeanList.add(implementorBean);
		}

		return new SimpleImplementorBeanFactory(implementorBeansMap);
	}

	/**
	 * Create an instance by <i>implementor</i> beans map.
	 * 
	 * @param implementorBeansMap
	 *            The <i>implementor</i> beans map to be built.
	 * @return
	 */
	public static SimpleImplementorBeanFactory valueOf(
			Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
	{
		return new SimpleImplementorBeanFactory(implementorBeansMap);
	}
}
