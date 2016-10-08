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
 * This annotation should be annotated on <i>implement method</i>.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Validity
{
	/**
	 * The identification of the validity method.
	 * <p>
	 * It can be any of the following values :
	 * </p>
	 * <ul>
	 * <li>The name of the validity method if no duplication;</li>
	 * <li>The {@linkplain Refered#value() @Refered.value} of the validity
	 * method;</li>
	 * <li>The signature part of the validity method (eg.
	 * <code>"isValid(java.lang.String)"</code>).</li>
	 * </ul>
	 * 
	 * @return
	 */
	String value();
}
