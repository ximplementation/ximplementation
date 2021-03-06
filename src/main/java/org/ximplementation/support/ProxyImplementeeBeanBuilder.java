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

/**
 * Implementee bean builder based on JDK {@linkplain Proxy}.
 * <p>
 * It creating <i>implementee</i> bean of {@linkplain Proxy} instance, with
 * {@linkplain ProxyImplementeeInvocationHandler} as its invocation handler.
 * </p>
 * <p>
 * Note that the <i>implementee</i> bean also implements the
 * {@linkplain ProxyImplementee} interface for token.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
public class ProxyImplementeeBeanBuilder implements ImplementeeBeanBuilder
{
	private ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory;

	public ProxyImplementeeBeanBuilder()
	{
		super();
		this.implementeeMethodInvocationFactory = new CachedImplementeeMethodInvocationFactory();
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
						this.implementeeMethodInvocationFactory));

		return proxy;
	}

	/**
	 * The {@linkplain InvocationHandler} for JDK {@linkplain Proxy}
	 * <i>implementee</i> bean.
	 * <p>
	 * Note that for {@code equals(Object)}, {@code hashCode()} and
	 * {@code toString()} methods, it will call the handler itself's methods.
	 * </p>
	 * 
	 * @author earthangry@gmail.com
	 * @date 2015-12-3
	 *
	 */
	public static class ProxyImplementeeInvocationHandler extends
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
				ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory)
		{
			super(implementation, implementorBeanFactory,
					implementeeMethodInvocationFactory);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			if (isEqualsMethod(method))
				return equals(args[0]);

			if (isHashCodeMethod(method))
				return hashCode();

			if (isToStringMethod(method))
				return toString();

			return invoke(method, args);
		}

		@Override
		public int hashCode()
		{
			return ProxyImplementeeInvocationHandler.class.hashCode() * 13
					+ super.hashCode();
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;

			ProxyImplementeeInvocationHandler otherHandler = null;

			if (obj instanceof ProxyImplementeeInvocationHandler)
			{
				otherHandler = (ProxyImplementeeInvocationHandler) obj;
			}
			else if (Proxy.isProxyClass(obj.getClass()))
			{
				InvocationHandler ih = Proxy.getInvocationHandler(obj);

				if (!(ih instanceof ProxyImplementeeInvocationHandler))
					return false;

				otherHandler = (ProxyImplementeeInvocationHandler) ih;
			}
			else
			{
				return false;
			}

			return super.equals(otherHandler);
		}
	}
}
