package com.newbegin.android_2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;


public class Card extends FrameLayout {

	private int value;
	//百分比，R，G，B
	private int[] color = { 0x11808080, 0x60a6ffff, 0x6046a3ff, 0x6000aeae,
			0x607d7dff, 0x600080ff,  0xb0a6ffff, 0xb046a3ff, 0xb000aeae,
			0xb07d7dff, 0xb00080ff,  0xf0a6ffff, 0xf046a3ff, 0xf000aeae,
			0xf07d7dff, 0xf00080ff };
	
	private TextView label = new TextView(getContext());

	public Card(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		/* 初始化value、number值以及初始背景色 */
		// value = 0;
		// setBackgroundColor(color[value]);
		init();
	}

	//初始化
	public void init(){
		value = 0;

		/*
		 * 这里应添加设置Card的通用属性
		 */
		label.setTextSize(32);
		label.setBackgroundColor(color[value]);
		label.setGravity(Gravity.CENTER);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(5, 5, 5, 5);
		addView(label, lp);

	}

	// 比较是否相等
	public boolean isEqual(Card other) {
		if (value == other.getValue())
			return true;
		else
			return false;
	}

	// value值增加
	public void plus() {
		value++;
		return;
	}


	//改变value
	public void setValue(int task){

		value = task;
		return;
	}

	// 刷新card代表的值以及背景色
	public void refresh() {
		if (value == 0) {
			label.setText("");
		} else {
			int number = (int) Math.pow(2, value);
			// 将number转换成字符串，否则显示int的地址
			label.setText("" + number);
		}
		label.setBackgroundColor(color[value]);
	}

	// 获取card的value
	public int getValue() {
		return value;
	}

}
