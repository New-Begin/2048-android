package com.newbegin.android_2048;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

public class GameManagerActivity extends Activity implements OnTouchListener {

	private GameLayout gameView; // contain a double dimensional array gameView
									// numArray;

	// 保存上一步操作的堆栈
	private HistoryStack historyRecord = new HistoryStack();
	private int [] lastCardMapValue;

	// 获取存储的历史最高分
	private SharedPreferences gameHistory;
	private SharedPreferences.Editor historyEditor;

	private int currentScore;// record current score
	private int highScore;

	// 判断游戏是否结束
	private boolean isContinue = true;
	// onTouch事件的初始坐标和偏移量，用于判断移动方向
	private float X, Y, offsetX, offsetY;

	// 显示当前分数和最高分的组件
	private TextView correntScoreTV;
	private TextView highScoreTV;

	// 游戏结束提示框
	private AlertDialog.Builder gameOverDialog;
	private AlertDialog.Builder gameExitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gameView = (GameLayout) findViewById(R.id.gameView);
		correntScoreTV = (TextView) findViewById(R.id.correntScore);
		highScoreTV = (TextView) findViewById(R.id.highScore);

		init();
	}

	/**
	 * initialize the background double dimensional array, the foreground
	 * GameView
	 */
	private void init() {
		// 1.load the history high score;
		gameHistory = this.getSharedPreferences("gameHistory",
				Context.MODE_PRIVATE);
		historyEditor = gameHistory.edit();
		highScore = gameHistory.getInt("highScore", 0);
		gameView.setScore(0);
		this.highScoreTV.setText("HighScore:" + Integer.toString(highScore));
		this.correntScoreTV.setText("CurrentScore:" + 0);
		historyRecord.clearStack();
		// 2.initialize data.
		gameView.clearCardMap();
		// 3.refresh UI.
		gameView.randomCard();
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
		switch (id) {
		case R.id.restart:

			this.init();
			break;
		case R.id.undo:
			if (!historyRecord.empty()) {
				gameView.setCardMapValue(historyRecord.pop());
				gameView.refreshView();
			} else
				Toast.makeText(getApplicationContext(), "菜鸡！只能回退一次！",
						Toast.LENGTH_LONG).show();
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	// 每次滑动gameView回调执行的方法
	// 实现onTouch接口,zhty add
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = event.getX();
			Y = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			//保存当前棋局
			if(!historyRecord.empty())
				lastCardMapValue = historyRecord.peek();
			historyRecord.push(gameView.getCardMapValue());

			offsetX = event.getX() - X;
			offsetY = event.getY() - Y;
			// 先判断方向
			if (Math.abs(offsetX) > Math.abs(offsetY)) {
				if (offsetX > 5) {
					Log.i("gameview", "right");
					isContinue = gameView.gameRight();
				} else if (offsetX < -5) {
					Log.i("gameview", "left");
					isContinue = gameView.gameLeft();
				}
			} else if (Math.abs(offsetX) < Math.abs(offsetY)) {
				if (offsetY > 5) {
					Log.i("gameview", "down");
					isContinue = gameView.gameDown();
				} else if (offsetY < -5) {
					Log.i("gameview", "up");
					isContinue = gameView.gameUp();
				}
			}
			System.out.println("onTouch-------->random");
			if (gameView.canMove[0] && gameView.canMove[1]
					&& gameView.canMove[2] && gameView.canMove[3])
				gameView.randomCard();
			// merge equal numbers
			gameView.refreshView();
			currentScore = gameView.getScore();
			this.correntScoreTV.setText("CurrentScore:"
					+ Integer.toString(currentScore));
			if (currentScore > highScore) {
				this.highScoreTV.setText("HighScore:"
						+ Integer.toString(currentScore));
			}
			
			if(Arrays.equals(gameView.getCardMapValue(), historyRecord.peek()))
			{
				historyRecord.push(lastCardMapValue);
			}

			if (!isContinue) {
				// pop a dialog which prompts that game is over. modify the
				// highscore.
				if (currentScore > highScore) {
					historyEditor.putInt("highScore", currentScore);
					historyEditor.commit();
				}
				showgameOverDialog();
			}
			break;
		default:
			break;
		}
		return true;
	}

	/** 游戏结束提示框 */
	private void showgameOverDialog() {
		gameOverDialog = new AlertDialog.Builder(this);
		gameOverDialog.setMessage("菜鸡，游戏到此结束！");
		gameOverDialog.setTitle("Game Over！！");
		//gameOverDialog.setCancelable(false);
		// 重新加载列表
		gameOverDialog.setPositiveButton("我怂退了！", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				GameManagerActivity.this.finish();
			}
		});
		// 退出当前Activity
		gameOverDialog.setNegativeButton("不服再来！", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				GameManagerActivity.this.init();
			}
		});
		gameOverDialog.create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {  
				gameExitDialog = new AlertDialog.Builder(this);
				gameExitDialog.setMessage("菜鸡，你怂你跑啊！");
				gameExitDialog.setTitle("退？！");
				//gameOverDialog.setCancelable(false);
				// 重新加载列表
				gameExitDialog.setPositiveButton("我怂退了！", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						GameManagerActivity.this.finish();
					}
				});
				// 退出当前Activity
				gameExitDialog.setNegativeButton("手滑点错！", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				gameExitDialog.create().show();
	  
	        }            
	        return false;           
	    }  


}
