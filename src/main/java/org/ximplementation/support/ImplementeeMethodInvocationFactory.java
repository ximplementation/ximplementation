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
 * Factory for getting the {@linkplain ImplementeeMethodInvocation}.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 * @see ProxyImplementeeBeanBuilder
 * @see ProxyImplementeeInvocationSupport
 */
public interface ImplementeeMethodInvocationFactory
{
	/**
	 * Gets the {@linkplain ImplementeeMethodInvocation} for specified
	 * <i>implementee</i> method invocation.
	 * 
	 * @param implementation
	 *            The {@code Implementation} about the <i>implementee</i>.
	 * @param implementeeMethod
	 *            The <i>implementee method</i> to be invoked.
	 * @param implementeeMethodParams
	 *            The invocation parameters of the <i>implementee method</i> to
	 *            be invoked.
	 * @param implementorBeanFactory
	 *            The {@code ImplementorBeanFactory} about the
	 *            <i>implementee</i>.
	 * @return The {@code ImplementeeMethodInvocation} instance, {@code null} if
	 *         no.
	 * @throws Throwable
	 */
	ImplementeeMethodInvocation get(
			Implementation<?> implementation,
			Method implementeeMethod, Object[] implementeeMethodParams,
			ImplementorBeanFactory implementorBeanFactory)
			throws Throwable;
}
