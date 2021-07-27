package model.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Color;
import model.Ray;

@Getter
@AllArgsConstructor
public class ScatterRecord {
    private final boolean scattered;
    private final Color attenuation;
    private final Ray scatterRay;
}
