package com.example.qistone.ViewModel;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class HomeViewModel extends ViewModel{
    // TODO: Implement the ViewModel
    HashMap<String, Object> map=null;

    public void setMap(HashMap<String, Object> map) {
        this.map = map;
    }
}