package ru.digitalleague.taxi_company.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import ru.digitalleague.taxi_company.model.TaxiDriverInfo;

@Repository
@Mapper
public interface TaxiDriveInfoMapper {

    @Select("SELECT driver_id, last_name, first_name, level, car_id, create_dttm, minute_cost, city_id, rating, busy" +
            "FROM taxi_drive_info WHERE city_id = #{cityId} AND car_id = '#{carId}' AND busy = false ORDER BY rating DESC LIMIT 1")
    TaxiDriverInfo findAvailableDriver(long cityId, String carModel);

    @Results(id = "driver", value = {
            @Result(property = "driverId", column = "driver_id"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "level", column = "level"),
            @Result(property = "createDttm", column = "create_dttm"),
            @Result(property = "carId", column = "car_id"),
            @Result(property = "minuteCost", column = "minute_cost"),
            @Result(property = "rating", column = "rating"),
            @Result(property = "isBusy", column = "busy"),
            @Result(property = "cityId", column = "city_id")
    })
    @Select("SELECT driver_id, first_name, last_name, level, create_dttm, model as car_model," +
            " taxi_drive_info.city_id, minute_cost, rating, busy, car_id" +
            " FROM taxi_drive_info" +
            " INNER JOIN car c on c.id = taxi_drive_info.car_id" +
            " INNER JOIN city_queue cq on cq.city_id = taxi_drive_info.city_id" +
            " WHERE name = #{cityName} and model = #{carModel} and busy = false and level = #{level}" +
            " limit 1")
    TaxiDriverInfo findDriverByCityAndCarModelAndLevel(String cityName, String carModel, int level);
}
