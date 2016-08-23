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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.NotImplement;
import org.ximplementation.ParamIndex;
import org.ximplementation.Priority;
import org.ximplementation.Refered;
import org.ximplementation.Validity;

/**
 * 实现信息解析器。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月5日
 *
 */
public class ImplementationResolver
{
	public ImplementationResolver()
	{
		super();
	}

	/**
	 * 解析接口者实现信息。
	 * 
	 * @param implementee
	 *            接口
	 * @param implementors
	 *            实现者数组
	 * @return
	 * @throws ImplementationResolveException
	 */
	public Implementation resolve(Class<?> implementee, Class<?>... implementors)
			throws ImplementationResolveException
	{
		Set<Class<?>> simplementors = new HashSet<Class<?>>(
				Arrays.asList(implementors));

		return doResolve(implementee, simplementors);
	}

	/**
	 * 解析接口者实现信息。
	 * 
	 * @param implementee
	 *            接口
	 * @param implementors
	 *            实现者套集
	 * @return
	 * @throws ImplementationResolveException
	 */
	public Implementation resolve(Class<?> implementee,
			Set<Class<?>> implementors) throws ImplementationResolveException
	{
		return doResolve(implementee, implementors);
	}

	/**
	 * 解析接口者实现信息。
	 * 
	 * @param implementee
	 *            接口类
	 * @param implementors
	 *            实现者套集
	 * @return
	 * @throws ImplementationResolveException
	 */
	protected Implementation doResolve(Class<?> implementee, Set<Class<?>> implementors)
			throws ImplementationResolveException
	{
		Implementation implementation = new Implementation();

		List<ImplementInfo> implementInfos = new ArrayList<ImplementInfo>();

		Method[] implementeeMethods = getCandicateImplementeeMethods(
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
	 * 解析{@linkplain ImplementInfo}。
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
	 * 解析{@linkplain ImplementMethodInfo}。
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

		Method[] implementMethods = getCandicateImplementMethods(implementor);

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
	 * 构建{@linkplain ImplementMethodInfo}。
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
	 * 解析{@linkplain ImplementMethodInfo}的属性。
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
	 * 解析{@linkplain ImplementMethodInfo}的
	 * {@linkplain ImplementMethodInfo#getParamTypes()} 属性。
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
	 * 解析{@linkplain ImplementMethodInfo}的
	 * {@linkplain ImplementMethodInfo#getGenericParamTypes()} 属性。
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
	 * 解析{@linkplain ImplementMethodInfo}的
	 * {@linkplain ImplementMethodInfo#getParamIndexes()}属性。
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
	 * 解析{@linkplain ImplementMethodInfo}的
	 * {@linkplain ImplementMethodInfo#getValidityMethod()}、
	 * {@linkplain ImplementMethodInfo#getValidityParamIndexes()}属性。
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
	 * 解析{@linkplain ImplementMethodInfo}的
	 * {@linkplain ImplementMethodInfo#getPriorityValue()}
	 * {@linkplain ImplementMethodInfo#getPriorityMethod()}、
	 * {@linkplain ImplementMethodInfo#getPriorityParamIndexes()}属性。
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
	 * 获取候选接口方法数组。
	 * 
	 * @param implementee
	 * @return
	 */
	protected Method[] getCandicateImplementeeMethods(Class<?> implementee)
	{
		return implementee.getMethods();
	}

	/**
	 * 获取候选实现方法数组。
	 * 
	 * @param implementor
	 * @return
	 */
	protected Method[] getCandicateImplementMethods(Class<?> implementor)
	{
		return implementor.getMethods();
	}

	/**
	 * 判断给定方法是否是<i>接口方法</i>。
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

		Class<?> delcClass = implementeeMethod.getDeclaringClass();

		if (Object.class.equals(delcClass))
			return false;

		return true;
	}

	/**
	 * 判断{@code implementor}是否是{@code implementee}的<i>实现者</i>。
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

		Class<?> myImplementee = implementorAno.value();

		if (implementee.isAssignableFrom(myImplementee))
			return true;

		Class<?>[] myImplementees = implementorAno.implementees();
		for (Class<?> _myImplementee : myImplementees)
		{
			if (implementee.isAssignableFrom(_myImplementee))
				return true;
		}

		return false;
	}

	/**
	 * 判断是否是实现方法。
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
				// 方法名相同且调用可行
				return implementeeMethodName.equals(implementMethod.getName())
						&& isInvokeFeasibleMethod(implementee,
								implementeeMethod, implementor,
								implementMethod);
			}
			else
			{
				if (implementAnoValue.equals(implementeeMethodRefered)
						|| implementAnoValue.equals(implementeeMethodSignature))
				{
					if (!isInvokeFeasibleMethod(implementee, implementeeMethod, implementor, implementMethod))
						throw new ImplementationResolveException("Method [" + implementMethod
								+ "] is not able to implement Method [" + implementeeMethod + "] ");

					return true;
				}
				else if (implementAnoValue.equals(implementeeMethodName))
				{
					return isInvokeFeasibleMethod(implementee, implementeeMethod, implementor, implementMethod);
				}
				else
					return false;
			}
		}
		else
			return isOverridenMethod(implementee, implementeeMethod, implementor, implementMethod);
	}

	/**
	 * 判断给定方法是否可能是<i>实现方法</i>。
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
	
		if (Object.class.equals(implementMethod.getDeclaringClass()))
			return false;

		if (getAnnotation(implementMethod, NotImplement.class) != null)
			return false;
	
		return true;
	}

	/**
	 * 判断是否是重写方法。
	 * 
	 * @param superClass
	 * @param superMethod
	 * @param subClass
	 * @param subMethod
	 * @return
	 */
	protected boolean isOverridenMethod(Class<?> superClass, Method superMethod, Class<?> subClass, Method subMethod)
	{
		if (!superMethod.getName().equals(subMethod.getName()))
			return false;

		Class<?>[] superParamTypes = superMethod.getParameterTypes();
		Class<?>[] subParamTypes = subMethod.getParameterTypes();

		// 参数数目
		if (superParamTypes.length != subParamTypes.length)
			return false;

		// 参数类型
		for (int i = 0; i < superParamTypes.length; i++)
		{
			Class<?> superParamType = superParamTypes[i];
			Class<?> subParamType = subParamTypes[i];

			if (!superParamType.equals(subParamType))
				return false;
		}

		// 返回值类型
		Class<?> superReturnType = superMethod.getReturnType();
		Class<?> subReturnType = subMethod.getReturnType();

		if (!superReturnType.isAssignableFrom(subReturnType))
			return false;

		return true;
	}

	/**
	 * 判断{@code implementMethod}方法对于{@code implementeeMethod}方法是否调用可行。
	 * <p>
	 * 如果方法{@code A}对于方法{@code B}调用可行，那么：
	 * </p>
	 * <ul>
	 * <li>{@code A}方法的返回值类型必须是{@code B}方法返回值类型的子类；</li>
	 * <li>{@code A}方法的参数类型必须是对应{@code B}方法参数类型的子类。</li>
	 * </ul>
	 * 
	 * @param implementee
	 * @param implementeeMethod
	 * @param implementor
	 * @param implementMethod
	 * @return
	 */
	protected boolean isInvokeFeasibleMethod(Class<?> implementee, Method implementeeMethod, Class<?> implementor,
			Method implementMethod)
	{
		// 返回值类型
		Class<?> implementeeReturnType = implementeeMethod.getReturnType();
		Class<?> implementorReturnType = implementMethod.getReturnType();

		if (!implementeeReturnType
				.isAssignableFrom(toWrapperType(implementorReturnType)))
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

			Class<?> implementeeParamType = implementeeParamTypes[myParamIndex];
			Class<?> implementorParamType = implementorParamTypes[i];

			if (!implementeeParamType
					.isAssignableFrom(toWrapperType(implementorParamType)))
				return false;
		}

		return true;
	}

	/**
	 * 查找被引用的方法名。
	 * <p>
	 * 如果没找到，此方法将返回{@code null}。
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
						Class<?> returnType = m.getReturnType();

						if (returnType.isPrimitive())
							returnType = toWrapperType(returnType);

						boolean returnValid = false;

						for (Class<?> validReturnType : validReturnTypes)
						{
							if (validReturnType.isPrimitive())
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
	 * 将类集合转换为类数组。
	 * 
	 * @param classes
	 * @return
	 */
	protected Class<?>[] toClassArray(Collection<? extends Class<?>> classes)
	{
		Class<?>[] classArray = new Class<?>[classes.size()];

		return classes.toArray(classArray);
	}

	/**
	 * 获取方法签名。
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
	 * 获取引用名。
	 * <p>
	 * 如果方法没有{@linkplain Refered}注解，将返回{@code null}。
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
	 * 解析方法参数的{@linkplain ParamIndex}值数组。
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
		}

		return paramIndexes;
	}

	/**
	 * 获取指定注解实例。
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
	 * 将基本类型转换为对应的包装类型。
	 * 
	 * @param primitiveType
	 * @return
	 */
	protected Class<?> toWrapperType(Class<?> primitiveType)
	{
		if (boolean.class.equals(Boolean.class))
			return Boolean.class;
		else if(byte.class.equals(primitiveType))
			return Byte.class;
		else if (char.class.equals(primitiveType))
			return Character.class;
		else if (double.class.equals(primitiveType))
			return Double.class;
		else if (float.class.equals(primitiveType))
			return Float.class;
		else if (int.class.equals(primitiveType))
			return Integer.class;
		else if (long.class.equals(primitiveType))
			return Long.class;
		else if (short.class.equals(primitiveType))
			return Short.class;
		else
			return primitiveType;
	}
}
