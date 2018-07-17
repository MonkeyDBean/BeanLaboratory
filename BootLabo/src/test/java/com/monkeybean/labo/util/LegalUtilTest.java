package com.monkeybean.labo.util;

import junit.framework.TestCase;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class LegalUtilTest extends TestCase {

    public void testIsLegalTimestamp() throws Exception {
        String[] wrongStr = {"rtgsyht", "197845-1-2", "00-45", "150679086100045", "1406790861000", "0506790861000"};
        String[] rightStr = {"1527335873000", "1917018061000"};
        for (String aWrongStr : wrongStr) {
            boolean result = LegalUtil.isLegalTimestamp(aWrongStr);
            assertTrue("wrong result: " + result + "; value: " + aWrongStr, !result);
        }
        for (String aRightStr : rightStr) {
            boolean result = LegalUtil.isLegalTimestamp(aRightStr);
            assertTrue("right result: " + result + "; value: " + aRightStr, result);
        }
    }

    public void testIsPhoneLegal() throws Exception {
        String[] wrongStr = {"ret", "120df..", "1354864354586", "1123545653", "138555554555", "12216559966"};
        String[] rightStr = {"18840123456", "56415678"};
        for (String aWrongStr : wrongStr) {
            boolean result = LegalUtil.isPhoneLegal(aWrongStr);
            assertTrue("wrong result: " + result + "; value: " + aWrongStr, !result);
        }
        for (String aRightStr : rightStr) {
            boolean result = LegalUtil.isPhoneLegal(aRightStr);
            assertTrue("right result: " + result + "; value: " + aRightStr, result);
        }
    }

    public void testMatchName() throws Exception {
        String[] wrongStr = {"😃string", "120df..", "咦13548"};
        String[] rightStr = {"谁的名字", "干哈"};
        for (String aWrongStr : wrongStr) {
            boolean result = LegalUtil.matchName(aWrongStr);
            assertTrue("wrong result: " + result + "; value: " + aWrongStr, !result);
        }
        for (String aRightStr : rightStr) {
            boolean result = LegalUtil.matchName(aRightStr);
            assertTrue("right result: " + result + "; value: " + aRightStr, result);
        }
    }

}