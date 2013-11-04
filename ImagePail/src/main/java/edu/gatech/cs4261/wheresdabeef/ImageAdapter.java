package edu.gatech.cs4261.wheresdabeef;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 10/10/13.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> images;
    private static ArrayList<Uri> mImageLocations;
    public ImageAdapter(Context c, int i, boolean grid) {
        mContext = c;
        images = new ArrayList<Integer>();
        if (grid) {
            switch (i)
            {
                case 1:
                    images.add(0);
                    images.add(1);
                    images.add(2);
                    images.add(3);
                    images.add(4);
                    images.add(5);
                    images.add(7);
                    images.add(8);
                    images.add(9);
                    images.add(10);
                    break;
                case 2:
                    images.add(6);
                    images.add(7);
                    images.add(8);
                    images.add(9);
                    images.add(10);
                    images.add(11);
                    break;
                case 3:
                    images.add(1);
                    images.add(3);
                    images.add(5);
                    images.add(7);
                    images.add(9);
                    images.add(11);
                    break;
                case 4:
                    images.add(0);
                    images.add(2);
                    images.add(4);
                    images.add(6);
                    images.add(8);
                    images.add(10);
                    break;
                case 5:
                    images.add(6);
                    images.add(0);
                    images.add(7);
                    images.add(1);
                    images.add(8);
                    images.add(2);
                    break;
                case 6:
                    images.add(3);
                    images.add(9);
                    images.add(4);
                    images.add(10);
                    images.add(5);
                    images.add(11);
                    break;
                default:
                    images.add(1);
            }
        }
        else{
            if (i < 12)
                images.add(i);
        }
        //for(int i = 0; i < rand; i++)
        //{
        // dog[i] = mThumbIds[(int) (8 * Math.random())];
        //}
    }

    public ImageAdapter(Context c,ArrayList<Uri> imageLocations) {
        mContext = c;
        mImageLocations = new ArrayList<Uri>();
        if (imageLocations != null) {
            mImageLocations = imageLocations;
        }
    }

    public int getCount() {
        return images.size();
    }

    public Uri getImageUri(int position) {
        return mImageLocations.get(position);
    }


    public int getImageId(int position) {
        return mThumbIds[images.get(position)];
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmap(Uri imageLocation, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageLocation.getPath(), options);

        //ExifInterface exif = null;

        //try {
            //exif = new ExifInterface(imageLocation.getPath());

        //} catch (IOException e) {
            //e.printStackTrace();
        //}
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageLocation.getPath(), options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = new SquareImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (SquareImageView) convertView;
        }
        if (position < images.size())
        {
            imageView.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(),mThumbIds[images.get(position)], 100, 100));
        }
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.i1, R.drawable.i2,
            R.drawable.i3, R.drawable.i4,
            R.drawable.i5, R.drawable.i6,
            R.drawable.i7, R.drawable.i8,
            R.drawable.i9, R.drawable.i10,
            R.drawable.i11, R.drawable.i12
    };

    private class SquareImageView extends ImageView {
        public SquareImageView(Context context) {
            super(context);
        }

        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
        }
    }
}