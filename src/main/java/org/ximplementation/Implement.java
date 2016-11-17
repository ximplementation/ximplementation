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
import java.lang.reflect.Method;

/**
 * Implement method annotation.
 * <p>
 * This annotation indicate that the method is an <i>implement method</i>.
 * </p>
 * <p>
 * <b>An <i>implement method</i> is a method in an <i>implementor</i> which
 * overriding or {@code @Implement} methods in <i>implementee</i>.</b>
 * </p>
 * <p>
 * <b>An <i>implementee method</i> is a method in an <i>implementee</i> which is
 * implemented by one or more methods in <i>implementor</i>.</b>
 * </p>
 * <p>
 * <i>Implement method</i> defined through {@code @Implement} is out of Java
 * syntax limitation, it can have different name, sub parameter type and less
 * parameters to the <i>implementee method</i>, and more than one <i>implement
 * method</i>s in one <i>implementor</i> for the same <i>implementee method</i>.
 * </p>
 * <p>
 * <i>Implement method</i> parameters can be annotated with
 * {@linkplain Index @Index} , for setting the parameter value to specified
 * index parameter value of the <i>implementee method</i> when invoking.
 * </p>
 * <p>
 * There may be many <i>implement method</i>s for one <i>implementee method</i>,
 * but only one of them will be invoked in an <i>implementee method</i>
 * invocation. <i>Implement method</i> annotated with
 * {@linkplain Validity @Validity} indicating it has an invocation validity,
 * annotated with {@linkplain Priority @Priority} indicating it has an
 * invocation priority comparing with other <i>implement method</i>s, only the
 * one with all parameter types matched and {@code true} validity and max
 * priority will be invoked.
 * </p>
 * <p>
 * Suppose there are two <i>implement method</i>s {@code A} and {@code B} for
 * the same <i>implementee method</i>, the priority evaluating rule is as
 * following:
 * </p>
 * <ol>
 * <li>If {@code A} or {@code B} is not annotated with
 * {@linkplain Priority @Priority}, then it will be treated as annotated with
 * {@code @Priority(priority=0)};</li>
 * <li>If the {@linkplain Priority @Priority} result of {@code A} {@code >} the
 * {@linkplain Priority @Priority} result of {@code B}, then {@code A}'s
 * priority {@code >} {@code B}'s priority;</li>
 * <li>Else, if the parameter types of {@code A} are closer to the current
 * invocation parameters than the parameter types of {@code B}, then {@code A}'s
 * priority {@code >} {@code B}'s priority (eg. {@code handle(Long)} is closer
 * to {@code handle(1L)} than {@code handle(Number)});</li>
 * <li>Else, if {@code A} is annotated with {@linkplain Validity @Validity} but
 * {@code B} not, then {@code A}'s priority {@code >} {@code B}'s priority;</li>
 * <li>Else, if {@code A}'s <i>implementor</i> is not in the same nor in the sub
 * package with the <i>implementee</i> but {@code B}'s <i>implementor</i> is,
 * then {@code A}'s priority {@code >} {@code B}'s priority;</li>
 * <li>Else, the max priority one is random.</li>
 * </ol>
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
 * public class Bar0 implements Foo
 * {
 * 	&#64;Override
 * 	public void handle(int a, int b)
 * 	{
 * 	}
 * }
 * 
 * &#64;Implementor(Foo.class)
 * public class Bar1
 * {
 * 	&#64;Implement
 * 	&#64;Priority("getPriority")
 * 	&#64;Validity("isValid")
 * 	public void handle(int a, int b)
 * 	{
 * 	}
 * 
 * 	public int getPriority(int a, int b)
 * 	{
 * 		return 1;
 * 	}
 * 
 * 	public boolean isValid(int a, int b)
 * 	{
 * 		return (a > 0);
 * 	}
 * }
 * 
 * &#64;Implementor(Foo.class)
 * public class Bar2
 * {
 * 	&#64;Implement
 * 	public void handle(int a, int b)
 * 	{
 * 	}
 * 
 * 	&#64;Implement("handle")
 * 	public void handle0(int a)
 * 	{
 * 	}
 * 
 * 	&#64;Implement("handle")
 * 	public void handle1(@Index(1) int b)
 * 	{
 * 	}
 * }
 * </pre>
 * 
 * <code>
 * <p>
 * Here, {@code Bar0.handle}, {@code Bar1.handle}, {@code Bar2.handle},
 * {@code Bar2.handle0}, {@code Bar2.handle1} are all <i>implement method</i>s
 * of {@code Foo.handle}.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 * @see Implementor
 * @see Priority
 * @see Validity
 * @see Index
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Implement
{
	/**
	 * The <i>implementee method</i> match pattern which this method
	 * implemented.
	 * <p>
	 * The match pattern is the following format:
	 * </p>
	 * <p>
	 * <i>&lt;method-name-pattern&gt</i>[([
	 * <i>&lt;parameter-type-pattern&gt,</i>] ... )]
	 * </p>
	 * <p>
	 * <i>&lt;method-name-pattern&gt</i> : [<i>&lt;class-name&gt.</i>]
	 * <i>&lt;method-name&gt</i>
	 * </p>
	 * <p>
	 * <i>&lt;parameter-type-pattern&gt</i> : <i>&lt;class-name&gt</i>
	 * </p>
	 * <p>
	 * <i>&lt;class-name&gt</i> : The {@linkplain Class#getSimpleName()} or The
	 * right-sub {@linkplain Class#getName()}, with array type pretty converted.
	 * </p>
	 * <p>
	 * <i>&lt;method-name&gt</i> : The {@linkplain Method#getName()}.
	 * </p>
	 * <p>
	 * Examples :
	 * <code>"plus", "Foo.plus", "org.example.Foo.plus", "plus(int, int)", "Foo.plus(int, int)",
	 * "plus(Integer, java.lang.Integer)", "plus(Date, Date)", "plus(Date[], Date[])"</code>
	 * .
	 * </p>
	 * <p>
	 * If not set, its value will be treated as this <i>implement method</i>
	 * name.
	 * </p>
	 * 
	 * @return
	 */
	String value() default "";
}
