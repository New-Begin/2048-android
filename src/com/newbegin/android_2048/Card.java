package com.newbegin.android_2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;


public class Card extends FrameLayout {

	private int value;
	private int[] color = { 0x0fff0000, 0x10ff0000, 0x20ff0000, 0x30ff0000,
			0x40ff0000, 0x50ff0000, 0x60ff0000, 0x70ff0000, 0x80ff0000,
			0x90ff0000, 0xa0ff0000, 0xb0ff0000, 0xc0ff0000, 0xd0ff0000,
			0xe0ff0000, 0xf0ff0000 };
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
