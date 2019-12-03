package cn.it.ssm.web;

public class B extends A {

    static {
        System.out.println("B Stataic");
    }
    {
        System.out.println("B noStataic");
    }

    public static String  testOut = "sta str";
    String no = "str";

    public B() {
        System.out.println("B Construct! ");
    }

    public static void main(String args[]) {
        B t = new B();
        Integer value = Integer.parseInt("we32");
        System.out.println(value);
    }
}

class A{
    static {
        System.out.println("A Stataic");
    }
    {
        System.out.println("A noStataic");
    }

    public static String  testOut = "sta A";
    String no = "A";

    public A() {
        System.out.println("A construct");
    }
}
