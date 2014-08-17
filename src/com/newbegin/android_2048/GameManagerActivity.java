package com.newbegin.android_2048;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class GameManagerActivity extends Activity implements OnTouchListener {
	
	private GameLayout gameView; //contain a double dimensional array gameView numArray;
	private ControlLayout controlPanel;
	
	private HistoryStack historyRecord = new HistoryStack();//a stack that remains past gameView.numArray;
	
	private SharedPreferences gameHistory;
	private SharedPreferences.Editor historyEditor;
	
	private int currentScore;//record current score
	private int highScore;
	
	//onTouch事件的初始坐标和偏移量，用于判断移动方向
	private float X, Y, offsetX, offsetY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//refer gameView and controlPanel
		gameView = (GameLayout)findViewById(R.layout.game_gird_layout);
		controlPanel = (ControlPanel)findViewById(R.layout.game_control_layout);
		
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
		controlPanel.setScore(highScore);
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

	//每次滑动gameView回调执行的方法
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		//merge equal numbers
		/*
		 * 
		 *
		 * 	historyRecord.push();
		 * 	boolean win = gameView.merge();
		 * 	gameView.refreshView();
		 * 	currentScore = gameView.getScore();
		 * 	controlPanel.refreshScore();
		 * 
		 *
		 * if(!win)
		 * {
		 * //pop a dialog  which prompts that game is over. modify the highscore.
		 * 	if(currentScore > highScore)
		 * 	{
		 * 		historyEditor.putInt("highScore",currentScore);
		 * 		historyEditor.commit();
		 * 	}
		 * }
		 * 
		 * 	
		 * 	
		 */
		return false;
	}
	
	

	//实现onTouch接口,zhty add
	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
				} else if (offsetX < -5) {
					Log.i("gameview", "left");
				}
			} else if (Math.abs(offsetX) < Math.abs(offsetY)) {
				if (offsetY > 5) {
					Log.i("gameview", "down");
				} else if (offsetY < -5) {
					Log.i("gameview", "up");
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

}
