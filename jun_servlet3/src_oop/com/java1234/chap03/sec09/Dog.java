package com.java1234.chap03.sec09;

/**
 * ����һ�����࣬�̳���Aniaml
 * @author user
 *
 */
public class Dog extends Animal{

	public static void main(String[] args) {
		Dog dog=new Dog();
		dog.setName("Pick");
		dog.setAge(1);
		dog.say();
	}
}