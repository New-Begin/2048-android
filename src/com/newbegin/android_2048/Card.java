package com.newbegin.android_2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;


public class Card extends FrameLayout {

	private int value;
	//�ٷֱȣ�R��G��B
	private int[] color = { 0xffEEE8AA, 0xffF5DEB3, 0x90FFA500, 0xffF4A460,
			0xffFF7F24, 0xffFF4040, 0xffEE0000, 0xffEEC900, 0xffEEAD0E,
			0xffEE9A00, 0xffEE3B3B, 0xffEE2C2C, 0xffEEC900, 0xffEEEE00,
			0xffEEEE00, 0xffEEEE00 };
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
			label.setText("");
		} else {
			int number = (int) Math.pow(2, value);
			// ��numberת�����ַ�����������ʾint�ĵ�ַ
			label.setText("" + number);
		}
		label.setBackgroundColor(color[value]);
	}

	// ��ȡcard��value
	public int getValue() {
		return value;
	}

}
