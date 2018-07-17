package com.monkeybean.labo.util;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class FilterUtf8Mb4UtilTest {

    @Test
    public void filterOffUtf8Mb4() throws Exception {
        String illegalStr1 = "An 😀awesome 😃string with a few 😉emojis!";
        String illegalStr2 = "\uD83C\uDFFB情シ";
        assertTrue(!FilterUtf8Mb4Util.filterOffUtf8Mb4(illegalStr1).equals(illegalStr1));
        assertTrue(!FilterUtf8Mb4Util.filterOffUtf8Mb4(illegalStr2).equals(illegalStr2));
    }

}