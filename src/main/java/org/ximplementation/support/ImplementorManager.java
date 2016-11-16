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
import java.util.Collection;
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
	 * Get the <i>implementor</i>s map.
	 * 
	 * @return The <i>implementor</i>s map, the key is <i>implementee</i>, the
	 *         value is the <i>implementor</i> set of it.
	 */
	public Map<Class<?>, Set<Class<?>>> getImplementorsMap()
	{
		return implementorsMap;
	}

	/**
	 * Set the <i>implementor</i>s map.
	 * 
	 * @param implementorsMap
	 *            The <i>implementor</i>s map, the key is <i>implementee</i>,
	 *            the value is the <i>implementor</i> set of it.
	 */
	public void setImplementorsMap(Map<Class<?>, Set<Class<?>>> implementorsMap)
	{
		this.implementorsMap = implementorsMap;
	}

	/**
	 * Get all <i>implementee</i>s.
	 * 
	 * @return
	 */
	public Set<Class<?>> getAllImplementees()
	{
		return this.implementorsMap.keySet();
	}

	/**
	 * Get <i>implementor</i> set for given <i>implementee</i>.
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
	 * All <i>implementee</i>s will be resolved and indexed for each
	 * <i>implementor</i>.
	 * </p>
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be added.
	 */
	public void add(Class<?>... implementors)
	{
		doAdd(implementors);
	}

	/**
	 * Add <i>implementor</i>s.
	 * <p>
	 * All <i>implementee</i>s will be resolved and indexed for each
	 * <i>implementor</i>.
	 * </p>
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be added.
	 */
	public void add(Collection<? extends Class<?>> implementors)
	{
		Class<?>[] implementorArray = toArray(implementors);

		doAdd(implementorArray);
	}

	/**
	 * Add <i>implementor</i>s only for given <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i>.
	 * @param implementors
	 *            The <i>implementor</i>s to be added.
	 */
	public void addFor(Class<?> implementee, Class<?>... implementors)
	{
		doAddFor(implementee, implementors);
	}

	/**
	 * Add <i>implementor</i>s only for given <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i>.
	 * @param implementors
	 *            The <i>implementor</i>s to be added.
	 */
	public void addFor(Class<?> implementee,
			Collection<? extends Class<?>> implementors)
	{
		Class<?>[] implementorArray = toArray(implementors);

		doAddFor(implementee, implementorArray);
	}

	/**
	 * Remove the <i>implementor</i> from all of its <i>implementee</i>s.
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be removed.
	 */
	public void remove(Class<?>... implementors)
	{
		doRemove(implementors);
	}

	/**
	 * Remove the <i>implementor</i> from all of its <i>implementee</i>s.
	 * 
	 * @param implementors
	 *            The <i>implementor</i>s to be removed.
	 */
	public void remove(Collection<? extends Class<?>> implementors)
	{
		Class<?>[] implementorArray = toArray(implementors);
	
		doRemove(implementorArray);
	}

	/**
	 * Remove all <i>implementor</i>s only for the <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be removed.
	 */
	public void removeFor(Class<?> implementee)
	{
		this.implementorsMap.remove(implementee);
	}

	/**
	 * Remove <i>implementor</i>s only for given <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be removed.
	 * @param implementors
	 *            The <i>implementor</i>s to be removed.
	 */
	public void removeFor(Class<?> implementee, Class<?>... implementors)
	{
		doRemoveFor(implementee, implementors);
	}

	/**
	 * Remove <i>implementor</i>s only for given <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be removed.
	 * @param implementors
	 *            The <i>implementor</i>s to be removed.
	 */
	public void removeFor(Class<?> implementee,
			Collection<? extends Class<?>> implementors)
	{
		Set<Class<?>> myImplementors = this.implementorsMap.get(implementee);

		if (myImplementors == null || myImplementors.isEmpty())
			return;

		myImplementors.removeAll(implementors);
	}

	/**
	 * Return if there are <i>implementor</i>s for the <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be checked.
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean hasImplementor(Class<?> implementee)
	{
		Set<Class<?>> implementors = this.implementorsMap.get(implementee);
	
		return (implementors != null && implementors.size() > 0);
	}

	/**
	 * Do add <i>implementor</i>s.
	 * 
	 * @param implementors
	 */
	protected void doAdd(Class<?>... implementors)
	{
		for (Class<?> implementor : implementors)
		{
			Set<Class<?>> implementees = doResolveImplementees(implementor);

			for (Class<?> implementee : implementees)
			{
				doAddFor(implementee, implementor);
			}
		}
	}

	/**
	 * Do add <i>implementor</i>s.
	 * 
	 * @param implementee
	 * @param implementors
	 */
	protected void doAddFor(Class<?> implementee,
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
	 * Do remove <i>implementor</i>s.
	 * 
	 * @param implementors
	 */
	protected void doRemove(Class<?>... implementors)
	{
		for (Class<?> implementee : this.implementorsMap.keySet())
		{
			doRemoveFor(implementee, implementors);
		}
	}

	/**
	 * Do remove <i>implementor</i>s.
	 * 
	 * @param implementee
	 * @param implementors
	 */
	protected void doRemoveFor(Class<?> implementee, Class<?>... implementors)
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
	 * Class collection to array.
	 * 
	 * @param classes
	 * @return
	 */
	protected Class<?>[] toArray(Collection<? extends Class<?>> classes)
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
			Collection<Class<?>> implementees)
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
	 * Get super classes for given class.
	 * 
	 * @param clazz
	 * @return
	 */
	protected static Class<?>[] getDiectLangSuperClasses(Class<?> clazz)
	{
		Class<?>[] superClasses = clazz.getInterfaces();

		// ignore Object class
		if (clazz.getSuperclass() != null)
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
	 * Get <i>implementee</i>s for given <i>implementor</i> by
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
