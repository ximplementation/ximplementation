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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.ximplementation.Implement;
import org.ximplementation.Implementor;

/**
 * The <i>implementor</i> manager.
 * <p>
 * It managers a collection of <i>implementor</i>s which indexed by
 * <i>implementee</i>s.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-7
 *
 */
public class ImplementorManager
{
	private static final Class<?>[] DEFAULT_IMPLEMENTOR_INTERFACECLASSES = { Object.class };

	private Map<Class<?>, Set<Class<?>>> implementorsMap;

	/**
	 * Create an empty {@code ImplementorManager}.
	 */
	public ImplementorManager()
	{
		super();
		this.implementorsMap = new HashMap<Class<?>, Set<Class<?>>>();
	}

	/**
	 * Create an {@code ImplementorManager} with an <i>implementor</i> map.
	 * 
	 * @param implementorsMap
	 *            The <i>implementor</i>s map, the key is <i>implementee</i>,
	 *            the value is the <i>implementor</i> set of it.
	 */
	public ImplementorManager(Map<Class<?>, Set<Class<?>>> implementorsMap)
	{
		super();
		this.implementorsMap = implementorsMap;
	}

	/**
	 * Get all <i>implementee</i>s.
	 * 
	 * @return
	 */
	public Set<Class<?>> getImplementees()
	{
		return this.implementorsMap.keySet();
	}

	/**
	 * Get <i>implementor</i> set for the specified <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i>.
	 * @return The <i>implementor</i> set, {@code null} if none.
	 */
	public Set<Class<?>> get(Class<?> implementee)
	{
		return this.implementorsMap.get(implementee);
	}

	/**
	 * Add <i>implementor</i>s.
	 * <p>
	 * All <i>implementee</i>s will be resolved for each <i>implementor</i>.
	 * </p>
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be added.
	 */
	public void add(Class<?>... implementors)
	{
		doAddImplementor(implementors);
	}

	/**
	 * Add <i>implementor</i>s.
	 * <p>
	 * All <i>implementee</i>s will be resolved for each <i>implementor</i>.
	 * </p>
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be added.
	 */
	public void add(Set<Class<?>> implementors)
	{
		Class<?>[] implementorArray = toArray(implementors);
	
		doAddImplementor(implementorArray);
	}

	/**
	 * Add <i>implementor</i>s for the specified <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i>.
	 * @param implementors
	 *            The <i>implementor</i>s to be added.
	 */
	public void add(Class<?> implementee, Class<?>... implementors)
	{
		doAddImplementor(implementee, implementors);
	}

	/**
	 * Add <i>implementor</i>s for the specified <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i>.
	 * @param implementors
	 *            The <i>implementor</i>s to be added.
	 */
	public void add(Class<?> implementee, Set<Class<?>> implementors)
	{
		Class<?>[] implementorArray = toArray(implementors);

		doAddImplementor(implementee, implementorArray);
	}

	/**
	 * Remove all <i>implementor</i>s for the <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be removed.
	 */
	public void remove(Class<?> implementee)
	{
		this.implementorsMap.remove(implementee);
	}

	/**
	 * Remove the specified <i>implementor</i>s for the <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be removed.
	 * @param implementors
	 *            The <i>implementor</i>s to be removed.
	 */
	public void remove(Class<?> implementee, Class<?>... implementors)
	{
		Set<Class<?>> myImplementors = this.implementorsMap.get(implementee);

		if (myImplementors == null || myImplementors.isEmpty())
			return;

		for (Class<?> implementor : implementors)
		{
			myImplementors.remove(implementor);
		}
	}

	/**
	 * Remove the specified <i>implementor</i>s for the <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be removed.
	 * @param implementors
	 *            The <i>implementor</i>s to be removed.
	 */
	public void remove(Class<?> implementee, Set<Class<?>> implementors)
	{
		Set<Class<?>> myImplementors = this.implementorsMap.get(implementee);

		if (myImplementors == null || myImplementors.isEmpty())
			return;

		for (Class<?> implementor : implementors)
		{
			myImplementors.remove(implementor);
		}
	}

	/**
	 * Remove the <i>implementor</i> from all of its <i>implementee</i>s for
	 * each in the <i>implementor</i>s.
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be removed.
	 */
	public void clear(Class<?>... implementors)
	{
		doClear(implementors);
	}

	/**
	 * Remove the <i>implementor</i> from all of its <i>implementee</i>s for
	 * each in the <i>implementor</i>s.
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be removed.
	 */
	public void clear(Set<Class<?>> implementors)
	{
		Class<?>[] implementorArray = toArray(implementors);

		doClear(implementorArray);
	}

	/**
	 * Do add <i>implementor</i>s.
	 * 
	 * @param implementors
	 */
	protected void doAddImplementor(Class<?>... implementors)
	{
		for (Class<?> implementor : implementors)
		{
			Set<Class<?>> implementees = doResolveImplementees(implementor);

			for (Class<?> implementee : implementees)
			{
				doAddImplementor(implementee, implementor);
			}
		}
	}

	/**
	 * Do add <i>implementor</i>s.
	 * 
	 * @param implementee
	 * @param implementors
	 */
	protected void doAddImplementor(Class<?> implementee,
			Class<?>... implementors)
	{
		Set<Class<?>> myImplementors = this.implementorsMap.get(implementee);

		if (myImplementors == null)
		{
			myImplementors = new HashSet<Class<?>>();
			this.implementorsMap.put(implementee, myImplementors);
		}

		for (Class<?> implementor : implementors)
		{
			myImplementors.add(implementor);
		}
	}

	/**
	 * Do add <i>implementor</i>.
	 * 
	 * @param implementee
	 * @param implementor
	 */
	protected void doAddImplementor(Class<?> implementee, Class<?> implementor)
	{
		Set<Class<?>> myImplementors = this.implementorsMap.get(implementee);

		if (myImplementors == null)
		{
			myImplementors = new HashSet<Class<?>>();
			this.implementorsMap.put(implementee, myImplementors);
		}

		myImplementors.add(implementor);
	}

	/**
	 * Do clear <i>implementor</i>s.
	 * 
	 * @param implementors
	 */
	protected void doClear(Class<?>... implementors)
	{
		for (Class<?> implementor : implementors)
		{
			Set<Class<?>> implementees = doResolveImplementees(implementor);

			for (Class<?> implementee : implementees)
			{
				remove(implementee, implementor);
			}
		}
	}

	/**
	 * Resolve all <i>implementee</i>s about the <i>implementor</i>.
	 * 
	 * @param implementor
	 * @return
	 */
	protected Set<Class<?>> doResolveImplementees(Class<?> implementor)
	{
		return resolveImplementees(implementor);
	}

	/**
	 * Class set to array.
	 * 
	 * @param classes
	 * @return
	 */
	protected Class<?>[] toArray(Set<Class<?>> classes)
	{
		Class<?>[] classArray = new Class<?>[classes.size()];
		classes.toArray(classArray);

		return classArray;
	}

	/**
	 * Resolve all <i>implementee</i>s about the <i>implementor</i>, then
	 * returns them as a set.
	 * 
	 * @param implementor
	 *            The <i>implementor</i> to be resolved.
	 * @return The <i>implementee</i> set.
	 */
	public static Set<Class<?>> resolveImplementees(Class<?> implementor)
	{
		Set<Class<?>> implementees = new HashSet<Class<?>>();

		resolveImplementees(implementor, implementees);

		return implementees;
	}

	/**
	 * Resolve all <i>implementee</i>s about the <i>implementor</i>, and write
	 * them into {@code implementees} set.
	 * 
	 * @param implementor
	 * @param implementees
	 */
	protected static void resolveImplementees(Class<?> implementor,
			Set<Class<?>> implementees)
	{
		Queue<Class<?>> beSupereds = new LinkedList<Class<?>>();

		Class<?>[] annoImplementees = getAnnotationImplementees(
				implementor);
		Class<?>[] directSupers = getDiectLangSuperClasses(implementor);

		if (annoImplementees != null && annoImplementees.length > 0)
			beSupereds.addAll(Arrays.asList(annoImplementees));

		if (directSupers != null && directSupers.length > 0)
			beSupereds.addAll(Arrays.asList(directSupers));

		Class<?> beSupered = null;
		while ((beSupered = beSupereds.poll()) != null)
		{
			implementees.add(beSupered);

			Class<?>[] supers = getDiectLangSuperClasses(beSupered);

			if (supers != null && supers.length > 0)
			{
				beSupereds.addAll(Arrays.asList(supers));
			}
		}
	}

	/**
	 * Get super classes for the specified class.
	 * 
	 * @param clazz
	 * @return
	 */
	protected static Class<?>[] getDiectLangSuperClasses(Class<?> clazz)
	{
		Class<?>[] superClasses = clazz.getInterfaces();

		// ignore Object class
		if (clazz.getSuperclass() != null
				&& !Object.class.equals(clazz.getSuperclass()))
		{
			Class<?>[] _superClasses = new Class<?>[superClasses.length + 1];

			for (int i = 0; i < superClasses.length; i++)
			{
				_superClasses[i] = superClasses[i];
			}

			_superClasses[_superClasses.length - 1] = clazz.getSuperclass();

			superClasses = _superClasses;
		}

		return superClasses;
	}

	/**
	 * Get <i>implementee</i>s for the specified <i>implementor</i> by
	 * {@linkplain Implement} annotation.
	 * <p>
	 * If none, a zero length array will be returned.
	 * </p>
	 * 
	 * @param implementor
	 * @return
	 */
	protected static Class<?>[] getAnnotationImplementees(Class<?> implementor)
	{
		Class<?>[] implementees = {};

		Implementor implementorAno = implementor
				.getAnnotation(Implementor.class);

		if (implementorAno != null)
		{
			Class<?>[] annoImplementees = implementorAno.value();

			if (!Arrays.equals(DEFAULT_IMPLEMENTOR_INTERFACECLASSES,
					annoImplementees))
			{
				implementees = annoImplementees;
			}
		}

		return implementees;
	}
}
