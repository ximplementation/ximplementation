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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.NotImplement;
import org.ximplementation.Index;
import org.ximplementation.Validity;

/**
 * {@linkplain ProxyImplementeeBeanBuilder} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-9-1
 *
 */
public class ProxyImplementeeBeanBuilderTest extends AbstractTestSupport
{
	private ProxyImplementeeBeanBuilder proxyImplementeeBeanBuilder;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception
	{
		this.proxyImplementeeBeanBuilder = new ProxyImplementeeBeanBuilder();
	}

	@After
	public void tearDown() throws Exception
	{
		this.proxyImplementeeBeanBuilder = null;
	}

	@Test
	public void buildTestByMap()
	{
		Implementation<BuildTest.Implementee1> implementation = new ImplementationResolver()
				.resolve(BuildTest.Implementee1.class,
						BuildTest.Implementor0.class,
						BuildTest.Implementor1.class,
						BuildTest.Implementor2.class);
		
		ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
				.valueOf(new BuildTest.Implementor0(),
						new BuildTest.Implementor1(),
						new BuildTest.Implementor2());

		BuildTest.Implementee1 implementee1 = this.proxyImplementeeBeanBuilder
				.build(implementation, implementorBeanFactory);

		assertNotNull(implementee1);
		assertTrue(implementee1 instanceof ProxyImplementee);
	}

	@Test
	public void buildTestByImplementorBeanFactory()
	{
		Implementation<BuildTest.Implementee1> implementation = new ImplementationResolver()
				.resolve(
				BuildTest.Implementee1.class, BuildTest.Implementor0.class,
				BuildTest.Implementor1.class, BuildTest.Implementor2.class);

		Map<Class<?>, List<?>> implementorBeansMap = new HashMap<Class<?>, List<?>>();
		implementorBeansMap.put(BuildTest.Implementor0.class,
				Arrays.asList(new BuildTest.Implementor0()));
		implementorBeansMap.put(BuildTest.Implementor1.class,
				Arrays.asList(new BuildTest.Implementor1()));
		implementorBeansMap.put(BuildTest.Implementor2.class,
				Arrays.asList(new BuildTest.Implementor2()));

		BuildTest.Implementee1 implementee1 = this.proxyImplementeeBeanBuilder
				.build(implementation,
						new SimpleImplementorBeanFactory(implementorBeansMap));

		assertNotNull(implementee1);
		assertTrue(implementee1 instanceof ProxyImplementee);
	}

	@Test
	public void doBuildTestNotInterface()
	{
		Map<Class<?>, List<?>> implementorBeansMap = new HashMap<Class<?>, List<?>>();
		implementorBeansMap.put(BuildTest.Implementor0.class,
				Arrays.asList(new BuildTest.Implementor0()));
		implementorBeansMap.put(BuildTest.Implementor1.class,
				Arrays.asList(new BuildTest.Implementor1()));
		implementorBeansMap.put(BuildTest.Implementor2.class,
				Arrays.asList(new BuildTest.Implementor2()));

		ImplementorBeanFactory implementorBeanFactory = new SimpleImplementorBeanFactory(
				implementorBeansMap);

		Implementation<BuildTest.Implementee0> implementation = new ImplementationResolver()
				.resolve(
				BuildTest.Implementee0.class, BuildTest.Implementor0.class,
				BuildTest.Implementor1.class, BuildTest.Implementor2.class);

		this.expectedException.expect(IllegalArgumentException.class);
		this.expectedException.expectMessage("must be an interface");

		this.proxyImplementeeBeanBuilder.doBuild(implementation,
					implementorBeanFactory);
	}

	@Test
	public void doBuildTest()
	{
		Map<Class<?>, List<?>> implementorBeansMap = new HashMap<Class<?>, List<?>>();
		implementorBeansMap.put(BuildTest.Implementor0.class,
				Arrays.asList(new BuildTest.Implementor0()));
		implementorBeansMap.put(BuildTest.Implementor1.class,
				Arrays.asList(new BuildTest.Implementor1()));
		implementorBeansMap.put(BuildTest.Implementor2.class,
				Arrays.asList(new BuildTest.Implementor2()));

		ImplementorBeanFactory implementorBeanFactory = new SimpleImplementorBeanFactory(
				implementorBeansMap);

		Implementation<BuildTest.Implementee1> implementation = new ImplementationResolver()
				.resolve(
				BuildTest.Implementee1.class, BuildTest.Implementor0.class,
				BuildTest.Implementor1.class, BuildTest.Implementor2.class);

		BuildTest.Implementee1 implementee1 = this.proxyImplementeeBeanBuilder
				.doBuild(implementation, implementorBeanFactory);

		assertEquals(152, implementee1.plus(150, 2));
		assertEquals(BuildTest.Implementor1.MY_RE, implementee1.plus(1, 2));
		assertEquals(9, implementee1.plus(250, 9));
	}

	@Test
	public void doBuildTestThrowUnsupportedOperationException()
	{
		Map<Class<?>, List<?>> implementorBeansMap = new HashMap<Class<?>, List<?>>();
		implementorBeansMap.put(BuildTest.Implementor0.class,
				Arrays.asList(new BuildTest.Implementor0()));
		implementorBeansMap.put(BuildTest.Implementor1.class,
				Arrays.asList(new BuildTest.Implementor1()));
		implementorBeansMap.put(BuildTest.Implementor2.class,
				Arrays.asList(new BuildTest.Implementor2()));

		ImplementorBeanFactory implementorBeanFactory = new SimpleImplementorBeanFactory(
				implementorBeansMap);

		Implementation<BuildTest.Implementee1> implementation = new ImplementationResolver()
				.resolve(
				BuildTest.Implementee1.class, BuildTest.Implementor0.class,
				BuildTest.Implementor1.class, BuildTest.Implementor2.class);

		BuildTest.Implementee1 implementee1 = this.proxyImplementeeBeanBuilder
				.doBuild(implementation, implementorBeanFactory);

		this.expectedException.expect(UnsupportedOperationException.class);
		this.expectedException
				.expectMessage("No valid implement method found for");

		implementee1.minus(2, 1);
	}

	public static class BuildTest
	{
		public static abstract class Implementee0
		{
			public abstract int plus(int a, int b);

			public abstract int minus(int a, int b);
		}

		public static interface Implementee1
		{
			int plus(int a, int b);

			int minus(int a, int b);
		}

		public static class Implementor0 implements Implementee1
		{
			@Override
			public int plus(int a, int b)
			{
				return a + b;
			}

			@NotImplement
			@Override
			public int minus(int a, int b)
			{
				return 0;
			}
		}

		@Implementor(Implementee1.class)
		public static class Implementor1
		{
			public static int MY_RE = -9999;

			@Implement
			@Validity("isValid")
			public int plus(int a, int b)
			{
				return MY_RE;
			}

			public boolean isValid(int a)
			{
				return a < 100;
			}
		}

		@Implementor(Implementee1.class)
		public static class Implementor2
		{
			@Implement
			@Validity("isValid")
			public int plus(@Index(1) int b)
			{
				return b;
			}

			public boolean isValid(int a)
			{
				return a > 200;
			}
		}
	}
}
