package edu.gatech.cs4261.wheresdabeef.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.PictureCallback;
import android.location.Location;

import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.location.LocationApi;

public class CameraManager {

    Bitmap image;
    Location location;

	public class CMPicCallback implements PictureCallback{

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
            image = BitmapFactory.decodeByteArray(data , 0, data .length);
        }
		
	}
	
	public class CMShutterCallback implements ShutterCallback{

		@Override
		public void onShutter() {
			location = LocationApi.stopPollingLocation();
		}
		
	}
	
	private Camera camera;

    public CameraManager(){

    }

	public void openCamera(Context context){
        LocationApi.startPollingLocation(context);
		camera = Camera.open();
        try{
            camera.startPreview();
        }catch(NullPointerException npe){
            // do nothing here
        }
	}
	
	public Image takePicture(){
		camera.takePicture(new CMShutterCallback(), null, null, new CMPicCallback());
        camera.release();
        Image im = new Image(image,location);
        return im;
	}

}
