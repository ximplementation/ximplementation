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
 * 默认接口方法调用信息计算器。
 * <p>
 * 此类先筛除参数类型不匹配和{@linkplain Validity @Validaty}不通过的实现方法，之后按照如下规则依次计算：
 * </p>
 * <ol>
 * <li>计算所有实现方法的{@linkplain Priority @Priority}优先级值，并返回优先级值最高的那个；</li>
 * <li>否则，返回参数类型最接近的那个；</li>
 * <li>否则，返回声明了{@linkplain Validity @Validaty}的那个；</li>
 * <li>否则，返回实现者类与接口类不在同一个包内的那个；</li>
 * <li>否则，随机返回一个。</li>
 * </ol>
 * 
 * @author earthangry@gmail.com
 * @date 2016年8月15日
 *
 */
public class DefaultImplementeeMethodInvocationInfoEvaluator
		implements ImplementeeMethodInvocationInfoEvaluator
{
	public DefaultImplementeeMethodInvocationInfoEvaluator()
	{
		super();
	}

	@Override
	public ImplementeeMethodInvocationInfo evaluate(
			Implementation implementation, Method implementeeMethod,
			Object[] implementeeMethodParams,
			ImplementorBeanFactory implementorBeanFactory) throws Throwable
	{
		ImplementMethodInfo implementMethodInfo = null;
		Object implementorBean = null;
		int priority = Integer.MIN_VALUE;

		ImplementInfo implementInfo = findImplementInfo(implementation,
				implementeeMethod);

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

			Object[] validityMethodParams = myImplementMethodInfo
					.getValidityParams(implementeeMethodParams);
			Object[] priorityMethodParams = myImplementMethodInfo
					.getPriorityParams(implementeeMethodParams);

			for (Object myImplementorBean : implementorBeans)
			{
				if (myImplementMethodInfo.hasValidityMethod())
				{
					Object isValid = myImplementMethodInfo.getValidityMethod()
							.invoke(myImplementorBean, validityMethodParams);

					if (!Boolean.TRUE.equals(isValid))
						continue;
				}

				int myPriority = myImplementMethodInfo.getPriorityValue();

				if (myImplementMethodInfo.hasPriorityMethod())
				{
					Number myPriorityNmb = (Number) myImplementMethodInfo
							.getPriorityMethod()
							.invoke(myImplementorBean, priorityMethodParams);
					if (myPriorityNmb != null)
						myPriority = myPriorityNmb.intValue();
				}

				boolean replace = false;

				if (implementMethodInfo == null)
					replace = true;
				else
				{
					if (myPriority == priority)
					{
						int methodInfoPriority = compareImplementMethodInfoPriority(
								implementation,
								implementeeMethod, implementeeMethodParams,
								implementMethodInfo, myImplementMethodInfo);

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
				: new ImplementeeMethodInvocationInfo(implementMethodInfo,
						implementorBean));
	}

	/**
	 * 查找{@linkplain ImplementInfo}。
	 * 
	 * @param implementation
	 * @param implementeeMethod
	 * @return
	 */
	protected ImplementInfo findImplementInfo(Implementation implementation,
			Method implementeeMethod)
	{
		return implementation.getImplementInfo(implementeeMethod);
	}

	/**
	 * 判断对象数组是否能作为{@linkplain ImplementMethodInfo#getImplementMethod()}方法的参数数组。
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
			if (!myParamTypes[i].isInstance(myParams[i]))
				return false;
		}

		return true;
	}

	/**
	 * 比较两个对{@code implementeeMethodParams}都有效的{@linkplain ImplementMethodInfo}
	 * 的优先级。
	 * <p>
	 * 如果是{@code first}的优先级高，返回{@code >0}；如果是{@code second}的优先级高，返回{@code <0}
	 * ；如果二者相同，返回{@code 0}。
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
			Implementation implementation,
			Method implementeeMethod, Object[] implementeeMethodParams,
			ImplementMethodInfo first, ImplementMethodInfo second)
	{
		int priority = compareImplementMethodParamTypePriority(implementation,
				implementeeMethod, implementeeMethodParams, first, second);
	
		// 定义了validity方法的要优于未定义validity方法的
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
	 * 比较两个{@linkplain ImplementMethodInfo}的参数类型优先级。
	 * <p>
	 * 如果是{@code first}的优先级高，返回{@code >0}；如果是{@code second}的优先级高，返回{@code <0}
	 * ；如果二者相同，返回{@code 0}。
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
			Implementation implementation,
			Method implementeeMethod, Object[] implementeeMethodParams,
			ImplementMethodInfo first, ImplementMethodInfo second)
	{
		// 比较二者的参数类型哪一个与implementeeMethodParams更贴近
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
	
				if (firstType.equals(secondType))
					;
				else if (firstType.isAssignableFrom(secondType))
				{
					secondCloserCount++;
				}
				else
					firstCloserCount++;
			}
		}
	
		return (firstCloserCount - secondCloserCount);
	}

	/**
	 * 比较两个实现者的优先级。
	 * <p>
	 * 如果是{@code firstImplementor}的优先级高，返回{@code >0}；如果是
	 * {@code secondImplementor} 的优先级高，返回{@code <0}；如果二者相同，返回{@code 0}。
	 * </p>
	 * 
	 * @param implementation
	 * @param firstImplementor
	 * @param secondImplementor
	 * @return
	 */
	protected int compareImplementorPriority(Implementation implementation,
			Class<?> firstImplementor, Class<?> secondImplementor)
	{
		String implementeePkg = implementation.getImplementee().getPackage()
				.getName();
	
		boolean firstSamePkg = firstImplementor.getPackage().getName()
				.startsWith(implementeePkg);
	
		boolean secondSamePkg = secondImplementor.getPackage().getName()
				.startsWith(implementeePkg);
	
		// 如果都跟或都不跟implementee在同一个包下，则认为相同，否则，不跟implementee在同一个包下的优先级高
		if (firstSamePkg)
		{
			return (secondSamePkg ? 0 : -1);
		}
		else
			return (secondSamePkg ? 1 : 0);
	}
}
