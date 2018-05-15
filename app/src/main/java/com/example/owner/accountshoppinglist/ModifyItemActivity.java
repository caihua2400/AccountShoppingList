package com.example.owner.accountshoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyItemActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_item);

        ShoppingItemDatabase dbConnection=new ShoppingItemDatabase(this);
        db= dbConnection.openDatabase();

        final EditText edit_name=findViewById(R.id.editText);
        final EditText edit_price=findViewById(R.id.editText2);
        final EditText edit_quantity=findViewById(R.id.editText3);
        final EditText edit_tag=findViewById(R.id.editText4);

        final ShoppingItem st=getIntent().getParcelableExtra("shoppingItem");
        edit_name.setText(st.getName());
        edit_price.setText(st.getPrice()+"");
        edit_quantity.setText(st.getQuantity()+"");
        edit_tag.setText(st.getTag());

        final Button button=findViewById(R.id.button_confirm_modify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ModifyItemActivity.this);
                builder.setTitle("modify item");
                builder.setNegativeButton("cancel modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("confirm modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        st.setName(edit_name.getText().toString());
                        st.setPrice(Integer.parseInt(edit_price.getText().toString()));
                        st.setQuantity(Integer.parseInt(edit_quantity.getText().toString()));
                        st.setTag(edit_tag.getText().toString());
                        DatabaseUtility.update(db,st);
                        Toast.makeText(ModifyItemActivity.this,"update successful",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ModifyItemActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                /*
                st.setName(edit_name.getText().toString());
                st.setPrice(Integer.parseInt(edit_price.getText().toString()));
                st.setQuantity(Integer.parseInt(edit_quantity.getText().toString()));
                st.setTag(edit_tag.getText().toString());
                DatabaseUtility.update(db,st);
                Toast.makeText(ModifyItemActivity.this,"update successful",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ModifyItemActivity.this,MainActivity.class);
                startActivity(intent);
                */
            }
        });


    }
}
