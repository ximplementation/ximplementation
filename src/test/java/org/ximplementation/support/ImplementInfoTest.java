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
 * {@linkplain ImplementInfo} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-29
 *
 */
public class ImplementInfoTest extends AbstractTestSupport
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
