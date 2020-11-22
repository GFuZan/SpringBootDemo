CREATE database demo1; 

CREATE database demo2; 

CREATE TABLE demo1.`user` ( 
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'
  , `name` varchar (128)
  , `age` varchar (128)
  , PRIMARY KEY (`id`)
)

CREATE TABLE demo2.`user` ( 
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'
  , `name` varchar (128)
  , `age` varchar (128)
  , PRIMARY KEY (`id`)
)

