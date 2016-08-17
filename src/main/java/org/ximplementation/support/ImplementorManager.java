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
import java.util.Map;
import java.util.Set;

import org.ximplementation.Implementor;

/**
 * 实现者管理类。
 * 
 * @author earthangry@gmail.com
 * @createDate 2015年12月7日
 *
 */
public class ImplementorManager
{
	private static final Class<?>[] DEFAULT_IMPLEMENTOR_INTERFACECLASSES = { Object.class };

	private Map<Class<?>, Set<Class<?>>> implementorsMap;

	/** 是否只解析interface作为implementee */
	private boolean onlyResolveInterface = true;

	public ImplementorManager()
	{
		super();
		this.implementorsMap = new HashMap<Class<?>, Set<Class<?>>>();
	}

	/**
	 * 是否仅解析{@code interface}作为接口。
	 * 
	 * @return
	 */
	public boolean isOnlyResolveInterface()
	{
		return onlyResolveInterface;
	}

	public void setOnlyResolveInterface(boolean onlyResolveInterface)
	{
		this.onlyResolveInterface = onlyResolveInterface;
	}

	/**
	 * 获取所有<i>接口</i>。
	 * 
	 * @return
	 */
	public Set<Class<?>> getImplementees()
	{
		return this.implementorsMap.keySet();
	}

	/**
	 * 获取指定<i>接口</i>的<i>实现者</i>集合。
	 * 
	 * @param implementee
	 * @return
	 */
	public Set<Class<?>> getImplementors(Class<?> implementee)
	{
		return this.implementorsMap.get(implementee);
	}

	/**
	 * 添加<i>实现者</i>集合。
	 * 
	 * @param implementors
	 */
	public void addImplementor(Class<?>... implementors)
	{
		for (Class<?> implementor : implementors)
		{
			Class<?>[] implementees = resolveDirectImplementees(implementor);

			if (implementees != null && implementees.length != 0)
				addImplementor(implementees, implementor);
		}
	}

	/**
	 * 添加一个<i>接口者</i>类映射。
	 * 
	 * @param implementees
	 * @param implementor
	 */
	protected void addImplementor(Class<?>[] implementees, Class<?> implementor)
	{
		if (implementees == null || implementees.length == 0)
			return;

		for (Class<?> implementee : implementees)
		{
			Set<Class<?>> implementors = this.implementorsMap.get(implementee);

			if (implementors == null)
			{
				implementors = new HashSet<Class<?>>();
				this.implementorsMap.put(implementee, implementors);
			}

			implementors.add(implementor);

			Class<?>[] parentImplementees = resolveDirectImplementees(
					implementee);

			if (parentImplementees != null && parentImplementees.length > 0)
				addImplementor(parentImplementees, implementor);
		}
	}

	/**
	 * 解析<i>实现者</i>所直接实现的<i>接口</i>类。
	 * <p>
	 * 如果无任何接口实现，此方法将返回空数组。
	 * </p>
	 * 
	 * @param implementor
	 * @return
	 */
	protected Class<?>[] resolveDirectImplementees(Class<?> implementor)
	{
		Class<?>[] implementees = resolveDiectLanguageImplementees(implementor);

		Implementor implementorAno = implementor.getAnnotation(Implementor.class);

		if (implementorAno != null)
		{
			Class<?>[] _implementees = {};

			Class<?> annoImplementee = implementorAno.value();
			Class<?>[] annoImplementees = implementorAno.implementees();

			if (!Object.class.equals(annoImplementee))
			{
				_implementees = new Class<?>[] { annoImplementee };
			}
			else if (!Arrays.equals(DEFAULT_IMPLEMENTOR_INTERFACECLASSES,
					annoImplementees))
			{
				_implementees = annoImplementees;
			}

			if (implementees == null || implementees.length == 0)
				implementees = _implementees;
			else if (_implementees != null && _implementees.length > 0)
			{
				Class<?>[] newImplementees = new Class<?>[_implementees.length
						+ implementees.length];

				for (int i = 0; i < _implementees.length; i++)
					newImplementees[i] = _implementees[i];

				for (int i = 0; i < implementees.length; i++)
					newImplementees[_implementees.length + i] = implementees[i];

				implementees = newImplementees;
			}
		}

		return implementees;
	}

	/**
	 * 解析<i>实现者</i>通过{@code implements}或者{@code extends}所直接实现的<i>接口</i>数组。
	 * <p>
	 * 如果无任何接口实现，此方法将返回空数组。
	 * </p>
	 * 
	 * @param implementor
	 * @return
	 */
	protected Class<?>[] resolveDiectLanguageImplementees(Class<?> implementor)
	{
		Class<?>[] implementees = implementor.getInterfaces();

		if (!this.isOnlyResolveInterface()
				&& !Object.class.equals(implementor.getSuperclass()))
		{
			Class<?>[] newImplementees = new Class<?>[implementees.length + 1];

			for (int i = 0; i < implementees.length; i++)
			{
				newImplementees[i] = implementees[i];
			}

			newImplementees[newImplementees.length - 1] = implementor
					.getSuperclass();
		}

		return implementees;
	}
}
