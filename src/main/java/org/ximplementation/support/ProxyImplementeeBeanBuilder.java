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
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;

/**
 * Implementee bean builder based on JDK {@linkplain Proxy}.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
public class ProxyImplementeeBeanBuilder implements ImplementeeBeanBuilder
{
	private ImplementeeMethodInvocationInfoEvaluator implementeeMethodInvocationInfoEvaluator;

	public ProxyImplementeeBeanBuilder()
	{
		super();
		this.implementeeMethodInvocationInfoEvaluator = new DefaultImplementeeMethodInvocationInfoEvaluator();
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

	@Override
	public <T> T build(Implementation<T> implementation,
			Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
	{
		return doBuild(implementation,
				new SimpleImplementorBeanFactory(implementorBeansMap));
	}

	@Override
	public <T> T build(Implementation<T> implementation,
			ImplementorBeanFactory implementorBeanFactory)
	{
		return doBuild(implementation, implementorBeanFactory);
	}

	/**
	 * Do build instance.
	 * 
	 * @param implementation
	 * @param implementorBeanFactory
	 * @return
	 */
	protected <T> T doBuild(Implementation<T> implementation,
			ImplementorBeanFactory implementorBeanFactory)
	{
		Class<?> implementee = implementation.getImplementee();
		if (!implementee.isInterface())
			throw new IllegalArgumentException("[implementee] must be an interface");

		@SuppressWarnings("unchecked")
		T proxy = (T) Proxy.newProxyInstance(implementee.getClassLoader(),
				new Class<?>[] { implementee, ProxyImplementee.class },
				new ProxyImplementeeInvocationHandler(implementation,
						implementorBeanFactory,
						this.implementeeMethodInvocationInfoEvaluator));

		return proxy;
	}

	protected class ProxyImplementeeInvocationHandler extends
			ProxyImplementeeInvocationSupport
			implements InvocationHandler
	{
		public ProxyImplementeeInvocationHandler()
		{
			super();
		}

		public ProxyImplementeeInvocationHandler(
				Implementation<?> implementation,
				ImplementorBeanFactory implementorBeanFactory,
				ImplementeeMethodInvocationInfoEvaluator implementeeMethodInvocationInfoEvaluator)
		{
			super(implementation, implementorBeanFactory,
					implementeeMethodInvocationInfoEvaluator);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			if (Object.class.equals(method.getDeclaringClass()))
				return method.invoke(this, args);

			return invoke(method, args);
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + " [implementee="
					+ this.implementation.getImplementee() + "]";
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((implementation == null) ? 0
					: implementation.hashCode());
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
			ProxyImplementeeInvocationHandler other = (ProxyImplementeeInvocationHandler) obj;
			if (implementation == null)
			{
				if (other.implementation != null)
					return false;
			}
			else if (!implementation.equals(other.implementation))
				return false;
			return true;
		}
	}
}
