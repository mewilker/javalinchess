package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;

public class ServiceTest {

    @Test
    public void failMe(){
        Assertions.assertTrue(false);
    }

    @ParameterizedTest
    @ValueSource (ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13})
    public void passMe(int num){
        Assertions.assertTrue(true);
    }
}
