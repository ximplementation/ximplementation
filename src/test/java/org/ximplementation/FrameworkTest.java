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
				TService.class, TServiceImplDefault.class, TServiceImplSpecial.class,
				TServiceImplInteger.class, TServiceImplDouble.class);

		Map<Class<?>, Set<?>> implementorBeans = new HashMap<Class<?>, Set<?>>();

		implementorBeans.put(TServiceImplDefault.class, Collections.singleton(new TServiceImplDefault()));
		implementorBeans.put(TServiceImplSpecial.class, Collections.singleton(new TServiceImplSpecial()));
		implementorBeans.put(TServiceImplInteger.class, Collections.singleton(new TServiceImplInteger()));
		implementorBeans.put(TServiceImplDouble.class,
				Collections.singleton(new TServiceImplDouble()));

		TService tservice = (TService) this.implementeeBeanBuilder
				.build(implementation,
				implementorBeans);

		{
			String re = tservice.handle(1.0F, 2.0F);
			Assert.assertEquals(TServiceImplDefault.MY_RE, re);
		}

		{
			String re = tservice.handle(1.0F, TServiceImplSpecial.B);
			Assert.assertEquals(TServiceImplSpecial.MY_RE, re);
		}

		{
			String re = tservice.handle(1, 2);
			Assert.assertEquals(TServiceImplInteger.MY_RE, re);
		}

		{
			String re = tservice.handle(1.0D, 2.0D);
			Assert.assertEquals(TServiceImplDouble.MY_RE, re);
		}
	}

	public static interface TService
	{
		String handle(Number a, Number b);
	}

	public static class TServiceImplDefault implements TService
	{
		public static final String MY_RE = TServiceImplDefault.class
				.getSimpleName();

		@Override
		public String handle(Number a, Number b)
		{
			return MY_RE;
		}
	}

	public static class TServiceImplSpecial implements TService
	{
		public static final String MY_RE = TServiceImplSpecial.class
				.getSimpleName();

		public static final Number B = new Float(11.1F);

		@Validity("isValid")
		@Override
		public String handle(Number a, Number b)
		{
			return MY_RE;
		}

		public boolean isValid(@ParamIndex(1) Number b)
		{
			return B.equals(b);
		}
	}

	@Implementor(TService.class)
	public static class TServiceImplInteger
	{
		public static final String MY_RE = TServiceImplInteger.class
				.getSimpleName();

		@Implement("handle")
		public String handle(Integer a, Integer b)
		{
			return MY_RE;
		}
	}

	@Implementor(TService.class)
	public static class TServiceImplDouble
	{
		public static final String MY_RE = TServiceImplDouble.class
				.getSimpleName();

		@Implement("handle")
		public String handle(@ParamIndex(1) Double b)
		{
			return MY_RE;
		}
	}
}
