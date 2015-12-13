package com.siddworks.android.popcorntime.util;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class CommonUtil {

	public static String getText(EditText et) {
		if(et != null && et.getText() != null)
		{
			return et.getText().toString();
		}
		return null;
	}

	public static String getRealPathFromURI(Context mContext, Uri contentURI) {
		String result;
		Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

    public static String getText(Spinner spinner) {
        if(spinner != null && spinner.getSelectedItem() instanceof String)
        {
            return (String) spinner.getSelectedItem();
        }
        return null;
    }

    public static void setEditable(View view, boolean editable) {
        if (view != null) {
            if (!editable) {
                view.setClickable(false);
                view.setFocusable(false);
                view.setFocusableInTouchMode(false);
            } else {
                view.setClickable(true);
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
            }
        }

    }

    public static void setSpinnerItem(Spinner spinner, String value) {
        if (spinner != null && value != null) {
            if(spinner.getAdapter() instanceof ArrayAdapter) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
                for (int position = 0; position < adapter.getCount(); position++)
                {
                    if(adapter.getItem(position).equals(value)) {
                        spinner.setSelection(position);
                        return;
                    }
                }
            }
        }
    }

    public static void setEditable(TextView editText, boolean editable) {
        setEditable((View)editText, editable);
        if (!editable) {
            editText.setCursorVisible(false);
        } else {
            editText.setCursorVisible(true);
        }
    }

    public static void setText(View view, String text) {
		if(view instanceof EditText) {
			setText((EditText) view, text);
		} else if (view instanceof TextView) {
			setText((TextView)view, text);
		} else if (view instanceof AutoCompleteTextView) {
			setText((AutoCompleteTextView)view, text);
		}
	}

	public static void setText(EditText et, String text) {
		if(et != null)
		{
			if(text != null) {
				et.setText(text);
			} else {
				et.setText("");
			}
		}
	}

	public static void setText(AutoCompleteTextView tv, String text) {
		if(tv != null)
		{
			if(text != null) {
				tv.setText(text);
			} else {
				tv.setText("");
			}
		}
	}

	public static void setText(TextView tv, String text) {
		if(tv != null)
		{
			if(text != null) {
				tv.setText(text);
			} else {
				tv.setText("");
			}
		}
	}

	public static void showToast(Context mContext, String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	public static void finishActivityWithMessage(Activity mContext, String message) {
		if (message != null) {
			CommonUtil.showToast(mContext, message);
		}
		mContext.finish();
	}

	public static void clearError(EditText et)
	{
		et.setError(null);
	}
	
	public static void setError(EditText et, String error)
	{
		et.setError(error);
	}

	public static void setEnabled(View et, boolean isEnabled) {
		et.setEnabled(isEnabled);
	}

	public static void setVisiblity(View view, int visibility) {
		if(view != null) {
			view.setVisibility(visibility);
		}
	}

	public static void setVisiblity(View view, boolean isVisible) {
		if(view != null) {
			if(isVisible) {
				view.setVisibility(View.VISIBLE);
			} else {
				view.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public static void fadeOutAndDisableView(final View view) {
	    Animation fadeOut = new AlphaAnimation(1, (float)0.5);
	    fadeOut.setInterpolator(new DecelerateInterpolator());
	    fadeOut.setDuration(500);

	    fadeOut.setAnimationListener(new AnimationListener()
	    {
	            public void onAnimationEnd(Animation animation) 
	            {
	            	view.setVisibility(View.VISIBLE);
	            	view.setAlpha((float) 0.5);
	            }
	            public void onAnimationRepeat(Animation animation) {}
	            public void onAnimationStart(Animation animation) {}
	    });

	    view.startAnimation(fadeOut);
	}

	public static void fadeOutAndHideView(final View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(1.0f);

	    Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
	    fadeOut.setInterpolator(new DecelerateInterpolator());
	    fadeOut.setDuration(duration);

	    fadeOut.setAnimationListener(new AnimationListener()
	    {
	            public void onAnimationEnd(Animation animation) 
	            {
	            	view.setVisibility(View.GONE);
                    view.setAlpha(0.0f);
	            }
	            public void onAnimationRepeat(Animation animation) {}
	            public void onAnimationStart(Animation animation) {}
	    });

	    view.startAnimation(fadeOut);
	}

    public static void fadeInAndShowView(final View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);

//        // Add listener to hide view after animation
        Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setAlpha(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        };
        // Start fadein animation
        view.animate().alpha(1).setListener(animationListener).setDuration(duration).setInterpolator(new DecelerateInterpolator());

//        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
//	    fadeIn.setInterpolator(new DecelerateInterpolator());
//	    fadeIn.setDuration(duration);
//
//	    fadeIn.setAnimationListener(new AnimationListener() {
//            public void onAnimationEnd(Animation animation) {
////                view.setVisibility(View.VISIBLE);
//                view.setAlpha(1.0f);
//            }
//
//            public void onAnimationRepeat(Animation animation) {}
//
//            public void onAnimationStart(Animation animation) {}
//        });
//
//	    view.startAnimation(fadeIn);
	}

	public static void showAlertWithOneButton(final String requestTag, final Context mContext, final String heading, final String message)
	{
		if(mContext != null) {
//		System.out.println("showAlertWithOneButton heading:"+heading+", message:"+message);
			((Activity)mContext).runOnUiThread(new Runnable() {
				public void run() {
					if (!((Activity) mContext).isFinishing()) {
						new AlertDialog.Builder(mContext)
								.setTitle(heading)
								.setMessage(message)
								.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mContext instanceof AlertButtonCallbacks) {
                                            ((AlertButtonCallbacks) mContext).alertDialogPositiveButtonPressed(requestTag);
                                        }
                                    }
                                })
								.show();
					}
				}
			});
		}
	}

	// TODO: 8/16/2015 Have proper alert icon
	public static void showAlertWithTwoButtons(final String requestTag, final Context mContext, final String heading, final String message,
											   final String yesText, final String noText)
	{
//		System.out.println("showAlertWithOneButton heading:"+heading+", message:"+message);
		((Activity)mContext).runOnUiThread(new Runnable() {
			public void run() {
				if(!((Activity)mContext).isFinishing()){
					new AlertDialog.Builder(mContext)
							.setTitle(heading)
							.setMessage(message)
							.setPositiveButton(yesText, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mContext instanceof AlertButtonCallbacks) {
                                        ((AlertButtonCallbacks) mContext).alertDialogPositiveButtonPressed(requestTag);
                                    }
                                }
                            })
							.setNegativeButton(noText, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mContext instanceof AlertButtonCallbacks) {
                                        ((AlertButtonCallbacks) mContext).alertDialogNegativeButtonPressed(requestTag);
                                    }
                                }
                            })
							.show();
				}
			}
		});
	}

	// TODO: 8/16/2015 Have proper alert icon
	public static void showAlertWithThreeButtons(
			final String requestTag, final Context mContext, final String heading, final String message,
		    final String yesText, final String neutralText, final String noText)
	{
//		System.out.println("showAlertWithOneButton heading:"+heading+", message:"+message);
		((Activity)mContext).runOnUiThread(new Runnable() {
			public void run() {
				if(!((Activity)mContext).isFinishing()){
					new AlertDialog.Builder(mContext)
							.setTitle(heading)
							.setMessage(message)
							.setPositiveButton(yesText, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if (mContext instanceof AlertButtonCallbacks) {
										((AlertButtonCallbacks) mContext).alertDialogPositiveButtonPressed(requestTag);
									}
								}
							})
							.setNeutralButton(neutralText, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if (mContext instanceof AlertButtonCallbacks) {
										((AlertButtonCallbacks) mContext).alertDialogNeutralButtonPressed(requestTag);
									}
								}
							})
							.setNegativeButton(noText, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mContext instanceof AlertButtonCallbacks) {
                                        ((AlertButtonCallbacks) mContext).alertDialogNegativeButtonPressed(requestTag);
                                    }
                                }
                            })
							.show();
				}
			}
		});
	}

	public static void startActivity(Context mContext,
			Class myClass) {
		Intent intent = new Intent(mContext, myClass);
		mContext.startActivity(intent);
	}

	public static String convertMilliSecondToTime(
			long milliSeconds) {
		if(milliSeconds < 0)
		{
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
	
	    long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds);
        milliSeconds -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds);
        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds);
	
        StringBuilder sb = new StringBuilder(64);
        if ( hours>0 ) {
        	if(hours <= 9) {
		        sb.append("0");
        	}
        	sb.append(hours);
	        sb.append(":");
        }
	 	if(minutes <= 9) {
		        sb.append("0");
	 	}
	        sb.append(minutes);
	        sb.append(":");
	 	if(seconds <= 9) {
		        sb.append("0");
	 	}
        sb.append(seconds);
        return sb.toString();
	}

	public static void clearText(EditText et) {
		if(et != null)
		{
			et.setText("");
		}
	}

    public static void hideKeyboard(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

	public static boolean isNumber(String digitString) {
		try {
			Integer.parseInt(digitString);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

    public static int toNumber(String digitString) {
        try {
            int number = Integer.parseInt(digitString);
            return number;
        } catch (Exception e) {
            return -1;
        }
    }

	public static void setClickable(Spinner spinner, boolean editable) {
		if (spinner != null) {
			spinner.setClickable(editable);
		}
	}
}








