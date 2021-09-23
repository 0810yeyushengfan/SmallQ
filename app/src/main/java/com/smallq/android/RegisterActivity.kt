package com.smallq.android

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.smallq.android.databinding.ActivityRegisterBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.IOError
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import java.util.zip.Inflater
import kotlin.collections.ArrayList

class RegisterActivity : AppCompatActivity() {
    //视图绑定
    lateinit var binding:ActivityRegisterBinding
    //定义伴随对象，相当于java中的static
    companion object{
        //启动拍照Activity的请求码
        val TAKE_PHOTO:Int=1111
        //申请权限时的请求码
        val ASK_PERMISSIONS:Int=2222
        //启动照片编辑Activity的请求码
        val CROP_PHOTO:Int=3333
        //
        val SELECT_PHOTO:Int=4444
    }
    //用于显示Bottom Sheet
    private var sheetDialog:BottomSheetDialog?=null
    //拍照所得图像保存到的绝对路径
    private var imageUri:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //视图绑定
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        sheetDialog= BottomSheetDialog(this)
        val imageViewAvatar=findViewById(R.id.imageViewAvatar) as ImageView
        imageViewAvatar.setOnClickListener{
            val view=layoutInflater.inflate(R.layout.image_pick_sheet_menu,null)
            val sheetItemTakePhoto=view.findViewById(R.id.sheetItemTakePhoto) as TextView
            val sheetItemSelectPicture=view.findViewById(R.id.sheetItemSelectPicture) as TextView
            val sheetItemCancel=view.findViewById(R.id.sheetItemCancel) as TextView
            sheetItemTakePhoto.setOnClickListener {
                //拍照
                //保存需要申请的权限
                val permissionsList = ArrayList<String>()
                //检查是否有使用相机的权限
                if (ActivityCompat.checkSelfPermission(
                        this@RegisterActivity,
                        android.Manifest.permission.CAMERA
                    ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    //如果此时没有持有相机权限
                    permissionsList.add(android.Manifest.permission.CAMERA)
                }
                //检查是否有使用外部存储的权限
                if (ActivityCompat.checkSelfPermission(
                        this@RegisterActivity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                    //如果此时没有持有外部存储权限
                    permissionsList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }

                if (permissionsList.isEmpty()) {
                    //不用申请权限了，直接显示拍照页面
                    showTakePhotoView()
                } else {
                    //还需先申请权限
                    ActivityCompat.requestPermissions(this@RegisterActivity,
                        //由List构建Array
                        Array(permissionsList.size) {
                                i-> permissionsList[i]}, ASK_PERMISSIONS)
                }
            }
            sheetItemSelectPicture.setOnClickListener{
                //从相册选择

            }
            sheetItemCancel.setOnClickListener{
                //隐藏Sheet
                sheetDialog?.dismiss()

            }
            sheetDialog!!.setContentView(view)
            sheetDialog!!.show()
        }
    }

    private fun showTakePhotoView(){
        //获取图像要保存到的文件对象
        val imageOutputFile=generateOutputFile(Environment.DIRECTORY_DCIM)
        //由文件对象获取Uri对象，它相当于文件的绝对路径
        imageUri=FileProvider.getUriForFile(this,"com.smallq.android.fileprovider",imageOutputFile!!)
        //创建Intent，启动拍照Activity
        val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)//照相
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)//指定图片输出地址
        //告诉拍照Activity，要申请Uri的读和写权限
        intent.flags= Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent,TAKE_PHOTO)//启动照相
    }

    private fun generateOutputFile(pathInExternalStorage:String):File?{
        //图片名称按照保存的时间命名
        val format=SimpleDateFormat("yyyyMMddHHmmss")
        val date= Date(System.currentTimeMillis())
        val photoFileName=format.format(date)+".png"
        //创建File对象用于存储拍照的图片，存储至外部存储的公开目录下
        val path=Environment.getExternalStoragePublicDirectory(pathInExternalStorage)
        val outputFile=File(path,photoFileName)
        try{
            if(outputFile.exists()){
                outputFile.delete()
            }
            outputFile.createNewFile()
        }catch (e:IOException){
            e.printStackTrace()
            return null
        }
        return outputFile
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ASK_PERMISSIONS -> {
                for (i in 0 until permissions.size) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "权限申请被拒绝，无法完成照片选择。", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                //能执行到这里说明全部通过，拍照！
                showTakePhotoView()
                //关掉Sheet
                sheetDialog?.dismiss()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> {
                val intent = Intent("com.android.camera.action.CROP") //剪裁
                //告诉剪裁Activity，要申请对Uri的读和写权限，因为编辑后的图像还是存到这个文件中
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.setDataAndType(this.imageUri, "image/*")
                intent.putExtra("scale", true)
                intent.putExtra("crop", true)
                //设置宽高比例
                intent.putExtra("aspectX", 1)
                intent.putExtra("aspectY", 1)
                //设置裁剪图片宽高
                intent.putExtra("outputX", 480)
                intent.putExtra("outputY", 480)
                //设置图像输出到的文件，跟输入文件是同一个
                intent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri)
                startActivityForResult(intent, CROP_PHOTO)
                //隐藏Sheet
                sheetDialog?.dismiss()
            }
            SELECT_PHOTO -> {
                //从图库中选择一个照片，请自行实现

            }
            CROP_PHOTO -> try {
                //图片解析成Bitmap对象
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                //将剪裁后照片显示出来
                binding.content.imageViewAvatar.setImageBitmap(bitmap)
                //隐藏Sheet
                sheetDialog?.dismiss()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}