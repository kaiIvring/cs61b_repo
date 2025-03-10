# CS61b Sp_24



## Lecture 1 - Introduction

  To run the code in `Hello.java`, we would first **compile** the code into a `.class` file using the command `javac HelloWorld.java`. Then, to run the code, we would use the command `java HelloWorld`

___

## Lecture 2 - Defining and Using Classes

Static and Non-static method:

static:针对**“类”**，"invoke using the **class name**,e.g. Dog.makeNoise" --> Class methods
属于类本身，可以直接通过类名调用，只能访问静态成员变量和静态方法(静态方法不依赖任何实例)

Non-static:针对**具体的对象**，"invoke using an **instance name**,e.g. maya.makeNoise" --> instance methods
属于类的实例，只能通过对象(实例)来调用，可以访问静态和非静态成员变量以及方法(非静态方法可以访问当前对象的所有属性和方法)
___

## Lecture 3 - Lists1 References, Recursion, and Lists

Primitive types : byte, short, int, long, float, double, boolen, char

Reference types : Everything else, e.g, arrays.

Primitive类型的变量创建时存放的**就是变量具体的值**；Reference类型的变量存放的只是一个**“reference”**，类似指针，只有使用“new”关键词后,该reference才指向具体变量，即：“new”关键词创建具体变量的值后将该变量的“地址”返回并赋给了Reference变量

___

## Lecture 4 - List2 SLLists(singly linked list)

**Access control**: public and private

public变量或方法允许用户使用和更改，private则不允许，但不起到保护作用（还是有可能“被攻击”) (用户无需在意private部分) --> 类比：public就像车的方向盘，油门... ; private就像车的发动机，内部线路之类的.

___

## Lecture 5 - List3 DLLists and Arrays(doubly linked list)

泛型:`public class SLList <pineapple>`尖括号内为占位符，可以是任意字母，后续代码引用时将其替换为具体数据类型即可

Array也是引用类型，创建一个4x4的二维数组时实际上创建了五个“引用”，一个指向“主数组”，后四个由“主数组”的四个空间分别指向四个“子数组”
___

## Lecture 6 - Testing

## write tests before write codes

___

## Lecture 7 - List4 Arrays and Lists

**Mian idea**:用数组(Array)创建一个列表(List), i.e. AList(Array_List)


数组中的泛型:Java不允许实例化泛型类型的数组(instantiate arrays of generic types):
```java
apple[] a = (apple[]) new Object[size + 1];
```
对于泛型类型的数组，当我们希望removeLast时，确实需要将最后一个元素设置为null，因为此时若不将其设为null那么Java认为我们仍保留对该元素的"reference",也就不会将该元素回收.

___

## Lecture 8 - Inheritance 1:Interface and Implementation Inheritance

## Interface(接口):可以看作是一类东西的上位词，用于定义一组方法规范（签名），而不实现这些方法的具体内容。
在具体的类中实现接口中定义的功能(继承)：
```java
public interface List61B<apple>
public class AList<apple> implements List61B<apple>
```

## Override and Overload
Override(重写):"子类对父类方法的重新定义",重写时，方法名和参数类型一致
Overload(重载):指同一类中定义多个具有相同名称但参数不同的方法，使用不同的参数调用不同的方法

## default mode
在接口中使用default关键词可以在接口中利用其他方法实现一个带有具体代码的方法，并且该方法在其子类中无需重写即可使用.同时，也可以在子类中Override父类提供的这一个default方法

## Static and Dynamic type
static type(a.k.a compile-time type):变量编译时确定的类型，通常是声明变量时指定的类型
dynamic type(a.k.a run-time type):在运行时实际引用的对象类型
赋值时看的是static type.
```java
Animal animal = new Dog();
```
上述代码中，Dog是动态类型，Animal是静态类型

**dynamic method selection**:如果动态类型重写了，使用动态类型的重写方法

静态类型用于编译时的类型检查，动态类型决定了实际调用的方法

## Summary
**Interface Inheritance**:只是说明这个接口对应的类能做什么，不包含具体的实现 a.k.a what.
**Implementation Inheritance**:即，default mode 包含具体的实现 a.k.a how(不算严格的implementation inheritance)
**关键在于理解interface是一种"is-a"关系，例如："AList is a List61B","small dog is a dog"...**

___

## Lecture 9 - Inheritance 2:Extends,Casting,Higher Order Functions

## extends:Implementation Inheritance
继承某个类，而不是接口,且可在此类的基础上添加其他方法,当然也可重写继承类中的方法
super关键词，用来访问父类中的方法：
```java
apple x = super.removeLast();
```
## Constructor Behavior
在extend的子类中使用构建函数时会默认调用父类中的默认构造函数(无参数)(因为一个类中可能有多个构建函数，若需要调用特定构造函数->使用super关键词)
```java
public VengefulSLList() {
   deletedItems = new SLList<Item>();
}

public VengefulSLList() {
   super();
   deletedItems = new SLList<Item>();
}
```
上述代码展示了默认调用父类构建函数的情况，两个构建函数是等价的

## Encapsulation(封装):
将对象的属性和方法捆绑在一起，并对外部隐藏内部实现的细节，只暴露有限的接口

## Cast(映射，转换)
改变compile-time type(static type):
*父类不能cast为子类，除非父类具有子类的动态类型*
```java
Animal a = new Animal();
Dog d = new Dog();
d = (Dog) a; // runtime error (compiler think it is ok, however the dynamic type does not match)

Animal b = new Dog();
Dog d1 = new Dog();
d1 = (Dog) b; // cast b(animal) to dog
```

```java
(poodle) maxDog(frank, frankJr)
```
上述代码将maxDog的返回类型转换为poodle(dog的子类)

## higher order functions
利用接口实现类似函数调用的功能:
用一个类继承一个接口，接口中内容相当于函数声明，类中相当于函数实现，在高阶函数实现类中以接口类型来表示函数，调用时新建"函数实现类"

```java
public static int doTwice(IntUnaryFunction f, int x){
...
}
int result = doTwice(new TenX(), 2);
```
___

## Lecture 10 - Inheritance_3:Subtype Polymorphism,Comparators,Comparable

## Subtype Polymorphism(子类型多态性):
通过父类或接口来使用其子类对象的多态性机制

## Comparable:
用于定义类的**自然顺序**,通常是类的默认排序
实现该类的接口可以将自己与其他同类对象进行比较，必须定义compareTo方法

## Comparator
为类定义额外的排序方法，通常为自定义的排序方法
实现方法：创建一个继承Comparator接口的类，并定义compare方法
一个比较器，可以将两个元素进行比较

___

## Lecture 11 - Inheritance_4:Iterators,Object Methods

## exception
throw an exception
```java
throw new exceptionName("exception content");
```
## Iterators
### The Enhanced For Loop

## Object Methods
### toSting
```java
System.out.println(x);
x.toString;
```
第一行代码实际上是调用第二行代码
### "==" vs .equals()
"==":比较变量的比特位,"golden rule of equals",对于reference type,比较的是两个变量是否指向同一个地址.
.equals():equality in the sense that we usually mean it.

*this*:表示调用当前方法的变量

```java
o instanceof Dog uddaDog
```
上述代码判断o是否是Dog类，若否则返回false，若是,则返回true并将o映射为Dog(并将o改名为uddaDog)

___

## Lecture 12 - Asymptotics_1

## order of growth(增长阶)
一个用于描述算法性能或函数增长速度的术语
big Theta (Θ) :oog is f(n) (准确增长速率)
big O (O) :oog is less than or equal to f(n) (greatest) (上界)
big Omega (Ω) :oog is greater than or equal to f(n) (least) (下界)
(时间复杂度)
f(n)表示算法的运行时间

___

## Lecture 13 - Ask Anything:Midterm 1

## DMS(dynamic method selection):
1. 方法调用时，编译器会根据变量的静态类型来判断是否可以调用某个方法
2. 方法的具体执行（即运行时的绑定）则是基于变量的动态类型。也就是说，即使静态类型中有该方法，最终调用的还是动态类型中对应的方法版本
3. 方法调用看静态类型，具体执行时看有没有动态类型重写

## public & private
General Java philosophy:consciously restrict what you're allowed to do as much as possible.

## Nested class
Static Nested Class
Non-static Nested Class(Inner Class) a.k.a 内部类
similar to static and non-static function

___

## lecture 14 - Disjoint Sets


