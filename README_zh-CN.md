[英文](README.md) | [中文](README_zh-CN.md)

# ximplementation
Ximplementation是一个基于Java注解的调用时路由框架。

它包括两个核心注解：

* `@Implementor`  
此注解标注于类，表明类是某个或者某些类的实现类，就像`implements`和`extends`关键字。

* `@Implement`  
此注解标注于`@Implementor`类的方法，表明方法是实现方法，就像`@Overriden`注解。


## 示例
假设有一个接口类如下:

```java

	public interface Service<T extends Number>
	{
		T plus(T a, T b);
		T minus(T a, T b);
	}
```

那么，你可以自由地编写它的实现类：

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

> `ServiceImplMinusInteger`并不是必须的。  
> 而且，你可以在同一个或者多个其他`@Implementor`实现类内为`plus`和/或`minus`编写多个实现方法。

之后，你可以通过如下方式获得`Service`的实例：

```java

	Implementation<Service> implementation = new ImplementationResolver().resolve(Service.class,
			 	ServiceImplDefault.class, ServiceImplPlusInteger.class, ServiceImplMinusInteger.class);
	
	ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
				.valueOf(new ServiceImplDefault<Number>(), new ServiceImplPlusInteger(), new ServiceImplMinusInteger());
	
	Service<Number> service = new ProxyImplementeeBeanBuilder().build(implementation, implementorBeanFactory);
```

对于`serivce.plus`方法的调用，如果参数类型是`Integer`，将被路由至`ServiceImplPlusInteger.plus`方法，否则，将被路由至`ServiceImplDefault.plus`方法；对于`serivce.minus`方法的调用，如果参数类型是`Integer`，将被路由至`ServiceImplMinusInteger.minus`方法，否则，将被路由至`ServiceImplDefault.minus`方法。