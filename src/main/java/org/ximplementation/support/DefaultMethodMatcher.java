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
import java.util.ArrayList;
import java.util.List;

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
		MethodPattern methodPattern = parseMethodPattern(
				pattern);

		return doMatch(methodPattern, method, clazz);
	}

	/**
	 * Do match check.
	 * 
	 * @param methodPattern
	 * @param method
	 * @param clazz
	 * @return
	 */
	protected boolean doMatch(MethodPattern methodPattern,
			Method method, Class<?> clazz)
	{
		String namePattern = methodPattern.getNamePattern();

		if (namePattern == null || namePattern.isEmpty())
			return false;

		String fullMethodName = method.getDeclaringClass().getName() + "."
				+ method.getName();

		if (!fullMethodName.endsWith(namePattern))
			return false;

		String[] paramPatterns = methodPattern.getParamPatterns();

		// not set param pattern : 'plus', "TestClass.plus"
		if (paramPatterns == null)
			return true;

		Class<?>[] paramTypes = method.getParameterTypes();
		
		if (paramTypes.length != paramPatterns.length)
			return false;

		for (int i = 0; i < paramTypes.length; i++)
		{
			String paramClassName = toReadableClassName(paramTypes[i]);
			String paramPattern = paramPatterns[i];

			if (!paramClassName.endsWith(paramPattern))
				return false;
		}

		return true;
	}

	/**
	 * To readable class name, with array type to {@code "Type[]"}.
	 * 
	 * @param clazz
	 * @return
	 */
	protected String toReadableClassName(Class<?> clazz)
	{
		if (!clazz.isArray())
			return clazz.getName();
		else
		{
			StringBuilder nameBuilder = new StringBuilder();
			toReadableClassName(clazz, nameBuilder);

			return nameBuilder.toString();
		}
	}

	/**
	 * To readable class name, with array type to {@code "Type[]"}.
	 * 
	 * @param clazz
	 * @param nameBuilder
	 */
	protected void toReadableClassName(Class<?> clazz,
			StringBuilder nameBuilder)
	{
		if (clazz.isArray())
		{
			toReadableClassName(clazz.getComponentType(), nameBuilder);
			nameBuilder.append("[]");
		}
		else
			nameBuilder.append(clazz.getName());
	}

	/**
	 * Parse {@linkplain MethodPattern} from a given {@code pattern}.
	 * 
	 * @param pattern
	 * @return
	 */
	protected MethodPattern parseMethodPattern(String pattern)
	{
		pattern = pattern.trim();

		MethodPattern methodPattern = new MethodPattern();
		
		String namePattern = null;
		List<String> params = null;

		char[] chars = pattern.toCharArray();
		int i = 0, length = chars.length;

		StringBuilder segment = new StringBuilder();
		boolean leftParamBracket = false;
		
		for (; i < length; i++)
		{
			char c = chars[i];

			if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
				continue;

			if (c == '(')
			{
				if (segment.length() == 0)
					throw new IllegalMethodPatternException(
							"[" + pattern
									+ "] is illegal method pattern, method name pattern should be present before '('");

				leftParamBracket = true;
				namePattern = segment.toString();
				segment.delete(0, segment.length());
			}
			else if (c == ',')
			{
				if (segment.length() == 0)
					throw new IllegalMethodPatternException(
							"[" + pattern
									+ "] is illegal method pattern, parameter type pattern should be present before ','");

				if (params == null)
					params = new ArrayList<String>();

				params.add(segment.toString());
				segment.delete(0, segment.length());
			}
			else if (c == ')')
			{
				if (!leftParamBracket)
					throw new IllegalMethodPatternException(
							"[" + pattern
									+ "] is illegal method pattern, '(' should be present before ')'");

				if (params == null)
					params = new ArrayList<String>();

				// handle "m()" case
				if (segment.length() > 0)
				{
					params.add(segment.toString());
					segment.delete(0, segment.length());
				}
			}
			else
				segment.append(c);
		}

		// no '(' case
		if (namePattern == null)
			namePattern = segment.toString();

		methodPattern.setNamePattern(namePattern);
		methodPattern
				.setParamPatterns((params == null ? null
						: params.toArray(new String[params.size()])));

		return methodPattern;
	}

	/**
	 * Method pattern.
	 * 
	 * @author earthangry@gmail.com
	 * @date 2016-11-15
	 *
	 */
	protected static class MethodPattern
	{
		private String namePattern;

		private String[] paramPatterns;

		public MethodPattern()
		{
			super();
		}

		public MethodPattern(String namePattern, String[] paramPatterns)
		{
			super();
			this.namePattern = namePattern;
			this.paramPatterns = paramPatterns;
		}

		public String getNamePattern()
		{
			return namePattern;
		}

		public void setNamePattern(String namePattern)
		{
			this.namePattern = namePattern;
		}

		public String[] getParamPatterns()
		{
			return paramPatterns;
		}

		public void setParamPatterns(String[] paramPatterns)
		{
			this.paramPatterns = paramPatterns;
		}
	}
}
