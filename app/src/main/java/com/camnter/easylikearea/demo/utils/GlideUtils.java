/*
 * {EasyGank}  Copyright (C) {2015}  {CaMnter}
 *
 * This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; type `show c' for details.
 *
 * The hypothetical commands `show w' and `show c' should show the appropriate
 * parts of the General Public License.  Of course, your program's commands
 * might be different; for a GUI interface, you would use an "about box".
 *
 * You should also get your employer (if you work as a programmer) or school,
 * if any, to sign a "copyright disclaimer" for the program, if necessary.
 * For more information on this, and how to apply and follow the GNU GPL, see
 * <http://www.gnu.org/licenses/>.
 *
 * The GNU General Public License does not permit incorporating your program
 * into proprietary programs.  If your program is a subroutine library, you
 * may consider it more useful to permit linking proprietary applications with
 * the library.  If this is what you want to do, use the GNU Lesser General
 * Public License instead of this License.  But first, please read
 * <http://www.gnu.org/philosophy/why-not-lgpl.html>.
 */

package com.camnter.easylikearea.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SizeReadyCallback;

/**
 * Description：GlideUtils
 * Created by：CaMnter
 * Time：2016-01-04 22:19
 */
public class GlideUtils {

    private static final String TAG = "GlideUtils";


    public static void displayNative(final ImageView view, @DrawableRes int resId) {
        // 不能崩
        if (view == null) {
            Log.e(TAG, "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                 .load(resId)
                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                 .crossFade()
                 .centerCrop()
                 .into(view)
                 .getSize(new SizeReadyCallback() {
                     @Override public void onSizeReady(int width, int height) {
                         view.setVisibility(View.VISIBLE);
                     }
                 });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
