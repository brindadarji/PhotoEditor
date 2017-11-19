package com.bosseditor;

import java.io.File;
import java.util.Random;

import com.bosseditor.adapters.ImagesThumbnailsGridAdapter;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

public class MainImageSelectionActivity extends Activity {

	// Global Variables
	final Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

	private ImagesThumbnailsGridAdapter adapter;
	ListView lst;
	GridView gridview1;
	String[] imgs;
	String[] dates;
	int[] srno;

	/**
	 * Layout References
	 */
	private void UIReferences() {
		getActionBar().setTitle("Edited Images");
		gridview1 = (GridView) findViewById(R.id.gridView1);
	}

	/**
	 * Layout click events
	 */
	private void UIClickEvents() {
		gridview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					final int position, long id) {
				
				final CharSequence[] options = { "View", "Delete", "Cancel"};

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainImageSelectionActivity.this);
				builder.setTitle("Choose");
				builder.setItems(options,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								if (options[item].equals("View")) {
									final Bitmap thumbBitmap = BitmapFactory
											.decodeFile(imgs[position]);
									AlertDialog.Builder thumbDialog = new AlertDialog.Builder(
											MainImageSelectionActivity.this);
									thumbDialog.setTitle("Date-Time:  " +dates[position]);
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
											MainImageSelectionActivity.this);
									thumbView.setImageBitmap(thumbBitmap);
									thumbDialog.setView(thumbView);
									AlertDialog alert = thumbDialog.create();
									alert.show();
								} else if (options[item].equals("Delete")) {
									    AppDatabase db=new AppDatabase(getApplicationContext());
										db.open();
										db.deleteAlert(AppDatabase.my_images_table,"sr_no=" +srno[position]);
										Toast.makeText(getApplicationContext(),"Deleted", Toast.LENGTH_LONG).show();
										db.close();
										allImages();

								} else if (options[item].equals("Cancel")) {
									dialog.dismiss();
								}
							}
						});
				builder.show();
			}
		});
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_image_selection);

		// Layout references
		UIReferences();
		// Layout click events
		UIClickEvents();

		allImages();
		
	}
	
	
	public void allImages()
	{
		AppDatabase db = new AppDatabase(getApplicationContext());
		db.open();

		Cursor cursor = db.fetchAllAlerts(AppDatabase.my_images_table,
				AppDatabase.new_images_Int);

		if (cursor != null) {
			// Toast.makeText(getApplicationContext(), cursor.getCount()+"hhhh",
			// 1).show();
			if (cursor.getCount() > 0) {
				imgs = new String[cursor.getCount()];
				srno=new int[cursor.getCount()];
				dates=new String[cursor.getCount()];
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					srno[i]=cursor.getInt(0);
					imgs[i] = cursor.getString(1);
					dates[i]=cursor.getString(2);
					cursor.moveToNext();
				}

				ImagesThumbnailsGridAdapter adapter = new ImagesThumbnailsGridAdapter(
						getApplicationContext(), cursor,imgs);
				gridview1.setAdapter(adapter);

			} else {
				// Toast.makeText(getApplicationContext(), "abc", 1).show();
			}
			cursor.close();
		} else {
			// Toast.makeText(getApplicationContext(), "Hellooo", 1).show();
		}
		db.close();
	}

	/**
	 * Get internal memory thumbnail images
	 */
	private void getAndDisplayImagesFromDevice() {
		// Code to fetch all thumbnail images from device's memory----

	}

	/**
	 * Get Original ImagePath from thumbID
	 * 
	 * @param reterievedImageId
	 * @return
	 */
	/*
	 * String getOriginalImagePathFromThumbID(int reterievedImageId) { String
	 * imagePath=null; if(reterievedImageId!=-1) { Log.i("ImageID",
	 * "imageId-"+reterievedImageId); String[]
	 * columnsReturn={MediaStore.Images.Media.DATA}; String
	 * whereimageId=MediaStore.Images.Media._ID+" LIKE ?"; String
	 * valuesIs[]={"%"+reterievedImageId}; Cursor mCursor =
	 * getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	 * columnsReturn, whereimageId, valuesIs, null); int rawDataPath=
	 * mCursor.getColumnIndex(MediaStore.Images.Media.DATA); for
	 * (mCursor.moveToFirst();!mCursor.isAfterLast(); mCursor.moveToNext()) {
	 * imagePath=mCursor.getString(rawDataPath); }
	 * 
	 * Bitmap thumbBitmap = BitmapFactory.decodeFile(imagePath);
	 * 
	 * //Create a Dialog to display the thumbnail AlertDialog.Builder
	 * thumbDialog = new AlertDialog.Builder(MainImageSelectionActivity.this);
	 * ImageView thumbView = new ImageView(MainImageSelectionActivity.this);
	 * thumbView.setImageBitmap(thumbBitmap); LinearLayout layout = new
	 * LinearLayout(MainImageSelectionActivity.this);
	 * layout.setOrientation(LinearLayout.VERTICAL); layout.addView(thumbView);
	 * thumbDialog.setView(layout); thumbDialog.show(); }
	 * 
	 * return imagePath; }
	 */
}
