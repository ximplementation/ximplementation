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
 * This annotation is useful if you don't want your <i>implement method</i>,
 * {@linkplain Validity @Validity} method or {@linkplain Priority @Priority}
 * method align to the <i>implementee method</i> parameters strictly, only one
 * or some of them.
 * </p>
 * <p>
 * This annotation can be annotated on <i>implement method</i> parameters,
 * {@linkplain Validity @Validity} method parameters and
 * {@linkplain Priority @Priority} method parameters of an <i>implementor</i>.
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
