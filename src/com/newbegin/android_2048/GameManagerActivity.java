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
	
	//保存上一步操作的堆栈
	private HistoryStack historyRecord = new HistoryStack();//a stack that remains past gameView.numArray;
	
	//获取存储的历史最高分
	private SharedPreferences gameHistory;
	private SharedPreferences.Editor historyEditor;
	
	private int currentScore;//record current score
	private int highScore;
	
	//判断游戏是否结束
	private boolean isOver = false;
	//onTouch事件的初始坐标和偏移量，用于判断移动方向
	private float X, Y, offsetX, offsetY;
	
	//显示当前分数和最高分的组件
	private TextView correntScoreTV;
	private TextView highScoreTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gameView = (GameLayout)findViewById(R.id.gameView);
		correntScoreTV = (TextView)findViewById(R.id.correntScore);
		highScoreTV = (TextView)findViewById(R.id.highScore);
		
		init();
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
		this.highScoreTV.setText("HighScore:" + Integer.toString(highScore));
		
		//2.initialize data.  
		gameView.initCardMap();
		//3.refresh UI.
		gameView.refreshView();

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

	//每次滑动gameView回调执行的方法
	//实现onTouch接口,zhty add
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		//记录当前棋局
		//historyRecord.push(gameView.getCardMap());
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = event.getX();
			Y = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			offsetX = event.getX() - X;
			offsetY = event.getY() - Y;
			// 先判断方向
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
			//merge equal numbers			
			gameView.refreshView();
			currentScore = gameView.getScore();
			this.correntScoreTV.setText("CurrentScore:" + Integer.toString(currentScore));
			if(currentScore > highScore)
			{
				this.highScoreTV.setText("HighScore:" + Integer.toString(currentScore));
			}
			
			if(isOver)
			 {
			 //pop a dialog  which prompts that game is over. modify the highscore.
			 if(currentScore > highScore)
			 {
				historyEditor.putInt("highScore",currentScore);
			  	historyEditor.commit();
			  	}
			  }	
			break;
		default:
			break;
		}
		return true;
	}

}
