package GymBro.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessException.class)
    public String exception1(Exception ex){
        System.out.println(ex.getMessage());
        return "Util/accessException";
    }

    @ExceptionHandler(NoObjectException.class)
    public String exception2(Exception ex){
        System.out.println(ex.getMessage());
        return "Util/noObjectException";
    }

}
