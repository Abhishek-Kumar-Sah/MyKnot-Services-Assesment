package com.avi.in.myknotproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowDialogBox {

    //Singleton Instance
    private static final ShowDialogBox dialogBox = new ShowDialogBox();


    //Method to initialize the class and show up the dialog box
    public  void show(Context context,String title, String imageURL,String successURL){

        //Inflating layout of dialog box
        View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_box_layout,null);

        TextView titleOfDialogBox = dialog.findViewById(R.id.dialog_box_title);
        titleOfDialogBox.setText(title);

        Button successButton = dialog.findViewById(R.id.dialog_box_button);
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(successURL));
                context.startActivity(intent);
            }
        });



        ImageView imageView = dialog.findViewById(R.id.dialog_box_image_view);

        //Using Picasso for Image download and loadUp
        Picasso.with(context).load(imageURL).into(imageView);

        //Calling SwipeDismissDialog box builder and passing Dialog box view to be generated
          new SwipeDismissDialog.Builder(context)
                  .setView(dialog)
                  .build()
                  .show();
    }

    //Method to get single instance of ShowDialogBox
    public static ShowDialogBox getSingletonInstance(){
        return dialogBox;
    }

    //Restricts the creation of more ShowDialogBox objects
    private ShowDialogBox(){};
}
