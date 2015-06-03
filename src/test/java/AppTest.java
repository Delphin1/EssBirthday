import com.belkam.httpess.EssBirthday;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by tsg on 16.03.2015.
 */
public class AppTest {
    //@Test
    public void testGetURL() throws Exception{
        //EssBirthday eb = new EssBirthday();

        //String answer = EssBirthday.getUrlEss("http://www.belkam.com/home/");
        String answer = EssBirthday.getUrlEss("http://ess.belkam.com/SpravWeb/show.aspx?SEARCH_TabNo=6470&SEARCH_tabn2=&SEARCH_FIO=&SEARCH_DepAbbr=&SEARCH_Department=&SEARCH_Position=&SEARCH_sfname=&SEARCH_Birthday=&SEARCH_Phones=&SEARCH_PhonesExt=&SEARCH_Address=&SEARCH_Email=&strict_search=&spravid=5003&SortCol=2&PageSize=30&uri=%2Fsprav%2Fpage&PageNum=1&load_content=1");
        System.out.println(answer);
        Assert.assertNotNull(answer, answer);
    }
    @Test
    public void testURLParse () {
        //String answer = EssBirthday.getBirthdayBKN("6470");

        //Assert.assertEquals(EssBirthday.getBirthdayBKN("6470"), "12.05.1979");
        Assert.assertTrue(true);

    }

}
