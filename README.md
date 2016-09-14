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

	Implementation implementation = new ImplementationResolver().resolve(Service.class,
			 	ServiceImplDefault.class, ServiceImplPlusInteger.class, ServiceImplMinusInteger.class);
	
	Map<Class<?>, Collection<?>> implementorBeans = new HashMap<Class<?>, Collection<?>>();
	implementorBeans.put(ServiceImplDefault.class, Arrays.asList(new ServiceImplDefault<Number>()));
	implementorBeans.put(ServiceImplPlusInteger.class, Arrays.asList(new ServiceImplPlusInteger()));
	implementorBeans.put(ServiceImplMinusInteger.class, Arrays.asList(new ServiceImplMinusInteger()));
	
	Service<Number> service = (Service<Number>)new ProxyImplementeeBeanBuilder().build(implementation, implementorBeans);
```

The `serivce.plus` method invocation will be delegated to `ServiceImplPlusInteger.plus` method if the parameter type is `Integer`, to `ServiceImplDefault.plus` method otherwise; and the `serivce.minus` method will be delegated to `ServiceImplMinusInteger.minus` method if the parameter type is `Integer`, to `ServiceImplDefault.minus` method otherwise.

## What it can do
Generally, it makes you able to change implementation of dependency without creating new dependency chain.

For example :

```java

	public class Controller<T extends Entity>
	{
		private Service<T> service;
		
		public Controller(Service<T> service)
		{
			this.service = service;
		}
		
		public String save(T entity)
		{
			service.save(entity);
			return "ok";
		}
	}
	
	public interface Service<T extends Entity>
	{
		void save(T entity);
	}
	
	public class ServiceImplOneEntity implements Service<OneEntity>
	{
		public void save(OneEntity oneEntity){...}
	}
	
	public class ServiceImplAnotherEntity implements Service<AnotherEntity>
	{
		public void save(AnotherEntity anotherEntity){...}
	}
```

You have to create a dependency chain for each implementation of `Service` traditionally :

```java

	//First
	Service<OneEntity> serviceOne = new ServiceImplOneEntity();
	Controller<OneEntity> controllerOne = new Controller<OneEntity>(serviceOne);
	
	//Second
	Service<AnotherEntity> serviceAnother = new ServiceImplAnotherEntity();
	Controller<AnotherEntity> controllerAnother = new Controller<AnotherEntity>(serviceAnother);
	
	//maybe more
	...
```

But with ximplementation, you need only to create one dependency chain no matter how many implementations for `Service` there :

```java

	Implementation implementation = ...;
	Map<Class<?>, Collection<?>> implementorBeans = ...;
	
	Service<Entity> service = (Service<Entity>)new ProxyImplementeeBeanBuilder().build(implementation, implementorBeans);
	Controller<Entity> controller = new Controller<Entity>(service);
```
