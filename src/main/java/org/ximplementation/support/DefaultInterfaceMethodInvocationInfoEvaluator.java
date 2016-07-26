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

/**
 * 默认接口方法调用信息计算器。
 * 
 * @author zangzf
 *
 */
public class DefaultInterfaceMethodInvocationInfoEvaluator
		implements InterfaceMethodInvocationInfoEvaluator
{
	public DefaultInterfaceMethodInvocationInfoEvaluator()
	{
		super();
	}

	@Override
	public InterfaceMethodInvocationInfo evaluate(Class<?> interfacee,
			Method interfaceMethod, Object[] interfaceMethodParams,
			ImplementMethodBeanInfo[] implementMethodBeanInfos) throws Throwable
	{
		if (implementMethodBeanInfos == null)
			return null;

		ImplementMethodInfo implementMethodInfo = null;
		Object implementorBean = null;
		int priority = 0;

		for (ImplementMethodBeanInfo implementMethodBeanInfo : implementMethodBeanInfos)
		{
			ImplementMethodInfo myImplementMethodInfo = implementMethodBeanInfo
					.getImplementMethodInfo();

			if (!isImplementMethodParamValid(myImplementMethodInfo,
					interfaceMethodParams))
				continue;

			Object[] validityMethodParams = (myImplementMethodInfo
					.hasValidityMethod()
							? extractValidityMethodParams(myImplementMethodInfo,
									interfaceMethodParams)
							: null);

			Object[] priorityMethodParams = (myImplementMethodInfo
					.hasPriorityMethod()
							? extractPriorityMethodParams(myImplementMethodInfo,
									interfaceMethodParams)
							: null);

			Collection<?> implementorBeans = implementMethodBeanInfo
					.getImplementorBeans();

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
						int methodInfoPriority = compareImplementMethodInfoPriority(interfacee,
								interfaceMethod, interfaceMethodParams,
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
				: new InterfaceMethodInvocationInfo(implementMethodInfo,
						implementorBean));
	}

	/**
	 * 判断对象数组是否能作为{@linkplain ImplementMethodInfo#getImplementMethod()}方法的参数数组。
	 * 
	 * @param implementMethodInfo
	 * @param params
	 * @return
	 */
	protected boolean isImplementMethodParamValid(
			ImplementMethodInfo implementMethodInfo, Object[] params)
	{
		Class<?>[] paramTypes = implementMethodInfo.getParamTypes();

		if (paramTypes.length > params.length)
			return false;

		int[] paramIndexes = implementMethodInfo.getParamIndexes();

		for (int i = 0; i < paramTypes.length; i++)
		{
			Class<?> myParamType = paramTypes[i];
			Object myParam = params[paramIndexes[i]];

			if (!myParamType.isInstance(myParam))
				return false;
		}

		return true;
	}

	/**
	 * 提取{@linkplain ImplementMethodInfo#getValidityMethod()}方法的参数数组。
	 * <p>
	 * 如果{@linkplain ImplementMethodInfo#hasValidityMethod()}为{@code false}
	 * ，此方法将返回{@code null}。
	 * </p>
	 * 
	 * @param implementMethodInfo
	 * @param implementMethodParams
	 * @return
	 */
	protected Object[] extractValidityMethodParams(
			ImplementMethodInfo implementMethodInfo,
			Object[] implementMethodParams)
	{
		if (!implementMethodInfo.hasValidityMethod())
			return null;

		return subArrayByIndex(implementMethodParams,
				implementMethodInfo.getValidityParamIndexes());
	}

	/**
	 * 提取{@linkplain ImplementMethodInfo#getPriorityMethod()}方法的参数数组。
	 * <p>
	 * 如果{@linkplain ImplementMethodInfo#hasPriorityMethod()}为{@code false}
	 * ，此方法将返回{@code null}。
	 * </p>
	 * 
	 * @param implementMethodInfo
	 * @param implementMethodParams
	 * @return
	 */
	protected Object[] extractPriorityMethodParams(
			ImplementMethodInfo implementMethodInfo,
			Object[] implementMethodParams)
	{
		if (!implementMethodInfo.hasPriorityMethod())
			return null;

		return subArrayByIndex(implementMethodParams,
				implementMethodInfo.getPriorityParamIndexes());
	}

	/**
	 * 比较两个对{@code interfaceMethodParams}都有效的{@linkplain ImplementMethodInfo}
	 * 的优先级。
	 * <p>
	 * 如果是{@code first}的优先级高，返回{@code >0}；如果是{@code second}的优先级高，返回{@code <0}
	 * ；如果二者相同，返回{@code 0}。
	 * </p>
	 * 
	 * @param interfacee
	 * @param interfaceMethod
	 * @param interfaceMethodParams
	 * @param first
	 * @param second
	 * @return
	 */
	protected int compareImplementMethodInfoPriority(Class<?> interfacee,
			Method interfaceMethod, Object[] interfaceMethodParams,
			ImplementMethodInfo first, ImplementMethodInfo second)
	{
		int priority = compareImplementMethodParamTypePriority(interfacee,
				interfaceMethod, interfaceMethodParams, first, second);
	
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
			priority = compareImplementorPriority(interfacee,
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
	 * @param interfacee
	 * @param interfaceMethod
	 * @param interfaceMethodParams
	 * @param first
	 * @param second
	 * @return
	 */
	protected int compareImplementMethodParamTypePriority(Class<?> interfacee,
			Method interfaceMethod, Object[] interfaceMethodParams,
			ImplementMethodInfo first, ImplementMethodInfo second)
	{
		// 比较二者的参数类型哪一个与interfaceMethodParams更贴近
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
	 * @param interfacee
	 * @param firstImplementor
	 * @param secondImplementor
	 * @return
	 */
	protected int compareImplementorPriority(Class<?> interfacee,
			Class<?> firstImplementor, Class<?> secondImplementor)
	{
		String interfacePkg = interfacee.getPackage().getName();
	
		boolean firstSamePkg = firstImplementor.getPackage().getName()
				.startsWith(interfacePkg);
	
		boolean secondSamePkg = secondImplementor.getPackage().getName()
				.startsWith(interfacePkg);
	
		// 如果都跟或都不跟interfacee在同一个包下，则认为相同，否则，不跟interfacee在同一个包下的优先级高
		if (firstSamePkg)
		{
			return (secondSamePkg ? 0 : -1);
		}
		else
			return (secondSamePkg ? 1 : 0);
	}

	/**
	 * 根据索引数组提取子对象数组。
	 * 
	 * @param source
	 * @param indexes
	 * @return
	 */
	protected Object[] subArrayByIndex(Object[] source, int[] indexes)
	{
		Object[] subObjs = new Object[indexes.length];

		for (int i = 0; i < indexes.length; i++)
		{
			subObjs[i] = source[indexes[i]];
		}

		return subObjs;
	}
}
