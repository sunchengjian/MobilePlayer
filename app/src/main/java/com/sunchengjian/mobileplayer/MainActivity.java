package com.sunchengjian.mobileplayer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.sunchengjian.mobileplayer.fragment.BaseFragment;
import com.sunchengjian.mobileplayer.pager.LocalAudioPager;
import com.sunchengjian.mobileplayer.pager.LocalVideoPager;
import com.sunchengjian.mobileplayer.pager.NetAudioPager;
import com.sunchengjian.mobileplayer.pager.NetVideoPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;

    private BaseFragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        initFragment();
        //设置监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_main.check(R.id.rb_local_video);
    }

    private void initFragment() {
        //把各个页面实例化放入集合中
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudioPager());
        fragments.add(new NetAudioPager());
        fragments.add(new NetVideoPager());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_local_video:
                    position = 0;
                    break;
                case R.id.rb_local_audio:
                    position = 1;
                    break;

                case R.id.rb_net_audio:
                    position = 2;
                    break;

                case R.id.rb_net_video:
                    position = 3;
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment currentFragment = fragments.get(position);//要显示的Fragment
            addFragment(currentFragment);
        }
    }

    private void addFragment(BaseFragment currentFragment) {
        if (tempFragment != currentFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //判断是否添加过-没有添加
            if (!currentFragment.isAdded()) {
                //把之前的隐藏
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }
                //添加当前的
                ft.add(R.id.fl_content, currentFragment);
            } else {
                // 当前Fragment已经添加过
                //把之前的隐藏
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }
                //显示当前的
                ft.show(currentFragment);
            }

            ft.commit();//提交事务
            //把当前的缓存起来
            tempFragment = currentFragment;

        }

    }
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }

}