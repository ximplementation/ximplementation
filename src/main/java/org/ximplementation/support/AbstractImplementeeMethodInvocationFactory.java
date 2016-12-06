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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract {@linkplain ImplementeeMethodInvocationFactory}.
 * 
 * @author earthangry@gmail.com
 * @date 2016-12-06
 *
 */
public abstract class AbstractImplementeeMethodInvocationFactory
		implements ImplementeeMethodInvocationFactory
{
	protected static final Class<?>[] EMPTY_CLASS_ARRAY = {};

	protected ConcurrentHashMap<ImplementMethodInfo, Class<?>[]> implementMethodParamTypes = new ConcurrentHashMap<ImplementMethodInfo, Class<?>[]>();

	public AbstractImplementeeMethodInvocationFactory()
	{
		super();
	}

	/**
	 * Find {@linkplain ImplementInfo}.
	 * 
	 * @param implementation
	 * @param implementeeMethod
	 * @return
	 */
	protected ImplementInfo findImplementInfo(Implementation<?> implementation,
			Method implementeeMethod)
	{
		return implementation.getImplementInfo(implementeeMethod);
	}

	/**
	 * Invoke validity method.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementMethodInfo
	 * @param validityMethod
	 * @param validityParams
	 * @param implementorBean
	 * @return
	 * @throws Throwable
	 */
	protected boolean invokeValidityMethod(Implementation<?> implementation,
			ImplementInfo implementInfo,
			ImplementMethodInfo implementMethodInfo,
			Method validityMethod,
			Object[] validityParams, Object implementorBean) throws Throwable
	{
		if (!validityMethod.isAccessible())
			validityMethod.setAccessible(true);

		return Boolean.TRUE.equals(validityMethod.invoke(implementorBean, validityParams));
	}

	/**
	 * Invoke priority method.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementMethodInfo
	 * @param priorityMethod
	 * @param priorityParams
	 * @param implementorBean
	 * @return
	 * @throws Throwable
	 */
	protected int invokePriorityMethod(Implementation<?> implementation,
			ImplementInfo implementInfo,
			ImplementMethodInfo implementMethodInfo,
			Method priorityMethod, Object[] priorityParams,
			Object implementorBean) throws Throwable
	{
		if (!priorityMethod.isAccessible())
			priorityMethod.setAccessible(true);

		Object priority = priorityMethod.invoke(implementorBean,
				priorityParams);

		return ((Number) priority).intValue();
	}

	/**
	 * Return if {@linkplain ImplementMethodInfo#getImplementMethod()} parameter
	 * types are valid for the given parameter types.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementMethodInfo
	 * @param implementeeMethodParamTypes
	 * @return
	 */
	protected boolean isImplementMethodParamTypeValid(
			Implementation<?> implementation, ImplementInfo implementInfo,
			ImplementMethodInfo implementMethodInfo,
			Class<?>[] implementeeMethodParamTypes)
	{
		Class<?>[] myParamTypes = getActualImplementMethodParamTypes(
				implementation, implementInfo, implementMethodInfo);

		if (myParamTypes == null || myParamTypes.length == 0)
			return true;

		if (myParamTypes.length > implementeeMethodParamTypes.length)
			return false;

		Class<?>[] inputParamTypes = copyArrayByIndex(
				implementeeMethodParamTypes,
				implementMethodInfo.getParamIndexes());

		for (int i = 0; i < myParamTypes.length; i++)
		{
			Class<?> myParamType = myParamTypes[i];
			Class<?> inputParamType = inputParamTypes[i];

			// the input parameter type is null, primitive implement method
			// parameter type is invalid
			if (inputParamType == null)
			{
				if (myParamType.isPrimitive())
					return false;
			}
			else
			{
				if (!toWrapperType(myParamType)
						.isAssignableFrom(toWrapperType(inputParamType)))
					return false;
			}
		}

		return true;
	}

	/**
	 * Compare two {@linkplain ImplementMethodInfo}'s priority which both method
	 * parameter types are assignable to {@code implementeeMethodParamTypes}.
	 * <p>
	 * Return {@code >0} if {@code first} is higher; returns {@code <0} if
	 * {@code second} is higher; returns {@code 0} if they are the same.
	 * </p>
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementeeMethodParamTypes
	 * @param first
	 * @param second
	 * @return
	 */
	protected int compareImplementMethodInfoPriority(
			Implementation<?> implementation,
			ImplementInfo implementInfo,
			Class<?>[] implementeeMethodParamTypes, 
			ImplementMethodInfo first, ImplementMethodInfo second)
	{
		int priority = compareImplementMethodParamTypePriority(implementation,
				implementInfo, implementeeMethodParamTypes, first, second);
	
		// the one with @Validity is higher
		if (priority == 0)
		{
			boolean firstHasValidity = first.hasValidityMethod();
			boolean secondHasValidity = second.hasValidityMethod();
	
			if (firstHasValidity && !secondHasValidity)
				priority = 1;
			else if (!firstHasValidity && secondHasValidity)
				priority = -1;
		}
	
		if (priority == 0)
		{
			priority = compareImplementorPriority(implementation,
					first.getImplementor(), second.getImplementor());
		}
	
		return priority;
	}

	/**
	 * Compare two {@linkplain ImplementMethodInfo}'s method parameter type
	 * priority which both are assignable to {@code implementeeMethodParamTypes}
	 * .
	 * <p>
	 * Return {@code >0} if {@code first} is higher; returns {@code <0} if
	 * {@code second} is higher; returns {@code 0} if they are the same.
	 * </p>
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementeeMethodParamTypes
	 * @param first
	 * @param second
	 * @return
	 */
	protected int compareImplementMethodParamTypePriority(
			Implementation<?> implementation,
			ImplementInfo implementInfo, Class<?>[] implementeeMethodParamTypes,
			ImplementMethodInfo first, ImplementMethodInfo second)
	{
		// which is closer to implementee method parameters
		int firstCloserCount = 0;
		int secondCloserCount = 0;
	
		Class<?>[] firstParamTypes = getActualImplementMethodParamTypes(
				implementation, implementInfo, first);
		int[] firstParamIndexes = first.getParamIndexes();
		Class<?>[] secondParamTypes = getActualImplementMethodParamTypes(
				implementation, implementInfo, second);
		int[] secondParamIndexes = second.getParamIndexes();
	
		for (int i = 0; i < firstParamTypes.length; i++)
		{
			Class<?> firstType = firstParamTypes[i];
	
			int secondParamIndex = -1;
	
			if (i < secondParamIndexes.length
					&& firstParamIndexes[i] == secondParamIndexes[i])
				secondParamIndex = i;
			else
			{
				for (int j = 0; j < secondParamIndexes.length; j++)
				{
					if (firstParamIndexes[i] == secondParamIndexes[j])
					{
						secondParamIndex = j;
						break;
					}
				}
			}
	
			if (secondParamIndex > -1)
			{
				Class<?> secondType = secondParamTypes[secondParamIndex];
	
				// the same closer
				if (firstType.equals(secondType))
					;
				// sub type second is closer
				else if (firstType.isAssignableFrom(secondType))
				{
					secondCloserCount++;
				}
				// sub type first is closer
				else if (secondType.isAssignableFrom(firstType))
				{
					firstCloserCount++;
				}
				else
				{
					// primitive type is closer
					if (firstType.isPrimitive())
						firstCloserCount++;
					else if (secondType.isPrimitive())
						secondCloserCount++;
				}
			}
		}
	
		return (firstCloserCount - secondCloserCount);
	}

	/**
	 * Compare two <i>implementor</i>'s priority.
	 * <p>
	 * Return {@code >0} if {@code firstImplementor} is higher; returns
	 * {@code <0} if {@code secondImplementor} is higher; returns {@code 0} if
	 * they are the same.
	 * </p>
	 * 
	 * @param implementation
	 * @param firstImplementor
	 * @param secondImplementor
	 * @return
	 */
	protected int compareImplementorPriority(Implementation<?> implementation,
			Class<?> firstImplementor, Class<?> secondImplementor)
	{
		String implementeePkg = implementation.getImplementee().getPackage()
				.getName();
	
		boolean firstSamePkg = firstImplementor.getPackage().getName()
				.startsWith(implementeePkg);
	
		boolean secondSamePkg = secondImplementor.getPackage().getName()
				.startsWith(implementeePkg);
	
		// the same priority if they both or not with implementee, the one not
		// is higher
		if (firstSamePkg)
		{
			return (secondSamePkg ? 0 : -1);
		}
		else
			return (secondSamePkg ? 1 : 0);
	}

	/**
	 * Get the actual <i>implement method</i> parameter types with all generic
	 * type erased.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementMethodInfo
	 * @return
	 */
	protected Class<?>[] getActualImplementMethodParamTypes(
			Implementation<?> implementation, ImplementInfo implementInfo,
			ImplementMethodInfo implementMethodInfo)
	{
		Class<?> implementor = implementMethodInfo.getImplementor();
		Method implementMethod = implementMethodInfo.getImplementMethod();

		// if method is declared in the implementor, the parameter types are
		// erased, return them is ok
		if (implementMethod.getDeclaringClass().equals(implementor))
			return implementMethodInfo.getParamTypes();

		Class<?>[] paramTypes = implementMethodInfo.getParamTypes();
		Type[] gparamTypes = implementMethodInfo.getGenericParamTypes();
		
		//not generic actual
		if(Arrays.equals(paramTypes, gparamTypes))
			return paramTypes;

		// if method is declared in ancestor class, type variables should be
		// resolved

		Class<?>[] actualParamTypes = getCachedActualImplementMethodParamTypes(
				implementMethodInfo);

		if (actualParamTypes != null)
			return actualParamTypes;

		Map<TypeVariable<?>, Type> typeVariablesMap = resolveTypeParams(
				implementor);

		actualParamTypes = new Class<?>[paramTypes.length];

		for (int i = 0; i < paramTypes.length; i++)
		{
			Class<?> paramType = paramTypes[i];
			Type gparamType = gparamTypes[i];

			// the element is not generic
			if (gparamType.equals(paramType))
				actualParamTypes[i] = paramType;
			else
			{
				actualParamTypes[i] = erase(gparamType, typeVariablesMap);
			}
		}

		cacheActualImplementMethodParamTypes(implementMethodInfo,
				actualParamTypes);

		return actualParamTypes;
	}

	/**
	 * Get cached actual <i>implement method</i> parameter types.
	 * 
	 * @param implementMethodInfo
	 * @return
	 */
	protected Class<?>[] getCachedActualImplementMethodParamTypes(
			ImplementMethodInfo implementMethodInfo)
	{
		return this.implementMethodParamTypes.get(implementMethodInfo);
	}

	/**
	 * Cache actual <i>implement method</i> parameter types.
	 * 
	 * @param implementMethodInfo
	 * @param actualImplementMethodParamTypes
	 */
	protected void cacheActualImplementMethodParamTypes(
			ImplementMethodInfo implementMethodInfo,
			Class<?>[] actualImplementMethodParamTypes)
	{
		this.implementMethodParamTypes.put(implementMethodInfo,
				actualImplementMethodParamTypes);
	}

	/**
	 * Extract type array.
	 * <P>
	 * Return an array of {@code 0} length if the {@code objs} is {@code null}
	 * or empty.
	 * </P>
	 * 
	 * @param objs
	 * @return
	 */
	protected Class<?>[] extractTypes(Object[] objs)
	{
		if (objs == null)
			return EMPTY_CLASS_ARRAY;
	
		Class<?>[] classes = new Class<?>[objs.length];
	
		for (int i = 0; i < objs.length; i++)
		{
			classes[i] = (objs[i] == null ? null : objs[i].getClass());
		}
	
		return classes;
	}

	/**
	 * Copy array by indexes.
	 * <P>
	 * Return an array of {@code 0} length if the {@code indexes} is
	 * {@code null} or empty.
	 * </P>
	 * 
	 * @param source
	 * @param indexes
	 * @return
	 */
	protected Class<?>[] copyArrayByIndex(Class<?>[] source,
			int[] indexes)
	{
		if (indexes == null)
			return EMPTY_CLASS_ARRAY;
	
		Class<?>[] copied = new Class<?>[indexes.length];
	
		for (int i = 0; i < indexes.length; i++)
		{
			copied[i] = source[indexes[i]];
		}
	
		return copied;
	}

	/**
	 * Resolve type parameter map.
	 * 
	 * @param type
	 * @return
	 */
	protected Map<TypeVariable<?>, Type> resolveTypeParams(Type type)
	{
		return TypeUtil.resolveTypeParams(type);
	}

	/**
	 * Erase the given {@linkplain Type} to {@linkplain Class}.
	 * 
	 * @param type
	 * @param typeVariablesMap
	 * @return
	 */
	protected Class<?> erase(Type type,
			Map<TypeVariable<?>, Type> typeVariablesMap)
	{
		return TypeUtil.erase(type, typeVariablesMap);
	}

	/**
	 * Return the wrapper type of the given type if it is a primitive, returns
	 * itself if not.
	 * 
	 * @param type
	 * @return
	 */
	protected Class<?> toWrapperType(Class<?> type)
	{
		return TypeUtil.toWrapperType(type);
	}
}
