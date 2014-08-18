package com.newbegin.android_2048;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
	private HistoryStack historyRecord = new HistoryStack();//a stack that remains past gameView CardMap;
	
	//��ȡ�洢����ʷ��߷�
	private SharedPreferences gameHistory;
	private SharedPreferences.Editor historyEditor;
	
	private int currentScore;//record current score
	private int highScore;
	
	//�ж���Ϸ�Ƿ����
	private boolean isContinue = true;
	//onTouch�¼��ĳ�ʼ�����ƫ�����������ж��ƶ�����
	private float X, Y, offsetX, offsetY;
	
	//��ʾ��ǰ��������߷ֵ����
	private TextView correntScoreTV;
	private TextView highScoreTV;

	//��Ϸ������ʾ��
	private AlertDialog.Builder gameOverDialog;
	
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
		gameView.setScore(0);
		this.highScoreTV.setText("HighScore:" + Integer.toString(highScore));
		this.correntScoreTV.setText("CurrentScore:" + 0);
		historyRecord.clearStack();
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
		switch (id) {
		case R.id.restart:
			this.init();
			return true;
		case R.id.undo:
			gameView.setCardMap(historyRecord.pop());
			gameView.refreshView();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	//ÿ�λ���gameView�ص�ִ�еķ���
	//ʵ��onTouch�ӿ�,zhty add
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = event.getX();
			Y = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			//��¼��ǰ���
			historyRecord.push(gameView.getCardMap());
			
			offsetX = event.getX() - X;
			offsetY = event.getY() - Y;
			// ���жϷ���
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
			gameView.randomCard();
			//merge equal numbers			
			gameView.refreshView();
			currentScore = gameView.getScore();
			this.correntScoreTV.setText("CurrentScore:" + Integer.toString(currentScore));
			if(currentScore > highScore)
			{
				this.highScoreTV.setText("HighScore:" + Integer.toString(currentScore));
			}
			
			if(!isContinue)
			 {
			 //pop a dialog  which prompts that game is over. modify the highscore.
			 if(currentScore > highScore)
			 {
				historyEditor.putInt("highScore",currentScore);
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
	
	/** ��Ϸ������ʾ��*/
	private void showgameOverDialog()
	{
		gameOverDialog = new AlertDialog.Builder(this);
		gameOverDialog.setMessage("�˼�����Ϸ���˽�����");
		gameOverDialog.setTitle("Game Over����");
		gameOverDialog.setCancelable(false);
		//���¼����б�
		gameOverDialog.setPositiveButton("�����ˣ�", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				GameManagerActivity.this.finish();
			}
		});
		//�˳���ǰActivity
		gameOverDialog.setNegativeButton("�Ҳ�����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				GameManagerActivity.this.init();
			}
		});
		gameOverDialog.create().show();
	}	

}
