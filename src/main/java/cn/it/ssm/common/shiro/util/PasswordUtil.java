package cn.it.ssm.common.shiro.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordUtil {

    public static String generaterSalt() {
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        return salt;
    }

    public static String passwordEncypt(String password, String salt) {
        SimpleHash simpleHash = new SimpleHash("MD5", password, salt, 2);
        return simpleHash.toString();
    }

    public static void main(String[] args) {
        String s = generaterSalt();
        System.out.println(s);
    }
}
