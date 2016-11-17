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
 * Validity annotation.
 * <p>
 * This annotation indicate that the <i>implement method</i> has an invocation
 * validation by a validity method, the validity method should be invoked first
 * for determining if the <i>implement method</i> is applicable for the current
 * <i>implementee method</i> invocation.
 * </p>
 * <p>
 * Validity method should return a value of {@code boolean} type, and its
 * parameters can be annotated with {@linkplain Index @Index} , for setting the
 * parameter value to specified index parameter value of the <i>implementee
 * method</i> when invoking.
 * </p>
 * <p>
 * This annotation should be annotated on <i>implement method</i>.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 * @see Implement
 * @see Index
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Validity
{
	/**
	 * The validity method match pattern.
	 * <p>
	 * See {@linkplain Implement#value()} API documentation for detail match
	 * pattern format.
	 * </p>
	 * <p>
	 * Examples :
	 * <code>"isValid", "Foo.isValid", "org.example.Foo.isValid", "isValid(int, int)", "Foo.isValid(int, int)",
	 * "isValid(Integer, java.lang.Integer)", "isValid(Date, Date)", "isValid(Date[], Date[])"</code>
	 * .
	 * </p>
	 * 
	 * @return
	 */
	String value();
}
