package com.thibault01.k_net;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class chanelTvAdaptater extends BaseAdapter {
	List<chanelTv> chanelTvAdaptaterList;
	MainActivity mainActivity;
	
	public chanelTvAdaptater(MainActivity _mainActivity, List<chanelTv> _chanelTvAdaptaterList)
	{
		mainActivity=_mainActivity;
		chanelTvAdaptaterList=_chanelTvAdaptaterList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chanelTvAdaptaterList.size();
	}

	@Override
	public chanelTv getItem(int arg0) {
		// TODO Auto-generated method stub
		return chanelTvAdaptaterList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		if(arg1==null)
		{
			LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.item_tv, arg2,false);
		}
		
		TextView textViewName = (TextView)arg1.findViewById(R.id.textView1);
		//TextView textViewUrl = (TextView)arg1.findViewById(R.id.textView2);
		ImageView imageViewLogo = (ImageView)arg1.findViewById(R.id.imageView1);
		
		chanelTv chaine = chanelTvAdaptaterList.get(arg0);
		
		textViewName.setText(chaine.Name);
		imageViewLogo.setImageResource(getImage(chaine.Id, chaine.radio));
		//textViewUrl.setText(chapter.Url);
		
		return arg1;
	}
	

	public int getImage(int name, boolean radio)
	{
		 int drawableResourceId = mainActivity.getResources().getIdentifier("_" + name, "drawable", mainActivity.getPackageName());

		 if(drawableResourceId == 0 || radio)
		 {
			 return R.drawable.logo;
		 }
		
		 return drawableResourceId;
	}
	
	public chanelTv getChanelTV(int position)
	{
		return chanelTvAdaptaterList.get(position);
	}

}