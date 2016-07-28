# ximplementation
Ximplementation lets you write implementation freely based on Java annotation.

It has two core annotations:

* `@Implementor`  
This annotation indicates that the class is an implementation of some classes, just like the 'implements' and 'extends' keywords.

* `@Implement`  
This annotation indicates that the method is an implementation method, just like the '@Overriden' annotation.

## Example
Suppose there is an interface:

```java

	public interface Service
	{
		int plus(int a, int b);
		int minus(int a, int b);
	}
```

Then you can write its implementation in a very free and different way:

```java

	@Implementor(Service.class)
	public class ServiceImplPlus
	{
		@Implement("plus")
		int plus(int a, int b){...}
	}

	@Implementor(Service.class)
	public class ServiceImplMinus
	{
		@Implement("minus")
		int minus(int a, int b){...}
	}
```

> The `ServiceImplMinus` is not necessary if you don't want to implement the `minus` method.  
> And, you can write more than one implementations for `plus` and/or `minus` methods in the same class or other `@Implementor` classes.

Finally, you can get a `Service` instance by:

```java

	Map<Class<?>, Collection<?>> implementorBeans = new HashMap<Class<?>, Collection<?>>();
	implementorBeans.put(ServiceImplPlus.class, Arrays.asList(new ServiceImplPlus()));
	implementorBeans.put(ServiceImplMinus.class, Arrays.asList(new ServiceImplMinus()));
	
	Service service = new ProxyInterfaceBeanBuilder().build(Service.class, implementorBeans);
```