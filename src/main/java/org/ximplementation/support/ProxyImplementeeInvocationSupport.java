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

	private ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory;

	public ProxyImplementeeInvocationSupport()
	{
		super();
	}

	public ProxyImplementeeInvocationSupport(Implementation<?> implementation,
			ImplementorBeanFactory implementorBeanFactory,
			ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory)
	{
		super();
		this.implementation = implementation;
		this.implementorBeanFactory = implementorBeanFactory;
		this.implementeeMethodInvocationFactory = implementeeMethodInvocationFactory;
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

	public ImplementeeMethodInvocationFactory getImplementeeMethodInvocationFactory()
	{
		return implementeeMethodInvocationFactory;
	}

	public void setImplementeeMethodInvocationFactory(
			ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory)
	{
		this.implementeeMethodInvocationFactory = implementeeMethodInvocationFactory;
	}

	/**
	 * Invoke the specified <i>implementee method</i>.
	 * 
	 * @param method
	 *            The <i>implementee method</i> to be invoked.
	 * @param parameters
	 *            The parameters of the <i>implementee method</i>.
	 * @return The <i>implementee method</i> invocation result.
	 * @throws Throwable
	 */
	public Object invoke(Method method, Object[] parameters) throws Throwable
	{
		ImplementeeMethodInvocation invocation = this.implementeeMethodInvocationFactory
				.get(this.implementation, method, parameters,
						this.implementorBeanFactory);

		if (invocation == null)
			throw new UnsupportedOperationException(
					"No valid implement method found for [" + method + "]");

		return invocation.invoke();
	}
}
