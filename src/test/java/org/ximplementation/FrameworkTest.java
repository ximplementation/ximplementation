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

package org.ximplementation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.support.Implementation;
import org.ximplementation.support.ImplementationResolver;
import org.ximplementation.support.ImplementeeBeanBuilder;
import org.ximplementation.support.ProxyImplementeeBeanBuilder;

/**
 * 框架设计测试。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月5日
 *
 */
public class FrameworkTest
{
	private ImplementationResolver implementationResolver;
	private ImplementeeBeanBuilder implementeeBeanBuilder;

	@Before
	public void setUp() throws Exception
	{
		this.implementationResolver = new ImplementationResolver();
		this.implementeeBeanBuilder = new ProxyImplementeeBeanBuilder();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		Implementation implementation = this.implementationResolver.resolve(
				TService.class, TService0.class, TService1.class,
				TService2.class,
				TService3.class);

		Map<Class<?>, Set<?>> implementorBeans = new HashMap<Class<?>, Set<?>>();

		implementorBeans.put(TService0.class, Collections.singleton(new TService0()));
		implementorBeans.put(TService1.class, Collections.singleton(new TService1()));
		implementorBeans.put(TService2.class, Collections.singleton(new TService2()));
		implementorBeans.put(TService3.class, Collections.singleton(new TService3()));

		TService tservice = (TService) this.implementeeBeanBuilder
				.build(implementation,
				implementorBeans);

		{
			String concat = tservice.concat("a", "b");
			Assert.assertEquals("ab", concat);
		}

		{
			String concat = tservice.concat("a", TService1.B);
			Assert.assertEquals(TService1.MY_PREFIX + "a" + TService1.B, concat);
		}

		{
			String concat = tservice.concat("a", TService2.B);
			Assert.assertEquals(TService2.MY_PREFIX + "a" + TService2.B, concat);
		}

		{
			String concat = tservice.concat("a", TService3.B);
			Assert.assertEquals(TService3.MY_PREFIX + "a" + TService3.B, concat);
		}
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
