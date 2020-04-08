package cn.it.ssm.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class)
@Documented
public @interface Mobile {

    String message() default "手机号码必须为11位数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
