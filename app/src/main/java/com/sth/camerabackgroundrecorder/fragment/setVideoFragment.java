package com.sth.camerabackgroundrecorder.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sth.camerabackgroundrecorder.R;
import com.sth.camerabackgroundrecorder.util.AppPara;
import com.sth.camerabackgroundrecorder.util.CameraSupportedParameters;
import com.sth.camerabackgroundrecorder.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;

public class setVideoFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private VideoAdapter adapter;
    List<Map<String, String>> lists = new ArrayList<Map<String, String>>();// 存放adapter的数据
    HashMap<String, String> map = new HashMap<String, String>();
    private static final int EX_FILE_PICKER_RESULT = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_video, container, false);
        initView(view);
        initVideoData();
        return view;
    }

    private void initVideoData() {
        lists.removeAll(lists);
        map.put("k", "循环录影时长:");
        map.put("v", AppPara.getInstance().getLoopDuration() + "分钟");
        lists.add(map);
        map = new HashMap<String, String>();
        map.put("k", "视频分辨率:");
        map.put("v", AppPara.getInstance().getVideo_Resolution_Ratio().toString());
        lists.add(map);
        map = new HashMap<String, String>();
        map.put("k", "摄像头:");
        map.put("v", AppPara.getInstance().getCurrentCameraId()==1 ? "前" : "后");
        lists.add(map);
        map = new HashMap<String, String>();
        map.put("k", "文件存储路径:");
        map.put("v", AppPara.getInstance().getSavePath());
        lists.add(map);
        map = new HashMap<String, String>();
        map.put("k", "竖屏摄像头视频旋转:");
        map.put("v", AppPara.getInstance().getRotationAngle()+"°");
        lists.add(map);
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(android.R.id.list);
        adapter = new VideoAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            //循环录像时间
            case 0: {
                String[] data = {"1分钟", "5分钟", "10分钟", "15分钟", "20分钟", "25分钟", "30分钟"};
                final int[] time = {1, 5, 10, 15, 20, 25, 30};
                new AlertDialog.Builder(getActivity())
                        .setTitle("循环录像时间")
                        .setItems(data, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppPara.getInstance().setLoopDuration(time[which]);
                                Tools.saveAppPara(AppPara.getInstance(),getActivity());
                                initVideoData();
                                adapter.notifyDataSetChanged();
                            }
                        }).create()
                        .show();
            }
            break;
            case 1: {
                // 视频分辨率
                final List<Camera.Size> videoSizes = CameraSupportedParameters.getInstance().getVideoSizes();
                String[] data_fbl = new String[videoSizes.size()];
                for (int i = 0; i < videoSizes.size(); i++) {
                    data_fbl[i] = videoSizes.get(i).width + "x" + videoSizes.get(i).height;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle("设置视频分辨率").setItems(data_fbl, new DialogInterface.OnClickListener() {//
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//
                        AppPara.getInstance().getVideo_Resolution_Ratio().setWidth(videoSizes.get(which).width);
                        AppPara.getInstance().getVideo_Resolution_Ratio().setHeight(videoSizes.get(which).height);
                        Tools.saveAppPara(AppPara.getInstance(), getActivity());
                        initVideoData();
                        adapter.notifyDataSetChanged();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            break;
            case 2: {
                //摄像头
                String[] cameraId = new String[]{"后", "前"};
                new AlertDialog.Builder(getActivity())
                        .setTitle("摄像头选择")
                        .setItems(cameraId, new DialogInterface.OnClickListener() {//
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//
                        AppPara.getInstance().setCurrentCameraId(which);
                        Tools.saveAppPara(AppPara.getInstance(), getActivity());
                        initVideoData();
                        adapter.notifyDataSetChanged();
                    }
                }).create()
                        .show();

            }
            break;
            //设置存储路径
            case 3:{
                Intent intent = new Intent(getActivity(), ru.bartwell.exfilepicker.ExFilePickerActivity.class);
                intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
                intent.putExtra(ExFilePicker.SET_CHOICE_TYPE, ExFilePicker.CHOICE_TYPE_DIRECTORIES);
                startActivityForResult(intent, EX_FILE_PICKER_RESULT);
            }
                break;
            case 4:
            {
                String[] data = {"0°","90°", "180°", "270°"};
                final int[] angle = {0,90, 180, 270};
                new AlertDialog.Builder(getActivity())
                        .setTitle("旋转角度")
                        .setItems(data, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppPara.getInstance().setRotationAngle(angle[which]);
                                Tools.saveAppPara(AppPara.getInstance(),getActivity());
                                initVideoData();
                                adapter.notifyDataSetChanged();
                            }
                        }).create()
                        .show();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EX_FILE_PICKER_RESULT) {
            if (data != null) {
                ExFilePickerParcelObject object = (ExFilePickerParcelObject) data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
                if (object.count > 0) {
                    // Here is object contains selected files names and path
                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < object.count; i++) {
                        buffer.append(object.names.get(i));
                        if (i < object.count - 1) buffer.append(", ");
                    }
                    String savePath=object.path+buffer.toString();
                    AppPara.getInstance().setSavePath(savePath);
                    Tools.saveAppPara(AppPara.getInstance(), getActivity());
                    initVideoData();
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private class VideoAdapter extends BaseAdapter {
        @Override
        public boolean areAllItemsEnabled() {
            return super.areAllItemsEnabled();
        }

        @Override
        public int getCount() {

            return lists.size();
        }

        @Override
        public Object getItem(int position) {

            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.setvideo_item, null);
                holder = new ViewHolder();
                holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
                holder.item_value = (TextView) convertView.findViewById(R.id.item_value);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.item_name.setText(lists.get(position).get("k"));
            holder.item_value.setText(lists.get(position).get("v"));
            return convertView;
        }
    }

    class ViewHolder {
        TextView item_name, item_value;
    }
}
