package io.hus.controller.cityController;

import io.hus.entity.cityEntity.City;
import io.hus.service.cityService.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping (value = "/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "javainuseapi")
public class CityController {

    private final CityService cityService;

    @Operation(summary = "List all cities")
    @GetMapping(value = "/open/cities")
    public ResponseEntity<List<City>> listCity(){
        List<City> cities = new ArrayList<>();
        cities = cityService.listAllCity();
        if (cities.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cities);
    }

    @Operation(summary = "Create a new city")
    @PostMapping(value = "/city/save")
    public ResponseEntity<City> createCity(@RequestBody City city){
        if( city.getName()==null || city.getCountry()==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(city);
        }
        City cityCreate = cityService.createCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(cityCreate);
    }

    @Operation(summary = "Update a city")
    @PutMapping(value = "/city/{id}")
    public ResponseEntity<City> updateCity(@PathVariable("id") Long id, @RequestBody City city ){

        Optional<City> cityDB = cityService.getCity(id);

        if(cityDB.isPresent()){
            City updateCity = city;
            updateCity.setId(id);
            updateCity.setCreateAt(cityDB.get().getCreateAt());
            updateCity.setStatus(cityDB.get().getStatus());
            updateCity = cityService.updateCity(updateCity);
            return ResponseEntity.ok(updateCity);
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    @Operation(summary = "Delete a city")
    @DeleteMapping(value = "/city/{id}")
    public ResponseEntity<City> deleteCity(@PathVariable("id") Long id){
        City cityDelete = cityService.deleteCity(id);
        if (cityDelete == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cityDelete);
    }

    @GetMapping(value = "/namecity/{name}")
    public ResponseEntity<City> getCityByName(@PathVariable("name") String name){
        City city = cityService.getCityByName(name);
        return ResponseEntity.ok(city);
    }

}
