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
import java.util.Arrays;

/**
 * 单元测试支持类。
 * 
 * @author earthangry@gmail.com
 * @date 2016年8月25日
 *
 */
public abstract class AbstractTestSupport
{
	public static Method getMethodByName(Class<?> clazz, String name)
	{
		for (Method method : clazz.getMethods())
		{
			if (method.getName().equals(name))
				return method;
		}

		for (Method method : clazz.getDeclaredMethods())
		{
			if (method.getName().equals(name))
				return method;
		}

		return null;
	}

	public static Method getMethodByName(Method[] methods, String name)
	{
		for (Method method : methods)
		{
			if (method.getName().equals(name))
				return method;
		}

		return null;
	}

	public static Method getMethodByNameAndType(Class<?> clazz, String name,
			Class<?>... paramTypes)
	{
		for (Method method : clazz.getMethods())
		{
			if (method.getName().equals(name)
					&& Arrays.equals(method.getParameterTypes(), paramTypes))
				return method;
		}

		for (Method method : clazz.getDeclaredMethods())
		{
			if (method.getName().equals(name)
					&& Arrays.equals(method.getParameterTypes(), paramTypes))
				return method;
		}

		return null;
	}

	public static Method getMethodByNameAndType(Method[] methods, String name,
			Class<?>... paramTypes)
	{
		for (Method method : methods)
		{
			if (method.getName().equals(name)
					&& Arrays.equals(method.getParameterTypes(), paramTypes))
				return method;
		}

		return null;
	}
}
