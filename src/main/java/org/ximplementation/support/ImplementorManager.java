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
 * The <i>implementor</i> manager。
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-7
 *
 */
public class ImplementorManager
{
	private static final Class<?>[] DEFAULT_IMPLEMENTOR_INTERFACECLASSES = { Object.class };

	private Map<Class<?>, Set<Class<?>>> implementorsMap;

	private boolean onlyInterfaceForLang = true;

	public ImplementorManager()
	{
		super();
		this.implementorsMap = new HashMap<Class<?>, Set<Class<?>>>();
	}

	/**
	 * Is only resolve {@code implements} (exclude {@code extends}) language
	 * <i>implementee</i>.
	 * 
	 * @return
	 */
	public boolean isOnlyInterfaceForLang()
	{
		return onlyInterfaceForLang;
	}

	public void setOnlyInterfaceForLang(boolean onlyInterfaceForLang)
	{
		this.onlyInterfaceForLang = onlyInterfaceForLang;
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
	 * @return
	 */
	public Set<Class<?>> getImplementors(Class<?> implementee)
	{
		return this.implementorsMap.get(implementee);
	}

	/**
	 * Add <i>implementor</i>s.
	 * <p>
	 * All <i>implementee</i>s will be resolved for each.
	 * </p>
	 * 
	 * @param implementors
	 */
	public void addImplementor(Class<?>... implementors)
	{
		Set<Class<?>> implementees = new HashSet<Class<?>>();

		for (Class<?> implementor : implementors)
		{
			resolveImplementees(implementor, implementees,
					this.onlyInterfaceForLang);

			for (Class<?> implementee : implementees)
			{
				Set<Class<?>> myImplementors = this.implementorsMap
						.get(implementee);

				if (myImplementors == null)
				{
					myImplementors = new HashSet<Class<?>>();
					this.implementorsMap.put(implementee, myImplementors);
				}

				myImplementors.add(implementor);
			}

			implementees.clear();
		}
	}

	/**
	 * Resolve all <i>implementee</i>s for the <i>implementor</i>, and write
	 * them into {@code implementees} set.
	 * 
	 * @param implementor
	 * @param implementees
	 * @param onlyInterfaceForLang
	 */
	protected void resolveImplementees(Class<?> implementor,
			Set<Class<?>> implementees, boolean onlyInterfaceForLang)
	{
		Queue<Class<?>> beSupereds = new LinkedList<Class<?>>();

		beSupereds.add(implementor);
		
		Class<?>[] annoImplementees = getAnnotationImplementees(
				implementor);

		if (annoImplementees != null && annoImplementees.length > 0)
		{
			Collection<Class<?>> beAdds = Arrays.asList(annoImplementees);

			implementees.addAll(beAdds);
			beSupereds.addAll(beAdds);
		}

		Class<?> beSupered = null;
		while ((beSupered = beSupereds.poll()) != null)
		{
			Class<?>[] supers = getDiectLangSuperClasses(beSupered,
					onlyInterfaceForLang);

			if (supers != null && supers.length > 0)
			{
				Collection<Class<?>> beAdds = Arrays.asList(supers);

				implementees.addAll(beAdds);
				beSupereds.addAll(beAdds);
			}
		}
	}

	/**
	 * Get super classes for the specified class.
	 * 
	 * @param clazz
	 * @param onlyInterface
	 * @return
	 */
	protected Class<?>[] getDiectLangSuperClasses(Class<?> clazz,
			boolean onlyInterface)
	{
		Class<?>[] superClasses = clazz.getInterfaces();

		// ignore Object class
		if (!onlyInterface
				&& !Object.class.equals(clazz.getSuperclass()))
		{
			Class<?>[] _superClasses = new Class<?>[superClasses.length + 1];

			for (int i = 0; i < superClasses.length; i++)
			{
				_superClasses[i] = superClasses[i];
			}

			_superClasses[_superClasses.length - 1] = clazz.getSuperclass();
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
	protected Class<?>[] getAnnotationImplementees(Class<?> implementor)
	{
		Class<?>[] implementees = {};

		Implementor implementorAno = implementor
				.getAnnotation(Implementor.class);

		if (implementorAno != null)
		{
			Class<?> annoImplementee = implementorAno.value();
			Class<?>[] annoImplementees = implementorAno.implementees();

			if (!Object.class.equals(annoImplementee))
			{
				implementees = new Class<?>[] { annoImplementee };
			}
			else if (!Arrays.equals(DEFAULT_IMPLEMENTOR_INTERFACECLASSES,
					annoImplementees))
			{
				implementees = annoImplementees;
			}
		}

		return implementees;
	}
}
