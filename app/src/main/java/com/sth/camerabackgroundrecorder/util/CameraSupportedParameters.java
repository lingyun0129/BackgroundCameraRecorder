package com.sth.camerabackgroundrecorder.util;

import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;

public class CameraSupportedParameters {
	private List<String> antibandings;// mCamera.getParameters().getSupportedAntibanding();
	private List<String> colorEffects;// mCamera.getParameters().getSupportedColorEffects();
	private List<String> flashModes;// mCamera.getParameters().getSupportedFlashModes();
	private List<String> focusModes;// mCamera.getParameters().getSupportedFocusModes();
	private List<Size> thumbnailSizes;// mCamera.getParameters().getSupportedJpegThumbnailSizes();
	private List<Integer> pictureFormats;// mCamera.getParameters().getSupportedPictureFormats();
	private List<Integer> previewFormats;// mCamera.getParameters().getSupportedPreviewFormats();
	private List<Size> pictureSizes;// mCamera.getParameters().getSupportedPictureSizes();
	private List<int[]> previewFpsRange;// mCamera.getParameters().getSupportedPreviewFpsRange();
	private List<Size> previewSizes;// mCamera.getParameters().getSupportedPreviewSizes();
	private List<String> sceneModes;// mCamera.getParameters().getSupportedSceneModes();
	private List<Size> videoSizes;// mCamera.getParameters().getSupportedVideoSizes();
	private List<String> whiteBalance;// mCamera.getParameters().getSupportedWhiteBalance();
	private List<Integer> zoomRatios;// =parameters.getZoomRatios();
	private List<Integer> previewFrameRates;// =parameters.getZoomRatios();

	public List<Integer> getPreviewFrameRates() {
		return previewFrameRates;
	}

	public void setPreviewFrameRates(List<Integer> previewFrameRates) {
		this.previewFrameRates = previewFrameRates;
	}

	public List<String> getAntibandings() {
		return antibandings;
	}

	public List<String> getColorEffects() {
		return colorEffects;
	}

	public List<String> getFlashModes() {
		return flashModes;
	}

	public List<String> getFocusModes() {
		return focusModes;
	}

	public List<Size> getThumbnailSizes() {
		return thumbnailSizes;
	}

	public List<Integer> getPictureFormats() {
		return pictureFormats;
	}

	public List<Integer> getPreviewFormats() {
		return previewFormats;
	}

	public List<Size> getPictureSizes() {
		return pictureSizes;
	}

	public List<int[]> getPreviewFpsRange() {
		return previewFpsRange;
	}

	public List<Size> getPreviewSizes() {
		return previewSizes;
	}

	public List<String> getSceneModes() {
		return sceneModes;
	}

	public List<Size> getVideoSizes() {
		return videoSizes;
	}

	public List<String> getWhiteBalance() {
		return whiteBalance;
	}

	public static CameraSupportedParameters getInstance() {
		return instance;
	}

	public static CameraSupportedParameters instance = null;

	public static void init(Camera mCamera) {
		instance = new CameraSupportedParameters(mCamera);
	}

	private CameraSupportedParameters(Camera mCamera) {
		try {
			antibandings = mCamera.getParameters().getSupportedAntibanding();
			colorEffects = mCamera.getParameters().getSupportedColorEffects();
			flashModes = mCamera.getParameters().getSupportedFlashModes();
			focusModes = mCamera.getParameters().getSupportedFocusModes();
			thumbnailSizes = mCamera.getParameters().getSupportedJpegThumbnailSizes();
			pictureFormats = mCamera.getParameters().getSupportedPictureFormats();
			previewFormats = mCamera.getParameters().getSupportedPreviewFormats();
			pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
			previewFpsRange = mCamera.getParameters().getSupportedPreviewFpsRange();
			previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
			sceneModes = mCamera.getParameters().getSupportedSceneModes();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			videoSizes = mCamera.getParameters().getSupportedVideoSizes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		whiteBalance = mCamera.getParameters().getSupportedWhiteBalance();
		zoomRatios = mCamera.getParameters().getZoomRatios();
//		  = mCamera.getParameters().getSupportedPreviewFpsRange();
//		previewFrameRates=mCamera.getParameters().get

	}

	public List<Integer> getZoomRatios() {
		return zoomRatios;
	}

}
