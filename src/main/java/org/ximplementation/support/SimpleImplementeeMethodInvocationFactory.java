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
 * Simple {@linkplain ImplementeeMethodInvocationFactory}.
 * <p>
 * It evaluates the <i>implement method</i> with the max priority, then returns
 * an {@linkplain DefaultImplementeeMethodInvocation} instance.
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
public class SimpleImplementeeMethodInvocationFactory
		extends AbstractImplementeeMethodInvocationFactory
{
	public SimpleImplementeeMethodInvocationFactory()
	{
		super();
	}

	@Override
	public ImplementeeMethodInvocation get(
			Implementation<?> implementation, Method implementeeMethod,
			Object[] invocationParams,
			ImplementorBeanFactory implementorBeanFactory) throws Throwable
	{
		ImplementMethodInfo implementMethodInfo = null;
		Object implementorBean = null;
		int priority = Integer.MIN_VALUE;

		ImplementInfo implementInfo = findImplementInfo(implementation,
				implementeeMethod);

		if (implementInfo == null || !implementInfo.hasImplementMethodInfo())
			return null;
		
		Class<?>[] invocationParamTypes = extractTypes(invocationParams);

		for (ImplementMethodInfo myImplementMethodInfo : implementInfo
				.getImplementMethodInfos())
		{
			if (!isImplementMethodParamTypeValid(implementation,
					implementInfo,
					myImplementMethodInfo,
					invocationParamTypes))
				continue;

			Collection<?> implementorBeans = implementorBeanFactory
					.getImplementorBeans(
							myImplementMethodInfo.getImplementor());

			if (implementorBeans == null || implementorBeans.isEmpty())
				continue;

			Method validityMethod = myImplementMethodInfo.getValidityMethod();
			Object[] validityMethodParams = myImplementMethodInfo
					.getValidityParams(invocationParams);
			Method priorityMethod = myImplementMethodInfo.getPriorityMethod();
			Object[] priorityMethodParams = myImplementMethodInfo
					.getPriorityParams(invocationParams);

			for (Object myImplementorBean : implementorBeans)
			{
				if (validityMethod != null)
				{
					boolean isValid = invokeValidityMethod(implementation,
							implementInfo, myImplementMethodInfo,
							validityMethod, validityMethodParams,
							myImplementorBean);

					if (!isValid)
						continue;
				}

				int myPriority = myImplementMethodInfo.getPriorityValue();

				if (priorityMethod != null)
				{
					myPriority = invokePriorityMethod(implementation,
							implementInfo, myImplementMethodInfo,
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
								implementation, implementInfo,
								invocationParamTypes,
								implementMethodInfo,
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
				: new DefaultImplementeeMethodInvocation(implementation,
						implementInfo, invocationParams,
						implementMethodInfo, implementorBean));
	}
}
