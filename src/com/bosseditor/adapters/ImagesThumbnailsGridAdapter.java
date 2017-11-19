package com.bosseditor.adapters;

import java.io.File;
import java.util.ArrayList;

import com.bosseditor.MainImageSelectionActivity;
import com.bosseditor.R;
import com.bosseditor.adapters.*;
import com.bosseditor.types.ImageType;
import com.squareup.picasso.Picasso;

import android.R.string;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagesThumbnailsGridAdapter extends BaseAdapter {

	Context context;
	String []imgs;
	private Cursor cursor;
	LayoutInflater inflate;
	
	static class ViewData{
		TextView text;
		ImageView imgs;
	}

	public ImagesThumbnailsGridAdapter(Context applicationContext,Cursor c,String[] img) {
		this.context=applicationContext;
		this.cursor=c;
		this.imgs=img;
		inflate=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgs.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		/*ViewData views;
		if(convertView==null){
			convertView=inflate.inflate(R.layout.simple_list_item, null);
			views=new ViewData();
			views.imgs=(ImageView) convertView.findViewById(R.id.imageView1);
			convertView.setTag(views);
		}
		else{
			views=(ViewData) convertView.getTag();
		}
		//
		//views.imgs.setImageResource(R.drawable.ic_action_image_crop);
		return convertView;*/
		
		ViewHolder viewHolder = null;
		View row = convertView;
		if(row == null)
		{
			viewHolder = new ViewHolder();
			row =inflate.inflate(R.layout.item_image_grid, parent, false);
			
			viewHolder.imgs = (ImageView)row.findViewById(R.id.imageView1);
			row.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) row.getTag();
		}
		//cursor.moveToPosition(position);

		//Bitmap myBitmap=BitmapFactory.decodeFile(imgs[position]);
		//viewHolder.imgs.setImageBitmap(myBitmap1);
		
	  Picasso.with(context).load(new File(imgs[position])).centerCrop().resize(100, 100).into(viewHolder.imgs);
      return row;
	}
	
	/**
	 * Class to hold reference of item views.
	 */
	private class ViewHolder
	{
		public ImageView imgs;
	}
}
