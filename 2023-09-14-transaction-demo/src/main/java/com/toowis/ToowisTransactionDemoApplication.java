package com.toowis;

import java.sql.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@SpringBootApplication
public class ToowisTransactionDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToowisTransactionDemoApplication.class, args);
    }
    
    @RestController
    static class DemoClass {
        
        @Autowired
        private DemoService demoService;
        
        @GetMapping(path = "/getData")
        public LST_ECMT_QUEUE getData() {
            return demoService.getData();
        }
        
        @Transactional()
        @GetMapping(path = "/getDataTran")
        public LST_ECMT_QUEUE getDataTran() {
            
            demoService.getDataTran();
            demoService.getDataTran();
            
            return demoService.getDataTran();
        }
    }
    
    @Service
    static class DemoService {
        @Autowired private TestMapper testMapper;
        @Autowired private InstanceTransaction instanceTransaction;
        
        public LST_ECMT_QUEUE getData() {
            return testMapper.findByKey(2069414601L).orElse(new LST_ECMT_QUEUE());
        }
        
        @Transactional
        public LST_ECMT_QUEUE getDataTran() {
            
            try {
                testMapper.findByKey(0L).orElseThrow();
            } catch (NoSuchElementException e) {
                instanceTransaction.runInNewTransaction(() -> {
                    System.out.println(">>>>>>");
                    testMapper.findByKey(2069414601L);
                });
            }
            
            return testMapper.findByKey(2069414601L).orElse(new LST_ECMT_QUEUE());
        }
    }
    
    @Component
    static class InstanceTransaction {
        @Transactional(propagation = Propagation.REQUIRED)
        public <T> T runInTransaction(Supplier<T> supplier) {
            return supplier.get();
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public <T> T runInNewTransaction(Supplier<T> supplier) {
            return supplier.get();
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void runInNewTransaction(Runnable runnable) {
            runnable.run();
        }
    }
    
    @Mapper
    @Repository
    interface TestMapper {
        @Select("SELECT * FROM LST_ECMT_QUEUE WHERE EC_QUEUE_SEQ = #{EC_QUEUE_SEQ}")
        Optional<LST_ECMT_QUEUE> findByKey(@Param("EC_QUEUE_SEQ") long EC_QUEUE_SEQ);
    }
    
    @Data
    static class LST_ECMT_QUEUE {
        private long EC_QUEUE_SEQ;
        private Date UP_DT;
    }
}
