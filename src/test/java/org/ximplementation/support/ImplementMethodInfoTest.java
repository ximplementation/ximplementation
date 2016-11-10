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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;

/**
 * {@linkplain ImplementMethodInfo} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-29
 *
 */
public class ImplementMethodInfoTest extends AbstractTestSupport
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
	public void getParamsTest()
	{
		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				GetParamsTest.Implementor.class,
				getMethodByName(GetParamsTest.Implementor.class, "plus"));
		implementMethodInfo.setParamIndexes(new int[] { 2, 1, 0 });

		Object[] params = implementMethodInfo
				.getParams(new Object[] { 1, 2, 3 });
		
		assertArrayEquals(new Object[] { 3, 2, 1 }, params);
	}

	public static class GetParamsTest
	{
		public static class Implementor
		{
			public int plus(int a, int b, int c)
			{
				return 0;
			}
		}
	}

	@Test
	public void hasValidityMethodTest()
	{
		// this.validityMethod == null
		{
			ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
					HasValidityMethodTest.Implementor.class, getMethodByName(
							HasValidityMethodTest.Implementor.class, "plus"));

			assertFalse(implementMethodInfo.hasValidityMethod());
		}

		// this.validityMethod != null
		{
			ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
					HasValidityMethodTest.Implementor.class, getMethodByName(
							HasValidityMethodTest.Implementor.class, "minus"));

			implementMethodInfo.setValidityMethod(getMethodByName(
					HasValidityMethodTest.Implementor.class, "minusValid"));

			assertTrue(implementMethodInfo.hasValidityMethod());
		}
	}

	public static class HasValidityMethodTest
	{
		public static class Implementor
		{
			public int plus(int a, int b, int c)
			{
				return 0;
			}

			public int minus(int a, int b)
			{
				return 0;
			}

			public boolean minusValid(int a, int b)
			{
				return true;
			}
		}
	}

	@Test
	public void getValidityParamsTest()
	{
		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				GetValidityParamsTest.Implementor.class, getMethodByName(
						GetValidityParamsTest.Implementor.class, "plus"));
		implementMethodInfo.setValidityParamIndexes(new int[] { 2, 1, 0 });

		Object[] params = implementMethodInfo
				.getValidityParams(new Object[] { 1, 2, 3 });

		assertArrayEquals(new Object[] { 3, 2, 1 }, params);
	}

	public static class GetValidityParamsTest
	{
		public static class Implementor
		{
			public int plus(int a, int b, int c)
			{
				return 0;
			}
		}
	}

	@Test
	public void hasPriorityMethodTest()
	{
		// this.priorityMethod == null
		{
			ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
					HasPriorityMethodTest.Implementor.class, getMethodByName(
							HasPriorityMethodTest.Implementor.class, "plus"));

			assertFalse(implementMethodInfo.hasPriorityMethod());
		}

		// this.priorityMethod != null
		{
			ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
					HasPriorityMethodTest.Implementor.class, getMethodByName(
							HasPriorityMethodTest.Implementor.class, "minus"));

			implementMethodInfo.setPriorityMethod(getMethodByName(
					HasPriorityMethodTest.Implementor.class, "minusValid"));

			assertTrue(implementMethodInfo.hasPriorityMethod());
		}
	}

	public static class HasPriorityMethodTest
	{
		public static class Implementor
		{
			public int plus(int a, int b, int c)
			{
				return 0;
			}

			public int minus(int a, int b)
			{
				return 0;
			}

			public boolean minusValid(int a, int b)
			{
				return true;
			}
		}
	}

	@Test
	public void getPriorityParamsTest()
	{
		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				GetPriorityParamsTest.Implementor.class, getMethodByName(
						GetPriorityParamsTest.Implementor.class, "plus"));
		implementMethodInfo.setPriorityParamIndexes(new int[] { 2, 1, 0 });

		Object[] params = implementMethodInfo
				.getPriorityParams(new Object[] { 1, 2, 3 });

		assertArrayEquals(new Object[] { 3, 2, 1 }, params);
	}

	public static class GetPriorityParamsTest
	{
		public static class Implementor
		{
			public int plus(int a, int b, int c)
			{
				return 0;
			}
		}
	}

	@Test
	public void HasImplementMethodInfoTest()
	{
		// this.implementMethodInfos == null
		{
			Method implementeeMethod = getMethodByName(
					HasImplementMethodInfoTest.Implementee.class, "plus");

			ImplementInfo implementInfo = new ImplementInfo(implementeeMethod);

			assertFalse(implementInfo.hasImplementMethodInfo());
		}

		// this.implementMethodInfos.length = 0
		{
			Method implementeeMethod = getMethodByName(
					HasImplementMethodInfoTest.Implementee.class, "plus");

			ImplementInfo implementInfo = new ImplementInfo(implementeeMethod,
					new ImplementMethodInfo[0]);

			assertFalse(implementInfo.hasImplementMethodInfo());
		}

		// this.implementMethodInfos != null && this.implementMethodInfos.length
		// > 0
		{
			Method implementeeMethod = getMethodByName(
					HasImplementMethodInfoTest.Implementee.class, "plus");

			ImplementInfo implementInfo = new ImplementInfo(implementeeMethod,
					new ImplementMethodInfo[] { new ImplementMethodInfo(
							HasImplementMethodInfoTest.Implementor0.class,
							getMethodByName(
									HasImplementMethodInfoTest.Implementor0.class,
									"plus")) });

			assertTrue(implementInfo.hasImplementMethodInfo());
		}
	}

	public static class HasImplementMethodInfoTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);

			Number minus(Number a, Number b);
		}
		
		public static class Implementor0 implements Implementee
		{
			@Override
			public Number plus(Number a, Number b)
			{
				return null;
			}

			@Override
			public Number minus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}
	}

	@Test
	public void getImplementMethodInfoTest()
	{
		// this.implementMethodInfos == null
		{
			ImplementInfo implementInfo = new ImplementInfo(getMethodByName(
					GetImplementMethodInfoTest.Implementee.class, "plus"));

			assertNull(implementInfo.getImplementMethodInfo(
					GetImplementMethodInfoTest.Implementor0.class,
					getMethodByName(
					GetImplementMethodInfoTest.Implementor0.class, "plus")));
		}

		// return implementMethodInfo;
		{
			Implementation<GetImplementMethodInfoTest.Implementee> implementation = this.implementationResolver
					.resolve(GetImplementMethodInfoTest.Implementee.class,
							GetImplementMethodInfoTest.Implementor0.class,
							GetImplementMethodInfoTest.Implementor1.class);
			
			ImplementInfo implementInfo = implementation
					.getImplementInfo(getMethodByName(
							GetImplementMethodInfoTest.Implementee.class,
							"plus"));

			Method implementMethod = getMethodByName(
					GetImplementMethodInfoTest.Implementor1.class, "plus");

			assertEquals(implementMethod,
					implementInfo.getImplementMethodInfo(
							GetImplementMethodInfoTest.Implementor1.class,
							implementMethod)
							.getImplementMethod());
		}

		// return null;
		{
			ImplementInfo implementInfo = new ImplementInfo(getMethodByName(
					GetImplementMethodInfoTest.Implementee.class, "plus"),
					new ImplementMethodInfo[0]);

			assertNull(implementInfo.getImplementMethodInfo(
					GetImplementMethodInfoTest.Implementor0.class,
					getMethodByName(
					GetImplementMethodInfoTest.Implementor0.class, "plus")));
		}
	}

	public static class GetImplementMethodInfoTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);

			Number minus(Number a, Number b);
		}

		public static class Implementor0 implements Implementee
		{
			@Override
			public Number plus(Number a, Number b)
			{
				return null;
			}

			@Override
			public Number minus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}
	}

}
