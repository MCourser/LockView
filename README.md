#Android-LockView
I code this for my another project and share it.
Originally wanted to find an open source control with a look, but did not search to the right. So write an unlock control.

if you have any question, please contact me.
E-mail: ZUImachao@163.com


![SHOW](http://git.oschina.net/uploads/images/2015/1231/221521_a0116f6c_88777.gif "SHOW")

![MyProject](http://git.oschina.net/uploads/images/2015/1231/225304_a3114c6b_88777.gif "MyProject")

##Usage:
```
    <com.mahao.team42.lockview.widget.LockView
        android:id="@+id/activity_main_lockView"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true" /> 
```

```
    //set line color
    this.lockView.setColorLine(Color.RED);
		
    //set circle color
    this.lockView.setColorCircle(Color.GREEN);
		
    //set point color
    this.lockView.setColorPoint(Color.BLUE);
		
    //set column and row count
    this.lockView.setSpanCount(3);
		
    //set min point count to finish
    this.lockView.setMinPointCount(3);
		
    //set inner point circle radius
    this.lockView.setInnerCircleRadius(0.2F);
		
    //set inner trigger circle radius		
    this.lockView.setInnerTriggerCircleRadius(0.8F);
		
    //set outer circle radius
    this.lockView.setOuterCircleRadius(1.0F);
		
    //set set trigger circle show
    this.lockView.setTriggerCircleShow(true);
		
    //set listner
    this.lockView.setOnLockFinishListener(new OnLockFinishListener() {
        @Override
        public void onFinish(List<Integer> result) {
            //TODO
        }
    });
```