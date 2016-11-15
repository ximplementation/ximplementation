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
 * Default {@linkplain MethodMatcher}.
 * 
 * @author earthangry@gmail.com
 * @date 2016-11-14
 */
public class DefaultMethodMatcher implements MethodMatcher
{
	public DefaultMethodMatcher()
	{
		super();
	}

	@Override
	public boolean match(String pattern, Method method, Class<?> clazz)
	{
		String methodName = method.getName();

		if (methodName.equals(pattern))
			return true;

		// TODO

		return false;
	}

}
