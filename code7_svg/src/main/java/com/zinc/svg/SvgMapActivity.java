package com.zinc.svg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zinc.svg.widget.SvgMapView;

/**
 * author       : zinc
 * time         : 2019/3/28 上午11:28
 * desc         :
 * version      :
 */
public class SvgMapActivity extends Activity {

    private static final int[] COLOR1 = new int[]{
            R.color.t1_color1,
            R.color.t1_color2,
            R.color.t1_color3,
            R.color.t1_color4
    };

    private static final int[] COLOR2 = new int[]{
            R.color.t2_color1,
            R.color.t2_color2,
            R.color.t2_color3,
            R.color.t2_color4
    };

    private static final int[] COLOR3 = new int[]{
            R.color.t3_color1,
            R.color.t3_color2,
            R.color.t3_color3,
            R.color.t3_color4
    };

    private static final int[] COLOR4 = new int[]{
            R.color.t4_color1,
            R.color.t4_color2,
            R.color.t4_color3,
            R.color.t4_color4
    };

    private SvgMapView svgMap;

    private RadioGroup rgColor;
    private RadioButton rbC1;
    private RadioButton rbC2;
    private RadioButton rbC3;
    private RadioButton rbC4;
    private RadioGroup rgMap;
    private RadioButton rbChina;
    private RadioButton rbWorld;
    private TextView tvPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_map);

        svgMap = findViewById(R.id.svg_map);

        rgColor = findViewById(R.id.rg_color);

        rgMap = findViewById(R.id.rg_map);

        tvPlay = findViewById(R.id.tv_play);

        tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = rgColor.getCheckedRadioButtonId();
                if (i == R.id.rb_c1) {
                    svgMap.setMapColor(COLOR1);
                    svgMap.setOutlineColor(R.color.t1_outline);
                    svgMap.setSelColor(R.color.t1_sel_color);

                } else if (i == R.id.rb_c2) {
                    svgMap.setMapColor(COLOR2);
                    svgMap.setOutlineColor(R.color.t2_outline);
                    svgMap.setSelColor(R.color.t2_sel_color);

                } else if (i == R.id.rb_c3) {
                    svgMap.setMapColor(COLOR3);
                    svgMap.setOutlineColor(R.color.t3_outline);
                    svgMap.setSelColor(R.color.t3_sel_color);

                } else if (i == R.id.rb_c4) {
                    svgMap.setMapColor(COLOR4);
                    svgMap.setOutlineColor(R.color.t4_outline);
                    svgMap.setSelColor(R.color.t4_sel_color);
                }

                int i1 = rgMap.getCheckedRadioButtonId();
                if (i1 == R.id.rb_china) {
                    svgMap.setMapResource(R.raw.china);
                } else if (i1 == R.id.rb_world) {
                    svgMap.setMapResource(R.raw.world);
                }
            }
        });
    }
}
