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
 * 接口方法调用信息计算器。
 * @author zangzf
 *
 */
public interface InterfaceMethodInvocationInfoEvaluator
{
	/**
	 * 计算{@linkplain InterfaceMethodInvocationInfo}。
	 * <p>
	 * 如果无法计算，此方法应该返回{@code null}。
	 * </p>
	 * 
	 * @param interfacee
	 * @param interfaceMethod
	 * @param interfaceMethodParams
	 * @param implementMethodBeanInfos
	 * @return
	 * @throws Throwable
	 */
	InterfaceMethodInvocationInfo evaluate(Class<?> interfacee,
			Method interfaceMethod, Object[] interfaceMethodParams, ImplementMethodBeanInfo[] implementMethodBeanInfos)
			throws Throwable;
}
