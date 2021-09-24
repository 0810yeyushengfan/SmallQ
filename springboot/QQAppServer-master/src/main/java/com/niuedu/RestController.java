package com.niuedu;


import com.niuedu.dao.UserMapper;
import com.niuedu.model.ContactInfo;
import com.niuedu.model.Message;
import com.niuedu.model.ServerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/apis")
public class RestController {
    private UserMapper userMapper;

    private List<Message> messagesList = new ArrayList<>();

    @Autowired
    public RestController(UserMapper userMapper) {
        this.userMapper = userMapper;
        //创建数据库
        try {
            this.userMapper.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        messagesList.add(new Message("系统",new Date().getTime(),"欢迎进入聊天室!"));
    }

    @RequestMapping("/get_message")
    Message getMessage(){
        return new Message("路人甲", new Date().getTime(), "我说啥了我? Get out!");
    }

    @RequestMapping("/get_all_messages")
    public List<Message> getAllMessages(){
        return messagesList;
    }

    @RequestMapping("/get_messages")
    public ServerResult getMessages(@RequestParam("from") int index){
        return new ServerResult(0,null,
                messagesList.subList(index,messagesList.size()));
    }

    @RequestMapping("/msg_history")
    List<Message> getMessageHistory(){
        return messagesList;
    }

    @RequestMapping("/upload_message")
    ServerResult saveMessage(@RequestBody Message message){
        messagesList.add(message);
        return new ServerResult(0);
    }

    @RequestMapping("/login")
    ServerResult requestLogin(String name,String password){
        //查看messagesList里面是否有同名的联系人
        //password对不对就无所谓了
        ContactInfo contact = this.userMapper.findContactByName(name);
        if(contact!=null){
            //找到了，成功
            return new ServerResult(0,null,contact);
        }

        return new ServerResult(1,"找不到帐户名，请先注册！");
    }

    @RequestMapping("/register")
    ServerResult requestRegister(@RequestParam(value = "file",required = false) MultipartFile file,
                                 @RequestParam("name") String name,
                                 @RequestParam("password") String password){
        //先保存用户信息，以取得id
        ContactInfo ci = new ContactInfo(-1,name,password,"online");
        try {
            this.userMapper.addUser(ci);
        }catch (Exception e){
            String err = "数据库错误，是不是名字重复了？";
            return new ServerResult(0,err);
        }

        if(file!=null) {
            String ext = StringUtils.cleanPath(file.getOriginalFilename());
            saveUserAvatar(ci.getId(), file, ext);
        }

        return new ServerResult(0,null,ci);
    }

    @RequestMapping("/get_contacts")
    ServerResult getContacts(){
        //从数据库中获取联系人们
        List<ContactInfo> contacts = this.userMapper.getAllContacts();
        return new ServerResult(0,null,contacts);
    }

    private void saveUserAvatar(int userId, MultipartFile file, String ext) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new RuntimeException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                File classPath = new File(ResourceUtils.getURL("classpath:").getPath());
                File upload = new File(classPath.getAbsolutePath(),
                        "static/image/head/"+userId+"."+ext);
                file.transferTo(upload);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }
}
