package gank.douya.com.recyclerviewheaderandfooter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> mLists = new ArrayList();
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 生产数据
         * 正确写法不应该在onCreate方法里写
         */
        for (int i = 0; i < 15; i++) {
            mLists.add(i + "");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(mLists);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        /**
         * 通过LayoutInflater获取实例化对象
         */
        View headerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.header_layout, recyclerView, false);
        View footView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.foot_layout, recyclerView, false);
        myRecyclerViewAdapter.setHeader(headerView);
        myRecyclerViewAdapter.setFooter(footView);


    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter {

        private final List<String> mDatas;
        private View mheaderView;
        private View mfootView;


        public MyRecyclerViewAdapter(List<String> mDatas) {

            this.mDatas = mDatas;
        }

        public void setHeader(View headerView) {
            mheaderView = headerView;
            notifyItemInserted(0);//通知空出第一个位置，即0
        }

        public void setFooter(View footerView) {
            mfootView = footerView;
            notifyItemInserted(getItemCount() - 1);//保证getItemCount返回值正确后，这个方法总是通知空出最后一个位置，即getItemCount-1
        }

        @Override
        public int getItemViewType(int position) {
//            if (mfootView == null && mheaderView == null) {
//                return 0;
//            }
            if (position == 0) {
                //headerView
                return 1;
            }

            if (position == getItemCount() - 1) {
                //footerView
                return 2;
            }


            return 0;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (mheaderView != null && viewType == 1) {
                return new MyViewHolder(mheaderView);
            }

            if (mfootView != null && viewType == 2) {
                return new MyViewHolder(mfootView);
            }

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d("position", position + "");

            if (getItemViewType(position) == 1) {
                return;
            } else if (getItemViewType(position) == 2) {
                return;
            } else {
                //由于返回的holder类型在header和footer时，是为null，所以需要判断下类型
                if (holder instanceof MyViewHolder) {
                    MyViewHolder mHolder = (MyViewHolder) holder;

                    //巨坑：0位置被占用,item的postion是向后挪了一位的，因此需要减一mDatas才能获取到正确的数据
                    mHolder.mTv.setText(mDatas.get(position - 1));
                }
            }


        }

        @Override
        public int getItemCount() {

            if (mheaderView != null && mfootView == null) {
                return mDatas.size() + 1;
            } else if (mheaderView == null && mfootView != null) {
                return mDatas.size() + 1;
            } else if (mheaderView != null && mfootView != null) {
                return mDatas.size() + 2;
            }
            return mDatas.size();
        }


        /**
         * ViewHolder可以编写重名类ViewHolder来免去Holder在onBindViewHolder的强制转换
         */
        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mTv;
            private final View mView;

            public MyViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                if (itemView == mheaderView || itemView == mfootView) {
                    return;
                }
                mTv = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }

}
