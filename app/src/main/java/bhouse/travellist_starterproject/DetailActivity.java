package bhouse.travellist_starterproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends Activity implements View.OnClickListener {

  public static final String EXTRA_PARAM_ID = "place_id";
  private ListView mList;
  private ImageView mImageView;
  private TextView mTitle;
  private LinearLayout mTitleHolder;
  private ImageButton mAddButton;
  private LinearLayout mRevealView;
  private EditText mEditTextTodo;
  private boolean isEditTextVisible;
  private InputMethodManager mInputManager;
  private Place mPlace;
  private ArrayList<String> mTodoList;
  private ArrayAdapter mToDoAdapter;
  int defaultColor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    mPlace = PlaceData.placeList().get(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

    mList = (ListView) findViewById(R.id.list);
    mImageView = (ImageView) findViewById(R.id.placeImage);
    mTitle = (TextView) findViewById(R.id.textView);
    mTitleHolder = (LinearLayout) findViewById(R.id.placeNameHolder);
    mAddButton = (ImageButton) findViewById(R.id.btn_add);
    mRevealView = (LinearLayout) findViewById(R.id.llEditTextHolder);
    mEditTextTodo = (EditText) findViewById(R.id.etTodo);

    mAddButton.setOnClickListener(this);
    defaultColor = getResources().getColor(R.color.primary_dark);

    mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    mRevealView.setVisibility(View.INVISIBLE);
    isEditTextVisible = false;

    setUpAdapter();
    loadPlace();
    windowTransition();
    getPhoto();
  }

  private void setUpAdapter() {
    mTodoList = new ArrayList<>();
    mToDoAdapter = new ArrayAdapter(this, R.layout.row_todo, mTodoList);
    mList.setAdapter(mToDoAdapter);
  }

  private void loadPlace() {
    mTitle.setText(mPlace.name);
    mImageView.setImageResource(mPlace.getImageResourceId(this));
  }

  private void windowTransition() {

  }

  private void addToDo(String todo) {
    mTodoList.add(todo);
  }

  private void getPhoto() {
    Bitmap photo = BitmapFactory.decodeResource(getResources(), mPlace.getImageResourceId(this));
    colorize(photo);
  }

  private void colorize(Bitmap photo) {
    Palette palette = Palette.generate(photo);
    applyPalette(palette);
  }


  private void applyPalette(Palette palette) {

    // sets color for the window background. in this case, that's the rest
    // of the view, apart from the area where text is entered and the title
    // of the place
    getWindow().setBackgroundDrawable(
            new ColorDrawable(palette.getDarkMutedColor(defaultColor)));

    // for the LinearLayout holding the title of the place
    mTitleHolder.setBackgroundColor(palette.getLightVibrantColor(defaultColor));
    // for the LinearLayout containing the EditText
    mRevealView.setBackgroundColor(palette.getMutedColor(defaultColor));


  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_add:
        Animatable animatable;
        if (!isEditTextVisible) {
          revealEditText(mRevealView);

          // causes input cursor to be in EditText (it will have focus)
          mEditTextTodo.requestFocus();

          // show soft keyboard
          mInputManager.showSoftInput(mEditTextTodo, InputMethodManager.SHOW_IMPLICIT);

          // starts with + and morphs into check mark
          mAddButton.setImageResource(R.drawable.icn_morph);
          animatable = (Animatable) mAddButton.getDrawable();
          animatable.start();

        } else {
          addToDo(mEditTextTodo.getText().toString());
          mToDoAdapter.notifyDataSetChanged();
          mInputManager.hideSoftInputFromWindow(mEditTextTodo.getWindowToken(), 0);
          hideEditText(mRevealView);

          mAddButton.setImageResource(R.drawable.icn_morph_reverse);
          animatable = (Animatable) mAddButton.getDrawable();
          animatable.start();

        }
    }
  }

  private void revealEditText(LinearLayout view) {

    // start animation from bottom right of view. the offsets are so that the
    // animation appears to start at the fab instead of inside the view itself
    int cx = view.getRight() - 30;
    int cy = view.getBottom() - 60;

    // circle will expand to a radius of the larger of the height or the width of the view
    int finalRadius = Math.max(view.getWidth(), view.getHeight());

    Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

    // EditText is initially hidden. set it to be visible
    view.setVisibility(View.VISIBLE);
    isEditTextVisible = true;
    anim.start();
  }

  private void hideEditText(final LinearLayout view) {

    int cx = view.getRight() - 30;
    int cy = view.getBottom() - 60;
    int initialRadius = view.getWidth();

    // switch the start and end radii compared to before to reverse the animation
    Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

    // callback for when the animation finishes.
    // we don't want to hide the EditText until it finishes
    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        view.setVisibility(View.INVISIBLE);
      }
    });

    isEditTextVisible = false;
    anim.start();

  }

  @Override
  public void onBackPressed() {
    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
    alphaAnimation.setDuration(100);
    mAddButton.startAnimation(alphaAnimation);
    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {
        mAddButton.setVisibility(View.GONE);
        finishAfterTransition();
      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });
  }
}
