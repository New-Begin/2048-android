package com.newbegin.android_2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
public class Card extends TextView{
	
	private int value;
	private int[] color = {0x0fff0000,0x10ff0000,0x20ff0000,0x30ff0000,0x40ff0000,0x50ff0000,0x60ff0000,
			0x70ff0000,0x80ff0000,0x90ff0000,0xa0ff0000,0xb0ff0000,0xc0ff0000,0xd0ff0000,0xe0ff0000,0xf0ff0000};
	
	public Card(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		/*初始化value、number值以及初始背景色*/
//		value = 0;
//		setBackgroundColor(color[value]);
		init();
	}
	
	//初始化
	public void init(){
		value = 0;
		setTextSize(32);
		setBackgroundColor(color[value]);
		setGravity(Gravity.CENTER);
		GridLayout.LayoutParams lp = new GridLayout.LayoutParams();  
		lp.setMargins(10, 20, 30, 40);  
		setLayoutParams(lp); 
		/*
		 * 这里应添加设置Card的通用属性
		 */
	}
	
	//比较是否相等
	public boolean isEqual(Card other){
		if(value == other.getValue())
			return true;
		else
			return false;
	}
	
	//value值增加
	public void plus(){
		value++;
		return;
	}

	//改变value
	public void setValue(int task){
		value = task;
		return;
	}
	
	//刷新card代表的值以及背景色
	public void refresh(){
		if(value == 0){
			return;
		}
		else{
			int number = (int) Math.pow(2, value);
			//将number转换成字符串，否则显示int的地址
			setText(""+number);
			setBackgroundColor(color[value]);
		}
		
	}
	
	//获取card的value
	public int getValue(){
		return value;
	}
	
}
