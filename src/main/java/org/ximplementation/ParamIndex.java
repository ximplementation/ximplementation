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
 * This annotation indicate that the parameter value should be set to specified
 * index value of the corresponding <i>implementee</i> method parameter values
 * while its method invocation.
 * </p>
 * <p>
 * This annotation can be annotated on implement method parameter,
 * {@linkplain Validity @Validity} method parameter and
 * {@linkplain Priority @Priority} method parameter of an <i>implementor</i>.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface ParamIndex
{
	/**
	 * The index of the corresponding <i>implementee</i> method parameter values
	 * array.
	 * <p>
	 * The index should be start with {@code 0}.
	 * </p>
	 * 
	 * @return
	 */
	int value();
}
