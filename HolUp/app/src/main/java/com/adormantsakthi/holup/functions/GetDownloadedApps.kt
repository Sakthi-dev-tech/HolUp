import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable


fun GetDownloadedApps(context: Context): List<Pair<String, Drawable>> {
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val packageManager: PackageManager = context.packageManager
    val installedApplications: MutableList<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)

    val nonSystemApps = mutableListOf<Pair<String, Drawable>>()

    for (app in installedApplications) {
        if (app.activityInfo != null) {
            // if it doesn't give the right apps, try uncommenting the following lines of code
//            val res = packageManager.getResourcesForApplication(app.activityInfo.applicationInfo)
//            val name = if (app.activityInfo.labelRes != 0)
//            {res.getString(app.activityInfo.labelRes)} else
//            {app.activityInfo.applicationInfo.loadLabel(packageManager).toString()}

            val name = app.activityInfo.applicationInfo.loadLabel(packageManager).toString()
            val icon = app.activityInfo.applicationInfo.loadIcon(packageManager)

            nonSystemApps.add(Pair(name, icon))
        }

    }

    return nonSystemApps.distinct()
}
