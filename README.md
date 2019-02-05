#### 介绍
一个excel导入、导出的工具

##### 导入
1、支持 dom or sax
2、支持 03版 and 07版
3、支持转换，如格式转换，枚举转换
4、支持导入时进行验证，并返回验证结果
验证的返回分为
（1）遇到验证失败时立刻返回
（2）验证全部返回
5、当是 sax 模式时天然的支持 listener 
分为行事件监听，指定数量批次的监听，解析结束时的监听
事件返回 boolean，可以中止解析过程

例子
```
ExcelReader.<User>newBuilder().targetClass(User.class).parserType(ParserTypeEnum.DOM).addListener(new ReadEventListener<User>() {
            @Override
            public boolean readRow(User user) {
                log.info("readRow={}", user.toString());
                return true;
            }

            @Override
            public boolean readBatch(List<User> rowDatas) {
                log.info("readBatch");
                return true;
            }

            @Override
            public void readFinished() {
                log.info("readFinished");
            }

            @Override
            public boolean parseException(int rowNum, int colNum, User user, Exception e) {
                log.info("user={}", user.toString());
                log.info(e.getMessage());
                return true;
            }
        }).build().read(in07);
```


##### 导出
1、支持xls，xlsx

例子
```
ExcelWriter excelWriter = ExcelWriter
                .<User>newBuilder()
                .sheetName("123")
                .fileName("123")
                .targetClass(User.class)
                .excelType(ExcelTypeEnum.XLSX)
                .outputStream(outputStream)
                .build();

        LinkedList<User> list = new LinkedList<>();
        User user = new User();
        user.setName("name");
        user.setPhoneNo("phone");
        user.setAddress("address");
        user.setEmail("123@qq.com");
        user.setAge(1);
        user.setGender(1);
        user.setAsset(100.56D);
        user.setBirthday(new Date());
        list.add(user);
        excelWriter.write(list);


```


