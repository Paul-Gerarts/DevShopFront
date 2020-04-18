package be.syntra.devshop.DevshopFront.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class DataStore {
    Map<String, Boolean> map;
}
