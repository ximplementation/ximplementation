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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@linkplain TypeUtil} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-9-1
 *
 */
public class TypeUtilTest extends AbstractTestSupport
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
	public void toWrapperTypeTest()
	{
		assertEquals(Object.class, TypeUtil.toWrapperType(Object.class));
		assertEquals(Boolean.class, TypeUtil.toWrapperType(boolean.class));
		assertEquals(Byte.class, TypeUtil.toWrapperType(byte.class));
		assertEquals(Character.class, TypeUtil.toWrapperType(char.class));
		assertEquals(Double.class, TypeUtil.toWrapperType(double.class));
		assertEquals(Float.class, TypeUtil.toWrapperType(float.class));
		assertEquals(Integer.class, TypeUtil.toWrapperType(int.class));
		assertEquals(Long.class, TypeUtil.toWrapperType(long.class));
		assertEquals(Short.class, TypeUtil.toWrapperType(short.class));
	}
}
