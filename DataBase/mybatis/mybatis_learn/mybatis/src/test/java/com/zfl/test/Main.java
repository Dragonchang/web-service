package com.zfl.test;

import com.zfl.mapper.RoleMapper;
import com.zfl.po.Role;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program: mybatis
 * @description:
 * @author: zhangfl
 * @create: 2021-01-05 08:47
 **/
public class Main {
    public static void main(String[] args) {
        String resource="mybatis-config.xml";
        InputStream inputStream=null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory=null;
        sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession=null;
        try {
            sqlSession=sqlSessionFactory.openSession();
            RoleMapper roleMapper=sqlSession.getMapper(RoleMapper.class);
            Role role=roleMapper.getRole(1L);
            System.out.println(role.getId()+":"+role.getRoleName()+":"+role.getNote());
            sqlSession.commit();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            sqlSession.rollback();
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }

//        Properties ps = System.getProperties();
//        System.out.println(ps.getProperty("sun.boot.class.path"));
//        System.out.println("11111111111******************************");
//        System.out.println(ps.getProperty("java.ext.dirs"));
//        System.out.println("222222222222222******************************");
//        System.out.println(ps.getProperty("java.class.path"));
    }
}
