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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.support.Implementation;
import org.ximplementation.support.ImplementationResolver;
import org.ximplementation.support.ImplementeeBeanBuilder;
import org.ximplementation.support.ImplementorBeanFactory;
import org.ximplementation.support.ProxyImplementeeBeanBuilder;
import org.ximplementation.support.SimpleImplementorBeanFactory;

/**
 * Test case for showing concepts and design of this framework.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
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
		@SuppressWarnings("rawtypes")
		Implementation<Service> implementation = this.implementationResolver
				.resolve(
				Service.class, ServiceImplDefault.class, ServiceImplAnother.class,
				ServiceImplInteger.class, ServiceImplDouble.class);

		ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
				.valueOf(new ServiceImplDefault<Number>(),
						new ServiceImplAnother<Number>(),
						new ServiceImplInteger(), new ServiceImplDouble());

		@SuppressWarnings("unchecked")
		Service<Number> tservice = this.implementeeBeanBuilder
				.build(implementation,
						implementorBeanFactory);

		{
			String re = tservice.handle(1.0F, 2.0F);
			assertEquals(ServiceImplDefault.MY_RE, re);
		}

		{
			String re = tservice.handle(1.0F, ServiceImplAnother.B);
			assertEquals(ServiceImplAnother.MY_RE, re);
		}

		{
			String re = tservice.handle(1, 2);
			assertEquals(ServiceImplInteger.MY_RE, re);
		}

		{
			String re = tservice.handle(1.0D, 2.0D);
			assertEquals(ServiceImplDouble.MY_RE, re);
		}
	}

	public static interface Service<T extends Number>
	{
		String handle(T a, T b);
	}

	public static class ServiceImplDefault<T extends Number>
			implements Service<T>
	{
		public static final String MY_RE = ServiceImplDefault.class
				.getSimpleName();

		@Override
		public String handle(T a, T b)
		{
			return MY_RE;
		}
	}

	public static class ServiceImplAnother<T extends Number>
			implements Service<T>
	{
		public static final String MY_RE = ServiceImplAnother.class
				.getSimpleName();

		public static final Number B = new Float(11.1F);

		@Validity("isValid")
		@Override
		public String handle(T a, T b)
		{
			return MY_RE;
		}

		public boolean isValid(@Index(1) T b)
		{
			return B.equals(b);
		}
	}

	public static class ServiceImplInteger implements Service<Integer>
	{
		public static final String MY_RE = ServiceImplInteger.class
				.getSimpleName();

		@Override
		public String handle(Integer a, Integer b)
		{
			return MY_RE;
		}
	}

	@Implementor(Service.class)
	public static class ServiceImplDouble
	{
		public static final String MY_RE = ServiceImplDouble.class
				.getSimpleName();

		@Implement
		public String handle(@Index(1) Double b)
		{
			return MY_RE;
		}
	}
}
