package exercise;

import exercise.model.Address;
import exercise.annotation.Inspect;

import java.lang.reflect.Method;

public class Application {
    public static void main(String[] args) {
        var address = new Address("London", 12345678);

        // BEGIN
        for (Method method : address.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Inspect.class)) {
                String typeName = method.getReturnType().getTypeName();
                if (typeName.contains("String")) {
                    typeName = "String";
                }
                System.out.println("Method " + method.getName() + " returns a value of type " + typeName);
            }
        }
        // END
    }
}
