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
 * 基于{@linkplain Proxy}的接口实例构建器。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月3日
 *
 */
public class ProxyInterfaceBeanBuilder extends AbstractInterfaceBeanBuilder implements InterfaceBeanBuilder
{
	private ImplementationResolver implementationResolver;

	private InterfaceMethodInvocationInfoEvaluator interfaceMethodInvocationInfoEvaluator;

	public ProxyInterfaceBeanBuilder()
	{
		super();
		this.implementationResolver = new ImplementationResolver();
		this.interfaceMethodInvocationInfoEvaluator = new DefaultInterfaceMethodInvocationInfoEvaluator();
	}

	public ImplementationResolver getImplementationResolver()
	{
		return implementationResolver;
	}

	public void setImplementationResolver(ImplementationResolver implementationResolver)
	{
		this.implementationResolver = implementationResolver;
	}

	public InterfaceMethodInvocationInfoEvaluator getInterfaceMethodInvocationInfoEvaluator()
	{
		return interfaceMethodInvocationInfoEvaluator;
	}

	public void setInterfaceMethodInvocationInfoEvaluator(
			InterfaceMethodInvocationInfoEvaluator interfaceMethodInvocationInfoEvaluator)
	{
		this.interfaceMethodInvocationInfoEvaluator = interfaceMethodInvocationInfoEvaluator;
	}

	@Override
	public <T> T build(Class<T> interfacee,
			Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
	{
		return doBuildInterface(interfacee, implementorBeansMap);
	}

	/**
	 * 构建接口实例。
	 * 
	 * @param interfacee
	 * @param implementorBeansMap
	 * @return
	 */
	protected <T> T doBuildInterface(Class<T> interfacee,
			Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
	{
		if (!interfacee.isInterface())
			throw new IllegalArgumentException("[interfacee] must be an interface");

		Implementation implementation = this.implementationResolver.resolve(interfacee,
				implementorBeansMap.keySet());

		Map<Method, ImplementMethodBeanInfo[]> implementMethodBeanInfosMap = resolveImplementMethodBeanInfosMap(
				interfacee, implementation, implementorBeansMap);

		@SuppressWarnings("unchecked")
		T proxy = (T) Proxy.newProxyInstance(interfacee.getClassLoader(),
				new Class<?>[] { interfacee, ProxyInterface.class },
				new InterfaceInvocationHandler(interfacee,
						implementMethodBeanInfosMap,
						this.interfaceMethodInvocationInfoEvaluator));

		return proxy;
	}

	protected static class InterfaceInvocationHandler extends AbstractInterfaceBeanInvocationHandler
			implements InvocationHandler
	{
		private Class<?> interfaceClass;

		private Map<Method, ImplementMethodBeanInfo[]> implementMethodBeanInfosMap;

		public InterfaceInvocationHandler()
		{
			super();
		}

		public InterfaceInvocationHandler(Class<?> interfaceClass,
				Map<Method, ImplementMethodBeanInfo[]> implementMethodBeanInfosMap,
				InterfaceMethodInvocationInfoEvaluator interfaceMethodInvocationInfoEvaluator)
		{
			super(interfaceMethodInvocationInfoEvaluator);
			this.interfaceClass = interfaceClass;
			this.implementMethodBeanInfosMap = implementMethodBeanInfosMap;
		}

		public Class<?> getInterfaceClass()
		{
			return interfaceClass;
		}

		public void setInterfaceClass(Class<?> interfaceClass)
		{
			this.interfaceClass = interfaceClass;
		}

		public Map<Method, ImplementMethodBeanInfo[]> getImplementMethodBeanInfosMap()
		{
			return implementMethodBeanInfosMap;
		}

		public void setImplementMethodBeanInfosMap(Map<Method, ImplementMethodBeanInfo[]> implementMethodBeanInfosMap)
		{
			this.implementMethodBeanInfosMap = implementMethodBeanInfosMap;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			if (Object.class.equals(method.getDeclaringClass()))
				return method.invoke(this, args);

			ImplementMethodBeanInfo[] implementMethodBeanInfos = this.implementMethodBeanInfosMap.get(method);

			if (implementMethodBeanInfos == null)
				throw new UnsupportedOperationException("No implement method is found for [" + method + "]");

			InterfaceMethodInvocationInfo invocationInfo = evaluateInterfaceMethodInvocationInfo(interfaceClass, method,
					args, implementMethodBeanInfos);

			if (invocationInfo == null)
				throw new UnsupportedOperationException("No valid implement is found for [" + method + "]");

			Method implementMethod = invocationInfo.getImplementMethodInfo().getImplementMethod();
			Object implementBean = invocationInfo.getImplementorBean();

			return implementMethod.invoke(implementBean, args);
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + " [interfaceClass="
					+ interfaceClass + "]";
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((interfaceClass == null) ? 0 : interfaceClass.hashCode());
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
			InterfaceInvocationHandler other = (InterfaceInvocationHandler) obj;
			if (interfaceClass == null)
			{
				if (other.interfaceClass != null)
					return false;
			}
			else if (!interfaceClass.equals(other.interfaceClass))
				return false;
			return true;
		}
	}
}
