package com.monkeybean.lb;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.LoadBalancer;
import com.monkeybean.lb.request.RequestFactory;
import org.junit.Test;

import java.util.EnumSet;

/**
 * 流控测试
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class ProcessControlTest {

    @Test
    public void testProcedureCall() throws InterruptedException {

        //打印负载策略
        ProcessControl.printLoadStrategy();

        //实例初始化
        ProcessControl.instanceInitialize();

        //遍历负载策略
        EnumSet<RuleType> rules = EnumSet.allOf(RuleType.class);
        for (RuleType rule : rules) {
            System.out.println("");

            //策略变更
            LoadBalancer.getInstance().setLf(rule.name());

            //打印负载器信息
            LoadBalancer.getInstance().printInstanceList();

            //生成请求
            RequestFactory.getInstance().batchGenerateRequest(5);

            //等待消息队列处理完成
            Thread.sleep(300);
        }

        //验证源地址hash
        System.out.println("");
        LoadBalancer.getInstance().setLf(RuleType.IP_HASH.getCode());
        LoadBalancer.getInstance().printInstanceList();
        String requestOrigin1 = "http://127.0.0.1:80?key=test_test";
        RequestFactory.getInstance().generateRequest(requestOrigin1);
        String requestOrigin2 = "http://127.0.0.1:80?key=test_test";
        RequestFactory.getInstance().generateRequest(requestOrigin2);
        String requestOrigin3 = "http://127.0.0.1:80?key=test_test";
        RequestFactory.getInstance().generateRequest(requestOrigin3);
        String requestOrigin4 = "http://172.16.2.123:80?key=test_test";
        RequestFactory.getInstance().generateRequest(requestOrigin4);
        Thread.sleep(300);

        //验证key hash
        System.out.println("");
        LoadBalancer.getInstance().setLf(RuleType.KEY_HASH.getCode());
        LoadBalancer.getInstance().printInstanceList();
        requestOrigin1 = "http://127.0.0.1:80?key=test1";
        RequestFactory.getInstance().generateRequest(requestOrigin1);
        requestOrigin2 = "http://127.0.0.1:80?key=test1";
        RequestFactory.getInstance().generateRequest(requestOrigin2);
        requestOrigin3 = "http://127.0.0.1:80?key=test12345";
        RequestFactory.getInstance().generateRequest(requestOrigin3);
        requestOrigin4 = "http://127.0.0.1:80?key=1_test_test_1";
        RequestFactory.getInstance().generateRequest(requestOrigin4);
        Thread.sleep(300);

        //新增实例
        System.out.println("");
        String newInstanceId = LoadBalancer.getInstance().generateService();
        LoadBalancer.getInstance().printInstanceList();
        RequestFactory.getInstance().batchGenerateRequest(5);
        Thread.sleep(300);

        //验证一致性Hash
        System.out.println("");
        LoadBalancer.getInstance().setLf(RuleType.CONSISTENCY_HASH.getCode());
        LoadBalancer.getInstance().printInstanceList();
        RequestFactory.getInstance().generateRequest(requestOrigin1);
        RequestFactory.getInstance().generateRequest(requestOrigin2);
        RequestFactory.getInstance().generateRequest(requestOrigin3);
        RequestFactory.getInstance().generateRequest(requestOrigin4);
        Thread.sleep(300);

        //移除实例
        System.out.println("");
        LoadBalancer.getInstance().removeService("1");
        LoadBalancer.getInstance().removeService("2");
        LoadBalancer.getInstance().removeService("3");
        LoadBalancer.getInstance().printInstanceList();
        RequestFactory.getInstance().batchGenerateRequest(5);
        Thread.sleep(300);

        //移除所有实例
        System.out.println("");
        LoadBalancer.getInstance().removeService(newInstanceId);
        LoadBalancer.getInstance().printInstanceList();
        RequestFactory.getInstance().batchGenerateRequest(2);
        Thread.sleep(100);
    }

}