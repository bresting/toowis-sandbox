package toowis.sandbox;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringBootApplication
public class MybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }
    
    @Bean
    CommandLineRunner demo(CarMapper carMapper) {
        return args -> {
            List<Car> cars = Arrays.asList(
                      new Car("Hyundai", "sonata", 1995, null)
                    , new Car("Hyundai", "g80", 2022, null)
                    , new Car("kia", "sorento", 2018, null)
            );
            
            System.out.println("-- INSERT ---------------------------------------------------------------------------");
            cars.forEach(car -> {
                carMapper.insert(car);
                System.out.println(car.toString());
            });
            
            System.out.println("-- SELECT ---------------------------------------------------------------------------");
            carMapper.selectAll().forEach(System.out::println);
            
            System.out.println("-- SEARCH ---------------------------------------------------------------------------");
            carMapper.search("kia", null).forEach(System.out::println);
        };
    }
}

@Mapper
interface CarMapper {
    
    @Options(useGeneratedKeys = true)
    @Insert("insert into car(maker, model, year) values(#{maker}, #{model}, #{year})")
    void insert(Car car);
    
    @Select("select * from car")
    Collection<Car> selectAll();
    
    @Delete("delete from car where id = #{id}")
    void deleteById(long id);
    
    /**
     * xml 매퍼연결
     * @param maker
     * @param model
     * @return
     */
    Collection<Car> search(@Param("maker") String maker, @Param("model") String model);
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Car {
    private String maker, model;
    private int year;
    private Long id;
}