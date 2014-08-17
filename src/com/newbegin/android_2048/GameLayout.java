package com.newbegin.android_2048;

import java.util.LinkedList;
import java.util.Random;

import android.R.integer;
import android.content.Context;
import android.text.InputFilter.LengthFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

/**
 * @ClassName: GameView
 * @Description: ��Ϸ����������
 * @author Tony ZHENG zty@hust.edu.cn
 * @date 2014-8-8 ����11:19:54
 * 
 */
public class GameLayout extends GridLayout {

	/* ���һ�鿨Ƭ */
	private Card[][] cardMap = new Card[4][4];
	
	boolean haveBlank;//��¼���ʱ�Ƿ��пո�
	boolean merged;//��¼�Ƿ���� �ϲ�
	boolean canMove[] = { true, true, true, true };//��¼��ǰ�Ƿ�����˺ϲ����

	// ����
	private int score;

	// card�Ŀ��
	private int cardWidth;

	public int getter() {
		return score;
	}

	// ���캯��
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

	/**
	 * @Description: ��ʼ������,���û�������
	 * @param
	 * @return void
	 */
	public void initGameView(Context context) {

		// ��������
		this.setOnTouchListener((OnTouchListener) context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		cardWidth = (Math.max(w, h) - 10) / 4;
		randomCard(cardMap);
		refresh(cardMap, cardWidth, cardWidth);
	}

	// ��ʼ��cardMap
	public void initCardMap() {
		Card c;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				c = new Card(getContext());
				cardMap[i][j] = c;
			}
		}
	}
	
	//������ɿ�Ƭ
	public void randomCard(Card[][] cards) {
		LinkedList<Integer> ll = new LinkedList<Integer>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (cards[i][j] != null) {
					if (cards[i][j].getValue() == 0) {
						ll.add(4*i+j);
					}
				}
			}
		}
		Random rd = new Random();
		int index =rd.nextInt(ll.size());
		int x=ll.get(index)/4;
		int y=ll.get(index) -4*x;
		int value;
		//10��֮1�ĸ�������4
		if (rd.nextInt(10)==4) {
			value = 4;
		}else{
			value = 2;
		}
		cards[x][y].setValue(value);
	}

	// ��Card[][]�ڷŵ�GridLayout��
	public void refresh(Card[][] cards, int width, int height) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				addView(cards[i][j], width, height);
			}
		}
	}

	// /**
	// * @Description: ��Ƭ�ϲ�,���
	// * @param
	// * @return boolean
	// */
	// public boolean merge() {
	// return false;
	// }

	/**
	 * 
	 * @Description: ˢ��ҳ��
	 * @param
	 * @return void
	 */
	public void refreshView() {

	}

	/**
	 * @Description: ���ϻ���
	 * @param
	 * @return void
	 */
	// �󻬶��ϲ���亯��
	boolean gameLeft() {
		if (!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir, sec;
		// �ϲ�
		for (int i = 0; i < 4; i++) {
			for (fir = 0; fir < 4; fir++) {
				if (cardMap[i][fir].getValue() != 0) {
					sec = fir + 1;
					while (sec < 4) {
						if (cardMap[i][sec].getValue() != 0) {
							if (cardMap[i][fir].isEqual(cardMap[i][sec])) {
								
								//�ӷ���
								score += (int) Math.pow(2, cardMap[i][fir].getValue());
								
								cardMap[i][fir].plus();
								cardMap[i][sec].valueChange(0);
								fir = sec + 1;
								merged = true;
							} else
								fir = sec;
							break;
						}
						sec++;
					}
				}
			}
		}

		// ���
		for (int i = 0; i < 4; i++) {
			for (fir = 0; fir < 4; fir++) {
				if (cardMap[i][fir].getValue() == 0) {
					haveBlank = true;
					sec = fir + 1;
					while (sec < 4) {
						if (cardMap[i][sec].getValue() != 0) {
							cardMap[i][fir].valueChange(cardMap[i][sec]
									.getValue());
							cardMap[i][sec].valueChange(0);
							fir += 1;
						}
						sec++;
					}
				}
			}
		}

		// �ж���Ϸ�Ƿ�����Լ��������򻬶���û��Ч��
		if (merged == false) {
			if (haveBlank == false) {
				canMove[2] = false;
				canMove[3] = false;
				if (!vJudge()) {
					return false;
				}
				canMove[0] = canMove[1] = true;
				return true;
			}
			canMove[0] = canMove[1] = canMove[3] = true;
		}
		canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
		return true;
	}

	// �һ����ϲ���亯��
	boolean gameRight() {
		if (!canMove[3])
			return true;
		haveBlank = false;
		merged = false;
		int fir, sec;
		// �ϲ�
		for (int i = 0; i < 4; i++) {
			for (fir = 3; fir >= 0; fir--) {
				if (cardMap[i][fir].getValue() != 0) {
					sec = fir - 1;
					while (sec >= 0) {
						if (cardMap[i][sec].getValue() != 0) {
							if (cardMap[i][fir].isEqual(cardMap[i][sec])) {
								
								//�ӷ���
								score += (int) Math.pow(2, cardMap[i][fir].getValue());
								
								cardMap[i][fir].plus();
								cardMap[i][sec].valueChange(0);
								fir = sec - 1;
								merged = true;
							} else
								fir = sec;
							break;
						}
						sec--;
					}
				}
			}
		}

		// ���
		for (int i = 0; i < 4; i++) {
			for (fir = 3; fir >= 0; fir--) {
				if (cardMap[i][fir].getValue() == 0) {
					haveBlank = true;
					sec = fir - 1;
					while (sec >= 0) {
						if (cardMap[i][sec].getValue() != 0) {
							cardMap[i][fir].valueChange(cardMap[i][sec]
									.getValue());
							cardMap[i][sec].valueChange(0);
							fir -= 1;
						}
						sec--;
					}
				}
			}
		}

		// �ж���Ϸ�Ƿ�����Լ��������򻬶���û��Ч��
		if (merged == false) {
			canMove[2] = false;
			if (haveBlank == false) {
				canMove[3] = false;
				if (!vJudge()) {
					return false;
				}
				canMove[0] = canMove[1] = true;
				return true;
			}
			canMove[0] = canMove[1] = canMove[3] = true;
		}
		canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
		return true;
	}

	// �ϻ����ϲ���亯��
	boolean gameUp() {
		if (!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir, sec;
		// �ϲ�
		for (int i = 0; i < 4; i++) {
			for (fir = 0; fir < 4; fir++) {
				if (cardMap[fir][i].getValue() != 0) {
					sec = fir + 1;
					while (sec < 4) {
						if (cardMap[sec][i].getValue() != 0) {
							if (cardMap[fir][i].isEqual(cardMap[sec][i])) {
								
								//�ӷ���
								score += (int) Math.pow(2, cardMap[fir][i].getValue());
								
								cardMap[fir][i].plus();
								cardMap[sec][i].valueChange(0);
								fir = sec + 1;
								merged = true;
							} else
								fir = sec;
							break;
						}
						sec++;
					}
				}
			}
		}

		// ���
		for (int i = 0; i < 4; i++) {
			for (fir = 0; fir < 4; fir++) {
				if (cardMap[fir][i].getValue() == 0) {
					haveBlank = true;
					sec = fir + 1;
					while (sec < 4) {
						if (cardMap[sec][i].getValue() != 0) {
							cardMap[fir][i].valueChange(cardMap[sec][i]
									.getValue());
							cardMap[sec][i].valueChange(0);
							fir += 1;
						}
						sec++;
					}
				}
			}
		}

		// �ж���Ϸ�Ƿ�����Լ��������򻬶���û��Ч��
		if (merged == false) {
			canMove[2] = false;
			if (haveBlank == false) {
				canMove[3] = false;
				if (!vJudge()) {
					return false;
				}
				canMove[0] = canMove[1] = true;
				return true;
			}
			canMove[0] = canMove[1] = canMove[3] = true;
		}
		canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
		return true;
	}

	// �»����ϲ���亯��
	boolean gameDown() {
		if (!canMove[2])
			return true;
		haveBlank = false;
		merged = false;
		int fir, sec;
		// �ϲ�
		for (int i = 0; i < 4; i++) {
			for (fir = 3; fir >= 0; fir--) {
				if (cardMap[fir][i].getValue() != 0) {
					sec = fir - 1;
					while (sec < 4) {
						if (cardMap[sec][i].getValue() != 0) {
							if (cardMap[fir][i].isEqual(cardMap[sec][i])) {
								
								//�ӷ���
								score += (int) Math.pow(2, cardMap[fir][i].getValue());
								
								cardMap[fir][i].plus();
								cardMap[sec][i].valueChange(0);
								fir = sec - 1;
								merged = true;
							} else
								fir = sec;
							break;
						}
						sec--;
					}
				}
			}
		}

		// ���
		for (int i = 0; i < 4; i++) {
			for (fir = 3; fir >= 0; fir--) {
				if (cardMap[fir][i].getValue() == 0) {
					haveBlank = true;
					sec = fir - 1;
					while (sec < 4) {
						if (cardMap[sec][i].getValue() != 0) {
							cardMap[fir][i].valueChange(cardMap[sec][i]
									.getValue());
							cardMap[sec][i].valueChange(0);
							fir -= 1;
						}
						sec--;
					}
				}
			}
		}

		// �ж���Ϸ�Ƿ�����Լ��������򻬶���û��Ч��
		if (merged == false) {
			canMove[2] = false;
			if (haveBlank == false) {
				canMove[3] = false;
				if (!vJudge()) {
					return false;
				}
				canMove[0] = canMove[1] = true;
				return true;
			}
			canMove[0] = canMove[1] = canMove[3] = true;
		}
		canMove[0] = canMove[1] = canMove[2] = canMove[3] = true;
		return true;
	}

	// ��û�пո��������жϴ�ֱ������û����ȵ�������
	boolean vJudge() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (cardMap[j][i].isEqual(cardMap[j + 1][i])) {
					return true;
				}
			}
		}
		return false;
	}

	// ��û�пո��������ж�ˮƽ������û����ȵ�������
	boolean hJudge() {
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
