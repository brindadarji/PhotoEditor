package com.bosseditor.adapters;

import java.util.ArrayList;

import com.bosseditor.R;
import com.bosseditor.SampleImageSelectionActivity;
import com.bosseditor.types.ImageType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImagesGridAdapter extends BaseAdapter {

	//Global Variables
	private Context context;
	private ArrayList<ImageType> data;
	public ImagesGridAdapter(Context context, ArrayList<ImageType> data)
	{
		this.context = context;
		this.data = data;
	}
	
	@Override
	public int getCount() {
		
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		View row = convertView;
		if(row == null)
		{
			viewHolder = new ViewHolder();
			
			LayoutInflater inflater = ((SampleImageSelectionActivity)context).getLayoutInflater();
			row = inflater.inflate(R.layout.item_image_grid, parent, false);
			
			viewHolder.imageView = (ImageView)row.findViewById(R.id.imageView1);
			
			row.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) row.getTag();
		}
		
		//Get current position data.
		ImageType item = data.get(position);
		
		int imageResorceID = item.getImageResourceID();
		
		viewHolder.imageView.setImageResource(imageResorceID);
		
		return row;
	}
	
	/**
	 * Class to hold reference of item views.
	 */
	private class ViewHolder
	{
		public ImageView imageView;
	}

}
