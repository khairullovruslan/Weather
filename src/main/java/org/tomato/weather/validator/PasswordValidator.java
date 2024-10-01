package org.tomato.weather.validator;


import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {
    public static List<String> validatePassword(String password){

        List<String> errorMes = new ArrayList<>();
        if (password.length() < 8){
            errorMes.add("Пароль должен содержать не менее 8 символов");
        }
        if (!password.matches(".*[A-Z].*")){
            errorMes.add("Пароль должен содержать заглавные буквы (A-Z)");
        }
        if (!password.matches(".*[a-z].*")){
            errorMes.add("Пароль должен содержать строчные буквы (a-z)");
        }
        if (!password.matches(".*[0-9].*")){
            errorMes.add("Пароль должен содержать строчные цифры (0-9)");
        }
        if (! password.matches("""
.*[!@#$%^&*().].*""")){
            errorMes.add("Пароль должен содержать специальные символы цифры - !@#$%^&*().,");
        }
        return errorMes;
    }
}

