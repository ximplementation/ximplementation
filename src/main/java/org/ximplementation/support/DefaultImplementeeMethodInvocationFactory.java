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
import java.util.Collection;

import org.ximplementation.Priority;
import org.ximplementation.Validity;

/**
 * Default {@linkplain ImplementeeMethodInvocationFactory}.
 * <p>
 * It evaluates the <i>implement method</i> with the max priority, then returns
 * an {@linkplain SimpleImplementeeMethodInvocation} instance.
 * </p>
 * <p>
 * The <i>implement method</i> evaluating rule is as following:
 * </p>
 * <ol>
 * <li>Ignores all whose parameter type mismatched or
 * {@linkplain Validity @Validaty} not passed;</li>
 * <li>Gets the one with max {@linkplain Priority @Priority} If there is;</li>
 * <li>Else, gets the one whose method parameter is closest to the invocation
 * parameters;</li>
 * <li>Else, gets the only one which declared {@linkplain Validity @Validaty} ;
 * </li>
 * <li>Else, gets the only one whose <i>implementor</i> is not the same nor the
 * sub package with the <i>implementee</i>;</li>
 * <li>Else, gets one randomly.</li>
 * </ol>
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 *
 */
public class DefaultImplementeeMethodInvocationFactory
		implements ImplementeeMethodInvocationFactory
{
	public DefaultImplementeeMethodInvocationFactory()
	{
		super();
	}

	@Override
	public ImplementeeMethodInvocation get(
			Implementation<?> implementation, Method implementeeMethod,
			Object[] implementeeMethodParams,
			ImplementorBeanFactory implementorBeanFactory) throws Throwable
	{
		ImplementMethodInfo implementMethodInfo = null;
		Object implementorBean = null;
		int priority = Integer.MIN_VALUE;

		ImplementInfo implementInfo = findImplementInfo(implementation,
				implementeeMethod);

		if (implementInfo == null || !implementInfo.hasImplementMethodInfo())
			return null;

		for (ImplementMethodInfo myImplementMethodInfo : implementInfo
				.getImplementMethodInfos())
		{
			if (!isImplementMethodParamValid(myImplementMethodInfo,
					implementeeMethodParams))
				continue;

			Collection<?> implementorBeans = implementorBeanFactory
					.getImplementorBeans(
							myImplementMethodInfo.getImplementor());

			if (implementorBeans == null || implementorBeans.isEmpty())
				continue;

			Method validityMethod = myImplementMethodInfo.getValidityMethod();
			Object[] validityMethodParams = myImplementMethodInfo
					.getValidityParams(implementeeMethodParams);
			Method priorityMethod = myImplementMethodInfo.getPriorityMethod();
			Object[] priorityMethodParams = myImplementMethodInfo
					.getPriorityParams(implementeeMethodParams);

			for (Object myImplementorBean : implementorBeans)
			{
				if (validityMethod != null)
				{
					boolean isValid = invokeValidityMethod(implementation,
							implementeeMethod, myImplementMethodInfo,
							validityMethod, validityMethodParams,
							myImplementorBean);

					if (!isValid)
						continue;
				}

				int myPriority = myImplementMethodInfo.getPriorityValue();

				if (priorityMethod != null)
				{
					myPriority = invokePriorityMethod(implementation,
							implementeeMethod, myImplementMethodInfo,
							myImplementMethodInfo.getPriorityMethod(),
							priorityMethodParams,
							myImplementorBean);
				}

				boolean replace = false;

				if (implementMethodInfo == null)
					replace = true;
				else
				{
					if (myPriority == priority)
					{
						int methodInfoPriority = compareImplementMethodInfoPriority(
								implementation, implementeeMethod,
								implementeeMethodParams, implementMethodInfo,
								myImplementMethodInfo);

						replace = (methodInfoPriority <= 0);
					}
					else
						replace = (myPriority > priority);
				}

				if (replace)
				{
					implementMethodInfo = myImplementMethodInfo;
					implementorBean = myImplementorBean;
					priority = myPriority;
				}
			}
		}

		return (implementMethodInfo == null ? null
				: new SimpleImplementeeMethodInvocation(implementeeMethodParams,
						implementMethodInfo, implementorBean));
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
	 * @param implementeeMethod
	 * @param implementMethodInfo
	 * @param validityMethod
	 * @param validityParams
	 * @param implementorBean
	 * @return
	 * @throws Throwable
	 */
	protected boolean invokeValidityMethod(Implementation<?> implementation,
			Method implementeeMethod, ImplementMethodInfo implementMethodInfo,
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
	 * @param implementeeMethod
	 * @param implementMethodInfo
	 * @param priorityMethod
	 * @param priorityParams
	 * @param implementorBean
	 * @return
	 * @throws Throwable
	 */
	protected int invokePriorityMethod(Implementation<?> implementation,
			Method implementeeMethod, ImplementMethodInfo implementMethodInfo,
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
	 * types are valid for the given parameters.
	 * 
	 * @param implementMethodInfo
	 * @param implementeeMethodParams
	 * @return
	 */
	protected boolean isImplementMethodParamValid(
			ImplementMethodInfo implementMethodInfo,
			Object[] implementeeMethodParams)
	{
		Class<?>[] myParamTypes = implementMethodInfo.getParamTypes();

		if (myParamTypes == null || myParamTypes.length == 0)
			return true;

		if (myParamTypes.length > implementeeMethodParams.length)
			return false;

		Object[] myParams = implementMethodInfo
				.getParams(implementeeMethodParams);

		for (int i = 0; i < myParamTypes.length; i++)
		{
			Class<?> myParamType = myParamTypes[i];
			Object myParam = myParams[i];

			if (myParam == null)
			{
				if (myParamType.isPrimitive())
					return false;
			}
			else
			{
				if (!toWrapperType(myParamType).isInstance(myParam))
					return false;
			}
		}

		return true;
	}

	/**
	 * Compare two {@linkplain ImplementMethodInfo}'s priority which both are
	 * valid to {@code implementeeMethodParams}.
	 * <p>
	 * Return {@code >0} if {@code first} is higher; returns {@code <0} if
	 * {@code second} is higher; returns {@code 0} if they are the same.
	 * </p>
	 * 
	 * @param implementation
	 * @param implementeeMethod
	 * @param implementeeMethodParams
	 * @param first
	 * @param second
	 * @return
	 */
	protected int compareImplementMethodInfoPriority(
			Implementation<?> implementation,
			Method implementeeMethod, Object[] implementeeMethodParams,
			ImplementMethodInfo first, ImplementMethodInfo second)
	{
		int priority = compareImplementMethodParamTypePriority(implementation,
				implementeeMethod, implementeeMethodParams, first, second);
	
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
	 * priority.
	 * <p>
	 * Return {@code >0} if {@code first} is higher; returns {@code <0} if
	 * {@code second} is higher; returns {@code 0} if they are the same.
	 * </p>
	 * 
	 * @param implementation
	 * @param implementeeMethod
	 * @param implementeeMethodParams
	 * @param first
	 * @param second
	 * @return
	 */
	protected int compareImplementMethodParamTypePriority(
			Implementation<?> implementation,
			Method implementeeMethod, Object[] implementeeMethodParams,
			ImplementMethodInfo first, ImplementMethodInfo second)
	{
		// which is closer to implementee method parameters
		int firstCloserCount = 0;
		int secondCloserCount = 0;
	
		Class<?>[] firstParamTypes = first.getParamTypes();
		int[] firstParamIndexes = first.getParamIndexes();
		Class<?>[] secondParamTypes = second.getParamTypes();
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
	 * Return the wrapper type of the specified type if it is a primitive,
	 * returns itself if not.
	 * 
	 * @param type
	 * @return
	 */
	protected Class<?> toWrapperType(Class<?> type)
	{
		return TypeUtil.toWrapperType(type);
	}

	protected static class SimpleImplementeeMethodInvocation
			implements ImplementeeMethodInvocation
	{
		private Object[] implementeeMethodParams;

		private ImplementMethodInfo implementMethodInfo;

		private Object implementorBean;

		public SimpleImplementeeMethodInvocation()
		{
			super();
		}

		public SimpleImplementeeMethodInvocation(
				Object[] implementeeMethodParams,
				ImplementMethodInfo implementMethodInfo, Object implementorBean
				)
		{
			super();
			this.implementeeMethodParams = implementeeMethodParams;
			this.implementMethodInfo = implementMethodInfo;
			this.implementorBean = implementorBean;
		}

		public Object[] getImplementeeMethodParams()
		{
			return implementeeMethodParams;
		}

		public void setImplementeeMethodParams(Object[] implementeeMethodParams)
		{
			this.implementeeMethodParams = implementeeMethodParams;
		}

		public ImplementMethodInfo getImplementMethodInfo()
		{
			return implementMethodInfo;
		}

		public void setImplementMethodInfo(
				ImplementMethodInfo implementMethodInfo)
		{
			this.implementMethodInfo = implementMethodInfo;
		}

		public Object getImplementorBean()
		{
			return implementorBean;
		}

		public void setImplementorBean(Object implementorBean)
		{
			this.implementorBean = implementorBean;
		}

		@Override
		public Object invoke() throws Throwable
		{
			Object[] implementMethodParams = this.implementMethodInfo
					.getParams(this.implementeeMethodParams);

			return this.implementMethodInfo.getImplementMethod()
					.invoke(this.implementorBean, implementMethodParams);
		}
	}
}
