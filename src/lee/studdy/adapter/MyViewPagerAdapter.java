package lee.studdy.adapter;

import lee.studdy.R;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MyViewPagerAdapter extends PagerAdapter {

	private int[] images;
	private LayoutInflater inflater;

	public MyViewPagerAdapter(Context context, int[] images) {
		this.images = images;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View imageLayout = inflater.inflate(R.layout.viewpager_image_item,
				view, false);
		assert imageLayout != null;
		ImageView imageView = (ImageView) imageLayout
				.findViewById(R.id.showpic_viewpager_item_image);
		final ProgressBar spinner = (ProgressBar) imageLayout
				.findViewById(R.id.showpic_viewpager_item_loading);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		imageView.setImageResource(images[position]);

		view.addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
}
