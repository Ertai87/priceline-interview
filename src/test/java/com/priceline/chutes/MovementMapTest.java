package com.priceline.chutes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/* TODO: I would write more unit tests here but I'm running out of time.  The tests would include:

- For the default constructor, that the map is as expected
- For the parameterized constructor, that the map is created as expected
- For the parameterized constructor, that the constructor will error if the map contains invalid entries
- For the parameterized constructor, that the constructor should error on a mapping list of odd size
- For getFinalLocation, that the returned value will come from the map if exists
- For getFinalLocation, that the returned value will be the input value if not exists in the map

To see my unit test writing, please see the other test classes.
*/

@ExtendWith(MockitoExtension.class)
public class MovementMapTest {
    @Test
    public void dummyTest(){}
}
