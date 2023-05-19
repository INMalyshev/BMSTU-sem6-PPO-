package pgdata.integration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import pgdataMappers.ApproachMapper;
import pgdataMappers.ApproachPlanMapper;
import pgdataMappers.IntMapper;
import pgdataMappers.RequestMapper;
import pgdataMappers.TrainingMapper;
import pgdataMappers.TrainingPlanMapper;
import pgdataMappers.UserMapper;

import org.postgresql.Driver;


@Configuration
public class PgdataIntegrationTestConfiguration {
    
    @Bean
    DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:postgresql://localhost:5432/business_test");
        dataSource.setUsername("te_manager");
        dataSource.setPassword("PA$$WORD");

        return dataSource;
    }

    @Bean
    IntMapper intMapper() {
        return new IntMapper();
    } 

    @Bean
    UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    TrainingPlanMapper trainingPlanMapper() {
        return new TrainingPlanMapper();
    }

    @Bean
    ApproachPlanMapper approachPlanMapper() {
        return new ApproachPlanMapper();
    }

    @Bean
    TrainingMapper trainingMapper() {
        return new TrainingMapper();
    }

    @Bean
    ApproachMapper approachMapper() {
        return new ApproachMapper();
    }

    @Bean
    RequestMapper requestMapper() {
        return new RequestMapper();
    }

}
