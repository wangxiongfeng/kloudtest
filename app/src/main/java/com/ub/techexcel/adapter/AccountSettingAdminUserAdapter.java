package com.ub.techexcel.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kloudsync.techexcel5.R;
import com.kloudsync.techexcel5.bean.AccountSettingAdminUserBean;
import com.kloudsync.techexcel5.httpgetimage.ImageLoader;
import com.kloudsync.techexcel5.tool.AccountSettingUserPinyinComparator;

import java.util.Collections;
import java.util.List;

/**
 * Created by wang on 2018/3/23.
 */

public class AccountSettingAdminUserAdapter extends BaseAdapter {

    private Context context;
    private List<AccountSettingAdminUserBean> serviceList;
    public ImageLoader imageLoader;
    private int isShow;

    boolean fromSearch;
    String keyword;

    public AccountSettingAdminUserAdapter(Context context, List<AccountSettingAdminUserBean> serviceList, boolean isPublic, int isShow) {
        Log.e("老余adapter", "laozhang"+serviceList.size());
        this.context = context;
        this.serviceList = serviceList;
        this.isShow = isShow;
        imageLoader = new ImageLoader(context.getApplicationContext());
    }

    public AccountSettingContactAdapter.OnModifyServiceListener onModifyServiceListener;


    public interface OnModifyServiceListener {
        void select(AccountSettingAdminUserBean bean);
    }

    public void setOnModifyServiceListener(AccountSettingContactAdapter.OnModifyServiceListener onModifyServiceListener) {
        this.onModifyServiceListener = onModifyServiceListener;
    }

    //搜索
    public void updateListView2(List<AccountSettingAdminUserBean> list2) {
        this.serviceList = list2;
        notifyDataSetChanged();
    }

    //首字母
    public void updateListView(List<AccountSettingAdminUserBean> list2) {
        AddRobetinfo(list2);
        notifyDataSetChanged();

    }
    private void AddRobetinfo(List<AccountSettingAdminUserBean> list2) {
        SortCustomers(list2);
        this.serviceList = list2;
    }


    public void SortCustomers(List<AccountSettingAdminUserBean> list2) {
        Collections.sort(list2, new AccountSettingUserPinyinComparator());
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.account_setting_admin_user_item, null);
            holder.as_img_admin_user = convertView.findViewById(R.id.as_img_admin_user);
            holder.as_tv_admin_user_item = convertView.findViewById(R.id.as_tv_admin_user_item);
            holder.tv_sort = convertView.findViewById(R.id.tv_sort);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }





        final AccountSettingAdminUserBean bean = serviceList.get(position);
        final ViewHolder finalHolder = holder;



        holder.as_tv_admin_user_item.setText(bean.getUserName());


        String imageurl = bean.getAvatarUrl();
        Uri imageUri = Uri.parse(imageurl);
        Log.e("老余imageurl", bean.getAvatarUrl() + "");
        Log.e("老余UserName", bean.getUserName() + "");
        holder.as_img_admin_user.setImageURI(imageUri);


        holder.tv_sort.setText(bean.getSortLetters());
        int sectionVisible = getPositionForSection(serviceList,
                bean.getSortLetters().charAt(0));
        if (sectionVisible == position) {
            holder.tv_sort.setVisibility(View.VISIBLE);
        } else {
            holder.tv_sort.setVisibility(View.GONE);
        }



        Log.e("老余adapter", bean.getAvatarUrl() + "");
        return convertView;

    }
    class ViewHolder {

        TextView as_tv_admin_user_item,tv_sort;
        SimpleDraweeView as_img_admin_user;

    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public static int getPositionForSection(List<AccountSettingAdminUserBean> list,char section) {
        for (int i = 0; i < list.size(); i++) {
            String sortStr = list.get(i).getSortLetters();
            if(null == sortStr){
                continue;
            }
            char firstChar = sortStr.charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }


}
