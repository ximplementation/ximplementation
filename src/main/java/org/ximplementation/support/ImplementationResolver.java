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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.NotImplement;
import org.ximplementation.ParamIndex;
import org.ximplementation.Priority;
import org.ximplementation.Refered;
import org.ximplementation.Validity;

/**
 * {@linkplain Implementation} resolver.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
 *
 */
public class ImplementationResolver
{
	private Map<Type, Map<TypeVariable<?>, Type>> typeVariablesMap = new WeakHashMap<Type, Map<TypeVariable<?>, Type>>();

	public ImplementationResolver()
	{
		super();
	}

	/**
	 * Resolve {@linkplain Implementation} for an <i>implementee</i> and its
	 * <i>implementor</i>s.
	 * 
	 * @param implementee
	 * @param implementors
	 * @return
	 * @throws ImplementationResolveException
	 */
	public <T> Implementation<T> resolve(Class<T> implementee,
			Class<?>... implementors)
			throws ImplementationResolveException
	{
		Set<Class<?>> simplementors = new HashSet<Class<?>>(
				Arrays.asList(implementors));

		return doResolve(implementee, simplementors);
	}

	/**
	 * Resolve {@linkplain Implementation} for an <i>implementee</i> and its
	 * <i>implementor</i>s.
	 * 
	 * @param implementee
	 * @param implementors
	 * @return
	 * @throws ImplementationResolveException
	 */
	public <T> Implementation<T> resolve(Class<T> implementee,
			Set<Class<?>> implementors) throws ImplementationResolveException
	{
		return doResolve(implementee, implementors);
	}

	/**
	 * Do resolving.
	 * 
	 * @param implementee
	 * @param implementors
	 * @return
	 * @throws ImplementationResolveException
	 */
	protected <T> Implementation<T> doResolve(Class<T> implementee,
			Set<Class<?>> implementors)
			throws ImplementationResolveException
	{
		Implementation<T> implementation = new Implementation<T>();

		List<ImplementInfo> implementInfos = new ArrayList<ImplementInfo>();

		Method[] implementeeMethods = getCandidateImplementeeMethods(
				implementee);

		for (int i = 0; i < implementeeMethods.length; i++)
		{
			Method implementeeMethod = implementeeMethods[i];

			if (!isImplementeeMethod(implementee, implementeeMethod))
				continue;

			ImplementInfo implementInfo = resolveImplementInfo(implementee, implementeeMethods, implementeeMethod,
					implementors);

			implementInfos.add(implementInfo);
		}

		ImplementInfo[] implementInfoArray = new ImplementInfo[implementInfos.size()];
		implementInfos.toArray(implementInfoArray);

		implementation.setImplementee(implementee);
		implementation.setImplementInfos(implementInfoArray);

		return implementation;
	}

	/**
	 * Resolve {@linkplain ImplementInfo}.
	 * 
	 * @param implementee
	 * @param implementeeMethods
	 * @param implementeeMethod
	 * @param implementors
	 * @return
	 */
	protected ImplementInfo resolveImplementInfo(Class<?> implementee, Method[] implementeeMethods, Method implementeeMethod,
			Set<Class<?>> implementors)
	{
		ImplementInfo implementInfo = new ImplementInfo(implementeeMethod);

		List<ImplementMethodInfo> implementMethodInfos = new ArrayList<ImplementMethodInfo>();

		for (Class<?> implementor : implementors)
		{
			if (!isImplementor(implementee, implementor))
				continue;

			Collection<ImplementMethodInfo> myImplementMethodInfos = resolveImplementMethodInfo(implementee,
					implementeeMethods, implementeeMethod, implementor);

			if (myImplementMethodInfos != null && !myImplementMethodInfos.isEmpty())
				implementMethodInfos.addAll(myImplementMethodInfos);
		}

		ImplementMethodInfo[] implementMethodInfoArray = new ImplementMethodInfo[implementMethodInfos.size()];
		implementMethodInfos.toArray(implementMethodInfoArray);

		implementInfo.setImplementMethodInfos(implementMethodInfoArray);

		return implementInfo;
	}

	/**
	 * Resolve {@linkplain ImplementMethodInfo}.
	 * 
	 * @param implementee
	 * @param implementeeMethods
	 * @param implementeeMethod
	 * @param implementor
	 * @return
	 */
	protected Collection<ImplementMethodInfo> resolveImplementMethodInfo(Class<?> implementee, Method[] implementeeMethods,
			Method implementeeMethod, Class<?> implementor)
	{
		List<ImplementMethodInfo> implementMethodInfos = new ArrayList<ImplementMethodInfo>();

		String implementeeMethodName = implementeeMethod.getName();
		String implementeeMethodSignature = getMethodSignature(implementee, implementeeMethod);
		String implementeeMethodRefered = getRefered(implementeeMethod);

		Method[] implementMethods = getCandidateImplementMethods(implementor);

		for (int i = 0; i < implementMethods.length; i++)
		{
			Method implementMethod = implementMethods[i];

			if (isImplementMethod(implementee, implementeeMethod, implementeeMethodName, implementeeMethodSignature,
					implementeeMethodRefered, implementor, implementMethod))
			{
				ImplementMethodInfo implementMethodInfo = buildImplementMethodInfo(implementee, implementeeMethod,
						implementor, implementMethod);

				implementMethodInfos.add(implementMethodInfo);
			}
		}

		return implementMethodInfos;
	}

	/**
	 * Build {@linkplain ImplementMethodInfo}.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementor
	 * @param implementMethod
	 * @return
	 */
	protected ImplementMethodInfo buildImplementMethodInfo(Class<?> implementee, Method implementeeMethod,
			Class<?> implementor, Method implementMethod)
	{
		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(implementor, implementMethod);

		resolveImplementMethodInfoProperties(implementee, implementeeMethod, implementMethodInfo);

		return implementMethodInfo;
	}

	/**
	 * Resolve {@linkplain ImplementMethodInfo} properties.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementMethodInfo
	 */
	protected void resolveImplementMethodInfoProperties(Class<?> implementee, Method implementeeMethod,
			ImplementMethodInfo implementMethodInfo)
	{
		resolveImplementMethodInfoParamTypes(implementee, implementeeMethod, implementMethodInfo);
		resolveImplementMethodInfoGenericParamTypes(implementee, implementeeMethod, implementMethodInfo);
		resolveImplementMethodInfoParamIndexes(implementee, implementeeMethod, implementMethodInfo);
		resolveImplementMethodInfoValidity(implementee, implementeeMethod, implementMethodInfo);
		resolveImplementMethodInfoPriority(implementee, implementeeMethod, implementMethodInfo);
	}

	/**
	 * Resolve {@linkplain ImplementMethodInfo} 's
	 * {@linkplain ImplementMethodInfo#getParamTypes()} property.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementMethodInfo
	 */
	protected void resolveImplementMethodInfoParamTypes(Class<?> implementee, Method implementeeMethod,
			ImplementMethodInfo implementMethodInfo)
	{
		Class<?>[] paramTypes = implementMethodInfo.getImplementMethod().getParameterTypes();

		implementMethodInfo.setParamTypes(paramTypes);
	}

	/**
	 * Resolve {@linkplain ImplementMethodInfo} 's
	 * {@linkplain ImplementMethodInfo#getGenericParamTypes()} property.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementMethodInfo
	 */
	protected void resolveImplementMethodInfoGenericParamTypes(Class<?> implementee, Method implementeeMethod,
			ImplementMethodInfo implementMethodInfo)
	{
		Type[] genericParamTypes = implementMethodInfo.getImplementMethod().getGenericParameterTypes();

		implementMethodInfo.setGenericParamTypes(genericParamTypes);
	}

	/**
	 * Resolve {@linkplain ImplementMethodInfo} 's
	 * {@linkplain ImplementMethodInfo#getParamIndexes()} property.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementMethodInfo
	 */
	protected void resolveImplementMethodInfoParamIndexes(Class<?> implementee, Method implementeeMethod,
			ImplementMethodInfo implementMethodInfo)
	{
		int[] paramIndexes = getMethodParamIndexes(implementMethodInfo.getImplementor(),
				implementMethodInfo.getImplementMethod());

		implementMethodInfo.setParamIndexes(paramIndexes);
	}

	/**
	 * Resolve {@linkplain ImplementMethodInfo} 's
	 * {@linkplain ImplementMethodInfo#getValidityMethod()} and
	 * {@linkplain ImplementMethodInfo#getValidityParamIndexes()} property.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementMethodInfo
	 */
	protected void resolveImplementMethodInfoValidity(Class<?> implementee, Method implementeeMethod,
			ImplementMethodInfo implementMethodInfo)
	{
		Class<?> implementor = implementMethodInfo.getImplementor();
		Method implementMethod = implementMethodInfo.getImplementMethod();

		Validity validity = getAnnotation(implementMethod, Validity.class);

		if (validity != null)
		{
			String validityRef = validity.value();

			Method validityMethod = findReferedMethod(implementor, validityRef, boolean.class, Boolean.class);

			if (validityMethod == null)
				throw new ImplementationResolveException("Class [" + implementor
						+ "] : No method is found for [@Validity(\"" + validityRef + "\")] reference");

			int[] validityParamIndexes = getMethodParamIndexes(implementor, validityMethod);

			implementMethodInfo.setValidityMethod(validityMethod);
			implementMethodInfo.setValidityParamIndexes(validityParamIndexes);
		}
	}

	/**
	 * Resolve {@linkplain ImplementMethodInfo} 's
	 * {@linkplain ImplementMethodInfo#getPriorityValue()},
	 * {@linkplain ImplementMethodInfo#getPriorityMethod()} and
	 * {@linkplain ImplementMethodInfo#getPriorityParamIndexes()} property.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementMethodInfo
	 */
	protected void resolveImplementMethodInfoPriority(Class<?> implementee, Method implementeeMethod,
			ImplementMethodInfo implementMethodInfo)
	{
		Class<?> implementor = implementMethodInfo.getImplementor();
		Method implementMethod = implementMethodInfo.getImplementMethod();

		Priority priority = getAnnotation(implementMethod, Priority.class);

		if (priority != null)
		{
			int priorityValue = priority.value();
			Method priorityMethod = null;
			int[] priorityParamIndexes = null;

			String priorityMethodRef = priority.method();

			if (!priorityMethodRef.isEmpty())
			{
				priorityMethod = findReferedMethod(implementor, priorityMethodRef, Number.class);

				if (priorityMethod == null)
					throw new ImplementationResolveException("Class [" + implementor
							+ "] : No method is found for [@Priority(\"" + priorityMethodRef + "\")] reference");

				priorityParamIndexes = getMethodParamIndexes(implementor, priorityMethod);
			}

			implementMethodInfo.setPriorityValue(priorityValue);
			implementMethodInfo.setPriorityMethod(priorityMethod);
			implementMethodInfo.setPriorityParamIndexes(priorityParamIndexes);
		}
	}

	/**
	 * Get candidate <i>implementee</i> methods.
	 * <p>
	 * They will be used for resolving later.
	 * </p>
	 * 
	 * @param implementee
	 * @return
	 */
	protected Method[] getCandidateImplementeeMethods(Class<?> implementee)
	{
		return implementee.getMethods();
	}

	/**
	 * Get candidate implement methods.
	 * <p>
	 * They will be used for resolving later.
	 * </p>
	 * 
	 * @param implementor
	 * @return
	 */
	protected Method[] getCandidateImplementMethods(Class<?> implementor)
	{
		return implementor.getMethods();
	}

	/**
	 * Returns if the method is a legal <i>implementee</i> method.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @return
	 */
	protected boolean isImplementeeMethod(Class<?> implementee, Method implementeeMethod)
	{
		int modifier = implementeeMethod.getModifiers();

		if (Modifier.isStatic(modifier) || !Modifier.isPublic(modifier))
			return false;

		// exclude synthetic methods, compiler may generate odd methods
		// especially when extending generic type
		if (implementeeMethod.isSynthetic())
			return false;

		Class<?> delcClass = implementeeMethod.getDeclaringClass();

		if (Object.class.equals(delcClass))
			return false;

		return true;
	}

	/**
	 * Returns if the {@code implementor} is <i>implementor</i> of the
	 * {@code implementee}.
	 * 
	 * @param implementee
	 * @param implementor
	 * @return
	 */
	protected boolean isImplementor(Class<?> implementee, Class<?> implementor)
	{
		if (implementee.isAssignableFrom(implementor))
			return true;

		Implementor implementorAno = getAnnotation(implementor, Implementor.class);

		if (implementorAno == null)
			return false;

		Class<?>[] myImplementees = implementorAno.value();
		for (Class<?> _myImplementee : myImplementees)
		{
			if (implementee.isAssignableFrom(_myImplementee))
				return true;
		}

		return false;
	}

	/**
	 * Returns if the {@code implementMethod} is <i>implement method</i> of the
	 * {@code implementeeMethod}.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementeeMethodName
	 * @param implementeeMethodSignature
	 * @param implementeeMethodRefered
	 * @param implementor
	 * @param implementMethod
	 * @return
	 */
	protected boolean isImplementMethod(Class<?> implementee, Method implementeeMethod, String implementeeMethodName,
			String implementeeMethodSignature, String implementeeMethodRefered, Class<?> implementor,
			Method implementMethod)
	{
		if (!maybeImplementMethod(implementor, implementMethod))
			return false;

		Implement implementAno = getAnnotation(implementMethod, Implement.class);

		if (implementAno != null)
		{
			String implementAnoValue = implementAno.value();

			if (implementAnoValue == null || implementAnoValue.isEmpty())
			{
				// the same name and invoke feasible
				return implementeeMethodName.equals(implementMethod.getName())
						&& isInvocationFeasibleMethod(implementee,
								implementeeMethod, implementor,
								implementMethod);
			}
			else
			{
				if (implementAnoValue.equals(implementeeMethodRefered)
						|| implementAnoValue.equals(implementeeMethodSignature))
				{
					if (!isInvocationFeasibleMethod(implementee, implementeeMethod, implementor, implementMethod))
						throw new ImplementationResolveException("Method [" + implementMethod
								+ "] is not able to implement Method [" + implementeeMethod + "] ");

					return true;
				}
				else if (implementAnoValue.equals(implementeeMethodName))
				{
					return isInvocationFeasibleMethod(implementee, implementeeMethod, implementor, implementMethod);
				}
				else
					return false;
			}
		}
		else
			return isOverriddenMethod(implementee, implementeeMethod, implementor, implementMethod);
	}

	/**
	 * Returns if the {@code implementMethod} may be an <i>implement method</i>.
	 * 
	 * @param implementor
	 * @param implementMethod
	 * @return
	 */
	protected boolean maybeImplementMethod(Class<?> implementor, Method implementMethod)
	{
		int modifier = implementMethod.getModifiers();
	
		if (Modifier.isStatic(modifier) || !Modifier.isPublic(modifier))
			return false;

		// exclude synthetic methods, compiler may generate odd methods
		// especially when extending generic type
		if (implementMethod.isSynthetic())
			return false;
	
		if (Object.class.equals(implementMethod.getDeclaringClass()))
			return false;

		if (getAnnotation(implementMethod, NotImplement.class) != null)
			return false;
	
		return true;
	}

	/**
	 * Returns if the {@code subMethod} is overridden method of the
	 * {@code superMethod}.
	 * 
	 * @param superClass
	 * @param superMethod
	 * @param subClass
	 * @param subMethod
	 * @return
	 */
	protected boolean isOverriddenMethod(Class<?> superClass, Method superMethod, Class<?> subClass, Method subMethod)
	{
		if (!superClass.isAssignableFrom(subClass))
			return false;

		// method name check first
		if (!superMethod.getName().equals(subMethod.getName()))
			return false;

		Class<?>[] superParamTypes = superMethod.getParameterTypes();
		Class<?>[] subParamTypes = subMethod.getParameterTypes();

		// then parameter count
		if (superParamTypes.length != subParamTypes.length)
			return false;

		// then return type
		Class<?> superReturnType = superMethod.getReturnType();
		Class<?> subReturnType = subMethod.getReturnType();

		// generic return type resolve is not need, because sub type is
		// allowed, and Method.getReturnType() is erasure of generic.
		if (!superReturnType.isAssignableFrom(subReturnType))
				return false;

		Type[] gsuperParamTypes = superMethod.getGenericParameterTypes();
		Type[] gsubParamTypes = subMethod.getGenericParameterTypes();
		Map<TypeVariable<?>, Type> subTypeParams = null;

		// then parameter types
		for (int i = 0; i < superParamTypes.length; i++)
		{
			Class<?> superParamType = superParamTypes[i];
			Class<?> subParamType = subParamTypes[i];

			// method scope generic parameter type resolve is not need, because
			// it will be erased by Method.getParameterTypes() and can not be
			// changed in overridden methods.
			if (!superParamType.equals(subParamType))
			{
				Type gsuperParamType = gsuperParamTypes[i];

				// not generic actually
				if (gsuperParamType.equals(superParamType))
					return false;

				if (subTypeParams == null)
					subTypeParams = resolveTypeParams(subClass);

				if (!isOverriddenEquals(gsuperParamType, gsubParamTypes[i],
						subTypeParams))
					return false;
			}
		}

		return true;
	}

	/**
	 * Returns if the {@code implementMethod} method is invocation feasible to
	 * the {@code implementeeMethod} method.
	 * <p>
	 * Method {@code A} is invocation feasible to method {@code B} if :
	 * </p>
	 * <ul>
	 * <li>The return type of {@code A} is sub type of {@code B} after both are
	 * wrapped if primitive type;</li>
	 * <li>Each parameter type of {@code A} is sub or super type of {@code B}
	 * after both are wrapped if primitive type.</li>
	 * </ul>
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementor
	 * @param implementMethod
	 * @return
	 */
	protected boolean isInvocationFeasibleMethod(Class<?> implementee, Method implementeeMethod, Class<?> implementor,
			Method implementMethod)
	{
		// 返回值类型
		Class<?> implementeeReturnType = toWrapperType(
				implementeeMethod.getReturnType());
		Class<?> implementorReturnType = toWrapperType(
				implementMethod.getReturnType());

		if (!implementeeReturnType
				.isAssignableFrom(implementorReturnType))
			return false;

		// 参数类型
		Class<?>[] implementeeParamTypes = implementeeMethod
				.getParameterTypes();
		Class<?>[] implementorParamTypes = implementMethod.getParameterTypes();

		if (implementorParamTypes.length > implementeeParamTypes.length)
			return false;

		int[] implementParamIndexes = getMethodParamIndexes(implementor, implementMethod);

		for (int i = 0; i < implementorParamTypes.length; i++)
		{
			int myParamIndex = implementParamIndexes[i];

			if (myParamIndex >= implementeeParamTypes.length)
				return false;

			Class<?> implementeeParamType = toWrapperType(
					implementeeParamTypes[myParamIndex]);
			Class<?> implementorParamType = toWrapperType(
					implementorParamTypes[i]);

			if (!implementeeParamType
					.isAssignableFrom(implementorParamType)
					&& !implementorParamType
							.isAssignableFrom(implementeeParamType))
				return false;
		}

		return true;
	}

	/**
	 * Find refered method.
	 * <p>
	 * Returns {@code null} if not found.
	 * </p>
	 * 
	 * @param clazz
	 * @param methodRef
	 * @param validReturnTypes
	 * @return
	 */
	protected Method findReferedMethod(Class<?> clazz, String methodRef, Class<?>... validReturnTypes)
	{
		Method method = null;

		Method[] all = clazz.getMethods();

		List<Method> named = new ArrayList<Method>();

		for (Method m : all)
		{
			Refered refAnno = getAnnotation(m, Refered.class);

			if (refAnno != null && refAnno.value().equals(methodRef))
			{
				method = m;
				break;
			}
			else
			{
				if (m.getName().equals(methodRef))
				{
					if (validReturnTypes.length == 0)
					{
						named.add(m);
					}
					else
					{
						Class<?> returnType = toWrapperType(m.getReturnType());

						boolean returnValid = false;

						for (Class<?> validReturnType : validReturnTypes)
						{
							validReturnType = toWrapperType(
										validReturnType);

							if (validReturnType.isAssignableFrom(returnType))
							{
								returnValid = true;
							}
						}

						if (returnValid)
							named.add(m);
					}
				}
			}
		}

		if (method == null)
		{
			if (named.size() == 1)
				method = named.get(0);
			else if (named.size() == 0)
				throw new ImplementationResolveException(
						"Class [" + clazz.getName()
								+ "] : No method is found for '"
								+ methodRef + "' reference");
			else
				throw new ImplementationResolveException(
						"Class [" + clazz.getName() + "] : More than one '"
								+ methodRef
								+ "' named method is found for '" + methodRef
								+ "' reference");
		}

		return method;
	}

	/**
	 * Get the method signature.
	 * 
	 * @param clazz
	 * @param method
	 * @return
	 */
	protected String getMethodSignature(Class<?> clazz, Method method)
	{
		return method.toString();
	}

	/**
	 * Get refered name of the method.
	 * <p>
	 * Returns {@code null} if the method is not {@linkplain Refered} annotated.
	 * </p>
	 * 
	 * @param method
	 * @return
	 */
	protected String getRefered(Method method)
	{
		Refered refered = getAnnotation(method, Refered.class);

		return (refered == null ? null : refered.value());
	}

	/**
	 * Resolve {@linkplain ParamIndex} array of the method.
	 * 
	 * @param clazz
	 * @param method
	 * @return
	 */
	protected int[] getMethodParamIndexes(Class<?> clazz, Method method)
	{
		Annotation[][] paramAnnotationss = method.getParameterAnnotations();

		int[] paramIndexes = new int[paramAnnotationss.length];

		paramIndexes = new int[paramAnnotationss.length];

		for (int i = 0; i < paramAnnotationss.length; i++)
		{
			Annotation[] paramAnnotations = paramAnnotationss[i];

			ParamIndex paramIndexAnno = null;

			for (Annotation paramAnnotation : paramAnnotations)
			{
				if (paramAnnotation.annotationType().equals(ParamIndex.class))
				{
					paramIndexAnno = (ParamIndex) paramAnnotation;

					break;
				}
			}

			paramIndexes[i] = (paramIndexAnno == null ? i : paramIndexAnno.value());

			for (int j = 0; j < i; j++)
			{
				if (paramIndexes[j] == paramIndexes[i])
					throw new ImplementationResolveException(
							"Class [" + clazz.getName() + "] : Method ["
									+ method.toString()
									+ "] : The " + i
									+ "-th parameter index should not be duplicate with the "
									+ j + "-th parameter index of value ["
									+ paramIndexes[i] + "]");
			}
		}

		return paramIndexes;
	}

	/**
	 * Get the specified annotation of an element.
	 * 
	 * @param element
	 * @param clazz
	 * @return
	 */
	protected <T extends Annotation> T getAnnotation(AnnotatedElement element, Class<T> clazz)
	{
		return element.getAnnotation(clazz);
	}

	/**
	 * Get the wrapper type if primitive type.
	 * 
	 * @param type
	 * @return
	 */
	protected Class<?> toWrapperType(Class<?> type)
	{
		return TypeUtil.toWrapperType(type);
	}

	/**
	 * Resolve type parameter map.
	 * 
	 * @param type
	 * @return
	 */
	protected Map<TypeVariable<?>, Type> resolveTypeParams(Type type)
	{
		Map<TypeVariable<?>, Type> map = null;

		// make this class thread safe
		synchronized (this.typeVariablesMap)
		{
			map = this.typeVariablesMap.get(type);

			if (map == null)
			{
				map = TypeUtil.resolveTypeParams(type);
				this.typeVariablesMap.put(type, map);
			}
		}

		return map;
	}

	/**
	 * Returns if the type parameter in super class is overridden-equals with
	 * the type parameter in sub class.
	 * 
	 * @param superTypeParam
	 * @param subTypeParam
	 * @param subTypeParams
	 * @return
	 */
	protected boolean isOverriddenEquals(Type superTypeParam, Type subTypeParam,
			Map<TypeVariable<?>, Type> subTypeParams)
	{
		return TypeUtil.isOverriddenEquals(superTypeParam, subTypeParam,
				subTypeParams);
	}
}
