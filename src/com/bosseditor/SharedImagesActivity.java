package com.bosseditor;

import com.bosseditor.adapters.ImagesThumbnailsGridAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class SharedImagesActivity extends Activity {

	private ImagesThumbnailsGridAdapter adapter;
	ListView lst;
	GridView gridview1;
	String[] imgs;
	String[] dates;
	int[] srno;
	
	private void UIReferences()
	{
		getActionBar().setTitle("Shared Images");
		gridview1=(GridView) findViewById(R.id.gridView1);
	}
	
	private void UIClickEvents()
	{
		gridview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, final int position,
					long id) {
				
				final CharSequence[] options = { "View", "Delete", "Cancel" };

				AlertDialog.Builder builder = new AlertDialog.Builder(
						SharedImagesActivity.this);
				builder.setTitle("Choose");
				
				builder.setItems(options,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								if (options[item].equals("View")) {
									final Bitmap thumbBitmap = BitmapFactory
											.decodeFile(imgs[position]);
									AlertDialog.Builder thumbDialog = new AlertDialog.Builder(
											SharedImagesActivity.this);
									thumbDialog.setTitle("Date-Time:  "+dates[position]);
									thumbDialog
											.setPositiveButton(
													"Cancel",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.dismiss();
														}
													});
									ImageView thumbView = new ImageView(
											SharedImagesActivity.this);
									thumbView.setImageBitmap(thumbBitmap);
									thumbDialog.setView(thumbView);
									AlertDialog alert = thumbDialog.create();
									alert.show();
								} else if (options[item].equals("Delete")) {
									    AppDatabase db=new AppDatabase(getApplicationContext());
										db.open();
										db.deleteAlert(AppDatabase.my_shared_table,"sr_no=" +srno[position]);
										Toast.makeText(getApplicationContext(),"Deleted", Toast.LENGTH_LONG).show();
										db.close();
										allImages();
								} else if (options[item].equals("Cancel")) {
									dialog.dismiss();
								}
							}
						});
				builder.show();
				
				/*final Bitmap thumbBitmap = BitmapFactory.decodeFile(imgs[position]);
				//Create a Dialog to display the thumbnail
				AlertDialog.Builder thumbDialog = new AlertDialog.Builder(SharedImagesActivity.this);
				thumbDialog.setTitle("Image");
				thumbDialog.setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                            	dialog.dismiss();
                            }
                        });
				ImageView thumbView = new ImageView(SharedImagesActivity.this);
				thumbView.setImageBitmap(thumbBitmap);
				thumbDialog.setView(thumbView);
				AlertDialog alert=thumbDialog.create();
				alert.show();*/
			}
		});
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared_images);
		//Layout references
		UIReferences();
		//Layout click events
		UIClickEvents();
		allImages();
		
	}
	
	public void allImages()
	{
		AppDatabase db=new AppDatabase(getApplicationContext());
		db.open();
		
		Cursor cursor=db.fetchAllAlerts(AppDatabase.my_shared_table, AppDatabase.new_shared_Int);
		
		if(cursor!=null){
			//Toast.makeText(getApplicationContext(), cursor.getCount()+"hhhh", 1).show();
			if(cursor.getCount()>0){
				imgs=new String[cursor.getCount()];
				srno=new int[cursor.getCount()];
				dates=new String[cursor.getCount()];
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					srno[i]=cursor.getInt(0);
					imgs[i]=cursor.getString(1);
					dates[i]=cursor.getString(2);
					cursor.moveToNext();
				}
				
				ImagesThumbnailsGridAdapter adapter=new ImagesThumbnailsGridAdapter(getApplicationContext(),cursor,imgs);
				gridview1.setAdapter(adapter);

			}else{
				//Toast.makeText(getApplicationContext(), "abc", 1).show();
			}
			cursor.close();
		}else{
			//Toast.makeText(getApplicationContext(), "Hellooo", 1).show();
		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shared_images, menu);
		return true;
	}

}
