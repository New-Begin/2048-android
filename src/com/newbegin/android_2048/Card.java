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
		/* ��ʼ��value��numberֵ�Լ���ʼ����ɫ */
		// value = 0;
		// setBackgroundColor(color[value]);
		init();
	}

	//��ʼ��
	public void init(){
		value = 0;

		/*
		 * ����Ӧ�������Card��ͨ������
		 */
		label.setTextSize(32);
		label.setBackgroundColor(color[value]);
		label.setGravity(Gravity.CENTER);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(5, 5, 5, 5);
		addView(label, lp);

	}

	// �Ƚ��Ƿ����
	public boolean isEqual(Card other) {
		if (value == other.getValue())
			return true;
		else
			return false;
	}

	// valueֵ����
	public void plus() {
		value++;
		return;
	}


	//�ı�value
	public void setValue(int task){

		value = task;
		return;
	}

	// ˢ��card�����ֵ�Լ�����ɫ
	public void refresh() {
		if (value == 0) {
			return;
		} else {
			int number = (int) Math.pow(2, value);
			// ��numberת�����ַ�����������ʾint�ĵ�ַ
			label.setText("" + number);
			label.setBackgroundColor(color[value]);
		}

	}

	// ��ȡcard��value
	public int getValue() {
		return value;
	}

}
