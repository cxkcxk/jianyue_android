//阅读器主体

package test.com.jianyue;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    /**test**/


    /**解析 Gson 用到的变量**/
    String text;
    String Title;
    String Auther;
    String Text;
    List<String> list;
    String LJson;

    /**okhttp**/
    public static final String TAG = "MainActivity";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    String jsonTags = "{\"tag\":[\"ccc\",\"ddd\" ]}";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.MeiWen1)
    CheckBox MeiWen1;
    @BindView(R.id.QinGan1)
    CheckBox QinGan1;
    @BindView(R.id.LiShi1)
    CheckBox LiShi1;
    @BindView(R.id.ZhenTan1)
    CheckBox ZhenTan1;
    @BindView(R.id.LiZhi1)
    CheckBox LiZhi1;
    @BindView(R.id.YouMo1)
    CheckBox YouMo1;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.textView)
    TextView textView;
    private DrawerLayout mDrawerLayout;
    private ScrollView scrollView;
    private TextView bt_settings;
    public float textSize=7;

    public static final String DIALOG_TAG_2 = "dialog2";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //开启一个线程，做联网操作
        ButterKnife.bind(this);
        //绑定布局和按键
        mDrawerLayout = findViewById(R.id.drawer_layout);
        scrollView=findViewById(R.id.scrollView);
        bt_settings=findViewById(R.id.setting);
        Toolbar toolbar = findViewById(R.id.toolbar);//toolbar导入
        setSupportActionBar(toolbar);//toolbar绑定为actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//把返回键显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_tag);//把返回键和标签按钮绑定
        }
        //初始化样式
        init();
        //调用按键设置
        set_checkout();
        //点击更多设置按钮
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                Bottom_Dialog.newInstance().show(getFragmentManager(), DIALOG_TAG_2);
            }
        });

    }

    /*private void postJson() {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON , jsonTags);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.155.1:8080/jianyue/getArtical.html?json={\"tag\":[\"aaa\",\"bbb\"]}")
                .post(requestBody)
                .build();

        //发送请求获取响应
        try {
            Response response=okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                Log.i(TAG,response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    private void testjson(){
        try{
            final Request request = new Request.Builder()
                    .url("http://192.168.191.3:8080/jianyue/getArticle.html?json=历史")
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient();
            Call mcall = client.newCall(request);
            mcall.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    mHandler.obtainMessage(3, null).sendToTarget();

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    LJson = response.body().string();
                    //String json = response.body().string();
                    mHandler.obtainMessage(1, LJson).sendToTarget();
                }
            });
        }catch (Exception e){
            System.out.println("error!");
        }
    }

    Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(MainActivity.this, "hhh", Toast.LENGTH_SHORT).show();
                    System.out.println("hhh");
                    return true;
                case 1:
                    System.out.println(msg.obj.toString());
                    text = LJson;
                    list = GsonRead.getGson(text);
                    Title = list.get(0);
                    Auther = list.get(1);
                    Text = list.get(2);
                    toolbar.setTitle(Title);
                    textView.setText(Text);
                    text = "";
                    return true;
                default:
                    return false;
            }
        }
    });

    //加载toolbar布局
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //处理toolbar点击事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://侧边栏
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.flash://刷新
                new Thread() {
                    @Override
                    public void run() {
                        if(jsonTags == null) {
                            System.out.println("runFailed");
                        } else {
//                            //postJson();
//                            testjson();
                        }
                    }
                }.start();
                text = Util.getJson(MainActivity.this, "TestJson.json");
                GsonRead gsonRead;
                System.out.println(LJson);
                System.out.println(LJson);
                //text = LJson;
                list = GsonRead.getGson(text);
                Title = list.get(0);
                Auther = list.get(1);
                Text = list.get(2);
                toolbar.setTitle(Title);
                textView.setText(Text);
                text = "";
                break;
            default:
        }
        return true;
    }

    //读取SharedPreference，赋值给checkbox兴趣标签,复选框按键功能，把复选框的内容记录到shareperference
    public void set_checkout() {
        //读取SharedPreference，赋值给checkbox兴趣标签
        CheckBox meiwen1 =  findViewById(R.id.MeiWen1);
        CheckBox qingan1 =  findViewById(R.id.QinGan1);
        CheckBox zhentan1 =  findViewById(R.id.ZhenTan1);
        CheckBox lishi1 = findViewById(R.id.LiShi1);
        CheckBox lizhi1 =  findViewById(R.id.LiZhi1);
        CheckBox youmo1 = findViewById(R.id.YouMo1);
        
        SharePreference sp = new SharePreference(MainActivity.this);
        boolean flag = sp.getMeiWen();
        meiwen1.setChecked(flag);
        flag = sp.getQinGan();
        qingan1.setChecked(flag);
        flag = sp.getZhenTan();
        zhentan1.setChecked(flag);
        flag = sp.getLiShi();
        lishi1.setChecked(flag);
        flag = sp.getLiZhi();
        lizhi1.setChecked(flag);
        flag = sp.getYouMo();
        youmo1.setChecked(flag);



        //复选框按键功能，把复选框的内容记录到shareperference
        meiwen1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                SharePreference sp = new SharePreference(MainActivity.this);
                if (isChecked) {
                    sp.setMeiWenTrue();
                } else {
                    sp.setMeiWenFalse();
                }
            }
        });
        qingan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                SharePreference sp = new SharePreference(MainActivity.this);
                if (isChecked) {
                    sp.setQinGanTrue();
                } else {
                    sp.setQinGanFalse();
                }
            }
        });
        zhentan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                SharePreference sp = new SharePreference(MainActivity.this);
                if (isChecked) {
                    sp.setZhenTanTrue();
                } else {
                    sp.setZhenTanFalse();
                }
            }
        });
        lishi1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                SharePreference sp = new SharePreference(MainActivity.this);
                if (isChecked) {
                    sp.setLiShiTrue();
                } else {
                    sp.setLiShiFalse();
                }
            }
        });
        lizhi1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                SharePreference sp = new SharePreference(MainActivity.this);
                if (isChecked) {
                    sp.setLiZhiTrue();
                } else {
                    sp.setLiZhiFalse();
                }
            }
        });
        youmo1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                SharePreference sp = new SharePreference(MainActivity.this);
                if (isChecked) {
                    sp.setYouMoTrue();
                } else {
                    sp.setYouMoFalse();
                }
            }
        });

    }

    @OnClick({R.id.toolbar, R.id.MeiWen1, R.id.QinGan1, R.id.LiShi1, R.id.ZhenTan1, R.id.LiZhi1, R.id.YouMo1, R.id.drawer_layout, R.id.scrollView, R.id.textView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                Toast.makeText(MainActivity.this, "toolbar", Toast.LENGTH_SHORT);
                break;
            case R.id.MeiWen1:
                break;
            case R.id.QinGan1:
                break;
            case R.id.LiShi1:
                break;
            case R.id.ZhenTan1:
                break;
            case R.id.LiZhi1:
                break;
            case R.id.YouMo1:
                break;
            case R.id.drawer_layout:
                break;
            case R.id.scrollView:
                break;
            case R.id.textView:
                //Toast.makeText(MainActivity.this, "papapa", Toast.LENGTH_SHORT).show();
                //System.out.println("你点击了test");
                break;
        }
    }

    /*public String getTag() {
        Gson gson = new Gson();
        SharePreference sp = new SharePreference(MainActivity.this);
        if( sp.getGuWen() ) {
            jsonTags = gson.toJson("GuWen");
        }

        if( sp.getLiZhi() ) {
            jsonTags = gson.toJson("Jishi");
        }

        if( sp.getYouMo() ) {
            jsonTags = gson.toJson("YouMo");
        }

        if( sp.getLiShi() ) {
            jsonTags = gson.toJson("LiShi");
        }

        if( sp.getZhenTan() ) {
            jsonTags = gson.toJson("ZhenTan");
        }

        if( sp.getQinGan() ) {
            jsonTags = gson.toJson("QinGan");
        }
    }*/
    //初始化样式
    public void init(){
        SharePreference sp = new SharePreference(MainActivity.this);
        //设置文字和背景颜色
        if(sp.getNight()){
            textView.setBackgroundColor(Color.parseColor("#0d0d0b"));
            textView.setTextColor(Color.parseColor("#5b5952"));
        }
        else{
            if(sp.getWhite()){
                textView.setBackgroundColor(Color.parseColor("#ffffff"));
                textView.setTextColor(Color.parseColor("#333333"));
            }
            if(sp.getGreen()){
                textView.setBackgroundColor(Color.parseColor("#f0fdf0"));
                textView.setTextColor(Color.parseColor("#709a7b"));
            }
            if(sp.getYellow()){
                textView.setBackgroundColor(Color.parseColor("#f7f7e8"));
                textView.setTextColor(Color.parseColor("#b88940"));
            }
            if(sp.getPink()){
                textView.setBackgroundColor(Color.parseColor("#fff6ef"));
                textView.setTextColor(Color.parseColor("#db7d6d"));
            }
        }
        //设置文字大小
        int i=sp.getSize();//获取字号
        if(i==0){
            textSize=17;
        }
        else if(i==1){
            textSize=20;
        }
        else if(i==2){
            textSize=23;
        }
        textView.setTextSize(textSize);
    }

}
