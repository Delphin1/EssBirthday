package com.belkam.httpess;

import com.belkam.utils.JCIFSNTLMSchemeFactory;
import com.belkam.utils.URLHelper;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//<td align="center">6983</td>


/**
 * Created by tsg on 16.03.2015.
 */
public class EssBirthday {
//--------------------------------------------------------------------------------
    public static String getUrlEss(String URL)  {

        return URLHelper.getNTLM(URL, "", "", "SHEEP");  //URL, login, password, host
    }
//--------------------------------------------------------------------------------
    private static String clearTD (String strPar) {
        //String retStr = strPar.replace("<td>","");
        String retStr = strPar.replace("<td align=\"center\">","");
        retStr = retStr.replace("</td>","");
        return retStr;
    }
    //--------------------------------------------------------------------------------
    private static int getCurrentYear()
    {
        java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
        calendar.setTime(new java.util.Date());
        return calendar.get(java.util.Calendar.YEAR);
    }

//--------------------------------------------------------------------------------
    private static Boolean checkUser(String tabNum, String Content) {
        //String subString = "<td>"+tabNum+"</td><td>"+tabNum+"</td>";
        String subString = "<td align=\"center\">"+tabNum+"</td>";
        if (Content.indexOf(subString) == -1) {
            return false;
        }
        return true;
    }
//--------------------------------------------------------------------------------
    private static String downAndCheck(String tabNum, String bd, String sprNum ) {
        //log.debug("BidURL: {}", BidURL);
        //5003
        //String URL = "http://ess.belkam.com/SpravWeb/show.aspx?SEARCH_TabNo=" + tabNum +"&SEARCH_tabn2=&SEARCH_FIO=&SEARCH_DepAbbr=&SEARCH_Department=&SEARCH_Position=&SEARCH_sfname=&SEARCH_Birthday=" + bd + "&SEARCH_Phones=&SEARCH_PhonesExt=&SEARCH_Address=&SEARCH_Email=&strict_search=1&spravid=" + sprNum + "&SortCol=2&PageSize=30&uri=%2Fsprav%2Fpage&PageNum=1&load_content=1";
        //003
        String   URL = "http://ess.belkam.com/SpravWeb/show.aspx?SEARCH_TabNo=" + tabNum +"&SEARCH_FIO=&SEARCH_DepAbbr=&SEARCH_Department=&SEARCH_Position=&SEARCH_Birthday=" + bd + "&SEARCH_Address=&SEARCH_Email=&strict_search=1&spravid=" + sprNum + "&SortCol=2&PageSize=30&uri=%2Fsprav%2Fpage&PageNum=1&load_content=1";
        if (checkUser(tabNum, EssBirthday.getUrlEss(URL))) {
            return bd;
        }
        return null;
    }

//--------------------------------------------------------------------------------
    public static String getBirthdayBKN(String tabNum) {
        String sprNum = "003";
        int minYear = 14;
        int maxYear = 75;
        int midYear = (maxYear-minYear)/2 + minYear;
        int curYear = getCurrentYear();
        //5003
        //String TabURL = "http://ess.belkam.com/SpravWeb/show.aspx?SEARCH_TabNo=" + tabNum +"&SEARCH_tabn2=&SEARCH_FIO=&SEARCH_DepAbbr=&SEARCH_Department=&SEARCH_Position=&SEARCH_sfname=&SEARCH_Birthday=&SEARCH_Phones=&SEARCH_PhonesExt=&SEARCH_Address=&SEARCH_Email=&strict_search=1&spravid=" + sprNum + "&SortCol=2&PageSize=30&uri=%2Fsprav%2Fpage&PageNum=1&load_content=1";
        //003
        String TabURL = "http://ess.belkam.com/SpravWeb/show.aspx?SEARCH_TabNo=" + tabNum +"&SEARCH_FIO=&SEARCH_DepAbbr=&SEARCH_Department=&SEARCH_Position=&SEARCH_Birthday=&SEARCH_Address=&SEARCH_Email=&strict_search=1&spravid=" + sprNum + "&SortCol=2&PageSize=30&uri=%2Fsprav%2Fpage&PageNum=1&load_content=1";

        //Создаём логгер
        //PropertyConfigurator.configure("log4j.properties");
        Logger log = LoggerFactory.getLogger(EssBirthday.class);

        log.debug("Tab URL: " + TabURL);


        String Content = EssBirthday.getUrlEss(TabURL);

        if (!checkUser(tabNum, EssBirthday.getUrlEss(TabURL))) {
            log.warn("Сотрудник " + tabNum + " не найден!");
            return null;                            // Сотрудник не найден
        }


        //Pattern pattern = Pattern.compile("<td>(.*?)</td>");
        Pattern pattern = Pattern.compile("<td align=\"center\">(.*?)</td>");
        Matcher m = pattern.matcher(Content);

        //int i = 0;
//        String firm_id = "";
        String bd = "";
        m.find();
        m.find();
        bd = clearTD(m.group().toString());
/*          while (m.find() && i <= 9) {
              log.info("i: {}, {}", i, m.group().toString());
          switch (i) {
                case 6: firm_id = clearTD(m.group().toString());
                        break;
                case 9: bd = clearTD(m.group().toString());
                        break;
            }
            i++;
        } */


        //bd = "12.05";
        //System.out.println("firm_id: " + firm_id);
        log.debug("bd: " + bd);
        //bd = bd + ".1979";
        String BidURL = "";
        int offset = 0;
        int yearPlus   = curYear   - midYear;
        int yearMinus  = curYear   - midYear;
        String bdMinus = "";
        String bdPlus  = "";
        String resp = null;
        //Boolean findFlag = false;
        while ((midYear + offset <= maxYear) &&(midYear - offset >= minYear) /*&& !findFlag*/) {
            bdMinus = bd + "." + yearMinus;
            bdPlus  = bd + "." + yearPlus;
            log.debug("bdMinus: " + bdMinus);
            resp = downAndCheck(tabNum, bdMinus, sprNum);
            if (resp != null) {
                log.info("Tab: {}, birthday at {}, found for {} iterations", tabNum, resp, offset*2);
                return resp;
            }
            if (yearPlus != yearMinus) {
                resp = downAndCheck(tabNum, bdPlus, sprNum);
                if (resp != null) {
                    log.info("Tab: {}, birthday at {}, found for {} iterations", tabNum, resp, offset*2+1);
                    return resp;
                }
            }
            offset++;
            yearPlus  = curYear - midYear + offset;
            yearMinus = curYear - midYear - offset;
        }
        //System.out.println(m.group(1));
        return "";
    }
//--------------------------------------------------------------------------------

}
