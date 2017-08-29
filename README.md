# ximplementation
Ximplementation is a framework that lets you write implementation freely based on Java annotations and makes your program support invocation routing.

It has two core annotations:

* `@Implementor`  
This annotation is annotated on class, indicating that the class is an implementation of some classes, just as the `implements` and `extends` keywords.

* `@Implement`  
This annotation is annotated on method in `@Implementor` class, indicating that the method is an implement method, just as the `@Overriden` annotation.


## Example
Suppose there is an interface:

```java

	public interface Service<T extends Number>
	{
		T plus(T a, T b);
		T minus(T a, T b);
	}
```

You can write its implementation in a very free and different way:

```java

	public class ServiceImplDefault<T extends Number> implements Service<T>
	{
		public T plus(T a, T b){...}
		public T minus(T a, T b){...}
	}
	
	public class ServiceImplPlusInteger implements Service<Integer>
	{
		@Override
		public Integer plus(Integer a, Integer b){...}
		
		@NotImplement
		@Override
		public Integer minus(Integer a, Integer b){ throw new UnsupportedOperationException(); }
	}
	
	@Implementor(Service.class)
	public class ServiceImplMinusInteger
	{
		@Implement
		public Integer minus(Integer a, Integer b){...}
	}
	
```

> The `ServiceImplMinusInteger` is not necessary if you don't want.  
> And, you can write more than one implementations for `plus` and/or `minus` methods in the same class or other `@Implementor` classes.

Then, you can get a `Service` instance by:

```java

	Implementation<Service> implementation = new ImplementationResolver().resolve(Service.class,
			 	ServiceImplDefault.class, ServiceImplPlusInteger.class, ServiceImplMinusInteger.class);
	
	ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
				.valueOf(new ServiceImplDefault<Number>(), new ServiceImplPlusInteger(), new ServiceImplMinusInteger());
	
	Service<Number> service = new ProxyImplementeeBeanBuilder().build(implementation, implementorBeanFactory);
```

The `serivce.plus` method invocation will be delegated to `ServiceImplPlusInteger.plus` method if the parameter type is `Integer`, to `ServiceImplDefault.plus` method otherwise; and the `serivce.minus` method will be delegated to `ServiceImplMinusInteger.minus` method if the parameter type is `Integer`, to `ServiceImplDefault.minus` method otherwise.