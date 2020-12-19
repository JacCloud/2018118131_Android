package mrkj.healthylife.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import mrkj.healthylife.R;
import mrkj.healthylife.activity.ShowBPMActivity;
import mrkj.healthylife.base.BaseFragment;
import mrkj.library.wheelview.progressbar.RoundProgressBarHeartBMP;

/**
 * 心率测试
 *根据相关资料-->GitHub上的开源项目
 * (地址：https://github.com/phishman3579/android-heart-rate-monitor)
 * 进行修改而来的
 * 思路就是打开摄像头并打开闪关灯
 * 用手指放在在镜头出，随后对surfaceview
 * 中显示的图像进行处理
 * 随后得出心率的数值
 * 该功能具有一定的科学依据
 * 如在图像处理上具有基础的可以研究一下
 * 如果没有先关基础的参考下功能就好
 * 不必深究
 * 这里用到的图标引擎libs包中都已提供

 */
public class HeartFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = HeartFragment.class.getSimpleName();
    private View view;//界面的布局
    //摄像头预览
    private SurfaceView preview;
    private SurfaceHolder previewHolder;
    private Camera camera;//摄像头
    private PowerManager.WakeLock wakeLock;//用于控制闪光等
    private int mCurrentCamIndex;//相机的指数
    private ImageButton start_heart_test_btn;//开启测试的开关
    private RoundProgressBarHeartBMP roundProgressBarHeartBMP;//进度条
    private RelativeLayout relativeLayout;
    private static int progess = 0;//进度
    private static String bpm = "000";//默认值
    //测试
    //	曲线
    private Timer timer;//定时器
    private TimerTask task;
    private static int imageReslutValues;//图像处理的返回值
    private static int count;//计数
    private LinearLayout layout;
    private static double flag = 1;//标记位
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //        		刷新图表
            if (msg.what == 1) {
                updateChart();
                if (bpm != null) {
                    roundProgressBarHeartBMP.setText(bpm);
                    roundProgressBarHeartBMP.setProgress(progess);
                }
                if (progess == 10) {
                    Toast.makeText(getContext(), "测试结束", Toast.LENGTH_SHORT).show();
                    if (timer != null) {
                        timer.cancel();
                        task.cancel();
                    }
                    handler.removeMessages(1);
                    context.startActivity(new Intent(context, ShowBPMActivity.class));
                }
            }

        }
    };
    private String title = "pulse";
    private XYSeries series;//用于绘制图表
    private XYMultipleSeriesDataset mDataset;
    private GraphicalView chart;
    private XYMultipleSeriesRenderer renderer;
    private Context context;
    private int addX = -1;
    double addY;
    int[] xPoint = new int[300];//x轴
    int[] yPoint = new int[300];//y轴
    int[] values = new int[]{9, 10, 11, 12, 13, 14, 13, 12, 11, 10, 9, 8, 7, 6, 7, 8, 9, 10, 11, 10, 10};
    //用于计算心率
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    public enum TYPE {
        GREEN, RED
    }

    private static TYPE currentType = TYPE.GREEN;
    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Log.e(TAG, "onAttach");
    }

    @SuppressLint("InvalidWakeLockTag")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_heart, null);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        initView();
        Log.e(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
