package com.main.dinedroid.glassactivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.main.dinedroid.models.Table;

/**
 * Main activity.
 */
public class CardViewActivity extends Activity
{

	private Card _card;
	private View _cardView;
	private TextView _statusTextView;
	private TextView _title;
	public Handler Handler;
	private GestureDetector mGestureDetector;
	private BackgroundAsyncTask hailBG;
	private String SERVER_ADDRESS = "75.102.94.7";
	private int GET_HAILS = 1;
	private int REMOVE_HAIL = 0;
	private ArrayList<Table> hails = new ArrayList<Table>();

	private CustomCardScrollAdapter adapter;

	private List<Card> mCards;
	private CardScrollView mCardScrollView;

	private TextToSpeech _speech;
	String text[];
	int waiterId;
	String waiterName;
	private Context _context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		
		if(extras!=null)
		{
			text = extras.getStringArray("qr_result");
			waiterId = Integer.parseInt(text[1]);
			waiterName = text[2] + " " + text[3];
			Toast.makeText(this, "Hi "+text[2]+"!", Toast.LENGTH_SHORT).show();
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mGestureDetector = createGestureDetector(this);
		mCards = new ArrayList<Card>();

		mCardScrollView = new CardScrollView(this);
		adapter = new CustomCardScrollAdapter();
		ScheduledExecutorService scheduler =
				Executors.newSingleThreadScheduledExecutor();

		scheduler.scheduleAtFixedRate
		(new Runnable() {
			public void run() {
				// call service
				downloadHails();
			}
		}, 0, 5, TimeUnit.SECONDS);
		
		mCardScrollView.setAdapter(adapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);
		adapter.notifyDataSetChanged();


	}

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

	}

	/**
	 * This is the AsyncTask to remove the hail that is currently in view
	 * @param tableId A Table ID
	 * @param cardPosition The position of the card in the list of cards
	 */
	public void removeHail(int tableId, int cardPosition)
	{
		for(int i = 0; i < hails.size(); ++i)
		{
			Table e = hails.get(i);
			if(e.getId().intValue() == tableId	)
			{
				hails.remove(i);
				mCards.remove(cardPosition);
				if(hails.size()==0)
				{
					displayNull();
				}
				else
				{
					adapter.notifyDataSetChanged();
				}
				break;
			}
		}
		if(hailBG!=null)
		{
			hailBG.cancel(false);
		}
		hailBG = (BackgroundAsyncTask)new BackgroundAsyncTask().execute(0, tableId);
	}


	/**
	 * This calls the AsyncTask to download hails
	 */
	public void downloadHails()
	{
		if(hailBG!=null)
		{
			hailBG.cancel(false);
		}
		hailBG = (BackgroundAsyncTask)new BackgroundAsyncTask().execute(1);
	}

	/**
	 * Private class to capture gestures
	 * @param context The activity context
	 * @return 
	 */
	private GestureDetector createGestureDetector(Context context)
	{
		GestureDetector gestureDetector = new GestureDetector(context);
		// Create a base listener for generic gestures
		gestureDetector.setBaseListener(new GestureDetector.BaseListener()
		{
			@Override
			public boolean onGesture(Gesture gesture)
			{
				if (gesture == Gesture.TWO_TAP)
				{
					// do something on two finger tap
					AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					audio.playSoundEffect(Sounds.TAP);

					int c = mCardScrollView.getSelectedItemPosition();
					Card card = (Card) adapter.getItem(c);
					String text = (String) card.getText();
					@SuppressWarnings("unused")
					String[] split_result = text.split(":");
					String tableString = split_result[1].trim();
					int tableId = Integer.parseInt(tableString);
					removeHail(tableId, c);

					return true;
				}
				else if (gesture == Gesture.SWIPE_RIGHT)
				{

					return true;
				}
				else if (gesture == Gesture.SWIPE_LEFT)
				{
					// do something on left (backwards) swipe
					return true;
				}
				return false;
			}
		});
		gestureDetector.setFingerListener(new GestureDetector.FingerListener()
		{
			@Override
			public void onFingerCountChanged(int previousCount, int currentCount)
			{
				// do something on finger count changes
			}
		});
		gestureDetector.setScrollListener(new GestureDetector.ScrollListener()
		{
			@Override
			public boolean onScroll(float displacement, float delta,
					float velocity)
			{
				return false;
				// do something on scrolling
			}
		});
		return gestureDetector;
	}

	/*
	 * Send generic motion events to the gesture detector
	 */
	@Override
	public boolean onGenericMotionEvent(MotionEvent event)
	{
		if (mGestureDetector != null)
		{
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}

	/**
	 * Creates an adapter than can store an array of Cards
	 * @author Shrivatsa
	 *
	 */
	private class CustomCardScrollAdapter extends CardScrollAdapter
	{
		@Override
		public int getCount()
		{
			return mCards.size();
		}

		@Override
		public Object getItem(int position)
		{
			return mCards.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			return mCards.get(position).getView();
		}

		@Override
		public int getPosition(Object item) {
			// TODO Auto-generated method stub
			return mCards.indexOf(item);
		}

		public int findIdPosition(Object arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public int findItemPosition(Object arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	/**
	 * This completely refreshes the list of cards
	 * with the ones that have been downloaded.
	 */
	public void updateCards()
	{
		mCards.clear();
		for(Table e: hails)
		{
			Card tempCard = new Card(this);
			tempCard.setText("Table: "+e.getId());
			String status = "No order";
			if(e.getOrderStatus()!=null)
			{
				switch(e.getOrderStatus())
				{
				case 1:
					status = "OK";
					break;
				case 2:
					status = "!Delayed!";
					break;
				case 3:
					status = "!!Problem!!";
					break;
				}
			}
			tempCard.setFootnote("Order Status: "+status+"\t\tHails: "+hails.size());
			mCards.add(tempCard);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * Display a default card
	 */
	public void displayNull()
	{
		Card nullCard = new Card(this);
		nullCard.setText("No Hails");
		mCards.add(nullCard);
		adapter.notifyDataSetChanged();
	}

	/**
	 * The AsyncTask that is used to download the hails for the attached waiter
	 */
	public class BackgroundAsyncTask extends AsyncTask<Integer, Integer, Boolean>{
		@Override
		protected void onPreExecute(){

		}
		@Override
		protected void onPostExecute(Boolean result){
			//Toast.makeText(getApplicationContext(), "Successfully logged in!", Toast.LENGTH_LONG).show();
			if(result)
			{
				if(hails.size() > 0)
				{
					updateCards();
				}
				else
				{
					displayNull();
				}
			}
			else
			{
				Toast.makeText(CardViewActivity.this, "Communication Error!", Toast.LENGTH_LONG).show();
			}
		}
		@Override
		protected Boolean doInBackground(Integer... params) {
			//read from sharedPref
			//getPreferences();
			// TODO Auto-generated method stub
			if(params[0] == GET_HAILS)
			{
				try{
					Socket s = new Socket(SERVER_ADDRESS, 4322);
					ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
					ObjectInputStream in;
					out.flush();
					out.writeObject("Hail||Get_Hail||"+waiterId);
					in = new ObjectInputStream(s.getInputStream());
					hails = (ArrayList<Table>)in.readObject();

					in.close();
					out.close();
					s.close();
				}
				catch(Exception e){
					Log.d("communication",e.getMessage());
					return false;
				}
				return true;
			}
			else if(params[0] == REMOVE_HAIL)
			{
				boolean result = false;
				try{
					Socket s = new Socket(SERVER_ADDRESS, 4322);
					ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
					ObjectInputStream in;
					out.flush();
					out.writeObject("Hail||Remove_Hail||"+waiterId+"||"+params[1]);
					in = new ObjectInputStream(s.getInputStream());
					result = in.readBoolean();
					in.close();
					out.close();
					s.close();
				}
				catch(Exception e){
					Log.d("communication",e.getMessage());
					return false;
				}
				return result;
			}
			return false;
		}

	}



}
