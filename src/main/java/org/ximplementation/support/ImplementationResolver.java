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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.Index;
import org.ximplementation.NotImplement;
import org.ximplementation.Priority;
import org.ximplementation.Validity;

/**
 * {@linkplain Implementation} resolver.
 * <p>
 * It resolves all methods including inherited in the given <i>implementee</i>
 * and its <i>implementor</i>s which are not {@code static}.
 * </p>
 * <p>
 * Note that this class is thread-safe and can be accessed by multiple threads.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
 *
 */
public class ImplementationResolver
{
	protected static final String METHOD_SIGNATURE_PART_REGEX = ".*\\(.*\\).*";

	private Map<Type, Map<TypeVariable<?>, Type>> typeVariablesMap = new WeakHashMap<Type, Map<TypeVariable<?>, Type>>();

	private MethodMatcher methodMatcher = new DefaultMethodMatcher();

	public ImplementationResolver()
	{
		super();
	}

	/**
	 * Get {@linkplain MethodMatcher} used for matching methods.
	 * 
	 * @return
	 */
	public MethodMatcher getMethodMatcher()
	{
		return methodMatcher;
	}

	/**
	 * Set {@linkplain MethodMatcher} used for matching methods.
	 * 
	 * @param methodMatcher
	 */
	public void setMethodMatcher(MethodMatcher methodMatcher)
	{
		this.methodMatcher = methodMatcher;
	}

	/**
	 * Resolve the {@code Implementation} for an <i>implementee</i> and its
	 * <i>implementor</i>s.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be resolved.
	 * @param implementors
	 *            The <i>implementor</i>s about the <i>implementee</i>.
	 * @return The {@code Implementation} for the <i>implementee</i> and its
	 *         <i>implementor</i>s.
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
	 * Resolve the {@code Implementation} for an <i>implementee</i> and its
	 * <i>implementor</i>s.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be resolved.
	 * @param implementors
	 *            The <i>implementor</i>s about the <i>implementee</i>.
	 * @return The {@code Implementation} for the <i>implementee</i> and its
	 *         <i>implementor</i>s.
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

		Collection<Method> implementeeMethods = getImplementeeMethods(
				implementee);

		for (Method implementeeMethod : implementeeMethods)
		{
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
	protected ImplementInfo resolveImplementInfo(Class<?> implementee,
			Collection<Method> implementeeMethods, Method implementeeMethod,
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
	protected Collection<ImplementMethodInfo> resolveImplementMethodInfo(
			Class<?> implementee, Collection<Method> implementeeMethods,
			Method implementeeMethod, Class<?> implementor)
	{
		List<ImplementMethodInfo> implementMethodInfos = new ArrayList<ImplementMethodInfo>();

		Collection<Method> implementMethods = getCandidateImplementMethods(
				implementor);

		for (Method implementMethod : implementMethods)
		{
			if (isImplementMethod(implementee, implementeeMethod, implementor,
					implementMethod))
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
			String validityMethodMatcher = validity.value();

			Method validityMethod = findMethod(implementor, validityMethodMatcher);

			if (validityMethod == null)
				throw new ImplementationResolveException("Class [" + implementor
						+ "] : No method is found for [@Validity(\"" + validityMethodMatcher + "\")] reference");
			
			Class<?> returnType = toWrapperType(validityMethod.getReturnType());
			
			if (!Boolean.class.equals(returnType))
				throw new ImplementationResolveException("Class [" + implementor
						+ "] : Validity method [" + validityMethod
						+ "] must return [" + boolean.class.getSimpleName()
						+ "] or [" + Boolean.class.getSimpleName() + "] type");

			// TODO check if parameters compatible

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
			int priorityValue = priority.priority();
			Method priorityMethod = null;
			int[] priorityParamIndexes = null;

			String priorityMethodMatcher = priority.value();

			if (!priorityMethodMatcher.isEmpty())
			{
				priorityMethod = findMethod(implementor, priorityMethodMatcher);

				if (priorityMethod == null)
					throw new ImplementationResolveException("Class [" + implementor
							+ "] : No method is found for [@Priority(\"" + priorityMethodMatcher + "\")] reference");

				Class<?> returnType = toWrapperType(
						priorityMethod.getReturnType());

				if (!Integer.class.equals(returnType))
					throw new ImplementationResolveException(
							"Class [" + implementor + "] : Priority method ["
									+ priorityMethod
									+ "] must return ["
									+ int.class.getSimpleName() + "] or ["
									+ Integer.class.getSimpleName() + "] type");

				// TODO check if parameters compatible

				priorityParamIndexes = getMethodParamIndexes(implementor, priorityMethod);
			}

			implementMethodInfo.setPriorityValue(priorityValue);
			implementMethodInfo.setPriorityMethod(priorityMethod);
			implementMethodInfo.setPriorityParamIndexes(priorityParamIndexes);
		}
	}

	/**
	 * Get <i>implementee method</i>s for the specified <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be got.
	 * @return
	 */
	protected Collection<Method> getImplementeeMethods(
			Class<?> implementee)
	{
		Collection<Method> implementeeMethods = new LinkedList<Method>();

		doGetImplementeeMethods(implementee, implementeeMethods);

		return implementeeMethods;
	}

	/**
	 * Do get <i>implementee method</i>s for the specified <i>implementee</i>.
	 * 
	 * @param implementee
	 *            The <i>implementee</i> to be got.
	 * @param implementeeMethods
	 *            An collection for storing <i>implementee method</i>s.
	 */
	protected void doGetImplementeeMethods(Class<?> implementee,
			Collection<Method> implementeeMethods)
	{
		Method[] myMethods = implementee.getDeclaredMethods();

		for (Method myMethod : myMethods)
		{
			if (!isImplementeeMethod(implementee, myMethod))
				continue;
			
			boolean hasOverriddenMethod = false;
			
			for(Method implementeeMethod : implementeeMethods)
			{
				if(isOverriddenMethod(implementee, myMethod, implementeeMethod.getDeclaringClass(), implementeeMethod))
				{
					hasOverriddenMethod = true;
					break;
				}
			}

			if (!hasOverriddenMethod)
				implementeeMethods.add(myMethod);
		}

		// methods in super class
		Class<?> superClass = implementee.getSuperclass();
		if (superClass != null)
			doGetImplementeeMethods(superClass, implementeeMethods);

		// methods in super interfaces
		Class<?>[] superInterfaces = implementee.getInterfaces();
		if (superInterfaces != null)
		{
			for (Class<?> superInterface : superInterfaces)
			{
				doGetImplementeeMethods(superInterface,
						implementeeMethods);
			}
		}
	}

	/**
	 * Return if the method is a legal <i>implementee</i> method.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @return
	 */
	protected boolean isImplementeeMethod(Class<?> implementee, Method implementeeMethod)
	{
		int modifier = implementeeMethod.getModifiers();
	
		// exclude static method
		if (Modifier.isStatic(modifier))
			return false;
	
		// exclude synthetic methods, compiler may generate odd methods
		// especially when extending generic type
		if (implementeeMethod.isSynthetic())
			return false;
	
		return true;
	}

	/**
	 * Get candidate implement methods.
	 * 
	 * @param implementor
	 * @return
	 */
	protected Collection<Method> getCandidateImplementMethods(
			Class<?> implementor)
	{
		Collection<Method> implementMethods = new LinkedList<Method>();

		doGetCandidateImplementMethods(implementor, implementMethods);

		return implementMethods;
	}

	/**
	 * Do get <i>implement method</i>s for the specified <i>implementor</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i> to be got.
	 * @param implementMethods
	 *            An collection for storing <i>implement method</i>s.
	 */
	protected void doGetCandidateImplementMethods(Class<?> implementor,
			Collection<Method> implementMethods)
	{
		Method[] myMethods = implementor.getDeclaredMethods();

		for (Method myMethod : myMethods)
		{
			if (!isCandidateImplementMethod(implementor, myMethod))
				continue;

			boolean hasOverriddenMethod = false;

			for (Method implementMethod : implementMethods)
			{
				if (isOverriddenMethod(implementor, myMethod,
						implementMethod.getDeclaringClass(),
						implementMethod))
				{
					hasOverriddenMethod = true;
					break;
				}
			}

			if (!hasOverriddenMethod)
				implementMethods.add(myMethod);
		}

		// methods in super class
		Class<?> superClass = implementor.getSuperclass();
		if (superClass != null)
			doGetImplementeeMethods(superClass, implementMethods);

		// methods in super interfaces
		Class<?>[] superInterfaces = implementor.getInterfaces();
		if (superInterfaces != null)
		{
			for (Class<?> superInterface : superInterfaces)
			{
				doGetImplementeeMethods(superInterface, implementMethods);
			}
		}
	}
	
	/**
	 * Return if the {@code implementMethod} is candidate <i>implement
	 * method</i>.
	 * <p>
	 * {@code @NotImplement} method must return {@code true} here, for if sub
	 * class method is annotated with {@code @NotImplement} and is ignored by
	 * {@linkplain #doGetCandidateImplementMethods(Class, Collection)}, its
	 * ancestor method will be added also, and this is illegal.
	 * </p>
	 * 
	 * @param implementor
	 * @param implementMethod
	 * @return
	 */
	protected boolean isCandidateImplementMethod(Class<?> implementor, Method implementMethod)
	{
		int modifier = implementMethod.getModifiers();
	
		// exclude static method
		if (Modifier.isStatic(modifier))
			return false;
	
		// exclude synthetic methods, compiler may generate odd methods
		// especially when extending generic type
		if (implementMethod.isSynthetic())
			return false;
	
		return true;
	}

	/**
	 * Return if the {@code implementor} is <i>implementor</i> of the
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
			if (isImplementor(implementee, _myImplementee))
				return true;
		}

		return false;
	}

	/**
	 * Return if the {@code implementMethod} is <i>implement method</i> of the
	 * {@code implementeeMethod}.
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementor
	 * @param implementMethod
	 * @return
	 */
	protected boolean isImplementMethod(Class<?> implementee,
			Method implementeeMethod, Class<?> implementor,
			Method implementMethod)
	{
		if (!maybeImplementMethod(implementor, implementMethod))
			return false;

		Implement implementAno = getAnnotation(implementMethod, Implement.class);

		if (implementAno != null)
		{
			String implementAnoValue = implementAno.value();

			if (implementAnoValue == null || implementAnoValue.isEmpty())
				implementAnoValue = implementMethod.getName();

			if (this.methodMatcher.match(implementAnoValue, implementeeMethod,
					implementee))
			{
				if (!isInvocationCompatible(implementee, implementeeMethod,
						implementor, implementMethod))
					throw new ImplementationResolveException(
							"Class [" + implementor.getName() + "] : method ["
									+ implementMethod
									+ "] is not compatible to implement method ["
									+ implementeeMethod + "] in [" + implementee
									+ "]");

				return true;
			}
			else
				return false;
		}
		else
			return isOverriddenMethod(implementee, implementeeMethod, implementor, implementMethod);
	}

	/**
	 * Return if the {@code implementMethod} may be an <i>implement method</i>.
	 * 
	 * @param implementor
	 * @param implementMethod
	 * @return
	 */
	protected boolean maybeImplementMethod(Class<?> implementor, Method implementMethod)
	{
		int modifier = implementMethod.getModifiers();
	
		// exclude static method
		if (Modifier.isStatic(modifier))
			return false;
	
		// exclude synthetic methods, compiler may generate odd methods
		// especially when extending generic type
		if (implementMethod.isSynthetic())
			return false;
	
		if (getAnnotation(implementMethod, NotImplement.class) != null)
			return false;
	
		return true;
	}

	/**
	 * Return if the {@code subMethod} is overridden method of the
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
	 * Return if the {@code implementMethod} method is invocation compatible to
	 * the {@code implementeeMethod} method.
	 * <p>
	 * Method {@code A} is invocation compatible to method {@code B} if :
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
	protected boolean isInvocationCompatible(Class<?> implementee,
			Method implementeeMethod, Class<?> implementor,
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
	 * Find method.
	 * 
	 * @param clazz
	 * @param matcher
	 * @return The matched method, {@code null} if no.
	 */
	protected Method findMethod(Class<?> clazz, String matcher)
	{
		Method[] myMethods = clazz.getDeclaredMethods();

		for (Method myMethod : myMethods)
		{
			// ignore synthetic methods
			if (myMethod.isSynthetic())
				continue;

			if (this.methodMatcher.match(matcher, myMethod, clazz))
				return myMethod;
		}

		Method method = null;

		// methods in super class
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null)
			method = findMethod(superClass, matcher);

		if (method != null)
			return method;

		// methods in super interfaces
		Class<?>[] superInterfaces = clazz.getInterfaces();
		if (superInterfaces != null)
		{
			for (Class<?> superInterface : superInterfaces)
			{
				method = findMethod(superInterface, matcher);

				if (method != null)
					return method;
			}
		}

		return null;
	}

	/**
	 * Resolve {@linkplain Index} array of the method.
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

			Index paramIndexAnno = null;

			for (Annotation paramAnnotation : paramAnnotations)
			{
				if (paramAnnotation.annotationType().equals(Index.class))
				{
					paramIndexAnno = (Index) paramAnnotation;

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
	 * Return if the type parameter in super class is overridden-equals with
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
