package com.newbegin.android_2048;

import android.content.Context;
import android.text.InputFilter.LengthFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

/**
 * @ClassName: GameView
 * @Description: 游戏操作的主类
 * @author Tony ZHENG zty@hust.edu.cn
 * @date 2014-8-8 上午11:19:54
 * 
 */
public class GameLayout extends GridLayout {

	/* 存放一组卡片 */
	private Card[][] cardMap;
	boolean haveBlank;
	boolean merged;
	boolean canMove[] = {true,true,true,true};
	Card[][] card;
	card = new Card[4][4];

	// 分数
	private int score;
	
	private int width;
	private int height;

	public int getter() {
		return score;
	}

	public GameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}

	public GameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	public GameLayout(Context context) {
		super(context);
		initGameView();
	}

	/**
	 * @Description: 初始化布局,设置滑动监听
	 * @param
	 * @return void
	 */
	public void initGameView() {

		// 滑动监听
		setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);
        width=(Math.max(w, h)-10)/4;
        refresh();
    }

	public void randomCard(int width,int height) {
		Card c;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				c=new Card(getContext());
				c.setValue(""+2);
				addView(c, width, height);
			}
		}
	}
	
	public void refresh(){
		for (Card card : cardMap) {
			addCard(width, height);
		}
	}

//	/**
//	 * @Description: 卡片合并,填充
//	 * @param
//	 * @return boolean
//	 */
//	public boolean merge() {
//		return false;
//	}

	/**
	 * 
	 * @Description: 刷新页面
	 * @param
	 * @return void
	 */
	public void refreshView() {

	}

	/**
	 * @Description: 向上滑动
	 * @param
	 * @return void
	 */
	public void swapUp() {

	}

	public void swapDown() {

	}

	public void swapLeft() {

	}

	public void swapRight() {

	}

}
