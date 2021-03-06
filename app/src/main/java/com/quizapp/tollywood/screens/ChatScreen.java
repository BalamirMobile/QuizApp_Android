package com.quizapp.tollywood.screens;

import java.util.ArrayList;
import java.util.Random;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.quizapp.tollywood.AppController;
import com.quizapp.tollywood.R;
import com.quizapp.tollywood.Screen;
import com.quizapp.tollywood.User;
import com.quizapp.tollywood.appcontrollers.ProfileAndChatController;
import com.quizapp.tollywood.chat.ChatViewAdapter;
import com.quizapp.tollywood.chat.Message;
import com.quizapp.tollywood.configuration.Config;
import com.quizapp.tollywood.widgets.GothamTextView;
import com.squareup.picasso.Picasso;

/**
 * ChatScreen is a screen which show chat log and enables chatting
 * 
 * @author Vinay Ainavolu
 *
 */

public class ChatScreen extends Screen {

	ArrayList<Message> messages;
	ChatViewAdapter chatListAdapter;
	ListView chatList;
	EditText text;
	private GothamTextView user1Name;
	private GothamTextView user1Status;
	private GothamTextView user2Name;
	private GothamTextView user2Status;
	private ImageView user1Image;
	private ImageView user2Image;
	private ProfileAndChatController pController;
	private Button sendButton;
	public User otherUser;

	private GothamTextView debugTextView;
	static Random rand = new Random();	
	static String sender;
		
	public ChatScreen(AppController controller , User user2) {
		super(controller);
		this.otherUser = user2;
		this.pController = (ProfileAndChatController) controller;
		View chatLayout = LayoutInflater.from(controller.getContext()).inflate(R.layout.chat_main, this, false);
		chatLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		text = (EditText) chatLayout.findViewById(R.id.text);

		debugTextView =(GothamTextView) chatLayout.findViewById(R.id.empty);
		
		sendButton = (Button) chatLayout.findViewById(R.id.send_button);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!text.getText().toString().trim().equalsIgnoreCase("")){
	 				pController.sendMessage(otherUser, text.getText().toString());
	 				addMessage(true, Config.getCurrentTimeStamp() , text.getText().toString());
 					scrollToBottom();
				}
 				text.setText("");
			}
		});
		
		
		user1Name = (GothamTextView)chatLayout.findViewById(R.id.user_name);
		user1Status = (GothamTextView)chatLayout.findViewById(R.id.user_status_msg);
		user1Image = (ImageView)chatLayout.findViewById(R.id.user1);
		
		user2Name = (GothamTextView)chatLayout.findViewById(R.id.user_name_2);
		user2Status = (GothamTextView)chatLayout.findViewById(R.id.user_status_msg_2);
		user2Image = (ImageView)chatLayout.findViewById(R.id.user2);
		
		
		
//		this.setTitle(sender);
		messages = new ArrayList<Message>();

  
		chatListAdapter = new ChatViewAdapter(controller.getContext(), messages, pController);
		chatList = (ListView) chatLayout.findViewById(R.id.chat_list_view);
		chatList.setAdapter(chatListAdapter);
		chatList.setEmptyView(chatLayout.findViewById(R.id.empty));
//		setListAdapter(chatListAdapter);
		addView(chatLayout);
		showUsers(getApp().getUser(),user2);
		
	}
	
	public void showUsers(User user2 , User user){
		user1Status.setText(user.getStatus());
		user1Name.setText(user.getName());
		if(user.pictureUrl!=null){
			getApp().getUiUtils().loadImageIntoView(getContext(), user1Image, user.pictureUrl, false);
		}
		user2Status.setText(user2.getStatus());
		user2Name.setText(user2.getName());
		if(user2.pictureUrl!=null){
			getApp().getUiUtils().loadImageIntoView(getContext(), user2Image , user2.pictureUrl, false);
		}
	}
	
	@Override
	public void beforeRemove() {
		pController.removeChatListeners();
		super.beforeRemove();
	}
	
	public void addMessage(boolean isCurrentUser , double timestamp ,  String textData){
		addNewMessage(new Message(textData, timestamp, isCurrentUser));
	}
		
	void addNewMessage(Message m){
		messages.add(m);
		chatListAdapter.notifyDataSetChanged();
	}

	public void setDebugMessage(String value) {
		debugTextView.setText(value);
	}
	@Override
	public boolean showMenu() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void refresh() {
		if(chatListAdapter!=null) chatListAdapter.notifyDataSetChanged();
	}

	public void scrollToBottom() {
		if(chatList!=null && chatListAdapter!=null)
			chatList.setSelection(chatListAdapter.getCount() - 1);
	}

	public ScreenType getScreenType(){
		return ScreenType.CHAT_SCREEN;
	}
}