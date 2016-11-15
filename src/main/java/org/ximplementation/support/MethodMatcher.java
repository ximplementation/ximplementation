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

import org.ximplementation.Implement;
import org.ximplementation.Priority;
import org.ximplementation.Validity;

/**
 * Method matcher.
 * <p>
 * It is used by {@linkplain ImplementationResolver} to check if
 * {@linkplain Implement#value()} matches given <i>implementee method</i>s, to
 * find {@linkplain Validity#value()} method, to find
 * {@linkplain Priority#value()} method.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2016-11-14
 * @see ImplementationResolver
 */
public interface MethodMatcher
{
	/**
	 * Check if the {@code pattern} matches the given {@linkplain Method}.
	 * 
	 * @param pattern
	 *            The pattern used for check.
	 * @param method
	 *            The {@linkplain Method} to be checked.
	 * @param clazz
	 *            The owner {@linkplain Class} of the method to be checked, it
	 *            may be the declaring class of the method or its sub class.
	 * @return {@code true} if matched, {@code false} if not.
	 */
	boolean match(String pattern, Method method, Class<?> clazz);
}
