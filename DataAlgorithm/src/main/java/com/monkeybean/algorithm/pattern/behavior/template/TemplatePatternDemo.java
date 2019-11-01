package com.monkeybean.algorithm.pattern.behavior.template;

/**
 * 设计模式代表最佳实践, 是软件开发人员在软件开发过程中面临一般问题的解决方案, 这些解决方案是众多软件开发人员长时间的试验和错误总结而出。
 * 六大原则(记忆方法, 首字母连接为Solid):
 * 1.单一原则(Single Responsibility Principle): 一个类只负责一个功能领域的相应职责, 就一个类而言, 应该只有一个引起它变化的原因, 单一原则是实现高内聚低耦合的指导方针。
 * 2.开闭原则(Open-Closed Principle): 一个软件实体(一个软件模块或一个由多个类组成的局部结构或一个独立的类)应当对扩展开放, 对修改关闭, 即软件实体尽量在不修改原代码的情况下进行扩展, 抽象化是开闭原则的关键。
 * 3.里式替换原则(Liskov Substitution Principle): 子类可以扩展父类的功能, 但是不能改变原有父类的功能, 所有引用父类的地方必须能透明的使用其子类的对象, 因此在程序中尽量使用父类来定义, 在运行时再确定子类类型, 用子类对象替换父类对象。
 * 4.迪米特法则(Law of Demeter): 一个软件实体应尽可能少地与其他实体发生相互作用, 尽量降低类与类之间的耦合, 如果两个对象之间不必直接通信, 一个对象调用另一个对象可通过第三者转发这个调用。
 * 5.接口隔离(Interface Segregation Principle): 使用多个专门的接口, 而不是单一的总借口, 即客户端不应该依赖那些它不需要的接口, 每一个接口应该承担一种相对独立的角色, 应该对客户端提供尽可能小的单独接口。
 * 6.依赖倒置原则(Dependence Inversion Principle): 抽象不应该依赖于细节, 细节应该依赖于抽象, 面向接口编程, 而不是针对实现编程。
 * <p>
 * Design Patterns - Elements of Reusable Object-Oriented Software(中文译名：设计模式 - 可复用的面向对象软件元素)中提到总共有23种设计模式.
 * 设计模式可分为三大类:创建型模式(Creation Pattern), 结构型模式(Structural Pattern), 行为型模式(Behavior Pattern)。
 * 除此之外，还有J2EE设计模式。
 * Reference: https://www.runoob.com/design-pattern/design-pattern-intro.html
 * 设计模式分类如下:
 * <p>
 * 创建型模式: 提供了一种在创建对象的同时隐藏创建逻辑的方式, 而不是使用new运算符直接实例化对象, 这使得程序在判断针对某个给定实例需要哪些对象时更加灵活。
 * 1.工厂模式(Factory Pattern)
 * 2.抽象工厂模式(Abstract Factory Pattern)
 * 3.单例模式(Singleton Pattern)
 * 4.建造者模式(Builder Pattern)
 * 5.原型模式(Prototype Pattern)
 * <p>
 * 结构型模式: 关注类和对象的组合, 继承的概念被用来组合接口和定义组合对象获得新功能的方式。
 * 1.适配器模式(Adapter Pattern)
 * 2.桥接模式(Bridge Patten)
 * 3.过滤器模式(Filter Criteria Pattern)
 * 4.组合模式(Composition Pattern)
 * 5.装饰器模式(Decorator Pattern)
 * 6.外观模式(Facade Pattern)
 * 7.享元模式(Flyweight Pattern)
 * 8.代理模式(Proxy Pattern)
 * <p>
 * 行为型模式: 专注对象之间的通信。
 * 1.责任链模式(Chain of Responsibility Pattern)
 * 2.命令模式(Command Pattern)
 * 3.解析器模式(Interpreter Pattern)
 * 4.迭代器模式(Iterator Pattern)
 * 5.中介者模式(Mediator Pattern)
 * 6.备忘录模式(Memento Pattern)
 * 7.观察者模式(Observer Pattern)
 * 8.状态模式(State Pattern)
 * 9.空对象模式(Null Object Pattern)
 * 10.策略模式(Strategy Pattern)
 * 11.模板模式(Template Pattern)
 * 12.访问者模式(Visitor Pattern)
 * <p>
 * J2EE模式: 专注表示层。
 * 1.MVC模式(MVC Pattern)
 * 2.业务代表模式(Business Delegate Pattern)
 * 3.组合实体模式(Composite Entity Pattern)
 * 4.数据访问对象模式(Data Access Object Pattern)
 * 5.前端控制器模式(Front Controller Pattern)
 * 6.拦截过滤器模式(Interception Filter Pattern)
 * 7.服务定位器模式(Service Locator Pattern)
 * 8.传输对象模式(Transfer Object Pattern)
 * <p>
 * <p>
 * 模板模式中, 抽象类公开定义了执行它的方法/模板, 它的子类按需重写方法实现, 但调用将以抽象类中的方式进行。
 * 意图为定义操作中的算法骨架, 将一些步骤延迟到子类中, 模板方法使得子类可以不改变算法结构即可重新定义算法的某些特定步骤。
 * 关键代码在抽象类中实现, 其他代码在子类中实现。
 * 优点为:
 * 1.封装不变部分, 扩展可变部分。
 * 2.提取公共代码, 便于维护。
 * 3.行为由父类控制, 子类维护。
 * 缺点为:
 * 每一个不同的实现都要有一个子类来实现, 导致类的个数增加, 使系统更加庞大。
 * 使用场景:
 * 1.有多个子类共有的方法且逻辑相同。
 * 2.重要、复杂的方法可以考虑作为模板方法。
 * 注意事项：
 * 为防止恶意操作, 一般模板方法都加上final关键字。
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class TemplatePatternDemo {
    public static void main(String[] args) {
        Game game = new RPG();
        game.play();
        System.out.println();
        game = new Ball();
        game.play();
    }
}
