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
 * Editable <i>implementor</i> bean factory.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 *
 */
public class EditableImplementorBeanFactory implements ImplementorBeanFactory
{
	private Map<Class<?>, List<Object>> implementorBeansMap = new HashMap<Class<?>, List<Object>>();

	public EditableImplementorBeanFactory()
	{
		super();
	}

	/**
	 * Add <i>implementor</i> beans.
	 * 
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be added.
	 */
	public void add(Object... implementorBeans)
	{
		for (Object implementorBean : implementorBeans)
			add(implementorBean.getClass(), implementorBean);
	}

	/**
	 * Add <i>implementor</i> beans.
	 * 
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be added.
	 */
	public void add(Collection<?> implementorBeans)
	{
		for (Object implementorBean : implementorBeans)
			add(implementorBean.getClass(), implementorBean);
	}

	/**
	 * Add <i>implementor</i> beans for given <i>implementor</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be added.
	 */
	public void add(Class<?> implementor, Object... implementorBeans)
	{
		List<Object> implementorBeaList = getImplementorBeansListWithCreation(
				implementor);

		for (Object implementorBean : implementorBeans)
			implementorBeaList.add(implementorBean);
	}

	/**
	 * Add <i>implementor</i> beans for given <i>implementor</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be added.
	 */
	public void add(Class<?> implementor, Collection<?> implementorBeans)
	{
		List<Object> implementorBeaList = getImplementorBeansListWithCreation(
				implementor);

		for (Object implementorBean : implementorBeans)
			implementorBeaList.add(implementorBean);
	}

	/**
	 * Add <i>implementor</i> bean.
	 * <p>
	 * Note that the <i>implementor</i> bean will not be added if the
	 * <i>implementor</i> {@code implementorBean.getClass()} is not added
	 * before.
	 * </p>
	 * 
	 * @param implementorBean
	 *            The <i>implementor</i> bean to be added.
	 * @return {@code true} if added, {@code false} if not.
	 */
	public boolean addFor(Object implementorBean)
	{
		return addFor(implementorBean.getClass(), implementorBean);
	}

	/**
	 * Add <i>implementor</i> beans for given <i>implementor</i>.
	 * <p>
	 * Note that the <i>implementor</i> beans will not be added if the
	 * <i>implementor</i> is not added before.
	 * </p>
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be added.
	 * @return {@code true} if added, {@code false} if not.
	 */
	public boolean addFor(Class<?> implementor, Object... implementorBeans)
	{
		List<Object> implementorBeaList = getImplementorBeansList(
				implementor);

		if (implementorBeaList == null)
			return false;

		for (Object implementorBean : implementorBeans)
			implementorBeaList.add(implementorBean);

		return true;
	}

	/**
	 * Add <i>implementor</i> beans for given <i>implementor</i>.
	 * <p>
	 * Note that the <i>implementor</i> beans will not be added if the
	 * <i>implementor</i> is not added before.
	 * </p>
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be added.
	 * @return {@code true} if added, {@code false} if not.
	 */
	public boolean addFor(Class<?> implementor, Collection<?> implementorBeans)
	{
		List<Object> implementorBeaList = getImplementorBeansList(
				implementor);

		if (implementorBeaList == null)
			return false;

		for (Object implementorBean : implementorBeans)
			implementorBeaList.add(implementorBean);

		return true;
	}

	/**
	 * Remove <i>implementor</i> beans.
	 * 
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be removed.
	 */
	public void remove(Object... implementorBeans)
	{
		for (Object implementorBean : implementorBeans)
			remove(implementorBean.getClass(), implementorBean);
	}

	/**
	 * Remove <i>implementor</i> beans.
	 * 
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be removed.
	 */
	public void remove(Collection<?> implementorBeans)
	{
		for (Object implementorBean : implementorBeans)
			remove(implementorBean.getClass(), implementorBean);
	}

	/**
	 * Remove <i>implementor</i> beans for given <i>implementor</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be removed.
	 */
	public void remove(Class<?> implementor, Object... implementorBeans)
	{
		List<Object> implementorBeaList = getImplementorBeansList(
				implementor);

		if (implementorBeaList == null)
			return;

		for (Object implementorBean : implementorBeans)
			implementorBeaList.remove(implementorBean);
	}

	/**
	 * Remove <i>implementor</i> beans for given <i>implementor</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be removed.
	 */
	public void remove(Class<?> implementor, Collection<?> implementorBeans)
	{
		List<Object> implementorBeaList = getImplementorBeansList(implementor);

		if (implementorBeaList == null)
			return;

		for (Object implementorBean : implementorBeans)
			implementorBeaList.remove(implementorBean);
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
	 * Get all the <i>implementor</i>s.
	 * 
	 * @return
	 */
	public Set<Class<?>> getAllImplementors()
	{
		return this.implementorBeansMap.keySet();
	}

	/**
	 * Get <i>implementor</i> beans list for given <i>implementor</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @return The <i>implementor</i> beans list, {@code null} if none.
	 */
	public List<Object> get(Class<?> implementor)
	{
		return this.implementorBeansMap.get(implementor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getImplementorBeans(Class<T> implementor)
	{
		return (Collection<T>) getImplementorBeansList(implementor);
	}

	/**
	 * Get the <i>implementor</i> beans list with creation.
	 * 
	 * @param implementor
	 * @return
	 */
	protected List<Object> getImplementorBeansListWithCreation(
			Class<?> implementor)
	{
		List<Object> implementorBeans = this.implementorBeansMap
				.get(implementor);

		if (implementorBeans == null)
		{
			implementorBeans = new ArrayList<Object>(1);
			this.implementorBeansMap.put(implementor, implementorBeans);
		}

		return implementorBeans;
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

	/**
	 * Create an instance by <i>implementor</i> beans array.
	 * 
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be built.
	 * @return
	 */
	public static EditableImplementorBeanFactory valueOf(
			Object... implementorBeans)
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		factory.add(implementorBeans);
		
		return factory;
	}

	/**
	 * Create an instance by <i>implementor</i> beans collection.
	 * 
	 * @param implementorBeans
	 *            The <i>implementor</i> beans to be built.
	 * @return
	 */
	public static EditableImplementorBeanFactory valueOf(
			Collection<?> implementorBeans)
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		factory.add(implementorBeans);

		return factory;
	}
	
	/**
	 * Create an instance by <i>implementor</i> beans map.
	 * 
	 * @param implementorBeansMap
	 *            The <i>implementor</i> beans map to be built.
	 * @return
	 */
	public static EditableImplementorBeanFactory valueOf(
			Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		for (Map.Entry<Class<?>, ? extends Collection<?>> entry : implementorBeansMap
				.entrySet())
		{
			factory.add(entry.getKey(), entry.getValue());
		}

		return factory;
	}
}
