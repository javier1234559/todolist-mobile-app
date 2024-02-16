package com.javier.todolist;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.javier.todolist.fragment.CategoryPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageView buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        buttonAdd = findViewById(R.id.add_button);

        addButtonAddEvent();

        FragmentStateAdapter adapter = new CategoryPagerAdapter(this);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(getCategoryTitle(position))
        ).attach();
    }

    private String getCategoryTitle(int position) {
        switch (position) {
            case 0:
                return "Pending";
            case 1:
                return "Done";
            case 2:
                return "Overdue";
            default:
                return "";
        }
    }

    private void addButtonAddEvent() {
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
            startActivity(intent);
        });
    }


}


