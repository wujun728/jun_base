/*
定义格式：
class 类名称
{}

标准的命名规范：首字母大写
*/
class Person
{
	// 名字
	String name ;
	// 年龄
	int age ;

	// 增加一个说话的方法
	public void shout()
	{
		System.out.println("姓名："+name) ;
		System.out.println("年龄："+age) ;
	}
};
// 要使用类需要通过对象
public class OODemo03
{
	public static void main(String args[])
	{
		// 格式：类 对象名 = new 类() ;
		// 声明一个对象 lxh1
		// 在方法中声明对象时必须赋值
		Person lxh1 = null ;
		// 为对象实例化，开辟堆内存空间
		lxh1 = new Person() ;
		lxh1.name = "李兴华" ;
		lxh1.age = 28 ;

		Person lxh2 = new Person() ;

		lxh2.name = "魔乐先生" ;
		lxh1.age = 20 ;

		// 试着输出：
		lxh1.shout() ;
		System.out.println("*********************************") ;
		lxh2.shout() ;
	}
};