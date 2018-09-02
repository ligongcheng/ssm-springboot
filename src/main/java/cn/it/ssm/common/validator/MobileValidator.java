package cn.it.ssm.common.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<Mobile, String> {
   public void initialize(Mobile constraint) {
   }

   public boolean isValid(String Str, ConstraintValidatorContext context) {
      if (StringUtils.isBlank(Str)) {
          return true;
      }
       //language=RegExp
       String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";

      return Str.matches(telRegex);
   }
}
