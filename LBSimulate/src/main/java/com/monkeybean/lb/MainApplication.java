package com.monkeybean.lb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Only Simply Simulate Implement Load Balancer
 * Client Request -> Elastic Load Balancer -> Elastic Application Server
 * Related Feature Just Like Nginx, Docker Service, Kubernetes Ingress, SpringCloud Zuul + Ribbon, ServiceMesh Envoy
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class MainApplication {

    public static void main(String[] args) throws InterruptedException {

        //添加初始实例
        if (!ProcessControl.instanceInitialize()) {
            System.out.println("instanceInitialize failed, service terminate");
            return;
        }
        System.out.println("");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            ProcessControl.printMenu();
            String command = br.readLine();
            while (true) {
                System.out.println("");
                ProcessControl.processInput(command);
                Thread.sleep(300);
                System.out.println("");
                ProcessControl.printMenu();
                command = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
