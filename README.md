### 以一个重构例子开始
#### step1
- 将switch单独抽取成一个方法amountFor()
- 将amountFor()移到Rental中
- 修改amountFor()为getCharge()，以及其他变量名称，使其可读性更强
#### step2
- 将常客积分的计算单独抽出一个方法放入Rental
- 抽出总价方法，getTotalCharge()，由于result在循环内被赋值，因此需要把整个循环一起抽出来
- 以相同的方式抽出总积分方法，getTotalFrequentRenterPoints()
- 去除无用的临时变量totalAmount、frequentRenterPoints
#### step3
- 将Rental.getCharge()搬迁至Movie中
- 将Rental.getFrequentRenterPoints()搬迁至Movie中
- 新增Price抽象类，包含一个抽象方法getPriceCode()
- 新增Price的三个子类
#### step4
- Movie不再持有_priceCode属性，而是Price对象
- 修改setPriceCode()方法实现
- Price新增抽象方法getCharge()，将Movie中getCharge()的各个条件逻辑分别散落至不同的Price子类中，并移除Movie中的getCharge()
- 以类似的方法处理Movie中的getFrequentRenterPoints()，首先为Price提供默认的getFrequentRenterPoints()实现，NewReleasePrice中对其进行覆盖

---
### 重构手法
- 抽取函数(extract method)
- 抽取类(extract class)
- 移动函数(move method)
- 使用查询替代临时变量(replace tem with query)
- 使用状态模式/策略模式替代类型码(replace type code with state/strategy)
- 使用多态替代条件判断(replace conditional with polymorphism)
- 将方法抽到父类(pull up method)
- 模板方法(form template method)
- 解耦条件(decompose conditional)
- 使用方法代替参数(replace parameter with method)
---

- 两顶帽子：添加新功能、重构。无论何时都要明白自己戴的是哪一顶帽子，添加新功能和重构不可同步进行。
- 重构重要的一点是：消除重复代码，消除重复代码则需要理解的代码就越少。

---

### 何时进行重构：
- 存在重复代码，使用==抽取函数==提炼重复代码
- 函数过长
    - 条件判断、循环通常是重构的信号
    - 注释也是重构的信号，只要函数名能准确表达所做的事情，就值得抽出单独的方法，即使只有一行代码
- 过大的类
    - 实例变量过多，可以考虑将类似的属性迁移至新的类中
    - 方法过长，可考虑抽取短方法并去除重复代码
- 参数列表过长
- 发散式变化，指的是一个类或方法使其改变的原因有多种，我们应该将此类/方法拆成多个，使每个类/方法只有一个变化的原因
- 散弹式修改，指的是一个变化引起多出代码的变更，不好找，应将这一系列相关的代码归拢到一个类中，如果没有类，就创建一个类
- 依恋情结，当一个方法使用了过多别的类的方法，应该将这个方法搬迁到那个类中。当一个方法使用了多个类的方法，应先将此方法搬迁到调用最多的方法的那个类，再将此方法拆成若干个小方法，分别搬迁至对应的类中。
- 数据泥团，两个类有相似/重复的属性，方法列表相似/过长，这些情况都考虑是否可以将其放入一个新增的类中，用以减少属性和方法参数的个数。
- 基本类型偏执，使用对象来代替基本类型(用于方法的参数列表过长，一组总是应该放在一起的属性等场景)。
- switch语句，不提倡使用switch，因为无法复用，会带来重复代码，当为一个switch添加case时，所有使用switch的地方都要加
- 平行继承体系，当为一个类增加子类，必须也要为另一个类增加子类时，应考虑让一个继承体系的实例引用另一个继承体系的实例
- 过度耦合的消息链，应向客户隐藏，使用方法进行包装
- 中间人，一个类的大部分接口都委托给其他类，则该类被称为中间人，要考虑去除
- 两个类太过亲密，尝尝发生于继承，可以考虑使用组合代替继承
- 异曲同工的类，两个类的函数功能相同，但参数列表不同，可以考虑进行重构
- 不完美的类库，对于不完美的类库，虽然不能直接更改源码，但也有手法进行改进
- Data Class，属性要私有，并提供取/设值函数
- 被拒绝的馈赠，当一个类复用了超类的实现，却又不愿意实现超类的接口时，应考虑组合
- 过多的注释，当你觉得需要写注释时，请先尝试重构，试着让注释变得多余


---
### 构筑测试体系
- 编写不完全的测试也比不写强
- 测试的重点之一是边界条件

### 重新组织函数
- Extract Method 提炼函数
- inline Method 内联函数，对于不必要的函数应去除；或者，函数提炼的不合理，内联后进行重新提炼
- Inline Temp 内联临时变量，如果变量妨碍了重构，就将其内联化，内联临时变量其实是使用查询替代临时变量的一种

```
double basePrice = anOrder.basePrice();
return (basePrice > 1000);
|
|
return (anOrder.basePrice() > 1000);
```
- Replace Temp With Query 使用查询替代临时变量
```
double basePrice = _quantity * _itemPrice;
if(basePrice > 1000){
    return basePrice * 0.95;
}else{
    return basePrice * 0.98;
}
|
|
if(basePrice() > 1000){
    return basePrice() * 0.95;
}else{
    return basePrice() * 0.98;
}
...
double basePrice(){
    return _quantity * _itemPrice;
}
```

- Introduce Explaining Variable 引入解释性变量，通常优先使用Extract Method，因为方法可以被其他客户端复用，当Extract Method 无法进行时才考虑使用Introduce Explaining Variable

```
if(platform.toUpperCase().indexOf("MAC") > -1 && browser.toUpperCase().indexOf("IE") > -1 && wasInitialized() && resize > 0){
    // do something
}
|
|
final boolean isMacOs = platform.toUpperCase().indexOf("MAC") > -1;
final boolean isIEBrowser = browser.toUpperCase().indexOf("IE") > -1;
final boolean wasResized = resize > 0;
if(isMacOs && isIEBrowser && wasInitialized() && wasResized){
    // do something
}
```
- Split Temporary Variable 分解临时变量，当一个变量被赋值多次时，要考虑该变量是否承担了多个责任，若承担了多个责任则要将其分解成多个临时变量

```
double temp = 2 * (_height + _width);
System.out.println(temp);
temp = _height * _width;
System.out.println(temp);
|
|
double perimeter = 2 * (_height + _width);
System.out.println(perimeter);
double area = _height * _width;
System.out.println(area);
```
- Remove Assignments to Parameters 移除对参数的赋值，允许改变参数的值，但要避免使参数引用另外一个对象，因为他会引起不必要的混乱
```
int discount(int inputVal){
    if(inputVal > 50){
        inputVal -= 2;
    }
    ...
}
|
|
int discount(int inputVal){
    int result = inputVal;
    if(inputVal > 50){
        result -= 2;
    }
    ...
}
```
- Replace Method with Method Object 以函数对象取代函数,当方法过大，且使用了很多临时变量,无法使用“extract method”抽出方法，考虑将整个方法移到一个新类中，临时变量作为成员变量即可
- Substitude Algorithm 替换算法，将原有算法替换为另一种更加清晰的算法

### 在对象之间搬移特性
- Move Method 搬移函数，观察该函数是否与别的类关系密切（它调用了大量别的类的方法，或者被别的类调用较多），若是，应该搬移到这个类中
- Move Field 搬移字段，该字段被别的类用的比较多，就该搬移到别的类中去
- Extract Class 提炼类，某个类做了不止一件事
- Inline Class 将类内联化，与Extract Class相反，该类没有做什么事，不足以担当一个类，那么应该将该类内联到使用它做多的那个类中
- Hide Delegate 隐藏委托关系，用于t.getA().getB().test()场景，不应向客户端暴露层级关系，应该改造成t.test()，这么做的好处是：不向客户端暴露具体细节，而且无需客户端维护调用中的出错。
- Remove Middle Man 移除中间人，与Hide Delegate相反，使用的场景是：当受托类方法越来越多时，服务端完全变成了中间人，只做中转，此时需要移除委托关系
- Introduce Foreign Method 引入外加函数，当你需要为某个类添加一个新的函数，但你又无法修改这个类时，不适用于需要大量添加外加方法的场景(应使用Introduce Local Extension)，例如：
```
Date date = new Date(previous.getYear(), previous.getMonth(), previous.getDate() + 1);
|
|
Date date = nextDay(previous);
public static Date nextDay(Date args){
    return new Date(previous.getYear(), previous.getMonth(), previous.getDate() + 1);
}
```
- Introduce Local Extension 引入本地扩展，与Introduce Foreign Method使用场景类似，用于需要为某个类添加一个新的函数，但你又无法修改这个类时，区别是，Introduce local extension适用于需要大量添加外加方法的场景，例如添加nextDay()方法的做法有两种：新增子类或者新增包装类
    - 子类
    ```
    public class MfDateSub extends Date{
        public MfDateSub(Date date){
            super(date.getTime());
        }
        public Date nextDay(){
            return new Date(getYear(), getMonth(), getDate() + 1);
        }
    }
    ```
    - 包装类
    ```
    public class MfDateWrap{
        private Date date;
        
        public MfDateWrap(Date date){
            this.date = date;
        }
        
        private int getYear(){
            return date.getYear();
        }
        
        ...getMonth(),getDate()...
        
        public Date nextDay(){
            return new Date(getYear(), getMonth(), getDate() + 1);
        }
    
    }
    ```
    - 包装类在使用中会有一个问题，即包装类的方法可以接受原始类作为参数，但是原始类方法不接受包装类作为参数，例如：dateWrap.after(date);是允许的，只需要在包装类中新增方法即可，但是date.after(dateWrap)是不允许的，因为你无法修改原始类的代码，但对于使用子类就不存在这样的问题；另外要避免覆盖包装类、原始类比较的equals()方法，因为java默认equals符合交换律，a.equals(b)为真，则b.equals(a)也为真，显然对于包装类、原始类之间是不符合的。
    
### 重新组织数据
- Self Encapsulate Field 自封装字段，建立set/get方法，通过这些方法去操作字段，该做法是饱受争议的：
    - 自封装的好处：子类可以覆盖父类的set/get方法，改变其获取的途径，可以做一些其他事情(延迟初始化等)
    - 直接访问变量的好处：代码容易阅读
    - 使用自封装字段唯一需要注意的是，不要在构造函数中使用set方法，应该对象创建完毕后使用
- Replace Data Value With Object 用对象取代数据值，例如起初可能只保存了电话号码这个属性，随着系统的扩展，又加入了抽取区号、格式化等特殊行为，此时应该建立电话号类，若没有特殊行为，只是单纯多了几个电话号码相关的属性，可以为其建立值对象，否则需要引入引用对象
- Change Value to Reference 将值对象改为引用对象。值对象是不可更改的对象，不能通过==来比较两个对象，因为值对象可能会有N个拷贝，应该使用equals方法进行比较。而引用对象使用==来比较，他代表真实世界中的一个实物(常常通过单个或多个属性进行区分引用对象之间的不同，确定唯一性)。从一开始可能使用值对象，但随着业务的增长，你可能需要向对象中加入一些可修改的属性，此时需要将值对象转换为引用对象。
- Change Reference to Value 将引用对象改为值对象，当引用对象很小且不可变时，应将其修改为值对象
- Replace Array with Object 使用对象代替数组，数组的使用场景是：以某种顺序容纳一组相似的对象。我们有时会看到，数组包含了若干个不同类型的数据，例如一个数组包含了名称、姓名、年龄等数组，此时应该使用对象去代替数组
- Duplicate Observed Data 复制被监视数据，函数可以随意搬迁，但数据不能，需要考虑同步的问题，通常使用观察者模式/事件监听器来进行数据同步
- Change Unidirectional Association to BiDirectional，将单向关联修改为双向关联。当两个类都要使用对方的特性时，可是双方持有对方的引用
- Change BiDirectional Association to Unidirectional 将双向关联改为单向关联
- Replace Magic Number with Symbolic Constant 使用字面常量代替奇异数
- Encapsulate Field 封装字段，将public字段修改为private，并提供访问函数
- Encapsulate Collection 封装集合，对于集合属性，set/get方法尤其注意，get不应该返回集合本身(这样客户端对集合做操作的话别人会一无所知)，而应该返回集合的拷贝。另外不应该提供set方法，应该提供添加/移除元素方法
- Replace Record with Data Class 以数据类型取代记录
- Replace Type Code with Class 以类取代类型码，使用枚举来代替类型码
- Replace Type Code with Subclasses 以子类取代类型码，如果类型码影响到行为，则需要使用以子类取代类型码来进行处理。有两种场景需要注意：1.该类已经有子类，2.类型码值在对象创建后发生了改变。
- Replace Type Code with State/Strategy 使用state/strategy取代类型码，用于无法通过继承手法消除的场景
- Replace Type Code with Fields 以字段取代子类，各个子类唯一差别只在“返回常量数据”函数身上

### 简化条件表达式
- Decompose Conditional 分解条件表达式
```
if(date.before(SUMMER_START) || date.after(SUMMER_END)){}
|
|
if(notSummer(date)){}
```
- Consolidate Conditional Expression 合并条件表达式
```
double disabilityAmount(){
    if(_seniority < 2){
        return 0;
    }
    if(_monthsDisabled > 12 ){
        return 0;
    }
}
|
|
double disabilityAmount(){
    if(isNotEligableForDisability()){
        return 0;
    }
}
```
- Consolidate Duplicate Conditional Fragments 合并重复的条件片段

- Remove Control Flag 移除控制标记
- 以卫语句取代嵌套条件表达式，用于多层if-else嵌套
```
double GetPayAmount()
{
    double result;
    if (IsDead())
    {
        result = DeadAmount();
    }
    else
    {
        if (IsSeparated())
        {
            result = SeparatedAmount();
        }
        else
        {
            if (IsRetired())
            {
                result = RetiredPayAmount();
            }
            else
            {
                result = NormalPayAmount();
            }
        }
    }

    return result;
}
```
修改为：
```
double getPayAmount()
{
    if (isDead())
    {
        return deadPayAmount();
    }
    if (isSeparated())
    {
        return separatedPayAmount();
    }
    if (isRetired())
    {
        return retiredPayAmount();
    }

    return normalPayAmount();
}
```
- Replace Conditional with Polymorphism 使用多态取代条件表达式
```
class Employee{
    int payAmount(){
        switch(getType()){
            case EmployeeType.ENGINEER:
                return _monthlySalary;
            case EmployeeType.SALESMAN:
                return _monthlySalary + _commission;
            。。。
        }
    }
    
    int getType(){
        return _type.getTypeCode;
    }
    private EmployeeType _type;
}

abstract class EmployeeType{
    abstract int getTypeCode();
}

class Engineer extends EmployeeType{
    int getTypeCode(){
        return Employee.ENGINEER;
    }
}

其他子类..
```
修改后：
```
class Employee{
    int payAmount(){
        return _type.payAmount(this);
    }
}

abstract class EmployeeType{
    abstract int getTypeCode();
    abstract int payAmount(Employee emp);
}

class Engineer extends EmployeeType{
    int getTypeCode(){
        return Employee.ENGINEER;
    }
    int payAmount(Employee emp){
        return emp.getMonthlySalary();
    }
}

class Salesman extends EmployeeType{
    int getTypeCode(){
        return Employee.SALESMAN;
    }
    int payAmount(Employee emp){
        return emp.getMonthlySalary() + emp.getCommission();
    }
}
```

- introduce NULL Object 引入NULL对象，在实际写代码过程中，常常会遇到NULL判断的情况，此时可引入NULL对象来优雅的处理
```
class Site{
    private Customer customer;
    public void test(){
        Customer customer = site.getCustomer();
        String customerName;
        if(customer == null){
            customerName = "test";
        }else{
            customerName = customer.getName();
        }
    }
    
    public void getCustomer(){
        return customer;
    }
}
```
修改为：
```
class Customer{
    private String name;
    
    public static Customer newNull(){
        return new NullCustomer();
    }
    
    public boolean isNull(){
        return false;
    }
    
    public String getName(){
        return this.name;
    }
}

class NullCustomer extends Customer{

    public boolean isNull(){
        return true;
    }
    
    public String getName(){
        return "test";
    }
}

class Site{
    private Customer customer;
    
    public void test(){
        Customer customer = site.getCustomer();
        String customerName = customer.getName();
        
    }
    
    public Customer getCustomer(){
        return customer == null ? Customer.newNull() : this.customer;
    }
    
}

```
- Introduce Assertion 引入断言, 通常情况下，系统上线后会将断言统统删除。断言可以作为上线前的交流和调试的辅助，可以帮助程序阅读者理解代母所做的假设。

### 简化函数调用
- Rename Method 函数改名，当函数的名称未能揭示函数的用途时。
- Add Parameter 添加参数，某个函数需要从调用端获得更多的信息
- Remove Parameter 移除参数
- Separate Query From Modifier 将查询函数和修改函数分离，若果某个函数既返回了对象状态值又修改对象状态，应将两者分离，因为查询是无副作用的，而修改则是有副作用的，应该将其分离。并发问题是需要考虑的，由于查询、修改不在同一动作中完成，会存在并发问题，解决办法是：增加一个函数，同时进行查询与修改，并将该函数声明为synchronized.
```
以下函数查询了入侵者，同时发送了警告
void checkSecurity(String[] people){
    String found = foundMiscreant(people);
    。。。
}

String foundMiscreant(String[] people){
    for(int i=0;i<people.length;i++){
        if(people[i].equals("Don")){
            sendAlert();
            return "Don";
        }
    }
    return "";
}
```
修改为：
```
String foundPerson(String[] people){
    for(int i=0;i<people.length;i++){
        if(people[i].equals("Don")){
            return "Don";
        }
    }
    return "";
}
void sendAlert(String[] people){
    for(int i=0;i<people.length;i++){
        if(people[i].equals("Don")){
            sendAlert();
            return;
        }
    }
}
void checkSecurity(String[] people){
    sendAlert(people);
    String found = foundPerson(people);
    。。。
}

```

- Parameterize Method 令函数携带参数，当两个函数做了相似的事情，但他们的参数略有不同，导致行为也略有不同，此时可以增加参数，来合并这两个函数
```
void tenPercentRaise(){
    salary *= 1.1;
}

void fivePercentRaise(){
    salary *= 1.05;
}

|
|

void raise(double factor){
    salary *= (1+ factor);
}
```

- Replace Parameter with Explicit Methods 以明确函数取代参数，当一个函数的行为完全取决于参数。
```
static final int ENGINEE = 0;
static final int SALESMAN = 1;
static final int MANAGER = 2;

static Employee create(int type){
    switch(type){
        case ENGINEE:
            return new Enginee();
        case SALESMAN:
            return new Salesman();
        case MANAGER:
            return enw Manager();
        default:
            throw new IllegalArgumentException("");
    }
}

Employee kent = Employee.create(ENGINEE);

|
|
|
static Employee createEnginee(){
    return new Enginee();
}

static Employee createSalesman(){
    return new Salesman();
}
...
Employee kent = Employee.createEnginee();
```
- Preserve Whole Object 保持对象完整，若一个函数的参数列表都来自另一个对象，那么可以考虑将该对象作为参数进行传递，如果函数大量依赖该对象，则应该考虑将函数移到对象所在的类中。传递对象这样做的好处有：
    - 保持参数列表的稳固，以防后面需要添加参数
    - 可以使用该对象中的方法，防止代码重复
当然缺点也有：
    - 一旦传递对象可能会导致依赖结构恶化，如果是这种情况则不应该传递对象
    - 性能可能有所差异

- Replace Parameter with Methods 以函数取代参数,如果函数需要的参数是从别的函数计算来的，则应该去掉该参数，转由该函数去调用别的函数，这样可以简化参数列表，降低阅读者对代码理解的难度
```
int a = getA();
int b = getB();
test(a, b);
|
|
|
test();
```
- Introduce Parameter Object 引入参数对象，当一组参数总是被一起传递，且好几个函数都使用了这一组参数，我们可以使用对象来对他们进行包装
```
test1(Date start, Date end);
test2(Date start, Date end);
|
|
|
test1(DateRange range);
test2(DateRange range);
```
- Remove Setting Method 移除set方法，当你不希望对象创建好后字段还能被修改，则应该移除set方法，同时将字段申明为final
- Hide Method 隐藏函数，如果有一个方法没有被别的类用到，那么将其修改为private
- Replace Constructor with Factory Method 以工厂函数取代构造器，当需要根据类型码来创建对象时，构造器已经不符合我们的要求了，此时需要使用工厂函数
- Encapsulate Downcast 封装向下转型，在java 5模板机制退出后，几乎看不到需要向下转型的场景了，如果还是出现了向下转型的情况，请考虑使用模板类(泛型)
- Replace Error Code with Exception 以异常代替错误码，重构的目标就是代码的可理解性，而抛出错误码常常并容易理解
```
int withdraw(int amount){
    if(amount > _balance){
        return -1;
    }else {
        _balance -= amount;
        return 0
    }
}
|
|
|
void withdraw(int amount) throws BalanceException{
    if(amount > _balance) throw new BalanceException();
    _balance -= amount;
}
```
- Replace Exception With Test，使用测试来代替异常，用来针对异常滥用的情况，可以预先进行判断从而避免抛出异常，异常只应该出现在罕见的、异常的行为，而不应该用于条件检查。


### 处理概括关系(处理继承体系的重构)
- Pull Up Field 字段上移，几个子类拥有相同的字段，那么应将该字段移到超类中
- Pull Up Method 函数上移，有些函数在各个子类中产生完全相同的结果，应该将该函数移入超类中，这样可以避免代码重复。在抽取的过程中可能遇到这样的情况，抽取的函数引用了其他函数，而其他函数在各个子类中实现不同，可以将该函数也抽取到超类，并申明为抽象函数。
- Pull Up Constructor Body 构造函数本体上移，各个子类拥有几乎一致的构造函数
```
class Manager extends Employee{
    public Manager(String name, String id, int grade){
        _name = name;
        _id = id;
        _grade = grade;
    }
}
|
|
|
class Manager extends Employee{
    public Manager(String name, String id, int grade){
        super(name, id);
        _grade = grade;
    }
}
```
- Push Down Method 函数下移，当某一个函数只与部分子类有关，应该将其下移到相关的子类中
- Push Down Field 字段下移，超类中的某个字段只被部分子类用到
- Extract Subclass 提炼子类，类中的某些行为只部分实例用到，其他实例不需要他们
- Extract Superclass 提炼超类，为两个类建立一个超类，将相同的特性移到超类中。
- Extract Interface 提炼接口，当一个类需要使用若干个类的特性，例如A类需要调用B类的b方法，需要C类的c方法，需要D类的d方法，则应该提炼一个新的接口E，E包含b、c、d三个方法，然后让B、C、D类分别实现E接口。
- Collapse Hierarchy 折叠继承体系，超类和子类之间区别不大，可以将其合体
- Form Template Method 塑造模板函数，两个子类的方法名称、参数列表一致，只是细节有略微的区别，可以使用模板函数进行重构
- Replace Inheritance with Delegation 以委托取代继承，某个子类只使用超类接口中的一部分，或者根本不需要继承来的数据。常常会出现这样的情况：一开始继承了某类，后来发现超类中许多操作并不真正适用子类，换句话说通过继承获得了一大堆并不需要的属性和行为，此时可以使用委托，在子类中保存超类的引用，去除他们之间的继承关系（组合），在子类中编写委托函数，其中调用父类的行为。
- Replace Delegation with Inheritance 以继承取代委托，是上面的反例，若委托函数过多，则应该去除委托，改为继承。此处有一个原则：若该类没有使用所有的受托类的行为，或者没有过多的委托函数，则不应使用此重构方式，否则可以让客户端自己调用受托函数，或者使用此重构方式

### 大型重构
- 由于真实情况是比较复杂的，无法精确的知道重构的步骤，一个大型重构可能会花费几天几十天甚至几个月的时间，当需要开发新功能时，无法腾出这么多时间来进行重构，只能按部就班在添加新功能或修补错误时进行重构，只需要每天你的程序世界比以前更加安全即可。
- Tease Apart Inheritance 梳理并分解继承体系，根据继承体系的责任划分将一个混乱的继承体系分解成若干个继承体系，判断继承体系是否承担了两项或多项责任：子类名称通过都以相同的形容词开始
- Convert Procedural Design to Objects 将过程化设计转化为对象设计
- Separate Domain from Presentation 将领域和表述/显示分离，将领域类(业务逻辑)和展示类(只负责)进行分离，使得多种展示方式成为了可能，也是MVC所倡导的
- Extract Hierarchy 提炼继承体系。在渐进式设计中可能会出现这样的情况，刚开始一个类即可满足需求，随着设计方案的演化，最终一个类可能实现了很多不同的概念，标记变量和条件表达式遍布各地，此时需要梳理出其中的逻辑使用该重构方法进行重构

---
- 附：[第一章源码地址](https://github.com/lijian0706/refactor)
