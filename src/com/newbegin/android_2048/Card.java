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
		/*��ʼ��value��numberֵ�Լ���ʼ����ɫ*/
//		value = 0;
//		setBackgroundColor(color[value]);
		init();
	}
	
	//��ʼ��
	public void init(){
		value = 0;
		setTextSize(32);
		setBackgroundColor(color[value]);
		setGravity(Gravity.CENTER);
		GridLayout.LayoutParams lp = new GridLayout.LayoutParams();  
		lp.setMargins(10, 20, 30, 40);  
		setLayoutParams(lp); 
		/*
		 * ����Ӧ�������Card��ͨ������
		 */
	}
	
	//�Ƚ��Ƿ����
	public boolean isEqual(Card other){
		if(value == other.getValue())
			return true;
		else
			return false;
	}
	
	//valueֵ����
	public void plus(){
		value++;
		return;
	}

	//�ı�value
	public void valueChange(int task){
		value = task;
		return;
	}
	
	//ˢ��card�����ֵ�Լ�����ɫ
	public void refresh(){
		if(value == 0){
			return;
		}
		else{
			int number = (int) Math.pow(2, value);
			//��numberת�����ַ�����������ʾint�ĵ�ַ
			setText(""+number);
			setBackgroundColor(color[value]);
		}
		
	}
	
	//��ȡcard��value
	public int getValue(){
		return value;
	}
	
	//zhty add
	public void setValue(int value){
		this.value = value;
		refresh();
	}
	
}
