CREATE TABLE tb_member (
    mb_id int8 NOT NULL,
    mb_password varchar(255) NULL,
    mb_email varchar(255) NULL,
    mb_name varchar(255) NULL,
    mb_role varchar(255) NOT NULL,
    CONSTRAINT tb_member_pk PRIMARY KEY (mb_id)
);

INSERT INTO tb_member(mb_id, mb_password, mb_email, mb_name, mb_role) VALUES (
    1
   ,'$2a$10$E2y9hpP.ymhrJn9DBehwP.h2Ta4w.7JlOCFJKWNQpwjEj.ZR3H2uq'  -- 1234
   , 'bresting@gmail.com'
   , '사용자명'
   , 'ROLE_ADMIN'
)