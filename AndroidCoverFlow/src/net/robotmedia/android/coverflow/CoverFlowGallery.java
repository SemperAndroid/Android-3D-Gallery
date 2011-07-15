package net.robotmedia.android.coverflow;

import android.content.Context;
import android.graphics.Camera;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public abstract class CoverFlowGallery extends Gallery
{

	/**
	 * Graphics Camera used for transforming the matrix of ImageViews
	 */
	protected Camera	mCamera	       = new Camera();

	/**
	 * The maximum angle the Child ImageView will be rotated by
	 */
	protected int	 mMaxRotationAngle	= -45;

	/**
	 * The maximum zoom on the centre Child
	 */
	protected int	 mMaxZoom	       = -120;

	/**
	 * The Center X of the Coverflow
	 */
	protected int	 mCoverFlowHorizontalCenter;

	/**
	 * The Center Y of the Coverflow
	 */
	protected int	 mCoverFlowVerticalCenter;

	// ---------------------------------------
	// Constructors

	public CoverFlowGallery(Context context)
	{
		super(context);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlowGallery(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlowGallery(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	// -------------------------------------
	// Logic

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
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mCoverFlowHorizontalCenter = getHorizontalCenterOfCoverflow();
		mCoverFlowVerticalCenter = getVerticalCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see #setStaticTransformationsEnabled(boolean)
	 */
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t)
	{
		performChildStaticTransformation(child, t);
		return true;
	}

	// ----------------------------------------------
	// Getters & Setters

	/**
	 * Get the max rotational angle of the image
	 * 
	 * @return the mMaxRotationAngle
	 */
	public int getMaxRotationAngle()
	{
		return mMaxRotationAngle;
	}

	/**
	 * Set the max rotational angle of each image
	 * 
	 * @param maxRotationAngle
	 *            the mMaxRotationAngle to set
	 */
	public void setMaxRotationAngle(int maxRotationAngle)
	{
		mMaxRotationAngle = maxRotationAngle;
	}

	/**
	 * Get the Max zoom of the centre image
	 * 
	 * @return the mMaxZoom
	 */
	public int getMaxZoom()
	{
		return mMaxZoom;
	}

	/**
	 * Set the max zoom of the centre image
	 * 
	 * @param maxZoom
	 *            the mMaxZoom to set
	 */
	public void setMaxZoom(int maxZoom)
	{
		mMaxZoom = maxZoom;
	}

	// ---------------------------------------
	// Abstract methods

	protected abstract int getHorizontalCenterOfCoverflow();

	protected abstract int getVerticalCenterOfCoverflow();

	protected abstract int getHorizontalCenterOfView(View view);

	protected abstract int getVerticalCenterOfView(View view);

	protected abstract void performChildStaticTransformation(View child, Transformation t);

}
