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
 * The {@linkplain ImplementeeMethodInvocationInfo} evaluator.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 *
 */
public interface ImplementeeMethodInvocationInfoEvaluator
{
	/**
	 * Evaluate {@linkplain ImplementeeMethodInvocationInfo}.
	 * <p>
	 * It should returns {@code null} if no result.
	 * </p>
	 * 
	 * @param implementation
	 * @param implementeeMethod
	 * @param implementeeMethodParams
	 * @param implementorBeanFactory
	 * @return
	 * @throws Throwable
	 */
	ImplementeeMethodInvocationInfo evaluate(
			Implementation<?> implementation,
			Method implementeeMethod, Object[] implementeeMethodParams,
			ImplementorBeanFactory implementorBeanFactory)
			throws Throwable;
}
