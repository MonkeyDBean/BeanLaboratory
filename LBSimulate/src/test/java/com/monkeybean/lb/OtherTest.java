package com.monkeybean.lb;

import com.monkeybean.lb.cache.CacheData;
import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.constant.ServerStatus;
import org.junit.Test;

/**
 * 其他测试
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class OtherTest {

    @Test
    public void testEnumCache() {
        String testStr = "RANDOM";
        assert RuleType.generate(testStr) == RuleType.RANDOM;
        int testCode = 1;
        assert RuleType.RANDOM.name().equals(CacheData.ruleTypeCodeMap.get(testCode));
        assert ServerStatus.RUNNING == CacheData.getServerStatus(testCode);
    }
}
