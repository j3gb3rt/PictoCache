package edu.gatech.cs4261.wheresdabeef.camera;

import android.hardware.Camera;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.PictureCallback;

public class CameraManager {
	
	public class CMPicCallback implements PictureCallback{

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
		}
		
	}
	
	public class CMShutterCallback implements ShutterCallback{

		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private Camera camera;

	public void openCamera() {
		camera = Camera.open();
	}
	
	public void takePicture(){
		//camera.takePicture(, raw, jpeg)
	}

}
