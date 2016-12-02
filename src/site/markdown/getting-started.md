#Getting started
Ximplementation has two core annotations:

* `@Implementor`  
This annotation is annotated on class, indicating that the class is an implementation of some classes, just as the `implements` and `extends` keywords.

* `@Implement`  
This annotation is annotated on method in `@Implementor` class, indicating that the method is an implement method, just as the `@Overriden` annotation.


Suppose there is an interface as the following :

	public interface Service<T extends Number>
	{
		T plus(T a, T b);
		T minus(T a, T b);
	}
	
You can write its implementation in a very free and different way.

## First
Write the implementation classes :

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

The `ServiceImplDefault` and `ServiceImplPlusInteger` are written by `implements` keywords, and `ServiceImplPlusInteger` does not implement the `Service.minus` method by `@NotImplement` annotation, the `ServiceImplMinusInteger` is written by `@Implementor` and only implements the `Service.minus` method.

## Second
Create the `Service` instance :

	Implementation<Service> implementation = new ImplementationResolver().resolve(Service.class,
			 	ServiceImplDefault.class, ServiceImplPlusInteger.class, ServiceImplMinusInteger.class);
	
	ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
				.valueOf(new ServiceImplDefault<Number>(), new ServiceImplPlusInteger(), new ServiceImplMinusInteger());
	
	Service<Number> service = new ProxyImplementeeBeanBuilder().build(implementation, implementorBeanFactory);
	
## Finally
Invoke methods :

	service.plus(1.0F, 2.0F);
	service.minus(1.0F, 2.0F);
	service.plus(1, 2);
	service.minus(1, 2);

The `service.plus(1.0F, 2.0F)` and `service.minus(1.0F, 2.0F)` will be delegated to the `ServiceImplDefault` instance, the `service.plus(1, 2)` will be delegated to the `ServiceImplPlusInteger` instance, and the `service.minus(1, 2)` will be delegated to the `ServiceImplMinusInteger` instance.