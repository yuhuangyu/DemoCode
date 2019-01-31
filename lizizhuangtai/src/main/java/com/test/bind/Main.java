package com.test.bind;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.lizizhuangtai.R;

import java.lang.reflect.Method;

/**
 * Created by fj on 2018/8/28.
 */

public class Main extends Activity {

    /*@InjectView(R.id.bbt)
    Button bt;

    @InjectString("123456789")
    String testkey;

    private TvBean tvBean = new TvBean(this);
    private TvBean2 tvBean2 = new TvBean2(this);*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        /*Binder.get(this)
                .addEvent("onTouch", new Binder.Event() {
                    @Override
                    protected void init(Object[] objects) {
                        for (Object object : objects) {
                            if (object instanceof View) {
                                View view = (View) object;
                                view.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Method method = Binder.get(Main.this).getRealMethod("onTouch");
                                        if (null != method){
                                            try {
                                                return (boolean) method.invoke(Main.this, v, event);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        return false;
                                    }
                                });
                            }
                        }
                    }
                })
                .addEvent("sss", new Binder.Event() {
                    @Override
                    protected void init(Object[] objects) {
                        Method sss = Binder.get(Main.this).getRealMethod("sss");

                    }
                })
                .build();

        bt.setText("点击 "+testkey);*/



    }

    /*@onTouch({R.id.tv})
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.tv:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.e("sdk", "ACTION_DOWN");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("sdk", "ACTION_UP");
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.e("sdk", "ACTION_MOVE");
                }
                break;

            default: return false;
        }
        return true;
    }

    @onEditor({R.id.et,R.id.tv})
    public void afterTextChanged(Editable s) {
        tvBean.setName(""+s);
        tvBean.setColor(Color.parseColor("#000000"));
    }

    @onClick({R.id.bind_view1, R.id.bind_view2, R.id.bbt})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.bind_view1:
                tvBean.setName("hello click1");
                tvBean.setColor(Color.parseColor("#ff0000"));
                tvBean2.setColor(Color.parseColor("#f00000"));
                Toast.makeText(this,"11111",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bind_view2:
                tvBean.setName("hello click2");
                tvBean.setColor(Color.parseColor("#f0f000"));
                tvBean2.setColor(Color.parseColor("#00f000"));
                Toast.makeText(this,"22222",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bbt:
                tvBean.setName("hello 点击");
                tvBean.setColor(Color.parseColor("#0ff000"));
                tvBean2.setColor(Color.parseColor("#0f0000"));
                Toast.makeText(this,"bbt",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @onLongClick({R.id.bind_view1, R.id.bind_view2, R.id.bbt})
    public boolean longClick(View view) {
        switch (view.getId()) {
            case R.id.bind_view1:
                Toast.makeText(this,"long_11111",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bind_view2:
                Toast.makeText(this,"long_22222",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bbt:
                Toast.makeText(this,"long_bbt",Toast.LENGTH_SHORT).show();
                break;
            default: return false;
        }
        return true;
    }*/
}
