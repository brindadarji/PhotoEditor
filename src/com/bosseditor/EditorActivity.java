
package com.bosseditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.xml.transform.sax.TemplatesHandler;

import com.bosseditor.ConvolutionMatrix;
import com.bosseditor.R;
import com.bosseditor.AppDatabase;

import android.R.string;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class EditorActivity extends Activity {

	private int currentAngle = 0;

	private float textX,textY;
	public static final double PI = 3.14159d;
	public static final double FULL_CIRCLE_DEGREE = 360d;
	public static final double HALF_CIRCLE_DEGREE = 180d;
	public static final double RANGE = 256d;
	int PICK_FROM_GALLERY;
	File file,fileCrop;
	String picturePath,fname;

	private Bitmap mainBitmap, previewBitmap, tempPreBitmap, tempMainBitmap,
			TempMain,TempPre;
	private static ImageView viewImage, imageViewPlus;
	private static LinearLayout linearlayoutMain, layOutEffectsMain;
	//crop
	private static LinearLayout layoutCrop;
	// Rotate
	private static LinearLayout layoutRotate;
	// Effects
	private static LinearLayout layoutSubEffects, layoutEffects, layoutBack;
	private static ImageView imgBW, imgHighLight, imgReflection, imgShepia1,
			imgShepia2, imgEmboss, imgEngrave, imgRoundedCorner, imgSketch;
	// Frames
	private static LinearLayout layoutFrames, layoutMainFrames,
			layoutFramesBack;
	private static ImageView imgFrame1, imgFrame2,imgFrame3,imgFrame4,imgFrame5,imgFrame6,imgFrame7;
	// Colors
	private static LinearLayout layoutColors, layoutColorsSub,
			layoutColorsBack, layoutSeekBar;
	private static ImageView imgColorBright, imgColorContrast, imgColorSharpen,
			imgColorTint, imgColorHue, imgColorShading1, imgColorShading2;
	private SeekBar SeekBar;
	private static Button b;
	//Text
	private static LinearLayout layoutText,layoutTextSub,layoutTextBack;
	private static Button btnTextOk,btnTextErase;
	private static EditText editText;
	
	private void UIReferences() {
		
		
		linearlayoutMain = (LinearLayout) findViewById(R.id.LinearLayoutMain);
		imageViewPlus = (ImageView) findViewById(R.id.imageViewPlus);
		layOutEffectsMain = (LinearLayout) findViewById(R.id.layoutEffectsMain);
		//Crop
		layoutCrop=(LinearLayout) findViewById(R.id.layoutCrop);
		// Rotate
		layoutRotate = (LinearLayout) findViewById(R.id.layoutRotate);

		// Effects
		layoutEffects = (LinearLayout) findViewById(R.id.layoutEffects);
		layoutSubEffects = (LinearLayout) findViewById(R.id.layoutSubEffects);
		layoutBack = (LinearLayout) findViewById(R.id.layoutBack);
		imgBW = (ImageView) findViewById(R.id.imgBW);
		imgReflection = (ImageView) findViewById(R.id.imgReflection);
		imgHighLight = (ImageView) findViewById(R.id.imgHighlight);
		imgShepia1 = (ImageView) findViewById(R.id.imgShepia1);
		imgShepia2 = (ImageView) findViewById(R.id.imgShepia2);
		imgEmboss = (ImageView) findViewById(R.id.imgEmboss);
		imgEngrave = (ImageView) findViewById(R.id.imgEngrave);
		imgRoundedCorner = (ImageView) findViewById(R.id.imgRoundedCorner);
		imgSketch = (ImageView) findViewById(R.id.imgSketch);

		// Frames
		layoutFrames = (LinearLayout) findViewById(R.id.layoutFrames);
		layoutMainFrames = (LinearLayout) findViewById(R.id.layoutMainFrames);
		layoutFramesBack = (LinearLayout) findViewById(R.id.layoutframesBack);
		imgFrame1 = (ImageView) findViewById(R.id.imgFrame1);
		imgFrame2 = (ImageView) findViewById(R.id.imgFrame2);
		imgFrame3=(ImageView) findViewById(R.id.imgFrame3);
		imgFrame4=(ImageView) findViewById(R.id.imgFrame4);
		imgFrame5=(ImageView) findViewById(R.id.imgFrame5);
		imgFrame6=(ImageView) findViewById(R.id.imgFrame6);
		imgFrame7=(ImageView) findViewById(R.id.imgFrame7);

		// Colors
		layoutColors = (LinearLayout) findViewById(R.id.layoutColors);
		layoutColorsSub = (LinearLayout) findViewById(R.id.layoutColorsSub);
		layoutColorsBack = (LinearLayout) findViewById(R.id.layoutcolorsBack);
		imgColorBright = (ImageView) findViewById(R.id.imgColorBright);
		imgColorContrast = (ImageView) findViewById(R.id.imgColorContrast);
		imgColorSharpen = (ImageView) findViewById(R.id.imgColorSharpen);
		imgColorTint = (ImageView) findViewById(R.id.imgColorTint);
		imgColorHue = (ImageView) findViewById(R.id.imgColorHue);
		imgColorShading1 = (ImageView) findViewById(R.id.imgColorShading1);
		imgColorShading2 = (ImageView) findViewById(R.id.imgColorShading2);
		SeekBar = (SeekBar) findViewById(R.id.seekBar);
		layoutSeekBar = (LinearLayout) findViewById(R.id.layoutSeekBar);
		
		//Text
		layoutText=(LinearLayout) findViewById(R.id.layoutText);
		layoutTextSub=(LinearLayout) findViewById(R.id.layoutTextSub);
		btnTextOk=(Button) findViewById(R.id.btnTextOk);
		btnTextErase=(Button) findViewById(R.id.btnTextErase);
		editText=(EditText)findViewById(R.id.editText1);
		layoutTextBack=(LinearLayout) findViewById(R.id.layoutTextBack);

	}

	private void fillEffects() {
		// BW preview
		Bitmap BW = doGreyscale(tempPreBitmap);
		imgBW.setImageBitmap(BW);

		Bitmap reflection = doapplyReflection(tempPreBitmap);
		imgReflection.setImageBitmap(reflection);

		Bitmap Highlight = doHighlightImage(tempPreBitmap);
		imgHighLight.setImageBitmap(Highlight);

		Bitmap shepia1 = DocreateSepiaToningEffect(tempPreBitmap, 100, 255, 0,0);
		imgShepia1.setImageBitmap(shepia1);

		Bitmap shepia2 = DocreateSepiaToningEffect(tempPreBitmap, 100, 0, 0,205);
		imgShepia2.setImageBitmap(shepia2);

		Bitmap emboss = doapplyemboss(tempPreBitmap);
		imgEmboss.setImageBitmap(emboss);

		Bitmap engrave = doapplyengrave(tempPreBitmap);
		imgEngrave.setImageBitmap(engrave);

		Bitmap round =doroundCorner(tempPreBitmap,90);
		imgRoundedCorner.setImageBitmap(round);

		// type = 3(Negative), 4(Pencil Sketch), 5(Color Pencil Sketch)
		Bitmap sketch = doapplySketch(tempPreBitmap, 4, 110);
		imgSketch.setImageBitmap(sketch);
	}

	private void fillframes() {
		Bitmap frame1 = doapplyframe1(tempPreBitmap);
		imgFrame1.setImageBitmap(frame1);

		Bitmap frame2 = doapplyframe2(tempPreBitmap);
		imgFrame2.setImageBitmap(frame2);	
		
		Bitmap frame3=doapplyframe3(tempPreBitmap);
		imgFrame3.setImageBitmap(frame3);
		
		Bitmap frame4=doapplyframe4(tempPreBitmap);
		imgFrame4.setImageBitmap(frame4);
		
		Bitmap frame5=doapplyframe5(tempPreBitmap);
		imgFrame5.setImageBitmap(frame5);
		
		Bitmap frame6=doapplyframe6(tempPreBitmap);
		imgFrame6.setImageBitmap(frame6);
		
		Bitmap frame7=doapplyframe7(tempPreBitmap);
		imgFrame7.setImageBitmap(frame7);
	}

	private void fillColors() {
		Bitmap bright = doBrightness(tempPreBitmap, 50);
		imgColorBright.setImageBitmap(bright);

		Bitmap contrast = DocreateContrast(tempPreBitmap, 50);
		imgColorContrast.setImageBitmap(contrast);

		Bitmap sharpen = DoCreatesharpen(tempPreBitmap, 100);
		imgColorSharpen.setImageBitmap(sharpen);

		Bitmap tint = DocreatetintImage(tempPreBitmap, 50);
		imgColorTint.setImageBitmap(tint);

		Bitmap Hue = doapplyHueFilter(tempPreBitmap, 3);
		imgColorHue.setImageBitmap(Hue);

		Bitmap shading1 = DoapplyShadingFilter(tempPreBitmap, Color.MAGENTA);
		imgColorShading1.setImageBitmap(shading1);

		Bitmap shading2 = DoapplyShadingFilter(tempPreBitmap, Color.CYAN);
		imgColorShading2.setImageBitmap(shading2);
	}

	private void UIClickEvents() {
		imageViewPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (layOutEffectsMain.getVisibility() == View.VISIBLE) {
					layOutEffectsMain.setVisibility(View.GONE);
					imageViewPlus
							.setImageResource(R.drawable.ic_content_add_circle);
				} else {
					layOutEffectsMain.setVisibility(View.VISIBLE);
					imageViewPlus
							.setImageResource(R.drawable.ic_content_remove_circle);
				}
			}
		});

		layoutCrop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					TempMain=tempMainBitmap;
					TempPre=tempPreBitmap;
					Uri bmpUri = null;
					BitmapDrawable drawable = (BitmapDrawable) viewImage.getDrawable();
					Bitmap bitmap = drawable.getBitmap();
					 
					 String path = Environment.getExternalStorageDirectory().toString();
					 File myDir=new File(path + "/Set Images");
					 myDir.mkdir();
					 Random generator = new Random();
					 int n = 10000;
					 n = generator.nextInt(n);
					 String fname = "Image-"+ n +".jpg";
					 file = new File (myDir, fname);
					 boolean success = false;

					    // Encode the file as a PNG image.
					    FileOutputStream outStream;
					    try {

					        outStream = new FileOutputStream(file);
					        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
					        /* 100 to keep full quality of the image */

					        outStream.flush();
					        outStream.close();
					        success = true;
					    } catch (FileNotFoundException e) {
					        e.printStackTrace();
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					bmpUri=Uri.fromFile(file);
					cropCapturedImage(bmpUri);
					

				} catch (Exception ex) {
					// display an error message if user device doesn't support
					String errorMessage = "Sorry - your device doesn't support the crop action!";
					Toast.makeText(getApplicationContext(), errorMessage,
							Toast.LENGTH_SHORT).show();

				}
			}
		});
		
		layoutEffects.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				layoutSubEffects.setVisibility(View.VISIBLE);
				linearlayoutMain.setVisibility(View.GONE);
				layoutSeekBar.setVisibility(View.GONE);
				TempMain=tempMainBitmap;
				TempPre=tempPreBitmap;
				fillEffects();

				imgBW.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap BW= doGreyscale(tempMainBitmap);
						Bitmap BW1=doGreyscale(tempPreBitmap);
						viewImage.setImageBitmap(BW);
						TempMain=BW;
						TempPre=BW1;
					}
				});
		
				imgReflection.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap Reflection = doapplyReflection(tempMainBitmap);
						Bitmap Reflection1=doapplyReflection(tempPreBitmap);
						viewImage.setImageBitmap(Reflection);
						TempMain=Reflection;
						TempPre=Reflection1;
					}
				});
				
				
				imgHighLight.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap Highlight = doHighlightImage(tempMainBitmap);
						Bitmap Highlight1=doHighlightImage(tempPreBitmap);
						viewImage.setImageBitmap(Highlight);
						TempMain=Highlight;
						TempPre=Highlight1;
					}
				});
				
				
				imgShepia1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap shepia1 = DocreateSepiaToningEffect(tempMainBitmap, 100,
								255, 0, 0);
						Bitmap shepia2=DocreateSepiaToningEffect(tempPreBitmap, 100,
								255, 0, 0);
						viewImage.setImageBitmap(shepia1);
						TempMain=shepia1;
						TempPre=shepia2;
					}
				});
				
				
				imgShepia2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap shepia1 = DocreateSepiaToningEffect(tempMainBitmap, 100,
								0, 0, 205);
						Bitmap shepia2=DocreateSepiaToningEffect(tempPreBitmap, 100,
								0, 0, 205);
						viewImage.setImageBitmap(shepia1);
						TempMain=shepia1;
						TempPre=shepia2;
					}
				});
				
				imgEmboss.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap emboss1= doapplyemboss(tempMainBitmap);
						Bitmap emboss2=doapplyemboss(tempPreBitmap);
						viewImage.setImageBitmap(emboss1);
						TempMain=emboss1;
						TempPre=emboss2;
					}
				});
				
				imgEngrave.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap engrave1 = doapplyengrave(tempMainBitmap);
						Bitmap engrave2=doapplyengrave(tempPreBitmap);
						viewImage.setImageBitmap(engrave1);
						TempMain=engrave1;
						TempPre=engrave2;
					}
				});
				
				imgRoundedCorner.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap round1 = doroundCorner(tempMainBitmap,90);
						Bitmap round2=doroundCorner(tempPreBitmap,90);
						viewImage.setImageBitmap(round1);
						TempMain=round1;
						TempPre=round2;
					}
				});
				
				imgSketch.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Bitmap sketch1= doapplySketch(tempMainBitmap, 4, 110);
						Bitmap sketch2=doapplySketch(tempPreBitmap, 4, 110);
						viewImage.setImageBitmap(sketch1);
						TempMain=sketch1;
						TempPre=sketch2;
					}
				});
			}
		});
		
		layoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				linearlayoutMain.setVisibility(View.VISIBLE);
				layoutSubEffects.setVisibility(View.GONE);
				tempMainBitmap=TempMain;
				tempPreBitmap=TempPre;
				viewImage.setImageBitmap(tempMainBitmap);
			}
		});

		layoutFrames.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutMainFrames.setVisibility(View.VISIBLE);
				linearlayoutMain.setVisibility(View.GONE);
				fillframes();
				TempMain=tempMainBitmap;
				TempPre=tempPreBitmap;
				layoutSeekBar.setVisibility(View.GONE);
			}
		});
		layoutFramesBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutMainFrames.setVisibility(View.GONE);
				linearlayoutMain.setVisibility(View.VISIBLE);
				tempMainBitmap=TempMain;
				viewImage.setImageBitmap(tempMainBitmap);
				tempPreBitmap=TempPre;
			}
		});

		imgFrame1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bitmap frame1= doapplyframe1(tempMainBitmap);
				Bitmap frame2=doapplyframe1(tempPreBitmap);
				viewImage.setImageBitmap(frame1);
				TempMain=frame1;
				TempPre=frame2;

			}
		});

		imgFrame2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bitmap frame1 = doapplyframe2(tempMainBitmap);
				Bitmap frame2=doapplyframe2(tempPreBitmap);
				viewImage.setImageBitmap(frame1);
				TempMain=frame1;
				TempPre=frame2;
			}
		});
		
		imgFrame3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bitmap frame1 = doapplyframe3(tempMainBitmap);
				Bitmap frame2=doapplyframe3(tempPreBitmap);
				viewImage.setImageBitmap(frame1);
				TempMain=frame1;
				TempPre=frame2;
			}
		});
		
		imgFrame4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap frame1 = doapplyframe4(tempMainBitmap);
				Bitmap frame2=doapplyframe4(tempPreBitmap);
				viewImage.setImageBitmap(frame1);
				TempMain=frame1;
				TempPre=frame2;
			}
		});
		
		imgFrame5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap frame1 = doapplyframe5(tempMainBitmap);
				Bitmap frame2=doapplyframe5(tempPreBitmap);
				viewImage.setImageBitmap(frame1);
				TempMain=frame1;
				TempPre=frame2;
			}
		});
		
		imgFrame6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap frame1 = doapplyframe6(tempMainBitmap);
				Bitmap frame2=doapplyframe6(tempPreBitmap);
				viewImage.setImageBitmap(frame1);
				TempMain=frame1;
				TempPre=frame2;
			}
		});
		
		imgFrame7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap frame1 = doapplyframe7(tempMainBitmap);
				Bitmap frame2=  doapplyframe7(tempPreBitmap);
				viewImage.setImageBitmap(frame1);
				TempMain=frame1;
				TempPre=frame2;
			}
		});

		layoutColors.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutColorsSub.setVisibility(View.VISIBLE);
				linearlayoutMain.setVisibility(View.GONE);
				fillColors();
				TempMain=tempMainBitmap;
				TempPre=tempPreBitmap;
				layoutSeekBar.setVisibility(View.GONE);
			}
		});

		layoutColorsBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutColorsSub.setVisibility(View.GONE);
				linearlayoutMain.setVisibility(View.VISIBLE);
				layoutSeekBar.setVisibility(View.GONE);
				//SeekBar.setVisibility(View.GONE);
				tempMainBitmap=TempMain;
				tempPreBitmap=TempPre;
				viewImage.setImageBitmap(tempMainBitmap);
			}
		});

		imgColorBright.setOnClickListener(new OnClickListener() {
			int progress = 0;
			
			@Override
			public void onClick(View v) {
				layoutSeekBar.setVisibility(View.VISIBLE);
				SeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekbar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekbar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean arg2) {
						Bitmap brightness1 = doBrightness(tempMainBitmap,
								progress);
						Bitmap brightness2=doBrightness(tempPreBitmap, progress);
						viewImage.setImageBitmap(brightness1);
						TempMain=brightness1;
						TempPre=brightness2;
					}
				});
			}
		});

		imgColorContrast.setOnClickListener(new OnClickListener() {
			int progress = 0;

			@Override
			public void onClick(View v) {
				layoutSeekBar.setVisibility(View.VISIBLE);
				SeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						Bitmap contrast1 = DocreateContrast(tempMainBitmap,
								progress);
						Bitmap contrast2=DocreateContrast(tempPreBitmap,progress);
						viewImage.setImageBitmap(contrast1);
						TempMain=contrast1;
						TempPre=contrast2;
					}
				});
			}
		});

		imgColorSharpen.setOnClickListener(new OnClickListener() {
			int progress = 0;

			@Override
			public void onClick(View v) {
				layoutSeekBar.setVisibility(View.VISIBLE);
				SeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						Bitmap sharpen1 = DoCreatesharpen(tempMainBitmap,
								progress);
						Bitmap sharpen2=DoCreatesharpen(tempPreBitmap,progress);
						viewImage.setImageBitmap(sharpen1);
						TempMain=sharpen1;
						TempPre=sharpen2;
					}
				});
			}
		});

		imgColorTint.setOnClickListener(new OnClickListener() {
			int progress = 0;

			@Override
			public void onClick(View v) {
				layoutSeekBar.setVisibility(View.VISIBLE);
				SeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						Bitmap tint1 = DocreatetintImage(tempMainBitmap, progress);
						Bitmap tint2=DocreatetintImage(tempPreBitmap, progress);
						viewImage.setImageBitmap(tint1);
						TempMain=tint1;
						TempPre=tint2;
					}
				});
			}
		});

		imgColorHue.setOnClickListener(new OnClickListener() {
			int progress = 0;

			@Override
			public void onClick(View v) {
				layoutSeekBar.setVisibility(View.VISIBLE);
				SeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						Bitmap color1 = doapplyHueFilter(tempMainBitmap,
								progress);
						Bitmap color2=doapplyHueFilter(tempPreBitmap,progress);
						viewImage.setImageBitmap(color1);
						TempMain=color1;
						TempPre=color2;
					}
				});
			}
		});

		imgColorShading1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutSeekBar.setVisibility(View.GONE);
				Bitmap color1 = DoapplyShadingFilter(tempMainBitmap,
						Color.MAGENTA);
				Bitmap color2=DoapplyShadingFilter(tempPreBitmap,
						Color.MAGENTA);
				viewImage.setImageBitmap(color1);
				TempMain=color1;
				TempPre=color2;
			}
		});

		imgColorShading2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutSeekBar.setVisibility(View.GONE);
				Bitmap color1 = DoapplyShadingFilter(tempMainBitmap, Color.CYAN);
				Bitmap color2=DoapplyShadingFilter(tempPreBitmap, Color.CYAN);
				viewImage.setImageBitmap(color1);
				TempMain=color1;
				TempPre=color2;
			}
		});

		layoutRotate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				layoutSeekBar.setVisibility(View.GONE);
				viewImage.setImageBitmap(tempMainBitmap);
				rotateImageInLeftDirection();
			}
		});
		
		layoutText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(),"First Touch image to write your text", Toast.LENGTH_LONG).show();
				layoutTextSub.setVisibility(View.VISIBLE);
				linearlayoutMain.setVisibility(View.GONE);
				TempMain=tempMainBitmap;
			}
			
		});
		
		layoutTextBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				layoutTextSub.setVisibility(View.GONE);
				linearlayoutMain.setVisibility(View.VISIBLE);
				tempMainBitmap=TempMain;
			}
		});
		
		btnTextErase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				}
		});
	
		viewImage.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				textX=event.getX();
				textY=event.getY();
				return false;
			}
			
		});
		btnTextOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text=editText.getText().toString();
				Canvas canvas = new Canvas(tempMainBitmap);
				Paint paint = new Paint(); 
				paint.setColor(Color.WHITE); 
				paint.setTextSize(30); 
				canvas.drawText(text,textX,textY, paint);
			}
		});
		
		
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		getActionBar().setTitle("");
		b = (Button) findViewById(R.id.btnSelectPhoto);
		viewImage = (ImageView) findViewById(R.id.ivImage);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage();
				UIReferences();
				UIClickEvents();
				
			}
		});
	}

	private void selectImage() {
		final AlertDialog builder = new AlertDialog.Builder(EditorActivity.this).create();
		builder.setTitle("Add Photo!");
		
		 LinearLayout main=new LinearLayout(this);
		 
		 LinearLayout layoutcam    = new LinearLayout(this);
		 ImageView imgcam		   = new ImageView(this);
		 TextView txtcam	       = new TextView(this);
		 
		 LinearLayout layoutgallery      = new LinearLayout(this);
		 ImageView imggallery			 = new ImageView(this);
		 TextView txtgallery             = new TextView(this);
		 
		 LinearLayout layoutcancel       = new LinearLayout(this);
		 ImageView imgcancel			 = new ImageView(this);
		 TextView txtcancel              = new TextView(this);
		
		 
		 main.setOrientation(LinearLayout.VERTICAL);
		 
		 layoutcam.setOrientation(LinearLayout.HORIZONTAL);
		 imgcam.setImageResource(R.drawable.camera);
		 txtcam.setText("Take Photo");
		 layoutcam.addView(imgcam);
		 layoutcam.addView(txtcam);
		 
		 layoutgallery.setOrientation(LinearLayout.HORIZONTAL);
		 imggallery.setImageResource(R.drawable.galllery);
		 txtgallery.setText("Choose from Gallery");
		 layoutgallery.addView(imggallery);
		 layoutgallery.addView(txtgallery);
		 
		 layoutcancel.setOrientation(LinearLayout.HORIZONTAL);
		 imgcancel.setImageResource(R.drawable.cancel);
		 txtcancel.setText("Cancel");
		 layoutcancel.addView(imgcancel);
		 layoutcancel.addView(txtcancel);
		 
		 
		 main.addView(layoutcam);
		 main.addView(layoutgallery);
		 main.addView(layoutcancel);
	
		 builder.setView(main);
		 layoutcam.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				String path = Environment.getExternalStorageDirectory().toString();
				File myDir=new File(path + "/CAMERA");
				myDir.mkdir();
				Random generator = new Random();
				int n = 10000;
				n = generator.nextInt(n);
				fname = "Image-"+ n +".jpg";
				File file = new File (myDir, fname);
				//Toast.makeText(getApplicationContext(), ""+file.getAbsolutePath(), 1).show();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent, 1);
				builder.dismiss();
			}
		});
		 
		layoutgallery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 2);
				builder.dismiss();

			}
		});
		
		layoutcancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		 builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				File file = new File(Environment.getExternalStorageDirectory().toString()+"/CAMERA/" + fname);
				try {
					Bitmap bitmap;
					BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
					//Toast.makeText(getApplicationContext(), file.getAbsolutePath(),Toast.LENGTH_LONG).show();
					//bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
				    //bitmapOptions);
					//Toast.makeText(getApplicationContext(),file.getAbsolutePath(), Toast.LENGTH_LONG).show();
					
					mainBitmap = decodeSampledBitmapFromResource(
							file.getAbsolutePath(), 200, 200);
					previewBitmap = decodeSampledBitmapFromResource(
							file.getAbsolutePath(), 100, 100);
					tempMainBitmap = mainBitmap;
					picturePath=file.getAbsolutePath();
					viewImage.setImageBitmap(tempMainBitmap);
					tempPreBitmap = previewBitmap;
					linearlayoutMain.setVisibility(View.VISIBLE);
					//f.delete();
					OutputStream outFile = null;
					try {
						outFile = new FileOutputStream(file);
						mainBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
						outFile.flush();
						outFile.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else if (requestCode == 2) {

				Uri selectedImage = data.getData();
				String[] filePath = { MediaStore.Images.Media.DATA };
				Cursor c = getContentResolver().query(selectedImage, filePath,
						null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePath[0]);
				picturePath = c.getString(columnIndex);
				c.close();
				Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
				mainBitmap = decodeSampledBitmapFromResource(picturePath, 250,
						250);
				tempMainBitmap = mainBitmap;
				TempMain=mainBitmap;
				viewImage.setImageBitmap(tempMainBitmap);
				previewBitmap = decodeSampledBitmapFromResource(picturePath,
						100, 100);
				tempPreBitmap = previewBitmap;
				Log.w("path of image from gallery......******************.........",
						picturePath + "");

				linearlayoutMain.setVisibility(View.VISIBLE);
			}else if(requestCode==4)
			{
				Bundle extras = data.getExtras();
				// get the cropped bitmap from extras
				Bitmap thePic = extras.getParcelable("data");
				// set image bitmap to image view
				viewImage.setImageBitmap(thePic);
				storeimg(file);
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editor, menu);
		MenuItem itemSave=menu.findItem(R.id.save);
		itemSave.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				 BitmapDrawable drawable = (BitmapDrawable) viewImage.getDrawable();
				 Bitmap bitmap = drawable.getBitmap();
				 
				 String path = Environment.getExternalStorageDirectory().toString();
				 File myDir=new File(path + "/PhotoEditor");
				 myDir.mkdir();
				 Random generator = new Random();
				 int n = 10000;
				 n = generator.nextInt(n);
				 String fname = "Image-"+ n +".jpg";
				 File file = new File (myDir, fname);
				 boolean success = false;

				    // Encode the file as a PNG image.
				    FileOutputStream outStream;
				    try {

				        outStream = new FileOutputStream(file);
				        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
				        /* 100 to keep full quality of the image */

				        outStream.flush();
				        outStream.close();
				        success = true;
				    } catch (FileNotFoundException e) {
				        e.printStackTrace();
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				    
				    if (success) {
				        Toast.makeText(getApplicationContext(), "Image Saved Successfully",
				                Toast.LENGTH_LONG).show();
				    } else {
				        Toast.makeText(getApplicationContext(),
				                "Error during image saving", Toast.LENGTH_LONG).show();
				    }
				    
				    String Mainpath=path + "/PhotoEditor/" + fname;
				    AppDatabase db=new AppDatabase(getApplicationContext());
					db.open();
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
					String currentDateandTime = sdf.format(new Date());
					String values[]=new String []{Mainpath,currentDateandTime};
					db.createAlert(AppDatabase.my_images_table, AppDatabase.new_images_Int,values);
					//Toast.makeText(getApplicationContext(),currentDateandTime, Toast.LENGTH_LONG).show();
					db.close();
				return true;	
			}
			
		});
		MenuItem EditedImages=menu.findItem(R.id.editImages);
		EditedImages.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				Intent intent=new Intent(EditorActivity.this,MainImageSelectionActivity.class);
				startActivity(intent);
				return true;
			}
		});
		
		MenuItem SharedImages=menu.findItem(R.id.SharedImages);
		SharedImages.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				Intent intent=new Intent(EditorActivity.this,SharedImagesActivity.class);
				startActivity(intent);
				return false;
			}
		});
		
		MenuItem share=menu.findItem(R.id.shareImages);
		share.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Uri bmpUri = getLocalBitmapUri(viewImage);
				Intent shareIntent = new Intent();
			    shareIntent.setAction(Intent.ACTION_SEND);
			    shareIntent.setType("image/*");
			    shareIntent.putExtra(Intent.EXTRA_STREAM,bmpUri);
			    startActivity(Intent.createChooser(shareIntent, "Share with"));

				return true;
			}
		});
		
		MenuItem setAs=menu.findItem(R.id.setAs);
		setAs.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {

			                Uri picUri = getLocalBitmapUriSetAs(viewImage);  
			                Intent setAs = new Intent(Intent.ACTION_ATTACH_DATA);
			                setAs.setDataAndType(picUri, "image/*");
			                //setAs.putExtra(Intent.EXTRA_STREAM,picUri);
			                startActivityForResult(Intent.createChooser(setAs, "Set As"), 0);
							return true;          
			}
		});
		return true;
	}

	public Uri getLocalBitmapUri(ImageView imageView) {
		Uri bmpUri = null;
		BitmapDrawable drawable = (BitmapDrawable) viewImage.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		 
		 String path = Environment.getExternalStorageDirectory().toString();
		 File myDir=new File(path + "/Shared Images");
		 myDir.mkdir();
		 Random generator = new Random();
		 int n = 10000;
		 n = generator.nextInt(n);
		 String fname = "Image-"+ n +".jpg";
		 File file = new File (myDir, fname);
		 boolean success = false;

		    // Encode the file as a PNG image.
		    FileOutputStream outStream;
		    try {

		        outStream = new FileOutputStream(file);
		        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
		        /* 100 to keep full quality of the image */

		        outStream.flush();
		        outStream.close();
		        success = true;
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		 bmpUri=Uri.fromFile(file);
		 String Mainpath=path + "/Shared Images/" + fname;
		    AppDatabase db=new AppDatabase(getApplicationContext());
			db.open();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
			String currentDateandTime = sdf.format(new Date());
			String values[]=new String []{Mainpath,currentDateandTime};
			db.createAlert(AppDatabase.my_shared_table, AppDatabase.new_shared_Int, values);
			//Toast.makeText(getApplicationContext(), Mainpath, Toast.LENGTH_LONG).show();
			db.close();
	    return bmpUri;
	}
	
	
	public Uri getLocalBitmapUriSetAs(ImageView imageView) {
		Uri bmpUri = null;
		BitmapDrawable drawable = (BitmapDrawable) viewImage.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		 
		 String path = Environment.getExternalStorageDirectory().toString();
		 File myDir=new File(path + "/Set Images");
		 myDir.mkdir();
		 Random generator = new Random();
		 int n = 10000;
		 n = generator.nextInt(n);
		 String fname = "Image-"+ n +".jpg";
		 File file = new File (myDir, fname);
		 boolean success = false;

		    // Encode the file as a PNG image.
		    FileOutputStream outStream;
		    try {

		        outStream = new FileOutputStream(file);
		        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
		        /* 100 to keep full quality of the image */

		        outStream.flush();
		        outStream.close();
		        success = true;
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		 bmpUri=Uri.fromFile(file);
	    return bmpUri;
	}
	public Bitmap doGreyscale(Bitmap src) {
		// constant factors
		final double GS_RED = 0.299;
		final double GS_GREEN = 0.587;
		final double GS_BLUE = 0.114;
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Bitmap.Config.ARGB_8888);
		// pixel information
		int A, R, G, B;
		int pixel;
		// get image size
		int width = src.getWidth();
		int height = src.getHeight();
		// scan through every single pixel
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get one pixel color
				pixel = src.getPixel(x, y);
				// retrieve color of all channels
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// take conversion up to one single value
				R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		// return final image
		return bmOut;
	}

	public static Bitmap doapplyReflection(Bitmap originalImage) {
		// gap space between original and reflected
		final int reflectionGap = 4;
		// get image size
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// this will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// create a Bitmap with the flip matrix applied to it.
		// we only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		// create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		// create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		// draw in the gap
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		// draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		// create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	public static Bitmap doHighlightImage(Bitmap src) {
		// create new bitmap, which will be painted and becomes result image
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 10,
				src.getHeight() + 10, Bitmap.Config.ARGB_8888);
		// setup canvas for painting
		Canvas canvas = new Canvas(bmOut);
		// setup default color
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		// create a blur paint for capturing alpha
		Paint ptBlur = new Paint();
		ptBlur.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
		int[] offsetXY = new int[2];
		// capture alpha into a bitmap
		Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
		// create a color paint
		Paint ptAlphaColor = new Paint();
		ptAlphaColor.setColor(0xFFFFFFFF);
		// paint color for captured alpha region (bitmap)
		canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
		// free memory
		bmAlpha.recycle();

		// paint the image source
		canvas.drawBitmap(src, 0, 0, null);

		// return out final image
		return bmOut;
	}

	public Bitmap DocreateSepiaToningEffect(Bitmap src, int depth, double red,
			double green, double blue) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// constant grayscale
		final double GS_RED = 0.3;
		final double GS_GREEN = 0.59;
		final double GS_BLUE = 0.11;
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				// get color on each channel
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// apply grayscale sample
				B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
				// apply intensity level for sepid-toning on each channel
				R += (depth * red);
				if (R > 255) {
					R = 255;
				}
				G += (depth * green);
				if (G > 255) {
					G = 255;
				}

				B += (depth * blue);
				if (B > 255) {
					B = 255;
				}

				// set new pixel color to output image
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		return bmOut;

	}

	public static Bitmap doapplyemboss(Bitmap src) {
		double[][] EmbossConfig = new double[][] { { -1, 0, -1 }, { 0, 4, 0 },
				{ -1, 0, -1 } };
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
		convMatrix.applyConfig(EmbossConfig);
		convMatrix.Factor = 1;
		convMatrix.Offset = 127;
		return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}

	public static Bitmap doapplyengrave(Bitmap src) {
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
		convMatrix.setAll(0);
		convMatrix.Matrix[0][0] = -2;
		convMatrix.Matrix[1][1] = 2;
		convMatrix.Factor = 1;
		convMatrix.Offset = 95;
		return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}

	public static Bitmap doroundCorner(Bitmap src, float round) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create bitmap output
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// set canvas for painting
		Canvas canvas = new Canvas(result);
		canvas.drawARGB(0, 0, 0, 0);

		// config paint
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);

		// config rectangle for embedding
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		// draw rect to canvas
		canvas.drawRoundRect(rectF, round, round, paint);

		// create Xfer mode
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// draw source image to canvas
		canvas.drawBitmap(src, rect, rect, paint);

		// return final image
		return result;
	}

	public static final Bitmap doapplySketch(Bitmap src, int type, int threshold) {
		int width = src.getWidth();
		int height = src.getHeight();
		Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());

		int A, R, G, B;
		int sumR, sumG, sumB;
		int[][] pixels = new int[3][3];
		for (int y = 0; y < height - 2; ++y) {
			for (int x = 0; x < width - 2; ++x) {
				// get pixel matrix
				for (int i = 0; i < 3; ++i) {
					for (int j = 0; j < 3; ++j) {
						pixels[i][j] = src.getPixel(x + i, y + j);
					}
				}
				// get alpha of center pixel
				A = Color.alpha(pixels[1][1]);
				// init color sum
				sumR = sumG = sumB = 0;
				sumR = (type * Color.red(pixels[1][1]))
						- Color.red(pixels[0][0]) - Color.red(pixels[0][2])
						- Color.red(pixels[2][0]) - Color.red(pixels[2][2]);
				sumG = (type * Color.green(pixels[1][1]))
						- Color.green(pixels[0][0]) - Color.green(pixels[0][2])
						- Color.green(pixels[2][0]) - Color.green(pixels[2][2]);
				sumB = (type * Color.blue(pixels[1][1]))
						- Color.blue(pixels[0][0]) - Color.blue(pixels[0][2])
						- Color.blue(pixels[2][0]) - Color.blue(pixels[2][2]);
				// get final Red
				R = (int) (sumR + threshold);
				if (R < 0) {
					R = 0;
				} else if (R > 255) {
					R = 255;
				}
				// get final Green
				G = (int) (sumG + threshold);
				if (G < 0) {
					G = 0;
				} else if (G > 255) {
					G = 255;
				}
				// get final Blue
				B = (int) (sumB + threshold);
				if (B < 0) {
					B = 0;
				} else if (B > 255) {
					B = 255;
				}

				result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
			}
		}
		return result;
	}
	
	public void storeimg(File file) {
		viewImage.setDrawingCacheEnabled(true);
		Bitmap bitmap =viewImage.getDrawingCache();
		TempMain=bitmap;
		tempMainBitmap=TempMain;
		
		
		//tempPreBitmap=bitmap;
		// BitmapDrawable btmpDr = (BitmapDrawable) imgv.getDrawable();
		// Bitmap bitmap = btmpDr.getBitmap();

		if (file.exists()) {
			file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
				Toast.makeText(getApplicationContext(), "Saved to your folder",
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {

			}
		}
	}
	private Bitmap doapplyframe1(Bitmap src) {
		Bitmap newBitmap = null;

		int w = src.getWidth();
		int h = src.getHeight();

		Config config = src.getConfig();
		if (config == null) {
			config = Bitmap.Config.ARGB_8888;
		}

		newBitmap = Bitmap.createBitmap(w, h, config);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawColor(Color.rgb(144,238,144));

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		Rect frame = new Rect((int) (w * 0.05), (int) (w * 0.05),
				(int) (w * 0.95), (int) (h * 0.95));
		RectF frameF = new RectF(frame);
		newCanvas.drawRoundRect(frameF, (float) (w * 0.05), (float) (h * 0.05),
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
		newCanvas.drawBitmap(src, 0, 0, paint);
		return newBitmap;
	}

	private Bitmap doapplyframe2(Bitmap src) {
		Bitmap newBitmap = null;

		int w = src.getWidth();
		int h = src.getHeight();

		Config config = src.getConfig();
		if (config == null) {
			config = Bitmap.Config.ARGB_8888;
		}

		newBitmap = Bitmap.createBitmap(w, h, config);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawColor(Color.argb(90,238,232,170 ));

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		Rect frame = new Rect((int) (w * 0.05), (int) (w * 0.05),
				(int) (w * 0.95), (int) (h * 0.95));
		RectF frameF = new RectF(frame);
		newCanvas.drawOval(frameF,paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
		newCanvas.drawBitmap(src, 0, 0, paint);
		return newBitmap;
	}
	
	private Bitmap doapplyframe3(Bitmap src) {
		Bitmap newBitmap = null;

		int w = src.getWidth();
		int h = src.getHeight();

		Config config = src.getConfig();
		if (config == null) {
			config = Bitmap.Config.ARGB_8888;
		}

		newBitmap = Bitmap.createBitmap(w, h, config);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawColor(Color.rgb(0,139,139));

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		Rect frame = new Rect((int) (w * 0.05), (int) (w * 0.05),
				(int) (w * 0.95), (int) (h * 0.95));
		RectF frameF = new RectF(frame);
		newCanvas.drawRoundRect(frameF, (float) (w * 0.05), (float) (h * 0.05),
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
		newCanvas.drawBitmap(src, 0, 0, paint);
		return newBitmap;
	}
	
	private Bitmap doapplyframe4(Bitmap src) {
		Bitmap newBitmap = null;

		int w = src.getWidth();
		int h = src.getHeight();

		Config config = src.getConfig();
		if (config == null) {
			config = Bitmap.Config.ARGB_8888;
		}

		newBitmap = Bitmap.createBitmap(w, h, config);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawColor(Color.rgb(255,69,0));

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		Rect frame = new Rect((int) (w * 0.05), (int) (w * 0.05),
				(int) (w * 0.95), (int) (h * 0.95));
		RectF frameF = new RectF(frame);
		newCanvas.drawRoundRect(frameF, (float) (w * 0.05), (float) (h * 0.05),
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
		newCanvas.drawBitmap(src, 0, 0, paint);
		return newBitmap;
	}
	
	private Bitmap doapplyframe5(Bitmap src) {
		Bitmap newBitmap = null;

		int w = src.getWidth();
		int h = src.getHeight();

		Config config = src.getConfig();
		if (config == null) {
			config = Bitmap.Config.ARGB_8888;
		}

		newBitmap = Bitmap.createBitmap(w, h, config);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawColor(Color.argb(90,0,128,128));

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		Rect frame = new Rect((int) (w * 0.05), (int) (w * 0.05),
				(int) (w * 0.95), (int) (h * 0.95));
		RectF frameF = new RectF(frame);
		newCanvas.drawOval(frameF,paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
		newCanvas.drawBitmap(src, 0, 0, paint);
		return newBitmap;
	}
	
	private Bitmap doapplyframe6(Bitmap src) {
		Bitmap newBitmap = null;

		int w = src.getWidth();
		int h = src.getHeight();

		Config config = src.getConfig();
		if (config == null) {
			config = Bitmap.Config.ARGB_8888;
		}

		newBitmap = Bitmap.createBitmap(w, h, config);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawColor(Color.argb(90,220,20,60));

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		Rect frame = new Rect((int) (w * 0.05), (int) (w * 0.05),
				(int) (w * 0.95), (int) (h * 0.95));
		RectF frameF = new RectF(frame);
		newCanvas.drawOval(frameF,paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
		newCanvas.drawBitmap(src, 0, 0, paint);
		return newBitmap;
	}
	
	private Bitmap doapplyframe7(Bitmap src) {
		Bitmap newBitmap = null;

		int w = src.getWidth();
		int h = src.getHeight();

		Config config = src.getConfig();
		if (config == null) {
			config = Bitmap.Config.ARGB_8888;
		}

		newBitmap = Bitmap.createBitmap(w, h, config);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawColor(Color.rgb(165,42,42));

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		Rect frame = new Rect((int) (w * 0.05), (int) (w * 0.05),
				(int) (w * 0.95), (int) (h * 0.95));
		RectF frameF = new RectF(frame);
		newCanvas.drawRoundRect(frameF, (float) (w * 0.05), (float) (h * 0.05),
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
		newCanvas.drawBitmap(src, 0, 0, paint);
		return newBitmap;
	}

	public static Bitmap doBrightness(Bitmap src, int value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);

				// increase/decrease each channel
				R += value;
				if (R > 255) {
					R = 255;
				} else if (R < 0) {
					R = 0;
				}

				G += value;
				if (G > 255) {
					G = 255;
				} else if (G < 0) {
					G = 0;
				}

				B += value;
				if (B > 255) {
					B = 255;
				} else if (B < 0) {
					B = 0;
				}

				// apply new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public static Bitmap DocreateContrast(Bitmap src, double value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;
		// get contrast value
		double contrast = Math.pow((100 + value) / 100, 2);

		// scan through all pixels
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				// apply filter contrast for every channel R, G, B
				R = Color.red(pixel);
				R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (R < 0) {
					R = 0;
				} else if (R > 255) {
					R = 255;
				}

				G = Color.green(pixel);
				G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (G < 0) {
					G = 0;
				} else if (G > 255) {
					G = 255;
				}

				B = Color.blue(pixel);
				B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if (B < 0) {
					B = 0;
				} else if (B > 255) {
					B = 255;
				}

				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public static Bitmap DoCreatesharpen(Bitmap src, double weight) {
		double[][] SharpConfig = new double[][] { { 0, -2, 0 },
				{ -2, weight, -2 }, { 0, -2, 0 } };
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
		convMatrix.applyConfig(SharpConfig);
		convMatrix.Factor = weight - 8;
		return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}

	public static Bitmap DocreatetintImage(Bitmap src, int degree) {

		int width = src.getWidth();
		int height = src.getHeight();

		int[] pix = new int[width * height];
		src.getPixels(pix, 0, width, 0, 0, width, height);

		int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
		double angle = (PI * (double) degree) / HALF_CIRCLE_DEGREE;

		int S = (int) (RANGE * Math.sin(angle));
		int C = (int) (RANGE * Math.cos(angle));

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				int index = y * width + x;
				int r = (pix[index] >> 16) & 0xff;
				int g = (pix[index] >> 8) & 0xff;
				int b = pix[index] & 0xff;
				RY = (70 * r - 59 * g - 11 * b) / 100;
				GY = (-30 * r + 41 * g - 11 * b) / 100;
				BY = (-30 * r - 59 * g + 89 * b) / 100;
				Y = (30 * r + 59 * g + 11 * b) / 100;
				RYY = (S * BY + C * RY) / 256;
				BYY = (C * BY - S * RY) / 256;
				GYY = (-51 * RYY - 19 * BYY) / 100;
				R = Y + RYY;
				R = (R < 0) ? 0 : ((R > 255) ? 255 : R);
				G = Y + GYY;
				G = (G < 0) ? 0 : ((G > 255) ? 255 : G);
				B = Y + BYY;
				B = (B < 0) ? 0 : ((B > 255) ? 255 : B);
				pix[index] = 0xff000000 | (R << 16) | (G << 8) | B;
			}

		Bitmap outBitmap = Bitmap.createBitmap(width, height, src.getConfig());
		outBitmap.setPixels(pix, 0, width, 0, 0, width, height);

		pix = null;

		return outBitmap;
	}

	public static Bitmap doapplyHueFilter(Bitmap source, int level) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		float[] HSV = new float[3];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);

		int index = 0;
		// iteration through pixels
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// convert to HSV
				Color.colorToHSV(pixels[index], HSV);
				// increase Saturation level
				HSV[0] *= level;
				HSV[0] = (float) Math.max(0.0, Math.min(HSV[0], 360.0));
				// take color back
				pixels[index] |= Color.HSVToColor(HSV);
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	public static Bitmap DoapplyShadingFilter(Bitmap source, int shadingColor) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);

		int index = 0;
		// iteration through pixels
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// AND
				pixels[index] &= shadingColor;
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	private void rotateImageInLeftDirection() {
		if (currentAngle == 0) {
			currentAngle = 270;
		} else if (currentAngle == 270) {
			currentAngle = 180;
		} else if (currentAngle == 180) {
			currentAngle = 90;
		} else if (currentAngle == 90) {
			currentAngle = 0;
		}

		Matrix matrix = new Matrix();
		matrix.postRotate(currentAngle);

		if (tempMainBitmap != null) {
			Bitmap rotated = Bitmap.createBitmap(tempMainBitmap, 0, 0,
					tempMainBitmap.getWidth(), tempMainBitmap.getHeight(),
					matrix, true);
			tempMainBitmap = rotated;
			Bitmap rotated1 = Bitmap.createBitmap(tempPreBitmap, 0, 0,
					tempPreBitmap.getWidth(), tempPreBitmap.getHeight(),
					matrix, true);
			tempPreBitmap = rotated1;
			viewImage.setImageBitmap(rotated);
		}
	}
	
		
	public void cropCapturedImage(Uri picUri) {
		// call the standard crop action intent
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		// indicate image type and Uri of image
		cropIntent.setDataAndType(picUri, "image/*");
		// set crop properties
		cropIntent.putExtra("crop", "true");
		// indicate aspect of desired crop
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		// indicate output X and Y
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		// retrieve data on return
		cropIntent.putExtra("return-data", true);
		// start the activity - we handle returning in onActivityResult
		startActivityForResult(cropIntent, 4);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String path,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

}
