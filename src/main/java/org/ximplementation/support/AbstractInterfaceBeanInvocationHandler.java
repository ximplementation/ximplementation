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

/**
 * 抽象接口实例调用处理类。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月6日
 *
 */
public class AbstractInterfaceBeanInvocationHandler
{
	private InterfaceMethodInvocationInfoEvaluator interfaceMethodInvocationInfoEvaluator;

	public AbstractInterfaceBeanInvocationHandler()
	{
		super();
	}

	public AbstractInterfaceBeanInvocationHandler(
			InterfaceMethodInvocationInfoEvaluator interfaceMethodInvocationInfoEvaluator)
	{
		super();
		this.interfaceMethodInvocationInfoEvaluator = interfaceMethodInvocationInfoEvaluator;
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

	/**
	 * 计算接口方法的{@linkplain InterfaceMethodInvocationInfo 接口方法调用信息}。
	 * 
	 * @param interfacee
	 * @param interfaceMethod
	 * @param interfaceMethodParams
	 * @param implementMethodBeanInfos
	 * @return
	 * @throws Throwable
	 */
	protected InterfaceMethodInvocationInfo evaluateInterfaceMethodInvocationInfo(
			Class<?> interfacee,
			Method interfaceMethod, Object[] interfaceMethodParams, ImplementMethodBeanInfo[] implementMethodBeanInfos)
			throws Throwable
	{
		return this.interfaceMethodInvocationInfoEvaluator.evaluate(interfacee,
				interfaceMethod, interfaceMethodParams,
				implementMethodBeanInfos);
	}
}
