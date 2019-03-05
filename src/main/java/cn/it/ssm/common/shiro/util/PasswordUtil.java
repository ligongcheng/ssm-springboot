package cn.it.ssm.common.shiro.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.ArrayList;
import java.util.List;

public class PasswordUtil {

    public static final SecureRandomNumberGenerator GENERATOR = new SecureRandomNumberGenerator();

    public static String generaterSalt() {
        GENERATOR.setDefaultNextBytesSize(2);//2 bytes == 16 bits ，转为16进制有4位
        String salt = GENERATOR.nextBytes().toHex();
        return salt;
    }

    public static String passwordEncypt(String password, String salt) {
        SimpleHash simpleHash = new SimpleHash("MD5", password, salt, 2);
        return simpleHash.toString();
    }

    public static void main(String[] args) {

        List<String> a = new ArrayList<String>();
        ;
        test(a);
        System.out.println(a.size());


    }

    public static void test(List<String> a) {

        a.add("abc");
    }
}
