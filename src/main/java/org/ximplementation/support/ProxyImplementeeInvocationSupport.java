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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invocation support for proxy <i>implementee</i>.
 * <p>
 * It can be used as {@linkplain InvocationHandler} for building proxy
 * <i>implementee</i>.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-9-21
 *
 */
public class ProxyImplementeeInvocationSupport
{
	protected Implementation<?> implementation;

	protected ImplementorBeanFactory implementorBeanFactory;

	protected ImplementeeMethodInvocationInfoEvaluator implementeeMethodInvocationInfoEvaluator;

	public ProxyImplementeeInvocationSupport()
	{
		super();
	}

	public ProxyImplementeeInvocationSupport(Implementation<?> implementation,
			ImplementorBeanFactory implementorBeanFactory,
			ImplementeeMethodInvocationInfoEvaluator implementeeMethodInvocationInfoEvaluator)
	{
		super();
		this.implementation = implementation;
		this.implementorBeanFactory = implementorBeanFactory;
		this.implementeeMethodInvocationInfoEvaluator = implementeeMethodInvocationInfoEvaluator;
	}

	public Implementation<?> getImplementation()
	{
		return implementation;
	}

	public void setImplementation(Implementation<?> implementation)
	{
		this.implementation = implementation;
	}

	public ImplementorBeanFactory getImplementorBeanFactory()
	{
		return implementorBeanFactory;
	}

	public void setImplementorBeanFactory(
			ImplementorBeanFactory implementorBeanFactory)
	{
		this.implementorBeanFactory = implementorBeanFactory;
	}

	public ImplementeeMethodInvocationInfoEvaluator getImplementeeMethodInvocationInfoEvaluator()
	{
		return implementeeMethodInvocationInfoEvaluator;
	}

	public void setImplementeeMethodInvocationInfoEvaluator(
			ImplementeeMethodInvocationInfoEvaluator implementeeMethodInvocationInfoEvaluator)
	{
		this.implementeeMethodInvocationInfoEvaluator = implementeeMethodInvocationInfoEvaluator;
	}

	/**
	 * Invoke the specified method of the <i>implementee</i>.
	 * 
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	public Object invoke(Method method, Object[] args) throws Throwable
	{
		ImplementeeMethodInvocationInfo invocationInfo = this.implementeeMethodInvocationInfoEvaluator
				.evaluate(this.implementation, method, args,
						this.implementorBeanFactory);

		if (invocationInfo == null)
			throw new UnsupportedOperationException(
					"No valid implement method found for [" + method + "]");

		ImplementMethodInfo implementMethodInfo = invocationInfo
				.getImplementMethodInfo();

		Object implementBean = invocationInfo.getImplementorBean();
		Method implementMethod = implementMethodInfo.getImplementMethod();
		Object[] implementMethodParams = implementMethodInfo.getParams(args);

		return implementMethod.invoke(implementBean, implementMethodParams);
	}
}
