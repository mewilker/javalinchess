package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceTest {

    @Test
    public void failMe(){
        Assertions.assertTrue(false);
    }

    @Test
    public void passMe(){
        Assertions.assertTrue(true);
    }
}
