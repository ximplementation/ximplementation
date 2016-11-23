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
 * instances of them can be added.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 *
 */
public class PreparedImplementorBeanFactory implements ImplementorBeanFactory
{
	private Map<Class<?>, List<Object>> implementorBeansMap = new HashMap<Class<?>, List<Object>>();

	/**
	 * Create an empty instance.
	 */
	public PreparedImplementorBeanFactory()
	{
		super();
	}

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

		Set<Class<?>> implementors = implementation.getImplementors();

		for (Class<?> implementor : implementors)
			initImplementorBeansList(implementor);
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
	 * Prepare <i>implementor</i>s.
	 * <p>
	 * After prepared, beans of them then can be added.
	 * </p>
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be prepared.
	 */
	public void prepare(Class<?>... implementors)
	{
		for (Class<?> implementor : implementors)
			initImplementorBeansList(implementor);
	}

	/**
	 * Prepare <i>implementor</i>s.
	 * <p>
	 * After prepared, beans of them then can be added.
	 * </p>
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be prepared.
	 */
	public void prepare(Collection<? extends Class<?>> implementors)
	{
		for (Class<?> implementor : implementors)
			initImplementorBeansList(implementor);
	}

	/**
	 * Prepare <i>implementor</i>s of an {@linkplain Implementation}.
	 * <p>
	 * After prepared, beans of them then can be added.
	 * </p>
	 * 
	 * @param implementation
	 *            The {@linkplain Implementation} whose <i>implementor</i>s to
	 *            be prepared.
	 */
	public void prepare(Implementation<?> implementation)
	{
		Set<Class<?>> implementors = implementation.getImplementors();

		for (Class<?> implementor : implementors)
			initImplementorBeansList(implementor);
	}

	/**
	 * Return if given <i>implementor</i> is acceptable.
	 * 
	 * @param implementor
	 *            The <i>implementor</i> to be checked.
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean accept(Class<?> implementor)
	{
		return this.implementorBeansMap.containsKey(implementor);
	}

	/**
	 * Return if given <i>implementor</i> bean is acceptable.
	 * 
	 * @param implementorBean
	 *            The <i>implementor</i> bean to be checked.
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean accept(Object implementorBean)
	{
		return this.implementorBeansMap.containsKey(implementorBean.getClass());
	}

	/**
	 * Return if given <i>implementor</i> bean is added.
	 * 
	 * @param implementorBean
	 *            The <i>implementor</i> bean to be checked.
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean contains(Object implementorBean)
	{
		return contains(implementorBean.getClass(), implementorBean);
	}

	/**
	 * Return if given <i>implementor</i> bean is added.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBean
	 *            The <i>implementor</i> bean to be checked.
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean contains(Class<?> implementor, Object implementorBean)
	{
		List<Object> implementorBeaList = getImplementorBeansList(implementor);

		if (implementorBeaList == null)
			return false;

		for (int i = 0, len = implementorBeaList.size(); i < len; i++)
		{
			if (implementorBeaList.get(i).equals(implementorBean))
				return true;
		}

		return false;
	}

	/**
	 * Add an <i>implementor</i> bean.
	 * 
	 * @param implementorBean
	 *            The <i>implementor</i> bean to be added.
	 * @return {@code true} if the <i>implementor</i> bean is acceptable and
	 *         added, {@code false} if not.
	 */
	public boolean add(Object implementorBean)
	{
		return add(implementorBean.getClass(), implementorBean);
	}

	/**
	 * Add <i>implementor</i> beans for given <i>implementor</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be added.
	 * @return {@code true} if the <i>implementor</i> is acceptable and the
	 *         <i>implementor</i> beans are added, {@code false} if not.
	 */
	public boolean add(Class<?> implementor, Object... implementorBeans)
	{
		List<Object> implementorBeaList = getImplementorBeansList(implementor);

		if (implementorBeaList == null)
			return false;

		for (Object implementorBean : implementorBeans)
			implementorBeaList.add(implementorBean);

		return true;
	}

	/**
	 * Add <i>implementor</i> beans for given <i>implementor</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be added.
	 * @return {@code true} if the <i>implementor</i> is acceptable and the
	 *         <i>implementor</i> beans are added, {@code false} if not.
	 */
	public boolean add(Class<?> implementor, Collection<?> implementorBeans)
	{
		List<Object> implementorBeaList = getImplementorBeansList(implementor);

		if (implementorBeaList == null)
			return false;

		for (Object implementorBean : implementorBeans)
			implementorBeaList.add(implementorBean);

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getImplementorBeans(Class<T> implementor)
	{
		return (Collection<T>) getImplementorBeansList(implementor);
	}

	/**
	 * Init <i>implementor</i> beans list.
	 * 
	 * @param implementor
	 */
	protected void initImplementorBeansList(Class<?> implementor)
	{
		this.implementorBeansMap.put(implementor, new ArrayList<Object>(1));
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
