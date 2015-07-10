package com.visionfederation.venn;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final String IMAGE_TYPE = "image/";
	private static final String PREF_USER_VIEWED_INTRO = "PREF_USER_VIEWED_INTRO";

	private boolean mUserViewedIntro = false;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		createSharedPreferences();
		parseIntructions();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.d("MainActivity.onResume", ">>>>>onResume called!");

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		mUserViewedIntro = sharedPreferences.getBoolean(PREF_USER_VIEWED_INTRO,
				true);
		if (!mUserViewedIntro) {
			showIntro();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * if (!mNavigationDrawerFragment.isDrawerOpen()) {
		 * getMenuInflater().inflate(R.menu.main, menu); restoreActionBar();
		 * return true; }
		 */
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showIntro() {
		mUserViewedIntro = true;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		sp.edit().putBoolean(PREF_USER_VIEWED_INTRO, true).apply();

		Intent introductionIntent = new Intent(this, IntroductionActivity.class);
		startActivity(introductionIntent);
	}

	private void createSharedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (!sharedPreferences.contains(PREF_USER_VIEWED_INTRO)) {
			sharedPreferences.edit().putBoolean(PREF_USER_VIEWED_INTRO, false)
					.apply();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final Context context = getActivity();
			final View rootView = inflater.inflate(R.layout.fragment_main,
					container, false);
			final RelativeLayout mainRelativeLayout = (RelativeLayout) rootView
					.findViewById(R.id.mainRelativeLayout);
			if (mainRelativeLayout != null) {
				mainRelativeLayout.setBackground(getActivity().getResources()
						.getDrawable(R.drawable.bg_alps_2));
			}

			final LinearLayout menuLinearLayout = (LinearLayout) rootView
					.findViewById(R.id.menuLinearLayout);
			final ImageView selectButtonImageView = (ImageView) rootView
					.findViewById(R.id.imageViewSelectButton);
			if (selectButtonImageView != null) {
				selectButtonImageView
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								selectButtonImageView
										.setVisibility(View.INVISIBLE);
								menuLinearLayout.setVisibility(View.INVISIBLE);

								Intent selectPhotoIntent = new Intent(context,
										SelectorActivity.class);
								// selectPhotoIntent.setAction(Const.ACTION_LOAD_PHOTO_SELECTOR);
								selectPhotoIntent
										.setAction(Const.ACTION_LOAD_PHOTO_ALBUM_SELECTOR);
								startActivityForResult(selectPhotoIntent,
										Const.RESULT_SELECTED_PHOTOS);

								/*
								 * PhotoSelectGridFragment
								 * photoSelectGridFragment = new
								 * PhotoSelectGridFragment(); FragmentManager
								 * fragmentManager = getFragmentManager();
								 * FragmentTransaction fragmentTransaction =
								 * fragmentManager .beginTransaction();
								 * fragmentTransaction.replace(R.id.container,
								 * photoSelectGridFragment);
								 * fragmentTransaction.addToBackStack(null);
								 * fragmentTransaction.commit();
								 */
							}
						});
			}

			if (menuLinearLayout != null) {
				ImageButton aboutButton = (ImageButton) menuLinearLayout
						.findViewById(R.id.aboutButton);
				if (aboutButton != null) {
					aboutButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectButtonImageView.setVisibility(View.INVISIBLE);
							menuLinearLayout.setVisibility(View.INVISIBLE);

							Intent aboutIntent = new Intent(context,
									AboutActivity.class);
							startActivity(aboutIntent);
						}
					});
				}
			}

			final ImageView mainImageView = (ImageView) rootView
					.findViewById(R.id.imageViewMain);
			if (mainImageView != null) {
				mainImageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (selectButtonImageView.getVisibility() == View.INVISIBLE) {
							Animation topDown = AnimationUtils.loadAnimation(
									context, R.anim.select_button_enter_top);
							mainImageView.bringToFront();
							selectButtonImageView.startAnimation(topDown);
							selectButtonImageView.setVisibility(View.VISIBLE);
						} else {
							Animation topUp = AnimationUtils.loadAnimation(
									context, R.anim.select_button_exit_top);
							selectButtonImageView.startAnimation(topUp);
							selectButtonImageView.setVisibility(View.INVISIBLE);
						}

						if (menuLinearLayout.getVisibility() == View.INVISIBLE) {
							Animation bottomUp = AnimationUtils.loadAnimation(
									context, R.anim.menu_enter_bottom);
							menuLinearLayout.startAnimation(bottomUp);
							menuLinearLayout.setVisibility(View.VISIBLE);
						} else {
							Animation bottomDown = AnimationUtils
									.loadAnimation(context,
											R.anim.menu_exit_bottom);
							menuLinearLayout.startAnimation(bottomDown);
							menuLinearLayout.setVisibility(View.INVISIBLE);
						}
					}
				});
			}

			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Log.d("MainActivity.onActivityResult", ">>>>>>result incoming: "
					+ String.valueOf(resultCode));
			if (resultCode == Const.RESULT_SELECTED_PHOTOS) {
				List<String> photoUriStringList = data
						.getStringArrayListExtra(Const.FIELD_URI_STRING_LIST);
				if (photoUriStringList != null && !photoUriStringList.isEmpty()) {
					Intent intent = new Intent(getActivity(),
							ViewerActivity.class);
					intent.setAction(Const.ACTION_REFRESH_PHOTO_SET);
					intent.putStringArrayListExtra(Const.FIELD_URI_STRING_LIST,
							(ArrayList<String>) photoUriStringList);
					startActivity(intent);
				}
			}

		}
	}

	private void parseIntructions() {
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if (action == Intent.ACTION_SEND_MULTIPLE && type != null
				&& intent.hasExtra(Intent.EXTRA_STREAM)) {
			if (type.startsWith(IMAGE_TYPE)) {
				handleMultipleImagesReceived(intent);
			}
		} else if (action == Intent.ACTION_SEND && type != null
				&& intent.hasExtra(Intent.EXTRA_STREAM)) {
			if (type.startsWith(IMAGE_TYPE)) {
				handleSingleImageReceived(intent);
			}
		}
	}

	private void handleMultipleImagesReceived(Intent intent) {
		List<String> imageUriStrings = new ArrayList<String>();
		ArrayList<Parcelable> uriArrayList = intent
				.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		if (uriArrayList != null && !uriArrayList.isEmpty()) {
			for (Parcelable parcelable : uriArrayList) {
				Uri uri = (Uri) parcelable;
				imageUriStrings.add(uri.toString());
			}
			showPhotoSet(imageUriStrings);
		}
	}

	private void handleSingleImageReceived(Intent intent) {
		List<String> imageUriStrings = new ArrayList<String>();
		Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			imageUriStrings.add(imageUri.toString());
			showPhotoSet(imageUriStrings);
		}
	}

	private void showPhotoSet(List<String> photoSetUriStrings) {
		Intent intent = new Intent(this, ViewerActivity.class);
		intent.setAction(Const.ACTION_REFRESH_PHOTO_SET);
		intent.putStringArrayListExtra(Const.FIELD_URI_STRING_LIST,
				(ArrayList<String>) photoSetUriStrings);
		startActivity(intent);
	}
}
