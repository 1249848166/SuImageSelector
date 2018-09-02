package su.com.suimageselector;

import java.util.List;

public class CheckBoxImageViewGroup {

    List<String> checkedItems;
    int maxSize;
    CheckCallback callback;

    public CheckBoxImageViewGroup(List<String> checkedItems,int maxSize,CheckCallback callback) {
        this.checkedItems = checkedItems;
        this.maxSize=maxSize;
        this.callback=callback;
    }

    public void check(CheckBoxImageView checkBoxImageView,boolean checked){
        if(checked){
            if(checkedItems.size()<maxSize){
                checkedItems.add(checkBoxImageView.getTag().toString());
                checkBoxImageView.setChecked(true);
                checkBoxImageView.invalidate();
            }
        }else{
            checkedItems.remove(checkBoxImageView.getTag().toString());
            checkBoxImageView.setChecked(false);
            checkBoxImageView.invalidate();
        }
        if(callback!=null)
            callback.onCheckResult(this,checkBoxImageView);
    }

    public int size(){
        if(checkedItems!=null)
            return checkedItems.size();
        else
            return 0;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public List<String> getCheckedItems() {
        return checkedItems;
    }
}
