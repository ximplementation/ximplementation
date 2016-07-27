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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implementor;

/**
 * {@linkplain AbstractInterfaceBeanBuilder}单元测试用例类。
 * 
 * @author earthangry@gmail.com
 * @date 2016年7月27日
 *
 */
public class AbstractInterfaceBeanBuilderTest
{
	private AbstractInterfaceBeanBuilder abstractInterfaceBeanBuilder;

	private Implementation implementation;

	@Before
	public void setUp() throws Exception
	{
		this.abstractInterfaceBeanBuilder = new MyInterfaceBeanBuilder();

		this.implementation = new ImplementationResolver().resolve(
				TService.class, TServiceImpl1.class, TServiceImpl2.class,
				TServiceImpl3.class);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void resolveImplementMethodBeanInfosMapTest()
	{
		Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
		
		Collection<TServiceImpl1> tServiceImpl1s = Arrays.asList(
				new TServiceImpl1(), new TServiceImpl1(), new TServiceImpl1());

		implementorBeansMap.put(TServiceImpl1.class, tServiceImpl1s);

		Collection<TServiceImpl2> tServiceImpl2s = Arrays.asList(
				new TServiceImpl2(), new TServiceImpl2(), new TServiceImpl2());

		implementorBeansMap.put(TServiceImpl2.class, tServiceImpl2s);

		Collection<TServiceImpl3> tServiceImpl3s = Arrays.asList(
				new TServiceImpl3(), new TServiceImpl3(), new TServiceImpl3());

		implementorBeansMap.put(TServiceImpl3.class, tServiceImpl3s);
		
		Map<Method, ImplementMethodBeanInfo[]> map = this.abstractInterfaceBeanBuilder
				.resolveImplementMethodBeanInfosMap(TService.class,
						this.implementation, implementorBeansMap);

		assertEquals(3, map.size());

		{
			ImplementMethodBeanInfo[] implementMethodBeanInfos = map
					.get(getMethodByName(TService.class, "concat"));

			assertEquals(3, implementMethodBeanInfos.length);

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl1.class);

				assertEquals(getMethodByName(TServiceImpl1.class, "concat"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl1s, imbi.getImplementorBeans());
			}

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl2.class);

				assertEquals(getMethodByName(TServiceImpl2.class, "concat"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl2s, imbi.getImplementorBeans());
			}

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl3.class);

				assertEquals(getMethodByName(TServiceImpl3.class, "concat"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl3s, imbi.getImplementorBeans());
			}
		}

		{
			ImplementMethodBeanInfo[] implementMethodBeanInfos = map
					.get(getMethodByName(TService.class, "plus"));

			assertEquals(3, implementMethodBeanInfos.length);

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl1.class);

				assertEquals(getMethodByName(TServiceImpl1.class, "plus"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl1s, imbi.getImplementorBeans());
			}

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl2.class);

				assertEquals(getMethodByName(TServiceImpl2.class, "plus"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl2s, imbi.getImplementorBeans());
			}

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl3.class);

				assertEquals(getMethodByName(TServiceImpl3.class, "plus"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl3s, imbi.getImplementorBeans());
			}
		}

		{
			ImplementMethodBeanInfo[] implementMethodBeanInfos = map
					.get(getMethodByName(TService.class, "minus"));

			assertEquals(3, implementMethodBeanInfos.length);

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl1.class);

				assertEquals(getMethodByName(TServiceImpl1.class, "minus"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl1s, imbi.getImplementorBeans());
			}

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl2.class);

				assertEquals(getMethodByName(TServiceImpl2.class, "minus"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl2s, imbi.getImplementorBeans());
			}

			{
				ImplementMethodBeanInfo imbi = findByImplementorUnique(
						implementMethodBeanInfos, TServiceImpl3.class);

				assertEquals(getMethodByName(TServiceImpl3.class, "minus"),
						imbi.getImplementMethodInfo().getImplementMethod());

				assertEquals(tServiceImpl3s, imbi.getImplementorBeans());
			}
		}

	}

	protected Method getMethodByName(Class<?> clazz, String name)
	{
		Method[] methods = clazz.getMethods();

		for (Method method : methods)
		{
			if (method.getName().equals(name))
				return method;
		}

		return null;
	}

	protected ImplementMethodBeanInfo findByImplementorUnique(
			ImplementMethodBeanInfo[] beanInfos, Class<?> implementor)
	{
		ImplementMethodBeanInfo re = null;

		for (ImplementMethodBeanInfo beanInfo : beanInfos)
		{
			if (beanInfo.getImplementMethodInfo().getImplementor()
					.equals(implementor))
			{
				if (re != null)
					throw new IllegalStateException("Duplicate Implementor");

				re = beanInfo;
			}
		}

		return re;
	}

	public static interface TService
	{
		String concat(String a, String b);

		int plus(int a, int b);

		int minus(int a, int b);
	}

	public static class TServiceImpl1 implements TService
	{
		@Override
		public String concat(String a, String b)
		{
			return null;
		}

		@Override
		public int plus(int a, int b)
		{
			return 0;
		}

		@Override
		public int minus(int a, int b)
		{
			return 0;
		}
	}

	public static class TServiceImpl2 implements TService
	{
		@Override
		public String concat(String a, String b)
		{
			return null;
		}

		@Override
		public int plus(int a, int b)
		{
			return 0;
		}

		@Override
		public int minus(int a, int b)
		{
			return 0;
		}
	}

	@Implementor(TService.class)
	public static class TServiceImpl3
	{
		public String concat(String a, String b)
		{
			return null;
		}

		public int plus(int a, int b)
		{
			return 0;
		}

		public int minus(int a, int b)
		{
			return 0;
		}
	}

	protected static class MyInterfaceBeanBuilder
			extends AbstractInterfaceBeanBuilder
	{
	}
}
