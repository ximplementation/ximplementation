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

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implementor;
import org.ximplementation.ParamIndex;
import org.ximplementation.Priority;
import org.ximplementation.Refered;
import org.ximplementation.Validity;

/**
 * {@linkplain ImplementationResolver}单元测试用例。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月5日
 *
 */
public class ImplementationResolverTest
{
	private ImplementationResolver implementationResolver;

	@Before
	public void setUp() throws Exception
	{
		this.implementationResolver = new ImplementationResolver();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(TService0.class);
		implementors.add(TService1.class);
		implementors.add(TService2.class);
		implementors.add(TService3.class);

		Implementation implementation = this.implementationResolver.resolve(TService.class, implementors);

		Assert.assertNotNull(implementation);
	}

	public static interface TService
	{
		String concat(String a, String b);
	}

	@Implementor
	public static class TService0 implements TService
	{
		@Override
		public String concat(String a, String b)
		{
			return a + b;
		}
	}

	@Implementor
	public static class TService1 implements TService
	{
		public static final String MY_PREFIX = "TService1_";

		public static final String B = "b-TService1";

		@Validity("plusValid")
		@Override
		public String concat(String a, String b)
		{
			return MY_PREFIX + a + b;
		}

		public boolean plusValid(@ParamIndex(1) String b)
		{
			return B.equals(b);
		}
	}

	@Implementor(TService.class)
	public static class TService2
	{
		public static final String MY_PREFIX = "TService2_";

		public static final String B = "b-TService2";

		@Validity("plusValid")
		@Priority(1)
		public String concat(String a, String b)
		{
			return MY_PREFIX + a + b;
		}

		public boolean plusValid(@ParamIndex(1) String b)
		{
			return B.equals(b);
		}
	}

	@Implementor(TService.class)
	public static class TService3
	{
		public static final String MY_PREFIX = "TService3_";

		public static final String B = "b-TService3";

		@Validity("plusValid")
		@Priority(method = "Plus-Priority")
		public String concat(String a, String b)
		{
			return MY_PREFIX + a + b;
		}

		public boolean plusValid(@ParamIndex(1) String b)
		{
			return B.equals(b);
		}

		@Refered("Plus-Priority")
		public int plusPriority(@ParamIndex(1) String b)
		{
			return b.length();
		}
	}
}
