package com.newbegin.android_2048;


import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
	private int[] lastCardMapValue;

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
	
	//判断是否保存棋局退出,默认值为true,异常退出保存棋局
	private boolean isSaving = true;

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
		//加载上次退出时棋局
		if(gameHistory.getBoolean("isSaved", false))
		{
			highScore = gameHistory.getInt("highScore", 0);
			this.highScoreTV.setText("HighScore:" + Integer.toString(highScore));
			this.correntScoreTV.setText("CurrentScore:" + gameHistory.getInt("SavedScore", 0));
			gameView.setCardMapValue(this.reloadCardMapValue());
			//已经读取保存数据
			historyEditor.putBoolean("isSaved", false).commit();
		}
		else
		{
			highScore = gameHistory.getInt("highScore", 0);
			gameView.setScore(0);
			this.highScoreTV.setText("HighScore:" + Integer.toString(highScore));
			this.correntScoreTV.setText("CurrentScore:" + 0);
			historyRecord.clearStack();
			// 2.initialize data.
			gameView.clearCardMap();
			// 3.refresh UI.
			gameView.randomCard();
			gameView.randomCard();
		}
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
		case R.id.share:
			share();
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	// 每次滑动gameView回调执行的方法
	// 实现onTouch接口,zhty add
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 判断手指是否在屏幕上滑动
		boolean isMove = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = event.getX();
			Y = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			// 保存当前棋局
			if (!historyRecord.empty())
				lastCardMapValue = historyRecord.peek();
			historyRecord.push(gameView.getCardMapValue());

			offsetX = event.getX() - X;
			offsetY = event.getY() - Y;
			// 先判断方向
			if (Math.abs(offsetX) > Math.abs(offsetY)) {
				if (offsetX > 5) {
					Log.i("gameview", "right");
					isContinue = gameView.gameRight();
					isMove = true;
				} else if (offsetX < -5) {
					Log.i("gameview", "left");
					isContinue = gameView.gameLeft();
					isMove = true;
				}
			} else if (Math.abs(offsetX) < Math.abs(offsetY)) {
				if (offsetY > 5) {
					Log.i("gameview", "down");
					isContinue = gameView.gameDown();
					isMove = true;
				} else if (offsetY < -5) {
					Log.i("gameview", "up");
					isContinue = gameView.gameUp();
					isMove = true;
				}
			}
			System.out.println("onTouch-------->random");
			if (isMove) {
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

				if (Arrays.equals(gameView.getCardMapValue(),
						historyRecord.peek())) {
					historyRecord.push(lastCardMapValue);
				}
				//保存历史最高分到本地
				if (currentScore > highScore) {
						historyEditor.putInt("highScore", currentScore);
						historyEditor.commit();
					}
				System.out.println("------isContinue---"+ isContinue);
				//游戏结束，弹出提示框
				if (!isContinue) {
					System.out.println("---showGameOverDialog----");
					showgameOverDialog();
				}
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
		gameOverDialog.setCancelable(false);
		// 回退一步
		gameOverDialog.setPositiveButton("我怂退了!", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				isSaving = false;
				GameManagerActivity.this.finish();
				}				
		
		});
		//重新开始
		gameOverDialog.setNeutralButton("不服再来！",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				GameManagerActivity.this.init();			
			}
		} );
		// 退出游戏
		gameOverDialog.setNegativeButton("我点错了！", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (!historyRecord.empty()) {
					gameView.setCardMapValue(historyRecord.pop());
					gameView.refreshView();
				}
				
			}
		});
		gameOverDialog.create().show();
	}

	//点击返回按钮时调用。
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			gameExitDialog = new AlertDialog.Builder(this);
			gameExitDialog.setCancelable(false);
			gameExitDialog.setMessage("菜鸡，你怂你跑啊！");
			gameExitDialog.setTitle("退？！");
			// gameOverDialog.setCancelable(false);
			// 返回游戏
			gameExitDialog.setPositiveButton("我菜我退！", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					//正常退出且不保存棋盘
					isSaving = false;
					GameManagerActivity.this.finish();
				}		
			});
			gameExitDialog.setNeutralButton("下次接着干！", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					//正常退出且保存棋盘
					isSaving = true;
					//finish调用onStop
					GameManagerActivity.this.finish();
				}
			});
			//退出游戏
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

	/**
	 * 分享分数，使用Intent分享，没有调用WeiXin API之类的接口
	 */
	public void share() {
		// 分享的Intent
		Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
		// 分享的类型为纯文本
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
		intent.putExtra(Intent.EXTRA_TEXT, "我在超级2048中得了"+gameView.getScore()+"分，你行你也来啊https://github.com/New-Begin/2048-android");
		// 本地图片缓存
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
//				Locale.US);
//		String fname = "/sdcard/" + sdf.format(new Date()) + ".png";
//		View view = gameView.getRootView();
//		view.setDrawingCacheEnabled(true);
//		view.buildDrawingCache();
//		Bitmap bitmap = view.getDrawingCache();
//		if (bitmap != null) {
//			System.out.println("bitmap got!");
//			try {
//				FileOutputStream out = new FileOutputStream(fname);
//				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//				System.out.println("file" + fname + "output done.");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			System.out.println("bitmap is NULL!");
//		}
//		
//		intent.putExtra(Intent.EXTRA_STREAM,
//				Uri.fromFile(new File(fname)));
		try {
			// 跳转到处理分享Intent的Activity
			startActivity(intent);
		} catch (ActivityNotFoundException anfe) {
			Log.w("zhtyshare", anfe.toString());
		}
	}
	
	//Home键退出保存当前棋局
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(isSaving)
		{
			this.uploadCardMapValue(gameView.getCardMapValue());
		}	
	}

	/**
	 * 把棋盘数据保存到SharedPreference
	 * @param cardMapValue  要保存的数组
	 * @return 是否保存成功
	 */
	private boolean uploadCardMapValue(int[]cardMapValue)
	{
		historyEditor.putInt("SavedScore", gameView.getScore());
		for(int i = 0; i < 16; i++)
		{
			historyEditor.putInt("CardValue" + i,cardMapValue[i] );
			//System.out.println("cardValue------->" + cardMapValue[i]);
		}
		boolean isUploaded = historyEditor.commit();
		historyEditor.putBoolean("isSaved", isUploaded).commit();
		return isUploaded;
	}
	
	/**
	 * 从Sharedpreference读取保存的棋盘数据
	 * @return 读取的数组信息
	 */
	private int[] reloadCardMapValue()
	{
		int [] cardMapValue = new int[16];
		for(int i = 0; i < 16; i ++)
		{
			cardMapValue[i] =  gameHistory.getInt("CardValue" + i, 0);
		}
		return cardMapValue;
	}
}
