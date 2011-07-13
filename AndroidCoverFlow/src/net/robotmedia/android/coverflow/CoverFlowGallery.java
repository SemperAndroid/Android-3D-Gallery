package net.robotmedia.android.coverflow;

import net.robotmedia.utils.DataCache;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class CoverFlowGallery extends Gallery {

	public static final int DEFAULT_STRATEGY = 0;
	public static final int HONEYCOMB_STRATEGY = 1;
	
	public abstract static class ImageAdapter extends
			ArrayAdapter<BitmapDrawable> {

		protected static final int CACHE_SIZE = 30;

		protected Context mContext;
		protected DataCache<Integer, ImageView> mDataCache;

		public ImageAdapter(Context context) {
			super(context, 0x0);
			mContext = context;
			mDataCache = new DataCache<Integer, ImageView>(CACHE_SIZE);
		}

		public ImageAdapter(Context context, int cacheSize) {
			super(context, 0x0);
			mContext = context;
			mDataCache = new DataCache<Integer, ImageView>(cacheSize);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return getImage(position);
		}	

		protected ImageView getImage(int position) {
			ImageView i = mDataCache.get(Integer.valueOf(position));
			if (i != null) {
				return i;
			}
			i = createImage(position);
			mDataCache.put(Integer.valueOf(position), i);
			return i;
		}

		protected ImageView createImage(int position) {

			final int reflectionGap = 4;

			Bitmap originalImage = getItem(position).getBitmap();
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			// This will not scale but will flip on the Y axis
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			// Create a Bitmap with the flip matrix applied to it.
			// We only want the bottom half of the image
			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
					height / 2, width, height / 2, matrix, false);

			// Create a new bitmap with same width but taller to fit reflection
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 2), Config.ARGB_8888);

			// Create a new Canvas with the bitmap that's big enough for
			// the image plus gap plus reflection
			Canvas canvas = new Canvas(bitmapWithReflection);
			// Draw in the original image
			canvas.drawBitmap(originalImage, 0, 0, null);
			// Draw in the gap
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,
					deafaultPaint);
			// Draw in the reflection
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			// Create a shader that is a linear gradient that covers the
			// reflection
			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0,
					originalImage.getHeight(), 0,
					bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			// Set the paint to use this shader (linear gradient)
			paint.setShader(shader);
			// Set the Transfer mode to be porter duff and destination in
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			// Draw a rectangle using the paint with our linear gradient
			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			ImageView imageView = new ImageView(mContext);	
			imageView.setImageBitmap(bitmapWithReflection);
			imageView.setLayoutParams(new CoverFlowGallery.LayoutParams(
					getThumbnailWidth(), (int) Math
							.round(getThumbnailHeight() * 1.5)));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

			return imageView;
		}

		protected abstract int getThumbnailWidth();

		protected abstract int getThumbnailHeight();
	}

	public interface RenderStrategy {
		void displayChild(ImageView child, Transformation t);
	}

	public class HoneyCombStrategy implements RenderStrategy {

		public void displayChild(ImageView child, Transformation t) {
			mCamera.save();
			final Matrix imageMatrix = t.getMatrix();
			
			final int imageHeight = child.getLayoutParams().height;
			
			final int imageWidth = child.getLayoutParams().width;

			float imgXCenter = (float) getCenterOfView(child);
			float imgYCenter = (float) getCenterYOfView(child);

			float modifierFactor = imgXCenter / getWidth();

			// Dynamic Y position
			float yModifier = (float) (imgYCenter / getHeight())
					* modifierFactor;
			float yPos = imgYCenter * yModifier * yModifier * yModifier
					* yModifier;
			mCamera.translate(0.0f, yPos, 0.0f);

			// Dynamic Zoom
			float zoomAmount = mMaxZoom * modifierFactor * 5.5f;
			mCamera.translate(0.0f, 0.0f, -zoomAmount);

			// Dynamic Rotation
			float rot = (1.0f - ((float) (imgXCenter) / (getWidth())))
					* mMaxRotationAngle;
			mCamera.rotateY(rot);

			mCamera.getMatrix(imageMatrix);
			imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
			imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
			mCamera.restore();
		}
	}

	public class DefaultStrategy implements RenderStrategy {
		public void displayChild(ImageView child, Transformation t) {

			final int childCenter = getCenterOfView(child);
			final int childWidth = child.getWidth();
			int rotationAngle = 0;

			if (childCenter == mCoveflowCenter) {
				transformImageBitmap((ImageView) child, t, 0);
			} else {
				rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
				if (Math.abs(rotationAngle) > mMaxRotationAngle) {
					rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
							: mMaxRotationAngle;
				}
				transformImageBitmap((ImageView) child, t, rotationAngle);
			}
		}

		private void transformImageBitmap(ImageView child, Transformation t,
				int rotationAngle) {
			mCamera.save();
			final Matrix imageMatrix = t.getMatrix();
			;
			final int imageHeight = child.getLayoutParams().height;
			;
			final int imageWidth = child.getLayoutParams().width;
			final int rotation = Math.abs(rotationAngle);

			mCamera.translate(0.0f, 0.0f, 100.0f);

			// As the angle of the view gets less, zoom in
			if (rotation < mMaxRotationAngle) {
				float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
				mCamera.translate(0.0f, 0.0f, zoomAmount);
			}

			mCamera.rotateY(rotationAngle);
			mCamera.getMatrix(imageMatrix);
			imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
			imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
			mCamera.restore();
		}

	}

	public void setStrategy(int strategyId) {
		RenderStrategy strategy = null;

		switch (strategyId) {
		case DEFAULT_STRATEGY:
			strategy = new DefaultStrategy();
			break;

		case HONEYCOMB_STRATEGY:
			strategy = new HoneyCombStrategy();
			break;

		default:
			strategy = new DefaultStrategy();
			break;
		}

		mRenderStrategy = strategy;
	}

	protected RenderStrategy mRenderStrategy;

	/**
	 * Graphics Camera used for transforming the matrix of ImageViews
	 */
	protected Camera mCamera = new Camera();

	/**
	 * The maximum angle the Child ImageView will be rotated by
	 */
	protected int mMaxRotationAngle = -45;

	/**
	 * The maximum zoom on the centre Child
	 */
	protected int mMaxZoom = -120;

	/**
	 * The Centre of the Coverflow
	 */
	protected int mCoveflowCenter;

	public CoverFlowGallery(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlowGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlowGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	/**
	 * Get the max rotational angle of the image
	 * 
	 * @return the mMaxRotationAngle
	 */
	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	/**
	 * Set the max rotational angle of each image
	 * 
	 * @param maxRotationAngle
	 *            the mMaxRotationAngle to set
	 */
	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	/**
	 * Get the Max zoom of the centre image
	 * 
	 * @return the mMaxZoom
	 */
	public int getMaxZoom() {
		return mMaxZoom;
	}

	/**
	 * Set the max zoom of the centre image
	 * 
	 * @param maxZoom
	 *            the mMaxZoom to set
	 */
	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}

	/**
	 * Get the Centre of the Coverflow
	 * 
	 * @return The centre of this Coverflow.
	 */
	protected int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	/**
	 * Get the Centre of the View
	 * 
	 * @return The centre of the given view.
	 */
	protected static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	protected static int getCenterYOfView(View view) {
		return view.getBottom() + view.getHeight() / 2;
	}

	/**
	 * This is called during layout when the size of this view has changed. If
	 * you were just added to the view hierarchy, you're called with the old
	 * values of 0.
	 * 
	 * @param w
	 *            Current width of this view.
	 * @param h
	 *            Current height of this view.
	 * @param oldw
	 *            Old width of this view.
	 * @param oldh
	 *            Old height of this view.
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see #setStaticTransformationsEnabled(boolean)
	 */
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		mRenderStrategy.displayChild((ImageView) child, t);
		return true;
	}
}
