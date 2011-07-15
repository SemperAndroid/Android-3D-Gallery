package net.robotmedia.android.coverflow;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;

public class CircularCoverFlowGallery extends CoverFlowGallery
{
	public CircularCoverFlowGallery(Context context)
	{
		super(context);
		setMaxRotationAngle(-45);
		setMaxZoom(-120);
		setSpacing(-50); 
		setAnimationDuration(800);
	}

	public CircularCoverFlowGallery(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
		setMaxRotationAngle(-45);
		setMaxZoom(-120);
		setSpacing(-50); 
		setAnimationDuration(800);
	}

	public CircularCoverFlowGallery(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
		setMaxRotationAngle(-45);
		setMaxZoom(-120);
		setSpacing(-50); 
		setAnimationDuration(800);
	}

	@Override
	protected int getHorizontalCenterOfCoverflow()
	{
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
		        + getPaddingLeft();
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
		
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();

		final int imageHeight = child.getLayoutParams().height;

		final int imageWidth = child.getLayoutParams().width;

		float imgXCenter = (float) getHorizontalCenterOfView(child);
		float imgYCenter = (float) getVerticalCenterOfView(child);

		float modifierFactor = imgXCenter / getWidth();

		// Dynamic Y position
		float yModifier = (float) (imgYCenter / getHeight()) * modifierFactor;
		float yPos = imgYCenter * yModifier * yModifier * yModifier * yModifier;
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
