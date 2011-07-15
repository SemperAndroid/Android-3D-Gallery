package net.robotmedia.android.coverflow;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class HorizontalCoverFlowGallery extends CoverFlowGallery
{
	public HorizontalCoverFlowGallery(Context context)
	{
		super(context);
		setMaxRotationAngle(60);
		setMaxZoom(-120);
		setSpacing(-50);
		setAnimationDuration(800);
	}

	public HorizontalCoverFlowGallery(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
		setMaxRotationAngle(60);
		setMaxZoom(-120);
		setSpacing(-50);
		setAnimationDuration(800);
	}

	public HorizontalCoverFlowGallery(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
		setMaxRotationAngle(60);
		setMaxZoom(-120);
		setSpacing(-50);
		setAnimationDuration(800);
	}

	@Override
	protected int getHorizontalCenterOfCoverflow()
	{
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	@Override
	protected int getVerticalCenterOfCoverflow()
	{
		return (getHeight() - getPaddingBottom() - getPaddingTop()) / 2
		        + getPaddingBottom();
	}

	@Override
	protected int getHorizontalCenterOfView(View view)
	{
		return view.getLeft() + view.getWidth() / 2;
	}

	@Override
	protected int getVerticalCenterOfView(View view)
	{
		return view.getBottom() + view.getHeight() / 2;
	}

	@Override
	protected void performChildStaticTransformation(View child, Transformation t)
	{
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		final int childCenter = getHorizontalCenterOfView(child);
		final int childWidth = child.getWidth();
		int rotationAngle = 0;

		if (childCenter == mCoverFlowHorizontalCenter)
		{
			transformChild((ImageView) child, t, 0);
		}
		else
		{
			rotationAngle = (int) (((float) (mCoverFlowHorizontalCenter - childCenter) / childWidth) * mMaxRotationAngle);
			if (Math.abs(rotationAngle) > mMaxRotationAngle)
			{
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
				        : mMaxRotationAngle;
			}
			transformChild((ImageView) child, t, rotationAngle);
		}
	}

	private void transformChild(ImageView child, Transformation t, int rotationAngle)
	{
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();

		final int imageHeight = child.getLayoutParams().height;

		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);

		mCamera.translate(0.0f, 0.0f, 100.0f);

		// As the angle of the view gets less, zoom in
		if (rotation < mMaxRotationAngle)
		{
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
