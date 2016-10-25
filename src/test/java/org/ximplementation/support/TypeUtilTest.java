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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void resolveTypeParamsTest()
	{
		Map<TypeVariable<?>, Type> map = TypeUtil
				.resolveTypeParams(ResolveTypeParamsTest.B.class);

		assertEquals(1, map.size());
		assertThat(map, (Matcher) Matchers.hasEntry(Matchers.hasToString("T"),
				Matchers.equalTo(Integer.class)));
	}

	protected static class ResolveTypeParamsTest
	{
		public static class A<T>
		{
		}

		public static class B extends A<Integer>
		{
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void doResolveTypeParamsTest()
	{
		// type == null
		{
			Map<TypeVariable<?>, Type> typeVariablesMap = new HashMap<TypeVariable<?>, Type>();

			TypeUtil.doResolveTypeParams(null, typeVariablesMap);

			assertEquals(0, typeVariablesMap.size());
		}

		// type is Class
		{
			Map<TypeVariable<?>, Type> typeVariablesMap = new HashMap<TypeVariable<?>, Type>();

			TypeUtil.doResolveTypeParams(DoResolveTypeParamsTest.A.class,
					typeVariablesMap);

			assertEquals(3, typeVariablesMap.size());

			assertThat(typeVariablesMap,
					Matchers.hasEntry(Matchers.hasToString("SI"),
							Matchers.hasToString("T")));
			assertThat(typeVariablesMap,
					(Matcher) Matchers.hasEntry(Matchers.hasToString("G"),
							Matchers.equalTo(String.class)));
			assertThat(typeVariablesMap,
					(Matcher) Matchers.hasEntry(Matchers.hasToString("T"),
							Matchers.equalTo(Integer.class)));
		}
	}

	protected static class DoResolveTypeParamsTest
	{
		public static interface ISuperInterface<SI>
		{

		}

		public static interface SuperInterface<G>
		{
		}

		public static class SuperClass<T> implements ISuperInterface<T>
		{
		}

		public static class A extends SuperClass<Integer>
				implements SuperInterface<String>
		{
		}
	}

	@Test
	public void getFinalTypeTest()
	{
		Map<TypeVariable<?>, Type> typeVariablesMap = new HashMap<TypeVariable<?>, Type>();

		TypeUtil.doResolveTypeParams(GetFinalTypeTest.A.class,
				typeVariablesMap);

		{
			TypeVariable<?> typeVariable = GetFinalTypeTest.ISuperInterface.class
					.getTypeParameters()[0];

			Type finalType = TypeUtil.getFinalType(typeVariable,
					typeVariablesMap);

			assertEquals(Integer.class, finalType);
		}

		{
			TypeVariable<?> typeVariable = GetFinalTypeTest.SuperInterface.class
					.getTypeParameters()[0];

			Type finalType = TypeUtil.getFinalType(typeVariable,
					typeVariablesMap);

			assertEquals(String.class, finalType);
		}

		// ttype == null
		{
			TypeVariable<?> typeVariable = GetFinalTypeTest.AnotherClass.class
					.getTypeParameters()[0];

			Type finalType = TypeUtil.getFinalType(typeVariable,
					typeVariablesMap);

			assertEquals(typeVariable, finalType);
		}
	}

	protected static class GetFinalTypeTest
	{
		public static interface ISuperInterface<SI>
		{

		}

		public static interface SuperInterface<G>
		{
		}

		public static class SuperClass<T> implements ISuperInterface<T>
		{
		}

		public static class AnotherClass<U>
		{
		}

		public static class A extends SuperClass<Integer>
				implements SuperInterface<String>
		{
		}
	}

	@Test
	public void isOverriddenEqualsTest()
	{
		Map<TypeVariable<?>, Type> subTypeParams = new HashMap<TypeVariable<?>, Type>();

		TypeUtil.doResolveTypeParams(IsOverriddenEqualsTest.A.class,
				subTypeParams);

		// superTypeParam instanceof TypeVariable<?>
		{
			TypeVariable<?> superTypeParam = IsOverriddenEqualsTest.ISuperInterface.class
					.getTypeParameters()[0];

			assertTrue(TypeUtil.isOverriddenEquals(superTypeParam,
					Integer.class,
					subTypeParams));
		}

		// superTypeParam.equals(subTypeParam)
		{
			assertTrue(TypeUtil.isOverriddenEquals(Integer.class,
					Integer.class, subTypeParams));
		}

		// superTypeParam instanceof TypeVariable<?>
		// !(subTypeParam instanceof TypeVariable<?>)
		{
			TypeVariable<?> superTypeParam = IsOverriddenEqualsTest.ISuperInterface.class
					.getTypeParameters()[0];

			assertFalse(TypeUtil.isOverriddenEquals(superTypeParam,
					Integer.class, null));
		}

		// superTypeParam instanceof TypeVariable<?>
		// abounds.length != bbounds.length
		{
			Type superTypeParam = getMethodByName(
					IsOverriddenEqualsTest.B.class, "m0")
							.getGenericParameterTypes()[0];

			Type subTypeParam = getMethodByName(IsOverriddenEqualsTest.B.class,
					"m1").getGenericParameterTypes()[0];

			assertFalse(TypeUtil.isOverriddenEquals(superTypeParam,
					subTypeParam, null));
		}

		// superTypeParam instanceof TypeVariable<?>
		// isOverriddenEquals(abounds[i], bbounds[i])
		{
			Type superTypeParam = getMethodByName(
					IsOverriddenEqualsTest.B.class, "m0")
							.getGenericParameterTypes()[0];

			Type subTypeParam = getMethodByName(IsOverriddenEqualsTest.B.class,
					"m2").getGenericParameterTypes()[0];

			assertTrue(TypeUtil.isOverriddenEquals(superTypeParam,
					subTypeParam, null));
		}

		// ParameterizedType
		// !isNullableEquals(pa.getRawType(), pb.getRawType())
		{
			Type superTypeParam = getMethodByName(
					IsOverriddenEqualsTest.B.class, "m3")
							.getGenericParameterTypes()[0];

			Type subTypeParam = getMethodByName(IsOverriddenEqualsTest.B.class,
					"m4").getGenericParameterTypes()[0];

			assertFalse(TypeUtil.isOverriddenEquals(superTypeParam,
					subTypeParam,
					null));
		}

		// ParameterizedType
		{
			Type superTypeParam = getMethodByName(
					IsOverriddenEqualsTest.B.class, "m3")
							.getGenericParameterTypes()[0];

			Type subTypeParam = getMethodByName(IsOverriddenEqualsTest.B.class,
					"m5").getGenericParameterTypes()[0];

			assertTrue(TypeUtil.isOverriddenEquals(superTypeParam,
					subTypeParam, null));
		}

		// GenericArrayType
		{
			Type superTypeParam = getMethodByName(
					IsOverriddenEqualsTest.B.class, "m6")
							.getGenericParameterTypes()[0];

			Type subTypeParam = getMethodByName(IsOverriddenEqualsTest.B.class,
					"m7").getGenericParameterTypes()[0];

			assertTrue(TypeUtil.isOverriddenEquals(superTypeParam, subTypeParam,
					null));
		}
	}

	protected static class IsOverriddenEqualsTest
	{
		public static interface ISuperInterface<SI>
		{

		}

		public static interface SuperInterface<G>
		{
		}

		public static class SuperClass<T> implements ISuperInterface<T>
		{
		}

		public static class A extends SuperClass<Integer>
				implements SuperInterface<String>
		{
		}

		public static interface B
		{
			<T extends Number> void m0(T t);

			<G extends Number & Serializable> void m1(G t);

			<G extends Number> void m2(G t);

			<T extends Number> void m3(Collection<? extends T> c);

			<G extends Number> void m4(List<? extends G> c);

			<G extends Number> void m5(Collection<? extends G> c);

			<T extends Number> void m6(Collection<T[]> c);

			<G extends Number> void m7(Collection<G[]> c);
		}
	}
}
