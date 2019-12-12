package com.lxk.animandcustomviewdemo.layoutAnimation;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/12/12 16:34
 * 验证 layoutAnimation 的示例
 */
public class LayoutAnimationActivity extends BaseClickActivity {

    private ListView listView, listViewCode;
    private GridView gridView, gridViewCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);

        initView();

        setClickListener(
                R.id.bt_list_view_anim_attr,
                R.id.bt_list_view_anim_controller,
                R.id.bt_grid_view_anim_attr,
                R.id.bt_grid_view_anim_controller
        );
    }

    private void initView() {
        listView = findViewById(R.id.lv);
        listViewCode = findViewById(R.id.lv_code);
        gridView = findViewById(R.id.gv);
        gridViewCode = findViewById(R.id.gv_code);
    }

    @Override
    public void onClick(View v) {
        reset();
        switch (v.getId()) {
            case R.id.bt_list_view_anim_attr:
                testListViewLayoutAnimWithAttr();
                break;
            case R.id.bt_list_view_anim_controller:
                testListViewLayoutAnimController();
                break;
            case R.id.bt_grid_view_anim_attr:
                testGridLayoutAnimWithAttr();
                break;
            case R.id.bt_grid_view_anim_controller:
                testGridLayoutAnimController();
                break;
            default:
                break;
        }
    }


    private void reset() {
        listView.setVisibility(View.GONE);
        listViewCode.setVisibility(View.GONE);
        gridView.setVisibility(View.GONE);
        gridViewCode.setVisibility(View.GONE);
    }

    /**
     * ListView 的动画设置
     * 直接在 xml 中配置 android:layoutAnimation="@anim/layout_animation_test" 属性
     * <p>
     * 在 xml 中配置 只有第一次加载数据 在可视区域的才会有动画
     */
    private void testListViewLayoutAnimWithAttr() {
        listView.setVisibility(View.VISIBLE);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getData());
        listView.setAdapter(mAdapter);
    }

    /**
     * ListView 的动画设置
     * 直通过代码来设置 LayoutAnimationController
     * <p>
     * 每次点击 setAdapter 或者 adapter.notifyDataSetChanged 展示在可视区域的都会有动画
     */
    private void testListViewLayoutAnimController() {
        listViewCode.setVisibility(View.VISIBLE);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getData());
        listViewCode.setLayoutAnimation(getLayoutAnimationController());
        listViewCode.setAdapter(mAdapter);
    }

    /**
     * 获取布局动画管理器
     */
    private LayoutAnimationController getLayoutAnimationController() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        LayoutAnimationController animationController = new LayoutAnimationController(animation);
        animationController.setDelay(1);
//        animationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        animationController.setOrder(LayoutAnimationController.ORDER_REVERSE);
        animationController.setOrder(LayoutAnimationController.ORDER_RANDOM);
        animationController.setInterpolator(new AccelerateInterpolator());
        return animationController;
    }

    /**
     * GridView 的动画设置
     * 直接在 xml 中配置 android:layoutAnimation="@anim/grid_layout_animation_test" 属性
     * <p>
     * 在 xml 中配置 只有第一次加载数据 在可视区域的才会有动画
     */
    private void testGridLayoutAnimWithAttr() {
        gridView.setVisibility(View.VISIBLE);
        if (gridView.getAdapter() == null) {
            ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getData());
            gridView.setAdapter(mAdapter);
        } else {
            ((BaseAdapter) (gridView.getAdapter())).notifyDataSetChanged();
        }
    }


    /**
     * GridView 的动画设置
     * 通过代码来设置 LayoutAnimationController
     * <p>
     * 通过代码设置, 每次点击 setAdapter 或者 adapter.notifyDataSetChanged 展示在可视区域的都会有动画
     */
    private void testGridLayoutAnimController() {
        gridViewCode.setVisibility(View.VISIBLE);

        gridViewCode.setLayoutAnimation(getGridLayoutAnimationController());

        if (gridViewCode.getAdapter() == null) {
            ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getData());
            gridViewCode.setAdapter(mAdapter);
        } else {
            ((BaseAdapter) (gridViewCode.getAdapter())).notifyDataSetChanged();
        }
    }


    /**
     * 获取布局动画管理器
     * <p>
     * Direction 和 DirectionPriority 可以多样搭配
     */
    private GridLayoutAnimationController getGridLayoutAnimationController() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        GridLayoutAnimationController animationController = new GridLayoutAnimationController(animation);
//        animationController.setColumnDelay(0.5F);
//        animationController.setRowDelay(1F);

        animationController.setDirection(GridLayoutAnimationController.DIRECTION_RIGHT_TO_LEFT);
//        animationController.setDirection(GridLayoutAnimationController.DIRECTION_LEFT_TO_RIGHT);
//        animationController.setDirection(GridLayoutAnimationController.DIRECTION_TOP_TO_BOTTOM);
//        animationController.setDirection(GridLayoutAnimationController.DIRECTION_BOTTOM_TO_TOP);

//        animationController.setDirectionPriority(GridLayoutAnimationController.PRIORITY_ROW);
        animationController.setDirectionPriority(GridLayoutAnimationController.PRIORITY_COLUMN);
//        animationController.setDirectionPriority(GridLayoutAnimationController.PRIORITY_NONE);
//        animationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        animationController.setInterpolator(new AccelerateInterpolator());
        return animationController;
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        int count = 50;
        for (int i = 0; i < count; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }
}
