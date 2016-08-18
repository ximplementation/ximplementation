# ximplementation
Ximplementation lets you write implementation freely based on Java annotation.

It has two core annotations:

* `@Implementor`  
This annotation indicates that the class is an implementation of some classes, just as the 'implements' and 'extends' keywords.

* `@Implement`  
This annotation indicates that the method is an implementation method, just as the '@Overriden' annotation.

## Example
Suppose there is an interface:

```java

	public interface Service
	{
		Number plus(Number a, Number b);
		Number minus(Number a, Number b);
	}
```

You can write its implementation in a very free and different way:

```java

	@Implementor(Service.class)
	public class ServiceImplPlus
	{
		@Implement("plus")
		public Number plus(Number a, Number b){...}
	}
	
	@Implementor(Service.class)
	public class ServiceImplMinus
	{
		@Implement("minus")
		Number minus(Number a, Number b){...}
	}
	
	@Implementor(Service.class)
	public class ServiceImplPlusInteger
	{
		@Implement("plus")
		public Number plus(Integer a, Integer b){...}
	}
```

> The `ServiceImplMinus` and `ServiceImplPlusInteger` are not necessary if you don't want them.  
> And, you can write more than one implementations for `plus` and/or `minus` methods in the same class or other `@Implementor` classes.

Then, you can get a `Service` instance by:

```java

	Implementation implementation = new ImplementationResolver().resolve(Service.class,
	 	ServiceImplPlus.class, ServiceImplMinus.class, ServiceImplPlusInteger.class);
	
	Map<Class<?>, Collection<?>> implementorBeans = new HashMap<Class<?>, Collection<?>>();
	implementorBeans.put(ServiceImplPlus.class, Arrays.asList(new ServiceImplPlus()));
	implementorBeans.put(ServiceImplMinus.class, Arrays.asList(new ServiceImplMinus()));
	implementorBeans.put(ServiceImplPlusInteger.class, Arrays.asList(new ServiceImplPlusInteger()));
	
	Service service = new ProxyImplementeeBeanBuilder().build(implementation, implementorBeans);
```

The `serivce.plus` method invocation will be delegated to `ServiceImplPlusInteger.plus` method while the parameter type is `Integer`, to `ServiceImplPlus.plus` method otherwise; and the `serivce.minus` method will be delegated to `ServiceImplMinus.minus` method.