package com.monkeybean.algorithm.pattern.behavior.template;

/**
 * RPG游戏
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class RPG extends Game {

    @Override
    void initialize() {
        System.out.println("RPG Game Initialized! Start Playing!");
    }

    @Override
    void startPlay() {
        System.out.println("RPG Game Started. Enjoy The Game!");
    }

    @Override
    void endPlay() {
        System.out.println("RPG Game Finished!");
    }
}
