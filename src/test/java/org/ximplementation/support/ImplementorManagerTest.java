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

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implementor;

/**
 * {@linkplain ImplementorManager} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-29
 *
 */
public class ImplementorManagerTest extends AbstractTestSupport
{
	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void addImplementorTest()
	{
		// onlyInterfaceForLang = true
		{
			ImplementorManager implementorManager = new ImplementorManager();

			implementorManager
					.addImplementor(AddImplementorTest.Implementor0.class);

			HashSet<Class<?>> expected = new HashSet<Class<?>>();
			expected.add(AddImplementorTest.Implementee0.class);
			expected.add(AddImplementorTest.Implementee1.class);
			expected.add(AddImplementorTest.Implementee2.class);

			HashSet<Class<?>> actual = new HashSet<Class<?>>(
					implementorManager.getImplementees());

			assertEquals(expected, actual);
		}

		// onlyInterfaceForLang = false
		{
			ImplementorManager implementorManager = new ImplementorManager();
			implementorManager.setOnlyInterfaceForLang(false);

			implementorManager
					.addImplementor(AddImplementorTest.Implementor0.class);

			HashSet<Class<?>> expected = new HashSet<Class<?>>();
			expected.add(AddImplementorTest.Implementee0.class);
			expected.add(AddImplementorTest.Implementee1.class);
			expected.add(AddImplementorTest.Implementee2.class);
			expected.add(AddImplementorTest.AbstractImplementor.class);

			HashSet<Class<?>> actual = new HashSet<Class<?>>(
					implementorManager.getImplementees());

			assertEquals(expected, actual);
		}
	}

	public static class AddImplementorTest
	{
		public static interface Implementee0
		{

		}

		public static interface Implementee1 extends Implementee0
		{
		}

		public static interface Implementee2
		{
		}

		public static abstract class AbstractImplementor
		{
		}

		@Implementor(Implementee2.class)
		public static class Implementor0 extends AbstractImplementor
				implements Implementee1
		{
		}
	}
}
