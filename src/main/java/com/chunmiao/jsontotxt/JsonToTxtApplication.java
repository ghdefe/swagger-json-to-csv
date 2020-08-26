package com.chunmiao.jsontotxt;

import com.chunmiao.jsontotxt.entity.Root;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.*;

@SpringBootApplication
public class JsonToTxtApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JsonToTxtApplication.class, args);
        FileInputStream in = new FileInputStream("1.txt");
        JsonNode jsonNode = new ObjectMapper().readTree(in);

        /**
         * 取所有数据并存到HashMap中
         */
        String api;
        HashMap<String, List<Root>> hm = new HashMap<>();
        JsonNode node = jsonNode.findValue("paths");
        Iterator<String> stringIterator = node.fieldNames();
        while (stringIterator.hasNext()) {
            JsonNode tags = node.findValue((api = stringIterator.next())); //api
            Iterator<String> methodsname = tags.fieldNames();
            while (methodsname.hasNext()) {
                String method = methodsname.next(); //方法
                JsonNode methods = tags.findValue(method);
                String name = methods.findValue("tags").get(0).asText();
                String description = methods.findValue("description").asText();

                Root root = new Root(name, method, api,description);  //当前查询到的一个接口数据
                //放到hashmap里管理
                if (hm.containsKey(root.getName())) {
                    List<Root> roots = hm.get(root.getName());
                    roots.add(root);
                    hm.put(root.getName(), roots);
                } else {
                    ArrayList<Root> roots = new ArrayList<>();
                    roots.add(root);
                    hm.put(root.getName(), roots);
                }

            }

        }

        /**
         * 获得name的顺序，并按顺序写入csv
         */
        File file = new File("result.csv");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "GBK"));    //excel不能读取utf-8编码的csv文件

        Iterator<JsonNode> names = jsonNode.findValue("tags").iterator();
        while (names.hasNext()) {
            String name = names.next().findValue("name").asText();
            Iterator<Root> iterator1 = hm.get(name).iterator();
            bufferedWriter.write(name + ",");
            Boolean isFirst = true;
            while (iterator1.hasNext()) {
                if (!isFirst) {
                    bufferedWriter.write(",");
                } else {
                    isFirst = false;
                }
                Root next = iterator1.next();
                bufferedWriter.write(next.getMethod() + "," +
                        next.getApi() + "," + next.getDescription());
                bufferedWriter.newLine();
            }

        }
        bufferedWriter.close();

//
//        DrugPricLmtRemoteService
//        DrugPubonlnService
//        McsPubonlnService
//        MedinsPrucPlanService
//        MedinsPurcOrdInService
//        bufferedWriter.write(name + ","
//                        + method +","
//                        + api);
//                bufferedWriter.newLine();
//        bufferedWriter.close();
        Runtime.getRuntime().exec("cmd /c start F:/Project/JsonSoup/result.csv");
        System.out.println("done");


    }


    public static Root jsontoentity(FileInputStream in) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Root root = objectMapper.readValue(in, Root.class);

        return root;


    }

}
