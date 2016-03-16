package com.wuchuanlong.stockview;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.stockview.R;
import com.wedroid.framework.activity.WeDroidActivity;
import com.wuchuanlong.stockview.chart.BaseChartView;
import com.wuchuanlong.stockview.chart.SingleStockInfo;
import com.wuchuanlong.stockview.chart.StockBusiness;
import com.wuchuanlong.stockview.chart.TouchCallBack;
import com.wuchuanlong.stockview.chart.Type;

public class MainActivity extends WeDroidActivity {

//	TimeKChartView stockView;
	BaseChartView stockView;
	ProgressBar mProgressBar;
	private TextView mStockNameTv;// 名称
	private TextView mStockCodeTv;// 代码
	private TextView mNowPriceTv;// 当前价
	private TextView mStockZdTv;// 涨跌
	private TextView mStockZfTv;// 涨跌幅
	private TextView mOpenPriceTv;// 今开
	private TextView mHighPriceTv;// 最高价
	private TextView mDealCountTv;// 成交量
	private TextView mLowPriceTv;// 最低价
	private TextView mHsTv;// 换手率
	private TextView mZfTv;// 振幅
	private String name = "东方电气";
	private String code = "600875";
	private String market = "SH";
	// 请求的范围
	int oriSize = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.chart_activity_main);
		initView();
		day(null);
//		hour(null);
	}

	public void hour(View view) {
		if (hourList!=null && !hourList.isEmpty()){
			updateStockView(hourList);
		}else{
			oriSize = 240;
			requestStock(oriSize, Type.WEEK.getValue(), StockBusiness.FEN_SHI_CHART);
		}
	}
	public void week(View view) {
		if (weekList!=null && !weekList.isEmpty()){
			updateStockView(weekList);
		}else{
			oriSize = 100;
			requestStock(oriSize, Type.WEEK.getValue(), StockBusiness.WEEK_CHART);
		}
	}

	public void month(View view) {
		if (monthList!=null && !monthList.isEmpty()){
			updateStockView(monthList);
		}else{
			oriSize = 80;
			requestStock(oriSize, Type.MONTH.getValue(), StockBusiness.MONTH_CHART);
		}
	}

	public void day(View view) {
		if (dayList!=null && !dayList.isEmpty()){
			updateStockView(dayList);
		}else{
			oriSize = 100;
			requestStock(oriSize, Type.DAY.getValue(), StockBusiness.DAY_CHART);
		}
		
	}
	
	private void requestStock(int count,String type,int token){
		mProgressBar.setVisibility(View.VISIBLE);
		stockView.setVisibility(View.GONE);
		Map<String, String> map = new HashMap<String, String>();
		map.put("stock_code", code);
		map.put("market", market);
		map.put("type", type);
		map.put("count", ""+count);
		new StockBusiness(token, this, map).execute();
	}

	private void initView() {
		stockView = (BaseChartView) findViewById(R.id.stock_view);
		mStockNameTv = (TextView) $(R.id.tv_stock_name);// 名称
		mStockCodeTv = (TextView) $(R.id.tv_stock_code);// 代码
		mNowPriceTv = (TextView) $(R.id.tv_now_price);// 当前价
		mStockZdTv = (TextView) $(R.id.tv_stock_zd);// 涨跌
		mStockZfTv = (TextView) $(R.id.tv_stock_zf);// 涨跌幅
		mOpenPriceTv = (TextView) $(R.id.tv_open_price);// 今开
		mHighPriceTv = (TextView) $(R.id.tv_high_price);// 最高价
		mDealCountTv = (TextView) $(R.id.tv_deal_count);// 成交量
		mLowPriceTv = (TextView) $(R.id.tv_low_price);// 最低价
		mHsTv = (TextView) $(R.id.tv_hs);// 换手率
		mZfTv = (TextView) $(R.id.tv_zf);// 振幅
//		mProgressBar = (ProgressBar) $(R.id.pb);
		stockView.setTouchCallback(new TouchCallBack() {
			@Override
			public void updateViewInTouch(SingleStockInfo info) {
				updateRelativeView(info);
			}

			@Override
			public void ifParentIterceptorEvent(boolean interceptor) {
				
			}

			@Override
			public void enterBigView() {
				// TODO Auto-generated method stub
				
			}
		});

		mStockNameTv.setText(name);
		mStockCodeTv.setText(code);
	}

	List<SingleStockInfo> dayList;
	List<SingleStockInfo> monthList;
	List<SingleStockInfo> weekList;
	List<SingleStockInfo> hourList;

	@SuppressWarnings("unchecked")
	@Override
	protected void requestSuccess(Object result, int requestToken) {
		mProgressBar.setVisibility(View.GONE);
		stockView.setVisibility(View.VISIBLE);
		if (requestToken == StockBusiness.DAY_CHART) {
			dayList = (List<SingleStockInfo>) result;
			updateStockView(dayList);
		} else if (requestToken == StockBusiness.MONTH_CHART) {
			monthList = (List<SingleStockInfo>) result;
			updateStockView(monthList);
		} else if (requestToken == StockBusiness.WEEK_CHART) {
			weekList = (List<SingleStockInfo>) result;
			updateStockView(weekList);
		}else if (requestToken == StockBusiness.FEN_SHI_CHART){
			hourList = (List<SingleStockInfo>) result;
			updateStockView(hourList);
		}
		
	}
	
	public void updateStockView(List<SingleStockInfo> list){
		stockView.setStockList(list);
		stockView.setOriSize(oriSize);
		stockView.invalidate();
	}

	public void updateRelativeView(SingleStockInfo info) {
		mNowPriceTv.setText(info.getHigh() + "");
		;// 当前价
		mStockZdTv.setText("--");
		;// 涨跌
		mStockZfTv.setText("--");
		;// 涨跌幅
		mOpenPriceTv.setText(info.getOpen() + "");
		;// 今开
		mHighPriceTv.setText(info.getHigh() + "");
		;// 最高价
		mDealCountTv.setText((Math.rint(info.getDealCount() / 10000)) + "万手");
		;// 成交量
		mLowPriceTv.setText(info.getLow() + "");
		;// 最低价
		mHsTv.setText("--");
		;// 换手率
		mZfTv.setText("--");
		;// 振幅
	}

}
