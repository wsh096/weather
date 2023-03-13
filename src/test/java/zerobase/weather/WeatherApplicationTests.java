package zerobase.weather;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherApplicationTests {

   @Test
   @DisplayName("Equalstest")

   void equalTest(){
       //given
       //when
       //then
        assertEquals(1,1);
   }
    @Test
    @DisplayName("nulltest")

    void nullTest(){
        //given
        //when
        //then
        assertNull(null);
    }

    @Test
    @DisplayName("trueTest")

    void trueTest(){
        //given
        //when
        //then
        assertTrue(1==1);
    }
}
