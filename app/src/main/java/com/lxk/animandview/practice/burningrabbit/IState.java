package com.lxk.animandview.practice.burningrabbit;

/**
 * @author https://github.com/103style
 * @date 2020/4/22 15:52
 */
public interface IState {
    interface ViewGroupState {
        /**
         * 正常的状态
         */
        int NORMAL = 1;
        /**
         * 显示后面的视图的状态
         */
        int PULL_OUT = 2;

        /**
         * 拉出后面视图的过程中
         */
        int PULLING = 3;

        /**
         * 关闭后面的视图中
         */
        int CLOSING = 4;
    }

    interface MoveDirection {
        /**
         * 没有滑动
         */
        int NO_MOVE = 0;
        /**
         * 上滑
         */
        int UP = 1;
        /**
         * 下滑
         */
        int DONW = 2;
    }

}
