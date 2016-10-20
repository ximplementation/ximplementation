# Manual

## Definitions
An <b><i>implementor</i></b> is a class which implements one or more <b><i>implementee</i></b>s through the `implements` or `extends` keywords or through `@Implementor` annotation.

An <b><i>implementee</i></b> is a class which is implemented by one or more <b><i>implementor</i></b>s.

An <b><i>implement method</i></b> is a method in an <b><i>implementor</i></b> which overriding or `@Implement` methods in <b><i>implementee</i></b>. 

An <b><i>implementee method</i></b> is a method in an <b><i>implementee</i></b> which is implemented by one or more methods in <b><i>implementor</i></b>. 

## Annotations
`@Implementor`

This annotation indicate that the class is an <i>implementor</i> of some <i>implementee</i>s.

See the API documentation [Here](apidocs/org/ximplementation/Implementor.html) for more.

`@Implement`

This annotation indicate that the method is an <i>implement method</i>.

See the API documentation [Here](apidocs/org/ximplementation/Implement.html) for more.

`@NotImplement`

This annotation indicate that the method is not <i>implement method</i> of any <i>implementee method</i>.

See the API documentation [Here](apidocs/org/ximplementation/NotImplement.html) for more.

`@Validity`

This annotation indicate that the <i>implement method</i> has an invocation validation by a validity method, the validity method should be invoked first for determining if the <i>implement method</i> is applicable for the current <i>implementee method</i> invocation.

See the API documentation [Here](apidocs/org/ximplementation/Validity.html) for more.

`@Priority`

This annotation indicate that the <i>implement method</i> has an invocation priority, either a fixed value or the result of a priority method invocation.

See the API documentation [Here](apidocs/org/ximplementation/Priority.html) for more.

`@Index`

This annotation indicate that the method parameter value should be set to specified index parameter value of the <i>implementee method</i> when invoking.

See the API documentation [Here](apidocs/org/ximplementation/Index.html) for more.

`@Refered`

This annotation indicate that the method has been identified by a name, and can be referenced by this name.

See the API documentation [Here](apidocs/org/ximplementation/Refered.html) for more.

## Resolving and Instantiation
The [ImplementationResolver](apidocs/org/ximplementation/support/ImplementationResolver.html) is the main class for resolving implementations, it resolving a given <i>implementee</i> and its <i>implementor</i>s, then creating an [Implementation](apidocs/org/ximplementation/support/Implementation.html) instance which contains the implementation info for that <i>implementee</i>.

The [ImplementeeBeanBuilder](apidocs/org/ximplementation/support/ImplementeeBeanBuilder.html) is an interface for building <i>implementee</i> instances, it building an instance of an  <i>implementee</i> by its [Implementation](apidocs/org/ximplementation/support/Implementation.html) and an [ImplementorBeanFactory](apidocs/org/ximplementation/support/ImplementorBeanFactory.html).The [ProxyImplementeeBeanBuilder](apidocs/org/ximplementation/support/ProxyImplementeeBeanBuilder.html) is an concrete `ImplementeeBeanBuilder` based on JDK Proxy.

The [ImplementorBeanFactory](apidocs/org/ximplementation/support/ImplementorBeanFactory.html) is a factory for getting specified <i>implementor</i> instances when invoking the <i>implementee method</i>s. The [SimpleImplementorBeanFactory](apidocs/org/ximplementation/support/SimpleImplementorBeanFactory.html) and [PreparedImplementorBeanFactory](apidocs/org/ximplementation/support/PreparedImplementorBeanFactory.html) are concrete `ImplementorBeanFactory`s.

The [ImplementeeMethodInvocationFactory](apidocs/org/ximplementation/support/ImplementeeMethodInvocationFactory.html) is an interface for getting [ImplementeeMethodInvocation](apidocs/org/ximplementation/support/ImplementeeMethodInvocation.html) which encapsulating the final concrete invocation info(eg. the <i>implement method</i> with the max invocation priority and the corresponding <i>implementor</i> instance) for an <i>implementee method</i> invocation. The [DefaultImplementeeMethodInvocationFactory](apidocs/org/ximplementation/support/DefaultImplementeeMethodInvocationFactory.html) is a concrete `ImplementeeMethodInvocationFactory` and used by [ProxyImplementeeBeanBuilder](apidocs/org/ximplementation/support/ProxyImplementeeBeanBuilder.html) by default.

Each call on <i>implementee method</i> of an <i>implementee</i> instance created by [ProxyImplementeeBeanBuilder](apidocs/org/ximplementation/support/ProxyImplementeeBeanBuilder.html) will first call the [ImplementeeMethodInvocationFactory](apidocs/org/ximplementation/support/ImplementeeMethodInvocationFactory.html)'s `get` method for getting an [ImplementeeMethodInvocation](apidocs/org/ximplementation/support/ImplementeeMethodInvocation.html) instance, then call the got `ImplementeeMethodInvocation`'s `invoke()` method and returns its result as the <i>implementee method</i> invocation result.