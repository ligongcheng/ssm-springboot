package cn.it.ssm.common.shiro.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.security.SecureRandom;

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
        SecureRandom random = new SecureRandom();
        int i = random.nextInt();
        System.out.println(i);
    }
}
