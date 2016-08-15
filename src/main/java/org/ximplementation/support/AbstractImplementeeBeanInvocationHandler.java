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
public abstract class AbstractImplementeeBeanInvocationHandler
{
	private ImplementeeMethodInvocationInfoEvaluator implementeeMethodInvocationInfoEvaluator;

	public AbstractImplementeeBeanInvocationHandler()
	{
		super();
	}

	public AbstractImplementeeBeanInvocationHandler(
			ImplementeeMethodInvocationInfoEvaluator implementeeMethodInvocationInfoEvaluator)
	{
		super();
		this.implementeeMethodInvocationInfoEvaluator = implementeeMethodInvocationInfoEvaluator;
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
	 * 计算接口方法的{@linkplain ImplementeeMethodInvocationInfo 接口方法调用信息}。
	 * 
	 * @param implementation
	 * @param implementeeMethod
	 * @param implementeeMethodParams
	 * @param implementorBeanFactory
	 * @return
	 * @throws Throwable
	 */
	protected ImplementeeMethodInvocationInfo evaluateImplementeeMethodInvocationInfo(
			Implementation implementation,
			Method implementeeMethod, Object[] implementeeMethodParams,
			ImplementorBeanFactory implementorBeanFactory)
			throws Throwable
	{
		return this.implementeeMethodInvocationInfoEvaluator.evaluate(
				implementation,
				implementeeMethod, implementeeMethodParams,
				implementorBeanFactory);
	}
}
