package com.monkeybean.lb;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.LoadBalancer;
import com.monkeybean.lb.request.RequestFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;

/**
 * 流程控制
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class ProcessControl {

    /**
     * 实例初始化
     *
     * @return 成功返回true, 失败返回false
     */
    public static boolean instanceInitialize() {
        Properties prop = new Properties();
        try (InputStream in = ProcessControl.class.getResourceAsStream("/application.properties")) {
            prop.load(in);

            //负载策略初始化
            if (!LoadBalancer.getInstance().setLf(Integer.parseInt(prop.getProperty("server.load")))) {
                System.out.println("init LoadForward failed");
                return false;
            }
            int initServerNum = Integer.parseInt(prop.getProperty("server.num"));
            for (int i = 1; i <= initServerNum; i++) {
                String serverFlag = "server" + i;
                String instanceId = prop.getProperty(serverFlag + ".id");
                double initCPU = Double.parseDouble(prop.getProperty(serverFlag + ".cpu"));
                double initMemory = Double.parseDouble(prop.getProperty(serverFlag + ".memory"));
                int heavy = Integer.parseInt(prop.getProperty(serverFlag + ".heavy"));
                boolean createResult = LoadBalancer.getInstance().createService(instanceId, initCPU, initMemory, heavy);
                System.out.println("createService, instanceId: " + instanceId + ", createResult: " + createResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 输入处理
     *
     * @param command 指令
     */
    public static void processInput(String command) {
        switch (command) {
            case "1":
                LoadBalancer.getInstance().printInstanceList();
                break;
            case "2":
                LoadBalancer.getInstance().generateService();
                break;
            case "3":
                LoadBalancer.getInstance().randomRemoveService();
                break;
            case "4":
                LoadBalancer.getInstance().randomChangeLf();
                break;
            case "5":
                RequestFactory.getInstance().randomGenerateRequest();
                break;
            case "6":
                System.out.println("Exit the System !");
                System.exit(0);
            default:
                System.out.println("Wrong Command !");
        }
    }

    /**
     * 打印主菜单
     */
    public static void printMenu() {
        System.out.println("Welcome to Distributed System, here are the command you can execute: ");
        System.out.println("1) Show info of ELB");
        System.out.println("2) Add new EAS instance");
        System.out.println("3) Kill one EAS instance randomly");
        System.out.println("4) Change load balance rule of ELB By flag");
        System.out.println("5) Send request to ELB");
        System.out.println("6) Exit the system");
        System.out.println("Please input 1 to 6 to execute：");
    }

    /**
     * 打印负载策略
     */
    public static void printLoadStrategy() {
        System.out.println("Load Forward Strategy as follows");
        EnumSet<RuleType> rules = EnumSet.allOf(RuleType.class);
        for (RuleType rule : rules) {
            System.out.println(rule.name() + " : " + rule.getCode());
        }
        System.out.println("");
    }

}
