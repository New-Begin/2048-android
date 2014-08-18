package com.newbegin.android_2048;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;


public class GameManagerActivity extends Activity implements OnTouchListener {
	
	private GameLayout gameView; //contain a double dimensional array gameView numArray;
	
	//������һ�������Ķ�ջ
	private HistoryStack historyRecord = new HistoryStack();//a stack that remains past gameView.numArray;
	
	//��ȡ�洢����ʷ��߷�
	private SharedPreferences gameHistory;
	private SharedPreferences.Editor historyEditor;
	
	private int currentScore;//record current score
	private int highScore;
	
	//�ж���Ϸ�Ƿ����
	private boolean isOver = false;
	//onTouch�¼��ĳ�ʼ�����ƫ�����������ж��ƶ�����
	private float X, Y, offsetX, offsetY;
	
	//��ʾ��ǰ��������߷ֵ����
	private TextView correntScoreTV;
	private TextView highScoreTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//refer gameView and controlPanel
		gameView = (GameLayout)findViewById(R.id.gameView);
		correntScoreTV = (TextView)findViewById(R.id.correntScore);
		highScoreTV = (TextView)findViewById(R.id.highScore);
		
	}	
	
	/**
	 * initialize the background double dimensional array, the foreground GameView
	 */
	private void init()
	{
		//1.load the history high score;
		gameHistory = this.getSharedPreferences("gameHistory",Context.MODE_PRIVATE);
		historyEditor = gameHistory.edit();
		highScore = gameHistory.getInt("highScore", 0);
		//controlPanel.setScore(highScore);
		/*2.initialize data.  
			gameView.initAarry();
		*/
		/*3.refresh UI.
		 	gameView.refreshView();
		 */
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//ÿ�λ���gameView�ص�ִ�еķ���
	//ʵ��onTouch�ӿ�,zhty add
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//��¼��ǰ���
		historyRecord.push(gameView.getCardMap());
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = event.getX();
			Y = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			offsetX = event.getX() - X;
			offsetY = event.getY() - Y;
			// ���жϷ���
			if (Math.abs(offsetX) > Math.abs(offsetY)) {
				if (offsetX > 5) {
					Log.i("gameview", "right");
					isOver = gameView.gameRight();
				} else if (offsetX < -5) {
					Log.i("gameview", "left");
					isOver = gameView.gameLeft();
				}
			} else if (Math.abs(offsetX) < Math.abs(offsetY)) {
				if (offsetY > 5) {
					Log.i("gameview", "down");
					isOver = gameView.gameDown();
				} else if (offsetY < -5) {
					Log.i("gameview", "up");
					isOver = gameView.gameUp();
				}
			}
			break;
		default:
			break;
		}
		//merge equal numbers
		
		//boolean win = gameView.merge();
		gameView.refreshView();
		currentScore = gameView.getScore();
		//controlPanel.refreshScore();
		if(isOver)
		 {
		 //pop a dialog  which prompts that game is over. modify the highscore.
		 if(currentScore > highScore)
		 {
			historyEditor.putInt("highScore",currentScore);
		  	historyEditor.commit();
		  	}
		  }	
		return true;
	}

}
