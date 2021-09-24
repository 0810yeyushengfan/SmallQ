package com.niuedu.dao;

import com.niuedu.model.ContactInfo;
import org.apache.ibatis.annotations.*;

import javax.annotation.Generated;
import java.util.List;

@Mapper
public interface UserMapper {

    @Update("CREATE TABLE contact_info (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,name varchar(255) UNIQUE,password varchar(48),status varchar(10),primary key (id))")
    void create() throws Exception;

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO contact_info (name,password,status) VALUES (#{name},#{password},#{status})")
    void addUser(ContactInfo user);

    @Select("SELECT * FROM contact_info")
    List<ContactInfo> getAllContacts();

    @Select("SELECT * FROM contact_info WHERE name=#{0}")
    ContactInfo findContactByName(String name);
}
