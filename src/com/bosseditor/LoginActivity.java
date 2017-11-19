package com.bosseditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity {

	/**
	 * Layout references
	 */
	private Button buttonSignInFacebook;
	private Button buttonSignInGoogle;
	private Button buttonAsGuest;
	private void UIReferences()
	{
		//buttonSignInFacebook = (Button)findViewById(R.id.buttonSignInFacebook);
		//buttonSignInGoogle = (Button)findViewById(R.id.buttonSignInGoogle);
		buttonAsGuest = (Button)findViewById(R.id.buttonAsGuest);
	}
	
	/**
	 * Layout Click Events
	 */
	private void UIClickEvents()
	{
		//Facebook sign in click event
		/*buttonSignInFacebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getApplicationContext(), "Clicked Facebook sign in", Toast.LENGTH_SHORT).show();
			}
		});*/
		
		//Google sign in click event
		/*buttonSignInGoogle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Toast.makeText(getApplicationContext(), "Clicked Google sign in", Toast.LENGTH_SHORT).show();
			}
		});*/
		
		//Start as guest click event
		buttonAsGuest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				//Toast.makeText(getApplicationContext(), "Clicked Guest", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this,EditorActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//UI References
		UIReferences();
		//UI Click Events
		UIClickEvents();
	}
}
