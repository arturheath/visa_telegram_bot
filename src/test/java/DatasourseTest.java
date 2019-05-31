import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DatasourseTest {

    private static Datasourse datasourse;

    private String expectedReturn;
    private String argument;

    public DatasourseTest(String expectedReturn, String argument) {
        this.expectedReturn = expectedReturn;
        this.argument = argument;
    }

    @BeforeClass
    public static void setUp(){
        datasourse = Datasourse.getInstance();
        datasourse.openConnection();
        datasourse.useDatabase();
    }

    @AfterClass
    public static void clean(){
        datasourse.closeConnection();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> queryCountryDataTestCases(){
        return Arrays.asList(new Object[][]{
                {"Spain", "Испания"},
                {"Tanzania", "Танзания"},
                {"Tanzania", "Тунис"}
        });
    }

//    @Test
//    public void queryCountryDataEstimate() {
//        assertEquals(expectedReturn, datasourse.queryCountryDataEstimate(argument).getCountryNameEng());
//    }
}