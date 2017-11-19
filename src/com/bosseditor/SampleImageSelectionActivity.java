package com.bosseditor;

import java.util.ArrayList;

import com.bosseditor.adapters.ImagesGridAdapter;
import com.bosseditor.types.ImageType;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.app.Activity;
import android.os.Bundle;

public class SampleImageSelectionActivity extends Activity {

	//Global Variables
	private ArrayList<ImageType> data;
	private ImagesGridAdapter adapter;

	/**
	 * Layout References
	 */
	private GridView gridView;
	private void UIReferences()
	{
		getActionBar().setTitle("Sample GridView");
		//gridView = (GridView)findViewById(R.id.gridView1);
	}

	/**
	 * Layout click events
	 */
	private void UIClickEvents()
	{
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {

				ImageType selectedImageType = (ImageType) adapter.getItem(position);
				Toast.makeText(getApplicationContext(), selectedImageType.getImageName(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_image_selection);

		//Layout references
		UIReferences();
		//Layout click events
		UIClickEvents();

		//Data and adapter binding
		getImagesFromDevice();
		adapter = new ImagesGridAdapter(this, data);
		gridView.setAdapter(adapter);
	}

	/**
	 * Get internal memory images
	 */
	private void getImagesFromDevice()
	{
		//Temporary logic to display test images---------
		//ArrayList initialization
		data = new ArrayList<ImageType>();
		ImageType type = new ImageType();
		type.setImageName("Image1");
		type.setImageResourceID(R.drawable.ic_launcher);
		data.add(type);

		ImageType type1 = new ImageType();
		type1.setImageName("Image2");
		type1.setImageResourceID(R.drawable.ic_launcher);
		data.add(type1);
		//-----------------------------------------------

		//Code to fetch all images from device's memory----(pending) (covered in MainImageSelectionActivity)

	}
}
