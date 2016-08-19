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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implement;
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

		Implementation implementation = this.implementationResolver
				.resolve(TService.class, implementors);

		assertNotNull(implementation);
	}

	@Test
	public void doResolveTest()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(DoResolveTest.Implementor0.class);
		implementors.add(DoResolveTest.Implementor1.class);

		Implementation implementation = this.implementationResolver
				.doResolve(DoResolveTest.Implementee.class, implementors);
		
		assertEquals(DoResolveTest.Implementee.class,
				implementation.getImplementee());
		assertEquals(1, implementation.getImplementInfos().length);
		assertEquals("handle", implementation.getImplementInfos()[0]
				.getImplementeeMethod().getName());
	}

	public static class DoResolveTest
	{
		public static class Implementee
		{
			public static void notImplementable()
			{
			}

			public void handle()
			{
			}
		}

		public static class Implementor0 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}

		public static class Implementor1 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}
	}

	@Test
	public void resolveImplementInfoTest()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(ResolveImplementInfoTest.Implementor0.class);
		implementors.add(ResolveImplementInfoTest.Implementor1.class);
		implementors.add(ResolveImplementInfoTest.NotImplementor.class);

		Method[] implementeeMethods = ResolveImplementInfoTest.Implementee.class.getMethods();
		Method implementeeMethod = getMethodByName(implementeeMethods,
				"handle");
		
		ImplementInfo implementInfo = this.implementationResolver.resolveImplementInfo(
						ResolveImplementInfoTest.Implementee.class,
						implementeeMethods,
						implementeeMethod,
						implementors);

		assertEquals(implementeeMethod, implementInfo.getImplementeeMethod());
		assertEquals(2, implementInfo.getImplementMethodInfos().length);
	}

	public static class ResolveImplementInfoTest
	{
		public static class Implementee
		{
			public void handle()
			{
			}
		}

		public static class Implementor0 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}

		public static class Implementor1 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}

		public static class NotImplementor
		{
			public void handle()
			{
			}
		}
	}

	@Test
	public void resolveImplementMethodInfoTest()
	{
		Class<?> implementee = ResolveImplementMethodInfoTest.Implementee.class;
		Method[] implementeeMethods = implementee.getMethods();
		Method implementeeMethod = getMethodByName(implementeeMethods,
				"handle");
		Class<?> implementor = ResolveImplementMethodInfoTest.Implementor.class;

		Collection<ImplementMethodInfo> implementMethodInfos = this.implementationResolver
				.resolveImplementMethodInfo(implementee, implementeeMethods,
						implementeeMethod, implementor);

		assertEquals(2, implementMethodInfos.size());

		Set<Method> expectMethods = new HashSet<Method>();
		expectMethods.add(getMethodByName(implementor, "handle"));
		expectMethods.add(getMethodByName(implementor, "handleAnother"));

		Set<Method> actualMethods = new HashSet<Method>();
		for (ImplementMethodInfo implementMethodInfo : implementMethodInfos)
		{
			actualMethods.add(implementMethodInfo.getImplementMethod());
		}

		assertEquals(expectMethods, actualMethods);
	}

	public static class ResolveImplementMethodInfoTest
	{
		public static class Implementee
		{
			public void handle()
			{
			}
		}

		public static class Implementor extends Implementee
		{
			@Override
			public void handle()
			{
			}

			@Implement("handle")
			public void handleAnother()
			{
			}

			public void notImplementHandle()
			{
			}
		}
	}

	protected static Method getMethodByName(Class<?> clazz, String name)
	{
		Method[] methods = clazz.getMethods();

		for (Method method : methods)
		{
			if (method.getName().equals(name))
				return method;
		}

		return null;
	}

	protected static Method getMethodByName(Method[] methods, String name)
	{
		for (Method method : methods)
		{
			if (method.getName().equals(name))
				return method;
		}

		return null;
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
