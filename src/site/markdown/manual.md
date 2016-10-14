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

## Resolving

