package com.newbegin.android_2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;


public class Card extends FrameLayout {

	private int value;
	//�ٷֱȣ�R��G��B
	private int[] color = { 0x11808080, 0x60a6ffff, 0x6046a3ff, 0x6000aeae,
			0x607d7dff, 0x600080ff,  0xb0a6ffff, 0xb046a3ff, 0xb000aeae,
			0xb07d7dff, 0xb00080ff,  0xf0a6ffff, 0xf046a3ff, 0xf000aeae,
			0xf07d7dff, 0xf00080ff };
	
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
