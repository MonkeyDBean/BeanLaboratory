package com.monkeybean.algorithm.pattern.behavior.template;

/**
 * 父类
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public abstract class Game {
    abstract void initialize();

    abstract void startPlay();

    abstract void endPlay();

    /**
     * 模板方法
     */
    public final void play() {

        //初始化游戏
        initialize();

        //开始游戏
        startPlay();

        //结束游戏
        endPlay();
    }
}

