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
 * Prepared implementor bean factory.
 * <p>
 * It is initialized by prepared <i>implementor</i>s, then only beans which are
 * instance of these <i>implementor</i>s can be added.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 *
 */
public class PreparedImplementorBeanFactory implements ImplementorBeanFactory
{
	private Map<Class<?>, List<Object>> implementorBeansMap = new HashMap<Class<?>, List<Object>>();

	/**
	 * Create an instance by <i>implementor</i> set.
	 * 
	 * @param implementors
	 */
	public PreparedImplementorBeanFactory(Set<Class<?>> implementors)
	{
		super();

		for (Class<?> implementor : implementors)
		{
			setImplementorBeansList(implementor, new ArrayList<Object>(1));
		}
	}

	/**
	 * Create an instance by the <i>implementor</i>s of an
	 * {@linkplain Implementation}.
	 * 
	 * @param implementation
	 */
	public PreparedImplementorBeanFactory(Implementation<?> implementation)
	{
		super();

		ImplementInfo[] implementInfos = implementation.getImplementInfos();

		for (ImplementInfo implementInfo : implementInfos)
		{
			if (!implementInfo.hasImplementMethodInfo())
				continue;

			for (ImplementMethodInfo implementMethodInfo : implementInfo
					.getImplementMethodInfos())
			{
				Class<?> implementor = implementMethodInfo.getImplementor();

				if (!this.implementorBeansMap.containsKey(implementor))
					setImplementorBeansList(implementor,
							new ArrayList<Object>(1));
			}
		}
	}

	/**
	 * Get all the <i>implementor</i>s.
	 * 
	 * @return
	 */
	public Set<Class<?>> getAllImplementors()
	{
		return this.implementorBeansMap.keySet();
	}

	/**
	 * Return if a specified <i>implementor</i> is acceptable.
	 * 
	 * @param implementor
	 *            The <i>implementor</i> to be checked.
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean accept(Class<?> implementor)
	{
		return (getImplementorBeansList(implementor) != null);
	}

	/**
	 * Return if a specified <i>implementor</i> bean is acceptable.
	 * 
	 * @param implementorBean
	 *            The <i>implementor</i> bean to be checked.
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean accept(Object implementorBean)
	{
		return (getImplementorBeansList(implementorBean.getClass()) != null);
	}

	/**
	 * Add an <i>implementor</i> bean.
	 * 
	 * @param implementorBean
	 *            The <i>implementor</i> bean to be added.
	 * @return {@code true} if the <i>implementor</i> bean is acceptable,
	 *         {@code false} if not.
	 */
	public boolean addImplementorBean(Object implementorBean)
	{
		List<Object> implementorBeans = getImplementorBeansList(
				implementorBean.getClass());

		if (implementorBeans == null)
			return false;

		implementorBeans.add(implementorBean);

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getImplementorBeans(Class<T> implementor)
	{
		return (Collection<T>) getImplementorBeansList(implementor);
	}

	/**
	 * Get the <i>implementor</i> beans list.
	 * 
	 * @param implementor
	 * @return
	 */
	protected List<Object> getImplementorBeansList(Class<?> implementor)
	{
		return this.implementorBeansMap.get(implementor);
	}

	/**
	 * Set the <i>implementor</i> beans list.
	 * 
	 * @param implementor
	 * @param implementorBeans
	 */
	protected void setImplementorBeansList(Class<?> implementor,
			List<Object> implementorBeans)
	{
		this.implementorBeansMap.put(implementor, implementorBeans);
	}
}
