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

	// ������һ�������Ķ�ջ,Ŀǰʵ�ֻ���һ�������¿���չ�ӿ�
	private HistoryStack historyRecord = new HistoryStack();
	private int[] lastCardMapValue;
	private boolean[] lastCanMove;
	private boolean[] tempCanMove;

	// ��ȡ�洢����ʷ��߷�
	private SharedPreferences gameHistory;
	private SharedPreferences.Editor historyEditor;

	private int currentScore;// record current score
	private int highScore;

	// �ж���Ϸ�Ƿ����
	private boolean isContinue = true;
	// onTouch�¼��ĳ�ʼ�����ƫ�����������ж��ƶ�����
	private float X, Y, offsetX, offsetY;

	// ��ʾ��ǰ��������߷ֵ����
	private TextView correntScoreTV;
	private TextView highScoreTV;

	// ��Ϸ������ʾ��
	private AlertDialog.Builder gameOverDialog;
	private AlertDialog.Builder gameExitDialog;
	
	//�ж��Ƿ񱣴�����˳�,Ĭ��ֵΪtrue,�쳣�˳��������
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
		//�����ϴ��˳�ʱ���
		if(gameHistory.getBoolean("isSaved", false))
		{
			highScore = gameHistory.getInt("highScore", 0);
			this.highScoreTV.setText("HighScore:" + Integer.toString(highScore));
			int savedScore = gameHistory.getInt("SavedScore", 0);
			this.correntScoreTV.setText("CurrentScore:" + savedScore);
			gameView.setScore(savedScore);
			gameView.setCardMapValue(this.reloadCardMapValue());
			gameView.setCanMove(this.reloadCanMove());
			//�Ѿ���ȡ��������
			historyEditor.putBoolean("isSaved", false).commit();
		}
		else
		{
			highScore = gameHistory.getInt("highScore", 0);
			gameView.setScore(0);
			this.highScoreTV.setText("HighScore:" + Integer.toString(highScore));
			this.correntScoreTV.setText("CurrentScore:" + 0);
			gameView.setCanMove(new boolean[]{true,true,true,true});
			tempCanMove = lastCanMove = gameView.getCanMove();
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
				gameView.setCanMove(lastCanMove);
				gameView.refreshView();
				for(int i = 0; i < 4; i++)
				{
					System.out.println("undocanMove---->" + gameView.canMove[i]);
				}
			} else
				Toast.makeText(getApplicationContext(), "�˼���ֻ�ܻ���һ�Σ�",
						Toast.LENGTH_LONG).show();
			break;
		case R.id.share:
			share();
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	// ÿ�λ���gameView�ص�ִ�еķ���
	// ʵ��onTouch�ӿ�,zhty add
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// �ж���ָ�Ƿ�����Ļ�ϻ���
		boolean isMove = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = event.getX();
			Y = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			// ���浱ǰ���
			if (!historyRecord.empty())
				lastCardMapValue = historyRecord.peek();
			historyRecord.push(gameView.getCardMapValue());
			tempCanMove = gameView.getCanMove();
			
			for(int i = 0; i < 4; i++)
			{
				System.out.println("LastcanMove---->" + gameView.canMove[i]);
			}
			
			offsetX = event.getX() - X;
			offsetY = event.getY() - Y;
			// ���жϷ���
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
			for(int i = 0; i < 4; i++)
			{
				System.out.println("CurrentcanMove---->" + gameView.canMove[i]);
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
				else
					lastCanMove = tempCanMove;
				//������ʷ��߷ֵ�����
				if (currentScore > highScore) {
						historyEditor.putInt("highScore", currentScore);
						historyEditor.commit();
					}
				System.out.println("------isContinue---"+ isContinue);
				//��Ϸ������������ʾ��
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

	/** ��Ϸ������ʾ�� */
	private void showgameOverDialog() {
		gameOverDialog = new AlertDialog.Builder(this);
		gameOverDialog.setMessage("�˼�����Ϸ���˽�����");
		gameOverDialog.setTitle("Game Over����");
		gameOverDialog.setCancelable(false);
		// ����һ��
		gameOverDialog.setPositiveButton("��������!", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				isSaving = false;
				GameManagerActivity.this.finish();
				}				
		
		});
		//���¿�ʼ
		gameOverDialog.setNeutralButton("����������",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				GameManagerActivity.this.init();			
			}
		} );
		// �˳���Ϸ
		gameOverDialog.setNegativeButton("���ƴ��ˣ�", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (!historyRecord.empty()) {
					gameView.setCardMapValue(historyRecord.pop());
					gameView.setCanMove(lastCanMove);
					gameView.refreshView();
				}
				
			}
		});
		gameOverDialog.create().show();
	}

	//������ذ�ťʱ���á�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			gameExitDialog = new AlertDialog.Builder(this);
			gameExitDialog.setCancelable(false);
			gameExitDialog.setMessage("�˼����������ܰ���");
			gameExitDialog.setTitle("�ˣ���");
			// gameOverDialog.setCancelable(false);
			// ������Ϸ
			gameExitDialog.setPositiveButton("�Ҳ����ˣ�", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					//�����˳��Ҳ���������
					isSaving = false;
					GameManagerActivity.this.finish();
				}		
			});
			gameExitDialog.setNeutralButton("�´ν��Ÿɣ�", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					//�����˳��ұ�������
					isSaving = true;
					//finish����onStop
					GameManagerActivity.this.finish();
				}
			});
			//�˳���Ϸ
			gameExitDialog.setNegativeButton("�ֻ����", new OnClickListener() {
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
	 * ���������ʹ��Intent����û�е���WeiXin API֮��Ľӿ�
	 */
	public void share() {
		// �����Intent
		Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
		// ���������Ϊ���ı�
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
		intent.putExtra(Intent.EXTRA_TEXT, "���ڳ���2048�е���"+gameView.getScore()+"�֣�������Ҳ����https://github.com/New-Begin/2048-android");
		// ����ͼƬ����
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
			// ��ת���������Intent��Activity
			startActivity(intent);
		} catch (ActivityNotFoundException anfe) {
			Log.w("zhtyshare", anfe.toString());
		}
	}
	
	//Home���˳����浱ǰ���
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(isSaving)
		{
			this.uploadCardMapValue(gameView.getCardMapValue(),gameView.canMove);
		}	
	}

	/**
	 * ���������ݱ��浽SharedPreference
	 * @param cardMapValue  Ҫ���������
	 * @return �Ƿ񱣴�ɹ�
	 */
	private boolean uploadCardMapValue(int[]cardMapValue, boolean[]canMove)
	{
		historyEditor.putInt("SavedScore", gameView.getScore());
		for(int i = 0; i < 16; i++)
		{
			historyEditor.putInt("CardValue" + i,cardMapValue[i] );
			//System.out.println("cardValue------->" + cardMapValue[i]);
		}
		for(int i = 0; i < 4; i++)
		{
			historyEditor.putBoolean("CanMove" + i,canMove[i]);
		}
		boolean isUploaded = historyEditor.commit();
		historyEditor.putBoolean("isSaved", isUploaded).commit();
		return isUploaded;
	}
	
	/**
	 * ��Sharedpreference��ȡ��������̿�Ƭֵ
	 * @return ��ȡ��������Ϣ
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
	/**
	 * ��Sharedpreference��ȡ������ƶ���Ϣ
	 * @return 
	 */
	private boolean[] reloadCanMove()
	{
		boolean [] canMove = new boolean[4];
		for(int i = 0; i < 4; i ++)
		{
			canMove[i] =  gameHistory.getBoolean("CanMove" + i, true);
		}
		return canMove;
	}
}
