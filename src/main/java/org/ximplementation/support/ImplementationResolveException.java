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

/**
 * {@linkplain Implementation} resolving exception in
 * {@linkplain ImplementationResolver}.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
 *
 */
public class ImplementationResolveException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ImplementationResolveException()
	{
		super();
	}

	public ImplementationResolveException(String message)
	{
		super(message);
	}

	public ImplementationResolveException(Throwable cause)
	{
		super(cause);
	}

	public ImplementationResolveException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
