package com.example.qistone.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;

import com.example.qistone.R;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 数据库工具类：连接数据库用、获取数据库数据用
 * 相关操作数据库的方法均可写在该类
 */
public class DBUtils {
    //   连接形式大概是这样的："jdbc:mysql://localhost:3306/map_designer_test_db";
    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动
    private static String user = "root";// 用户名
    private static String password = "dailing";// 密码
    /*
     * 数据库的连接
     * */
    private static Connection getConn(String dbName){
        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "192.168.43.25";// 写成本机地址，不能写成localhost，同时手机和电脑连接的网络必须是同一个
            // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + dbName, user, password);
            Log.e("what??", " 执行到此");
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    /*
     * 数据库的查找
     * */

    public static HashMap<String, Object> getInfoByName(String phone_num){
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = getConn("jdbc");

        try {
            // mysql简单的查询语句。这里是根据MD_CHARGER表的NAME字段来查询某条记录
            String sql = "select * from Qistone_user where phone_num = ?;";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, phone_num);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        int count = rs.getMetaData().getColumnCount();
                        Log.e("DBUtils","列总数：" + count);
                        while (rs.next()){
                            // 注意：下标是从1开始的
                            for (int i = 1;i <= count;i++){
                                String field = rs.getMetaData().getColumnName(i);
                                map.put(field, rs.getString(field));
                            }
                        }
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        return null;
                    }
                }else {
                    return  null;
                }
            }else {
                return  null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }
    public static void insertDate(String name, String phone_num, String password, String sex,
                                  String old, String address,byte[] s) throws SQLException {
        // 根据数据库名称，建立连接
        int count = 1;
        Connection connection = getConn("jdbc");
        String sql1="select * from Qistone_user;";
        PreparedStatement ps = connection.prepareStatement(sql1);
        ResultSet rs=ps.executeQuery();
        if (rs != null){
            while (rs.next()) {
                count++;
            }
        }
        String sql2="insert into Qistone_user(user_id,phone_num,user_name,user_password,sex,old,address,headImage) values("+count+","
                +"'"+phone_num+"',"+"'"+name+"',"+"'"+password+"',"+"'"+sex+"',"+"'"+old+"',"+"'"+address+"',"+"'"+s+"');";//插入数据
        ps = connection.prepareStatement(sql2);//执行sql语句
        ps.executeUpdate();
        connection.close();
        ps.close();
    }

    public static void upDate(String name, String phone_num, String password, String sex,
                              String old, String address, byte[] imge_bytes) throws SQLException {
        // 根据数据库名称，建立连接

        Connection connection = getConn("jdbc");
        String sql="UPDATE Qistone_user set "+"user_name='"+name+"',"+"user_password='"+password+"',"+"sex='"+sex+"',"
                +"old='"+old+"',"+"address='"+address+"',"+"headImage='"+imge_bytes+"' where phone_num='"+phone_num+"';";//插入数据
        PreparedStatement ps = connection.prepareStatement(sql);//执行sql语句
        ps.executeUpdate();
        connection.close();
        ps.close();
    }
    public static byte[] img(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    //byte[]形式转换成bitmap型
    public static Bitmap getBitmapFromByte(byte[] temp){

        if(temp != null){
//            Bitmap bitmap = BitmapFactory.decodeByteArray(temp,0,temp.length);
//            Log.e("This.bitmap", String.valueOf(bitmap));
            YuvImage yuvimage=new YuvImage(temp, ImageFormat.NV21, 20,20, null);//20、20分别是图的宽度与高度
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvimage.compressToJpeg(new Rect(0, 0,20, 20), 80, baos);//80--JPG图片的质量[0-100],100最高
            byte[] jdata = baos.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
            Log.e("This.bitmap", String.valueOf(bitmap));
            return bitmap;
        }else{
            return null;
        }
    }
    //字符型转化成byte[]型
    public static byte[] strToByteArray(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();
        return byteArray;
    }
}