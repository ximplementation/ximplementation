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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameter index annotation.
 * <p>
 * This annotation indicate that the method parameter value should be set to
 * specified index parameter value of the <i>implementee method</i> when
 * invoking.
 * </p>
 * <p>
 * If a method parameter is not annotated with {@code @Index}, means that its
 * value should be set to the corresponding index parameter value of the
 * <i>implementee method</i> when invoking.
 * </p>
 * <p>
 * It can be annotated on <i>implement method</i> parameters,
 * {@linkplain Validity @Validity} target method parameters and
 * {@linkplain Priority @Priority} target method parameters of an
 * <i>implementor</i>, and is useful if you don't want them align to the
 * <i>implementee method</i> parameters strictly, only one or some of them.
 * </p>
 * <p>
 * Examples:
 * </p>
 * <code>
 * 
 * <pre>
 * public interface Foo
 * {
 * 	void handle(int a, int b);
 * }
 * 
 * &#64;Implementor(Foo.class)
 * public class Bar0
 * {
 * 	&#64;Implement
 * 	public void handle(int a)
 * 	{
 * 	}
 * }
 * 
 * &#64;Implementor(Foo.class)
 * public class Bar1
 * {
 * 	&#64;Implement
 * 	public void handle(&#64;Index(1) int b)
 * 	{
 * 	}
 * }
 * 
 * &#64;Implementor(Foo.class)
 * public class Bar2
 * {
 * 	&#64;Implement
 * 	&#64;Priority(method = "getPriority")
 * 	&#64;Validity("isValid")
 * 	public void handle(int a, int b)
 * 	{
 * 	}
 * 
 * 	public int getPriority(int a)
 * 	{
 * 		return 1;
 * 	}
 * 
 * 	public boolean isValid(&#64;Index(1) int b, &#64;Index(0) int a)
 * 	{
 * 		return (a > 0);
 * 	}
 * }
 * </pre>
 * 
 * <code>
 * <p>
 * Here, if {@code Foo.handle} invocation parameter array is {@code [11, 22]},
 * then the {@code Bar0.handle} parameter array is {@code [11]} if invoked, the
 * {@code Bar1.handle} parameter array is {@code [22]} if invoked,
 * {@code Bar2.handle} parameter array is {@code [11, 22]} if invoked,
 * {@code Bar2.getPriority} parameter array is {@code [11]} if invoked,
 * {@code Bar2.isValid} parameter array is {@code [22, 11]} if invoked.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface Index
{
	/**
	 * The index of the <i>implementee method</i> parameter.
	 * <p>
	 * The first <i>implementee method</i> parameter index is {@code 0}.
	 * </p>
	 * 
	 * @return
	 */
	int value();
}
