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

	protected ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory;

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
	 * @param implementeeMethod
	 *            The <i>implementee method</i> to be invoked.
	 * @param parameters
	 *            The parameters of the <i>implementee method</i>.
	 * @return The <i>implementee method</i> invocation result.
	 * @throws Throwable
	 */
	public Object invoke(Method implementeeMethod, Object[] parameters) throws Throwable
	{
		ImplementeeMethodInvocation invocation = getImplementeeMethodInvocation(
				implementeeMethod, parameters);

		if (invocation == null)
			throw new UnsupportedOperationException(
					"No valid implement method found for [" + implementeeMethod + "]");

		return invocation.invoke();
	}

	/**
	 * Get {@linkplain ImplementeeMethodInvocation}.
	 * 
	 * @param method
	 *            The <i>implementee method</i> to be invoked.
	 * @param parameters
	 *            The parameters of the <i>implementee method</i>.
	 * @return
	 * @throws Throwable
	 */
	protected ImplementeeMethodInvocation getImplementeeMethodInvocation(
			Method method, Object[] parameters) throws Throwable
	{
		return this.implementeeMethodInvocationFactory.get(this.implementation,
				method, parameters, this.implementorBeanFactory);
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [implementation=" + implementation
				+ ", implementorBeanFactory=" + implementorBeanFactory
				+ ", implementeeMethodInvocationFactory="
				+ implementeeMethodInvocationFactory + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((implementation == null) ? 0 : implementation.hashCode());
		result = prime * result + ((implementeeMethodInvocationFactory == null)
				? 0 : implementeeMethodInvocationFactory.hashCode());
		result = prime * result + ((implementorBeanFactory == null) ? 0
				: implementorBeanFactory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProxyImplementeeInvocationSupport other = (ProxyImplementeeInvocationSupport) obj;
		if (implementation == null)
		{
			if (other.implementation != null)
				return false;
		}
		else if (!implementation.equals(other.implementation))
			return false;
		if (implementeeMethodInvocationFactory == null)
		{
			if (other.implementeeMethodInvocationFactory != null)
				return false;
		}
		else if (!implementeeMethodInvocationFactory
				.equals(other.implementeeMethodInvocationFactory))
			return false;
		if (implementorBeanFactory == null)
		{
			if (other.implementorBeanFactory != null)
				return false;
		}
		else if (!implementorBeanFactory.equals(other.implementorBeanFactory))
			return false;
		return true;
	}
}
