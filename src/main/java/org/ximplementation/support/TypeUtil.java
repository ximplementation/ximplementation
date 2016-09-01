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

/**
 * Utility for type.
 * 
 * @author earthangry@gmail.com
 * @date 2016-9-1
 *
 */
public class TypeUtil
{
	private TypeUtil()
	{
	}

	/**
	 * Returns the wrapper type of the specified type if it is a primitive,
	 * returns itself if not.
	 * 
	 * @param type
	 * @return
	 */
	public static Class<?> toWrapperType(Class<?> type)
	{
		if (!type.isPrimitive())
			return type;
		else if (boolean.class.equals(type))
			return Boolean.class;
		else if (byte.class.equals(type))
			return Byte.class;
		else if (char.class.equals(type))
			return Character.class;
		else if (double.class.equals(type))
			return Double.class;
		else if (float.class.equals(type))
			return Float.class;
		else if (int.class.equals(type))
			return Integer.class;
		else if (long.class.equals(type))
			return Long.class;
		else if (short.class.equals(type))
			return Short.class;
		else
			return type;
	}
}
