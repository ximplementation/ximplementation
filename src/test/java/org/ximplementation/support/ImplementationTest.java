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
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implementor;

/**
 * {@linkplain Implementation}单元测试用例。
 * 
 * @author earthangry@gmail.com
 * @date 2016年8月29日
 *
 */
public class ImplementationTest extends AbstractTestSupport
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
	public void getImplementInfoTest()
	{
		// this.implementInfos == null
		{
			Implementation implementation = new Implementation(
					GetImplementInfoTest.Implementee.class, null);

			assertNull(implementation.getImplementInfo(
					getMethodByName(GetImplementInfoTest.Implementee.class,
							"plus")));
		}

		// return implementInfo
		{
			Method implementeeMethod = getMethodByName(
					GetImplementInfoTest.Implementee.class, "plus");

			Implementation implementation = this.implementationResolver.resolve(
					GetImplementInfoTest.Implementee.class,
					GetImplementInfoTest.Implementor0.class);

			assertEquals(implementeeMethod,
					implementation.getImplementInfo(implementeeMethod)
							.getImplementeeMethod());
			assertEquals(1, implementation.getImplementInfo(implementeeMethod)
					.getImplementMethodInfos().length);
		}

		// return null
		{
			Implementation implementation = this.implementationResolver.resolve(
					GetImplementInfoTest.Implementee.class,
					GetImplementInfoTest.Implementor1.class);

			assertNull(implementation.getImplementInfo(getMethodByName(
					GetImplementInfoTest.Implementor0.class, "plus")));
		}
	}

	public static class GetImplementInfoTest
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
}
