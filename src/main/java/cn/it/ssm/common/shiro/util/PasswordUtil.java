package cn.it.ssm.common.shiro.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordUtil {

    public static final SecureRandomNumberGenerator GENERATOR = new SecureRandomNumberGenerator();

    public static String getSalt() {
        GENERATOR.setDefaultNextBytesSize(2);//2 Bytes == 16 bits ，转为16进制有4位
        String salt = GENERATOR.nextBytes().toHex();
        return salt;
    }

    public static String passwordEncypt(String password, String salt) {
        SimpleHash simpleHash = new SimpleHash("MD5", password, salt, 2);
        return simpleHash.toString();
    }

    public static void main(String[] args) {

        List<String> a = new ArrayList<String>();
        a.add("1");
        a.add("7");
        a.add("3");
        a.add("13");
        List<String> collect = a.stream().filter(s -> s.length() < 2).sorted(Comparator.comparing(Integer::valueOf)).collect(Collectors.toList());
        System.out.println(collect);


    }
}
