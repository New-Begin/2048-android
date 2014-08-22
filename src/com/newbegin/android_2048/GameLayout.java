package com.newbegin.android_2048;

import java.util.LinkedList;
import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridLayout;

/**
 * @ClassName: GameView
 * @Description: 游戏操作的主类
 * @author Tony ZHENG zty@hust.edu.cn
 * @date 2014-8-8 上午11:19:54
 * 
 */
public class GameLayout extends GridLayout {

	/* 存放一组卡片 */
	private static Card[][] cardMap = new Card[4][4];

	private boolean haveBlank;// 记录填充时是否有空格
	private boolean merged;// 记录是否进行 合并
	private boolean moved;//记录是否进行了移动
	public boolean canMove[] = { true, true, true, true };// 记录当前操作是否进行了合并填充up down left right

	// 分数
	private int score = 0;

	// card的宽高
	private int cardWidth;

	// 构造函数
	public GameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView(context);
	}

	public GameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView(context);
	}

	public GameLayout(Context context) {
		super(context);
		initGameView(context);
	}

	// getter && setter
	public Card[][] getCardMap() {
		return cardMap;
	}

	public void setCardMapValue(int[] values) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				cardMap[i][j].setValue(values[4*i+j]);
			}
		}
	}
	
	/**
	 * 获取cardMap的value
	 * @return value数组
	 */
	public int[] getCardMapValue(){
		int[] a = new int[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				a[4*i+j] = cardMap[i][j].getValue();
			}
		}
		return a;
	}

	public void setScore(int currentScore) {
		this.score = currentScore;
	}

	public int getScore() {
		return score;
	}

	/**
	 * @Description: 初始化布局,设置滑动监听
	 * @param
	 * @return void
	 */
	public void initGameView(Context context) {

		initCardMap();

		// 滑动监听
		this.setOnTouchListener((OnTouchListener) context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		cardWidth = (Math.min(w, h) - 10) / 4;

		addCardView();
	}

	/**
	 * 初始化cardMap
	 */
	private void initCardMap() {
		Card c;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				c = new Card(getContext());
				cardMap[i][j] = c;
			}
		}
	}

	public void clearCardMap() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				cardMap[i][j].setValue(0);
			}
		}
	}

	/**
	 * 随机生成卡片
	 * 
	 * @param cards
	 */

	public void randomCard() {
		System.out.println("GameLayout-------->randomCard()");
		LinkedList<Integer> ll = new LinkedList<Integer>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (cardMap[i][j] != null) {
					if (cardMap[i][j].getValue() == 0) {
						ll.add(4 * i + j);
					}
				}
			}
		}
		Random rd = new Random();
		Log.i("gamelayout", "ll.size() = " + ll.size());
		if (ll.size() == 0)
			return;
		int index = rd.nextInt(ll.size());
		int x = ll.get(index) / 4;
		int y = ll.get(index) - 4 * x;
		int value;
		// 10分之1的概率生成4
		if (rd.nextInt(10) == 4) {
			value = 2;
		} else {
			value = 1;
		}
		cardMap[x][y].setValue(value);
	}

	/**
	 * 
	 * @Description: 刷新页面,并随机生成新的卡片
	 * @param
	 * @return void
	 */
	public void refreshView() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {

				cardMap[i][j].refresh();
				System.out.println("refreshView---->" + i + j + "-->"
						+ cardMap[i][j].getValue());
			}
		}
	}

	// 在GridLayout中添加Card
	public void addCardView() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				cardMap[i][j].refresh();
				addView(cardMap[i][j], cardWidth, cardWidth);
			}
		}
	}

	/**
	 * @Description: 向左滑动
	 * @param
	 * @return void
	 */

	// 左滑动合并填充函数
	boolean gameLeft() {
		if (!canMove[2]){
			return true;
		}
		haveBlank = false;
		merged = false;
		moved = false;
		int fir, sec;
		// 合并
		for (int i = 0; i < 4; i++) {
			sec = 0;
			fir = 0;
			while (fir < 4) {
				if (cardMap[i][fir].getValue() != 0) {
					sec = fir + 1;
					while (sec < 4) {
						if (cardMap[i][sec].getValue() != 0) {
							if (cardMap[i][fir].isEqual(cardMap[i][sec])) {

								//加分数
								score = score + (int) Math.pow(2, cardMap[i][fir].getValue() + 1);
								
								System.out.println("down score----->" + score);
								cardMap[i][fir].plus();
								cardMap[i][sec].setValue(0);
								fir = sec + 1;
								merged = true;
							} else
								fir = sec;
							break;
						}
						else sec++;
					}
				}
				else fir++;
				if(sec == 4)
					break;
			}
		}

		// 填充
		for (int i = 0; i < 4; i++) {
			for (fir = 0; fir < 4; fir++) {
				if (cardMap[i][fir].getValue() == 0) {
					haveBlank = true;
					sec = fir + 1;
					while (sec < 4) {
						if (cardMap[i][sec].getValue() != 0) {
							moved = true;
							cardMap[i][fir]
									.setValue(cardMap[i][sec].getValue());
							cardMap[i][sec].setValue(0);
							fir += 1;
						}
						sec++;
					}
				}
			}
		}

		// 判断游戏是否结束以及各个方向滑动有没有效果
		if (merged == false) {
			if (moved == false) {
				canMove[2] = false;
				if(haveBlank == false){
					if (!vJudge()) {
						canMove[0] = canMove[1] = canMove[3] = false;
						return false;
					}
					else{
						canMove[3] = false;
						canMove[0] = canMove[1] = true;
						return true;
					}
				}
				else{
					canMove[0] = canMove[1] = canMove[3] = true;
					return true;
				}				
			}
			else{
				canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
				return true;
			}			
		}
		else{
			canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
			return true;
		}
	}

	// 右滑动合并填充函数
	boolean gameRight() {
		if (!canMove[3]){
			return true;
		}
		haveBlank = false;
		merged = false;
		moved = false;
		int fir, sec;
		// 合并
		for (int i = 0; i < 4; i++) {
			sec = 3;
			fir = 3;
			while (fir >= 0) {
				if (cardMap[i][fir].getValue() != 0) {
					sec = fir - 1;
					while (sec >= 0) {
						if (cardMap[i][sec].getValue() != 0) {
							if (cardMap[i][fir].isEqual(cardMap[i][sec])) {
				//加分数
								score = score + (int) Math.pow(2, cardMap[i][fir].getValue() + 1);
								
								cardMap[i][fir].plus();
								cardMap[i][sec].setValue(0);
								fir = sec - 1;
								merged = true;
							} else
								fir = sec;
							break;
						}
						else sec--;
					}
				}
				else fir--;
				if(sec < 0)
					break;
			}
		}

		// 填充
		for (int i = 0; i < 4; i++) {
			for (fir = 3; fir >= 0; fir--) {
				if (cardMap[i][fir].getValue() == 0) {
					haveBlank = true;
					sec = fir - 1;
					while (sec >= 0) {
						if (cardMap[i][sec].getValue() != 0) {
							moved = true;
							cardMap[i][fir]
									.setValue(cardMap[i][sec].getValue());
							cardMap[i][sec].setValue(0);
							fir -= 1;
						}
						sec--;
					}
				}
			}
		}

		// 判断游戏是否结束以及各个方向滑动有没有效果
		if (merged == false) {
			if (moved == false) {
				canMove[3] = false;
				if(haveBlank == false){
					if (!vJudge()) {
						canMove[0] = canMove[1] = canMove[2] = false;
						return false;
					}
					else{
						canMove[2] = false;
						canMove[0] = canMove[1] = true;
						return true;
					}
				}
				else{
					canMove[0] = canMove[1] = canMove[2] = true;
					return true;
				}				
			}
			else{
				canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
				return true;
			}			
		}
		else{
			canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
			return true;
		}
	}

	// 上滑动合并填充函数
	boolean gameUp() {
		if (!canMove[0]){
			return true;
		}
		haveBlank = false;
		merged = false;
		moved = false;
		int fir, sec;
		// 合并
		for (int i = 0; i < 4; i++) {
			sec = 0;
			fir = 0;
			while (fir < 4) {
				if (cardMap[fir][i].getValue() != 0) {
					sec = fir + 1;
					while (sec < 4) {
						if (cardMap[sec][i].getValue() != 0) {
							if (cardMap[fir][i].isEqual(cardMap[sec][i])) {

								//加分数
								score = score + (int) Math.pow(2, cardMap[fir][i].getValue() + 1);
								
								cardMap[fir][i].plus();
								cardMap[sec][i].setValue(0);
								fir = sec + 1;
								merged = true;
							} else
								fir = sec;
							break;
						}
						else sec++;
					}
				}
				else fir++;
				if(sec == 4)
					break;
			}
		}

		// 填充
		for (int i = 0; i < 4; i++) {
			for (fir = 0; fir < 4; fir++) {
				if (cardMap[fir][i].getValue() == 0) {
					haveBlank = true;
					sec = fir + 1;
					while (sec < 4) {
						if (cardMap[sec][i].getValue() != 0) {
							moved = true;
							cardMap[fir][i]
									.setValue(cardMap[sec][i].getValue());
							cardMap[sec][i].setValue(0);
							fir += 1;
						}
						sec++;
					}
				}
			}
		}

		// 判断游戏是否结束以及各个方向滑动有没有效果
		if (merged == false) {
			if (moved == false) {
				canMove[0] = false;
				if(haveBlank == false){
					if (!hJudge()) {
						canMove[2] = canMove[1] = canMove[3] = false;
						return false;
					}
					else{
						canMove[1] = false;
						canMove[2] = canMove[3] = true;
						return true;
					}
				}
				else{
					canMove[2] = canMove[1] = canMove[3] = true;
					return true;
				}				
			}
			else{
				canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
				return true;
			}			
		}
		else{
			canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
			return true;
		}
	}

	// 下滑动合并填充函数
	boolean gameDown() {
		if (!canMove[1]){
			return true;
		}
		haveBlank = false;
		merged = false;
		moved = false;
		int fir, sec;
		// 合并
		for (int i = 0; i < 4; i++) {
			sec = 3;
			fir = 3;
			while (fir >= 0) {
				if (cardMap[fir][i].getValue() != 0) {
					sec = fir - 1;
					while (sec >= 0) {
						if (cardMap[sec][i].getValue() != 0) {
							if (cardMap[fir][i].isEqual(cardMap[sec][i])) {

								//加分数
								score = score + (int) Math.pow(2, cardMap[fir][i].getValue() + 1);
								
								cardMap[fir][i].plus();
								cardMap[sec][i].setValue(0);
								fir = sec - 1;
								merged = true;
							} else
								fir = sec;
							break;
						}
						else sec--;
					}
				}
				else fir--;
				if(sec < 0)
					break;
			}
		}

		// 填充
		for (int i = 0; i < 4; i++) {
			for (fir = 3; fir >= 0; fir--) {
				if (cardMap[fir][i].getValue() == 0) {
					haveBlank = true;
					sec = fir - 1;
					while (sec >= 0) {
						if (cardMap[sec][i].getValue() != 0) {
							moved = true;
							cardMap[fir][i]
									.setValue(cardMap[sec][i].getValue());
							cardMap[sec][i].setValue(0);
							fir -= 1;
						}
						sec--;
					}
				}
			}
		}

		// 判断游戏是否结束以及各个方向滑动有没有效果
		if (merged == false) {
			if (moved == false) {
				canMove[1] = false;
				if(haveBlank == false){
					if (!hJudge()) {
						canMove[2] = canMove[0] = canMove[3] = false;
						return false;
					}
					else{
						canMove[0] = false;
						canMove[2] = canMove[3] = true;
						return true;
					}
				}
				else{
					canMove[2] = canMove[0] = canMove[3] = true;
					return true;
				}				
			}
			else{
				canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
				return true;
			}			
		}
		else{
			canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
			return true;
		}
	}

	// 在没有空格的情况下判断垂直方向有没有相等的相邻数
	boolean vJudge() {
		Log.i("gameview", "vjudge");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (cardMap[j][i].isEqual(cardMap[j + 1][i])) {
					return true;
				}
			}
		}
		return false;
	}

	// 在没有空格的情况下判断水平方向有没有相等的相邻数
	boolean hJudge() {
		Log.i("gameview", "hjudge");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (cardMap[i][j].isEqual(cardMap[i][j + 1])) {
					return true;
				}
			}
		}
		return false;
	}

}
