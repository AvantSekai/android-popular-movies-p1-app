package app.com.example.android.popular_movies_p1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by shavant on 2/5/16.
 * NetworkCheck Class checks the users network connections and takes actions accordingly
 */
public class NetworkCheck {
    private ConnectivityManager cm;
    private Context context;
    private NetworkInfo activeNetwork;

    // Construct a Network Check by passing in the current context
    public NetworkCheck(Context context) {
        this.context = context;
        this.cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public NetworkInfo checkActiveNetwork() {
        this.activeNetwork = cm.getActiveNetworkInfo();

        if (this.activeNetwork == null) {
            buildDialog().create();
            buildDialog().show();
        }
        else { // We can get more info on the connection here

        }

        return this.activeNetwork;
    }


    public AlertDialog.Builder buildDialog() {
        // Setup Up Alert Dialog
        AlertDialog.Builder alertDialogbuild = new AlertDialog.Builder(this.context);
        alertDialogbuild.setTitle("No Internet connection.");
        alertDialogbuild.setMessage("You have no internet connection. Please check your WIFI settings.");

        // Set Cancel button for Click when user views message
        AlertDialog.Builder cancel = alertDialogbuild.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });
        alertDialogbuild.setCancelable(false);
        return alertDialogbuild;
    }
}
